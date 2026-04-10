package com.deliverytech.delivery_api.dto.requests;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para cadastro de pedidos.")
public class OrderDTO {

    @Schema(description = "ID do cliente que fez o pedido.", example = "1")
    @NotNull(message = "O campo clientId é obrigatório.")
    private Long clientId;

    @Schema(description = "ID do restaurante que recebe o pedido.", example = "1")
    @NotNull(message = "O campo restaurantId é obrigatório.")
    private Long restaurantId;

    @Schema(description = "Endereço de entrega do pedido.", example = "Rua A, 123")
    @NotBlank(message = "O campo de endereço de entrega é obrigatório.")
    private String deliveryAddress;

    @Schema(description = "Taxa de entrega do pedido.", example = "5.00")
    @NotNull(message = "O campo de taxa de entrega é obrigatório.")
    @PositiveOrZero(message = "A taxa de entrega deve ser zero ou maior.")
    private BigDecimal deliveryFee;
}
