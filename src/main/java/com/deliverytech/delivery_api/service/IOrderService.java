package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.requests.ItemOrderedDTO;
import com.deliverytech.delivery_api.dto.requests.OrderDTO;
import com.deliverytech.delivery_api.dto.responses.OrderResponseDTO;
import com.deliverytech.delivery_api.enums.OrderStatus;
import com.deliverytech.delivery_api.model.User;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderDTO dto, User loggedUser);
    OrderResponseDTO findOrderById(Long id, User loggedUser);
    Page<OrderResponseDTO> findOrdersByClient(User loggedUser, Pageable pageable);
    Page<OrderResponseDTO> myOrders(User loggedUser, Pageable pageable);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status, User loggedUser);
    BigDecimal calculateOrderTotal(List<ItemOrderedDTO> items);
    OrderResponseDTO cancelOrder(Long id, User loggedUser);
}