package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRestaurantId(Long restaurantId);
    
    List<Product> findByAvailableTrue();
    
    List<Product> findByCategory(String category);
    
    List<Product> findByPriceLessThanEqual(BigDecimal price);
}
