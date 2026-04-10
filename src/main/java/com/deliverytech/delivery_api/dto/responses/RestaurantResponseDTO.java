package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDTO {

    @Schema(description = "ID do restaurante.", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante.", example = "Big's Hamburgueria")
    private String name;

    @Schema(description = "Categoria do restaurante.", example = "Hamburgueria")
    private String category;

    @Schema(description = "Endereço do restaurante.", example = "Avenida 1, 123")
    private String address;

    @Schema(description = "Telefone do restaurante.", example = "1234-1234")
    private String phone;

    @Schema(description = "Avaliação média do restaurante.", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Indica se o restaurante está ativo.", example = "true")
    private Boolean active;
}
