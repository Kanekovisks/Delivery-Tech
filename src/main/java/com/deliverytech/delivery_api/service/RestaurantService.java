package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

@Service
public class RestaurantService implements IRestaurantService {
    private final RestaurantRepository repository;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RestaurantResponseDTO registerRestaurant(RestaurantDTO dto) {
        Restaurant restaurant = modelMapper.map(dto, Restaurant.class);
        restaurant.setActive(true);
        restaurant.setDeliveryFee(BigDecimal.valueOf(5.00)); // Valor padrão
        Restaurant saved = repository.save(restaurant);
        return modelMapper.map(saved, RestaurantResponseDTO.class);
    }

    @Override
    public RestaurantResponseDTO findRestaurantById(Long id) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        return modelMapper.map(restaurant, RestaurantResponseDTO.class);
    }

    @Override
    public List<RestaurantResponseDTO> findRestaurantsByCategory(String category) {
        return repository.findByCategory(category).stream()
                .map(r -> modelMapper.map(r, RestaurantResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResponseDTO> findAvailableRestaurants() {
        return repository.findByActiveTrue().stream()
                .map(r -> modelMapper.map(r, RestaurantResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantDTO dto) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        restaurant.setName(dto.getName());
        restaurant.setCategory(dto.getCategory());
        restaurant.setAddress(dto.getAddress());
        restaurant.setPhone(dto.getPhone());
        restaurant.setRating(dto.getRating());
        Restaurant saved = repository.save(restaurant);
        return modelMapper.map(saved, RestaurantResponseDTO.class);
    }

    @Override
    public BigDecimal calculateDeliveryFee(Long restaurantId, String cep) {
        Restaurant restaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        // Lógica simples: se CEP começa com 1, taxa reduzida, senão taxa padrão
        if (cep != null && cep.startsWith("1")) {
            return restaurant.getDeliveryFee().multiply(BigDecimal.valueOf(0.8));
        }
        return restaurant.getDeliveryFee();
    }
}
