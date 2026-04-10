package com.deliverytech.delivery_api.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.repository.RestaurantRepository;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }
}
