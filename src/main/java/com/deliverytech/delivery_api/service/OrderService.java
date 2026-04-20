package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.ClientRepository;
import com.deliverytech.delivery_api.repository.OrderRepository;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderService(
            OrderRepository orderRepository,
            ClientRepository clientRepository,
            RestaurantRepository restaurantRepository,
            ProductRepository productRepository,
            ModelMapper modelMapper) {

        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private OrderResponseDTO toDTO(Order order) {
        return modelMapper.map(order, OrderResponseDTO.class);
    }

    private void validateOrderOwner(Order order, User loggedUser) {
        if (!order.getClient().getEmail().equals(loggedUser.getEmail())) {
            throw new BusinessException("Você não tem permissão para acessar este pedido.");
        }
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderDTO dto, User loggedUser) {

        if (loggedUser == null) {
            throw new BusinessException("Usuário não autenticado.");
        }

        Client client = clientRepository.findByEmail(loggedUser.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        if (!client.isActive()) {
            throw new BusinessException("Cliente inativo.");
        }

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        if (!restaurant.getActive()) {
            throw new BusinessException("Restaurante inativo.");
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BusinessException("Pedido deve conter ao menos um produto.");
        }

        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemOrderedDTO itemDTO : dto.getItems()) {

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

            if (!product.isAvailable()) {
                throw new BusinessException("Produto " + product.getName() + " indisponível.");
            }

            ItemOrdered item = new ItemOrdered();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(product.getPrice());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            item.setSubtotal(subtotal);

            order.getItems().add(item);
            total = total.add(subtotal);
        }

        order.setTotalPrice(total.add(dto.getDeliveryFee()));

        return toDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO findOrderById(Long id, User loggedUser) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validateOrderOwner(order, loggedUser);

        return toDTO(order);
    }

    @Override
    public Page<OrderResponseDTO> findOrdersByClient(User loggedUser, Pageable pageable) {

        Client client = clientRepository.findByEmail(loggedUser.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        return orderRepository.searchItemsPerClient(client.getId(), pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status, User loggedUser) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validateOrderOwner(order, loggedUser);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Pedido já cancelado.");
        }

        if (order.getStatus() == OrderStatus.DELIVERED && status != OrderStatus.DELIVERED) {
            throw new BusinessException("Pedido entregue, incapaz de atualizar status.");
        }

        order.setStatus(status);

        return toDTO(orderRepository.save(order));
    }

    @Override
    public BigDecimal calculateOrderTotal(List<ItemOrderedDTO> items) {

        BigDecimal total = BigDecimal.ZERO;

        for (ItemOrderedDTO item : items) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

            if (!product.isAvailable()) {
                throw new BusinessException("Produto indisponível.");
            }

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(subtotal);
        }

        return total;
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id, User loggedUser) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validateOrderOwner(order, loggedUser);

        if (order.getStatus() == OrderStatus.DELIVERED ||
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Pedido não pode ser cancelado.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        return toDTO(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponseDTO> myOrders(User loggedUser, Pageable pageable) {

        Client client = clientRepository.findByEmail(loggedUser.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        return orderRepository.searchItemsPerClient(client.getId(), pageable)
                .map(this::toDTO);
    }
}