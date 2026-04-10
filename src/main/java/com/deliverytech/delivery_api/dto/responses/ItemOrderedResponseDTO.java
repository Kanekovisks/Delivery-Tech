package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrderedResponseDTO {

    @Schema(description = "ID do item do pedido.", example = "1")
    private Long id;

    @Schema(description = "ID do produto.", example = "1")
    private Long productId;

    @Schema(description = "Nome do produto.", example = "Pizza de Queijo")
    private String productName;

    @Schema(description = "Quantidade deste produto no pedido.", example = "2")
    private Integer quantity;

    @Schema(description = "Preço unitário do produto.", example = "40.50")
    private BigDecimal unitPrice;

    @Schema(description = "Subtotal do item.", example = "81.00")
    private BigDecimal subtotal;
}
