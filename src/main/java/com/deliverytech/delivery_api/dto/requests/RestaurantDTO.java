package com.deliverytech.delivery_api.dto.requests;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.validation.ValidCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para cadastro/atualização de restaurantes.")
public class RestaurantDTO {

    @Schema(description = "Nome do restaurante.", example = "Big's Hamburgueria")
    @NotBlank(message = "O campo de nome é obrigatório.")
    private String name;

    @Schema(description = "Categoria do restaurante.", example = "Hamburgueria")
    @NotBlank(message = "O campo de categoria é obrigatório.")
    @ValidCategory
    private String category;

    @Schema(description = "Endereço do restaurante.", example = "Avenida 1, 123")
    @NotBlank(message = "O campo de endereço é obrigatório.")
    private String address;

    @Schema(description = "Telefone do restaurante.", example = "1234-1234")
    @NotBlank(message = "O campo de telefone é obrigatório.")
    private String phone;

    @Schema(description = "Avaliação média do restaurante.", example = "4.5")
    @NotNull(message = "O campo de avaliação é obrigatório.")
    @PositiveOrZero(message = "Avaliação deve ser zero ou maior.")
    private BigDecimal rating;
}
