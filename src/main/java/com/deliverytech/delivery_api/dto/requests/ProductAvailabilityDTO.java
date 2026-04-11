package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAvailabilityDTO {

    @Schema(description = "Define se o produto está disponível.", example = "true")
    @NotNull(message = "O campo available é obrigatório.")
    private Boolean available;
}
