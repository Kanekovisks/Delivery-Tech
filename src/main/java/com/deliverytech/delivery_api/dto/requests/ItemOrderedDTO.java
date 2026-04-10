package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados de itens para inclusão em pedidos.")
public class ItemOrderedDTO {

    @Schema(description = "ID do produto.", example = "1")
    @NotNull(message = "O campo productId é obrigatório.")
    private Long productId;

    @Schema(description = "Quantidade do produto no pedido.", example = "2")
    @NotNull(message = "O campo quantidade é obrigatório.")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private Integer quantity;
}
