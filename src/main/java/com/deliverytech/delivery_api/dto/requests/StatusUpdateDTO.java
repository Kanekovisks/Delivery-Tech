package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateDTO {

    @Schema(description = "Define se o cliente deve ficar ativo.", example = "true")
    @NotNull(message = "O campo active é obrigatório.")
    private Boolean active;
}
