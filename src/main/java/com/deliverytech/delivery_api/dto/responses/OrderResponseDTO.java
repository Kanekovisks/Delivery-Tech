package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {

    @Schema(description = "ID do pedido.", example = "1")
    private Long id;

    @Schema(description = "Data de criação do pedido.", example = "2026-04-09T20:00:00")
    private LocalDateTime orderDate;

    @Schema(description = "Endereço de entrega do pedido.", example = "Rua A, 123")
    private String deliveryAddress;

    @Schema(description = "Número do pedido.", example = "PED-20260409-001")
    private String orderNumber;

    @Schema(description = "Taxa de entrega do pedido.", example = "5.00")
    private BigDecimal deliveryFee;

    @Schema(description = "Status do pedido.", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Preço total do pedido.", example = "65.00")
    private BigDecimal totalPrice;

    @Schema(description = "ID do cliente que realizou o pedido.", example = "1")
    private Long clientId;

    @Schema(description = "ID do restaurante para o pedido.", example = "1")
    private Long restaurantId;
}
