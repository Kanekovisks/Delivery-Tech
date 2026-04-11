package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.dto.requests.ItemOrderedDTO;
import com.deliverytech.delivery_api.dto.requests.OrderDTO;
import com.deliverytech.delivery_api.dto.responses.OrderResponseDTO;
import com.deliverytech.delivery_api.enums.OrderStatus;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderDTO dto);
    OrderResponseDTO findOrderById(Long id);
    List<OrderResponseDTO> findOrdersByClient(Long clientId);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status);
    BigDecimal calculateOrderTotal(List<ItemOrderedDTO> items);
    OrderResponseDTO cancelOrder(Long id);
}