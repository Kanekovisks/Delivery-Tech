package com.deliverytech.delivery_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.dto.responses.ProductResponseDTO;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ProductResponseDTO registerProduct(ProductDTO dto) {
        // Validar se restaurante existe
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        
        Product product = modelMapper.map(dto, Product.class);
        product.setRestaurant(restaurant);
        product.setAvailable(true);
        Product saved = productRepository.save(product);
        
        ProductResponseDTO response = modelMapper.map(saved, ProductResponseDTO.class);
        response.setRestaurantId(saved.getRestaurant().getId());
        return response;
    }

    @Override
    public List<ProductResponseDTO> findProductsByRestaurant(Long restaurantId) {
        return productRepository.findByRestaurantId(restaurantId).stream()
                .filter(Product::getAvailable)
                .map(p -> {
                    ProductResponseDTO dto = modelMapper.map(p, ProductResponseDTO.class);
                    dto.setRestaurantId(p.getRestaurant().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        if (!product.getAvailable()) {
            throw new EntityNotFoundException("Produto não disponível.");
        }
        ProductResponseDTO response = modelMapper.map(product, ProductResponseDTO.class);
        response.setRestaurantId(product.getRestaurant().getId());
        return response;
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        Product saved = productRepository.save(product);
        ProductResponseDTO response = modelMapper.map(saved, ProductResponseDTO.class);
        response.setRestaurantId(saved.getRestaurant().getId());
        return response;
    }

    @Override
    @Transactional
    public ProductResponseDTO toggleProductAvailability(Long id, boolean available) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        product.setAvailable(available);
        Product saved = productRepository.save(product);
        ProductResponseDTO response = modelMapper.map(saved, ProductResponseDTO.class);
        response.setRestaurantId(saved.getRestaurant().getId());
        return response;
    }

    @Override
    public List<ProductResponseDTO> findProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .filter(Product::getAvailable)
                .map(p -> {
                    ProductResponseDTO dto = modelMapper.map(p, ProductResponseDTO.class);
                    dto.setRestaurantId(p.getRestaurant().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}