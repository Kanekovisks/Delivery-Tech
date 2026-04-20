package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.enums.RestaurantCategory;
import com.deliverytech.delivery_api.model.User;

public interface IRestaurantService {
    RestaurantResponseDTO registerRestaurant(RestaurantDTO dto, User userLogged);
    RestaurantResponseDTO findRestaurantById(Long id);
    List<RestaurantResponseDTO> findRestaurantsByCategory(RestaurantCategory category);
    List<RestaurantResponseDTO> findAvailableRestaurants();
    RestaurantResponseDTO updateRestaurant(Long id, RestaurantDTO dto);
    BigDecimal calculateDeliveryFee(Long restaurantId, String cep);
}