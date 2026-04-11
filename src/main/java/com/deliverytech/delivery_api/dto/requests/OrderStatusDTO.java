package com.deliverytech.delivery_api.dto.requests;

import com.deliverytech.delivery_api.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDTO {

    @Schema(description = "Status desejado para o pedido.", example = "DELIVERED")
    @NotNull(message = "O campo status é obrigatório.")
    private OrderStatus status;
}
