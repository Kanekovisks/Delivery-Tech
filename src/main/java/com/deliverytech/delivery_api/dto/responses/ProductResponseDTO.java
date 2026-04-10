package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {

    @Schema(description = "ID do produto.", example = "1")
    private Long id;

    @Schema(description = "Nome do produto.", example = "Pizza de Queijo")
    private String name;

    @Schema(description = "Descrição do produto.", example = "Pizza de queijo ao forno")
    private String description;

    @Schema(description = "Categoria do produto.", example = "Pizza")
    private String category;

    @Schema(description = "Preço do produto.", example = "40.50")
    private BigDecimal price;

    @Schema(description = "Indica se o produto está disponível.", example = "true")
    private Boolean available;

    @Schema(description = "ID do restaurante ao qual o produto pertence.", example = "1")
    private Long restaurantId;
}
