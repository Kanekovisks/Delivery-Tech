package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.SalesPerRestaurant;
import com.deliverytech.delivery_api.enums.OrderStatus;
import com.deliverytech.delivery_api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByClientId(Long clientId);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findTop10ByOrderByOrderDateDesc();
    
    List<Order> findByOrderDateBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query(
        """
            SELECT
                r.name AS restaurantName,
                COALESCE(SUM(io.subtotal), 0) AS totalSales
            FROM ItemOrdered io
            JOIN io.order o
            JOIN o.restaurant r
            GROUP BY r.name
        """
    )
    List<SalesPerRestaurant> searchSalesPerRestaurant();
}
