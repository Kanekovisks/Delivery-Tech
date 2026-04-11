package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ItemOrderedDTO;
import com.deliverytech.delivery_api.dto.requests.OrderDTO;
import com.deliverytech.delivery_api.dto.responses.OrderResponseDTO;
import com.deliverytech.delivery_api.enums.OrderStatus;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Client;
import com.deliverytech.delivery_api.model.ItemOrdered;
import com.deliverytech.delivery_api.model.Order;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.ClientRepository;
import com.deliverytech.delivery_api.repository.ItemOrderedRepository;
import com.deliverytech.delivery_api.repository.OrderRepository;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ItemOrderedRepository itemOrderedRepository;
    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ItemOrderedRepository itemOrderedRepository,
                        ClientRepository clientRepository, RestaurantRepository restaurantRepository,
                        ProductRepository productRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.itemOrderedRepository = itemOrderedRepository;
        this.clientRepository = clientRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderDTO dto) {
        // Validar cliente
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        if (!client.isActive()) {
            throw new BusinessException("Cliente inativo.");
        }

        // Validar restaurante
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        if (!restaurant.getActive()) {
            throw new BusinessException("Restaurante inativo.");
        }

        // Validar itens
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BusinessException("Pedido deve ter pelo menos um item.");
        }

        // Calcular total
        BigDecimal total = calculateOrderTotal(dto.getItems());

        // Criar pedido
        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setDeliveryFee(dto.getDeliveryFee());
        order.setTotalPrice(total.add(dto.getDeliveryFee()));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderNumber("PED-" + System.currentTimeMillis());

        Order savedOrder = orderRepository.save(order);

        // Criar itens
        for (ItemOrderedDTO itemDto : dto.getItems()) {
            Long productId = itemDto.getProductId();
            if (productId == null) {
                throw new BusinessException("ID do produto não pode ser nulo.");
            }
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
            if (!product.getAvailable()) {
                throw new BusinessException("Produto não disponível: " + product.getName());
            }

            ItemOrdered item = new ItemOrdered();
            item.setProduct(product);
            item.setOrder(savedOrder);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            itemOrderedRepository.save(item);
        }

        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO findOrderById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID do pedido não pode ser nulo.");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
        return modelMapper.map(order, OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> findOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(o -> modelMapper.map(o, OrderResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        if (id == null) {
            throw new BusinessException("ID do pedido não pode ser nulo.");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
        // Validar transições
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Pedido já cancelado.");
        }
        if (order.getStatus() == OrderStatus.DELIVERED && status != OrderStatus.DELIVERED) {
            throw new BusinessException("Pedido entregue não pode ser alterado.");
        }
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponseDTO.class);
    }

    @Override
    public BigDecimal calculateOrderTotal(List<ItemOrderedDTO> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemOrderedDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
            if (!product.getAvailable()) {
                throw new BusinessException("Produto não disponível.");
            }
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);
        }
        return total;
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID do pedido não pode ser nulo.");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Pedido não pode ser cancelado.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponseDTO.class);
    }
}