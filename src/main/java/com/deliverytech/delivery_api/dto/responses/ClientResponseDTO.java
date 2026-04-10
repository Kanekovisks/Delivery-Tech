package com.deliverytech.delivery_api.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
    @Schema(description = "ID do cliente.", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente.", example = "Gabriel Duarte")
    private String name;
    
    @Schema(description = "E-mail do cliente.", example = "email@exemplo.com")
    private String email;
    
    @Schema(description = "Telefone do cliente.", example = "(12)91234-1234")
    private String phone;
    
    @Schema(description = "Endereço do cliente.", example = "Avenida 1, 123")
    private String address; 
    
    private Boolean active;
}
