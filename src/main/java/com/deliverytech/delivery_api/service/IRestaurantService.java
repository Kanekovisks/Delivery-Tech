package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;

public interface IRestaurantService {
    RestaurantResponseDTO registerRestaurant(RestaurantDTO dto);
    RestaurantResponseDTO findRestaurantById(Long id);
    List<RestaurantResponseDTO> findRestaurantsByCategory(String category);
    List<RestaurantResponseDTO> findAvailableRestaurants();
    RestaurantResponseDTO updateRestaurant(Long id, RestaurantDTO dto);
    BigDecimal calculateDeliveryFee(Long restaurantId, String cep);
}