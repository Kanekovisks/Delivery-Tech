package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = 
        """
            SELECT DISTINCT p FROM Order p
            JOIN FETCH p.client
            JOIN FETCH p.restaurant
            LEFT JOIN FETCH p.items i
            LEFT JOIN FETCH i.product
            WHERE p.client.id = :clientId
        """, 
        countQuery = "SELECT count(p) FROM Order p WHERE p.client.id = :clientId")
    Page<Order> searchItemsPerClient(@Param("clientId") Long clientId, Pageable pageable);

    @Query(value=
        """
            SELECT c.name AS client, COUNT(p.id) AS total_orders
            FROM orders p 
            JOIN clients c ON c.id = p.client_id
            GROUP BY c.nome
            ORDER BY total_orders DESC
        """, nativeQuery = true )
    List<Object[]> rankingClients();
}
