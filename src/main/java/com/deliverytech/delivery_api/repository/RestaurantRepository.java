package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCategory(String category);
    
    List<Restaurant> findByActiveTrue();
    
    List<Restaurant> findByDeliveryFeeLessThanEqual(BigDecimal deliveryFee);
    
    List<Restaurant> findTop5ByOrderByNameAsc();
}
