package com.deliverytech.delivery_api.service;

import java.util.List;

import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.dto.responses.ProductResponseDTO;

public interface IProductService {
    ProductResponseDTO registerProduct(ProductDTO dto);
    List<ProductResponseDTO> findProductsByRestaurant(Long restaurantId);
    ProductResponseDTO findProductById(Long id);
    ProductResponseDTO updateProduct(Long id, ProductDTO dto);
    ProductResponseDTO toggleProductAvailability(Long id, boolean available);
    List<ProductResponseDTO> findProductsByCategory(String category);
}