package com.deliverytech.delivery_api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para cadastro/Atualização de clientes.")
public class ClientDTO {
    @Schema(description = "Nome do cliente.", example = "Gabriel Duarte")
    @NotBlank(message = "O campo de nome é obrigatório.")
    private String name;

    @Schema(description = "E-mail do cliente.", example = "email@exemplo.com")
    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O campo de e-mail é obrigatório.")
    private String email;

    @Schema(description = "Telefone do cliente.", example = "(12)91234-1234")
    @Pattern(regexp = "^(?:\\+55\\s?)?(?:\\([1-9]{2}\\)|[1-9]{2})\\s?(?:9\\d{4}|\\d{4})[-\\s]?\\d{4}$", 
    message = "Formato de telefone inválido. Use (XX) XXXXX-XXXX.")
    @NotBlank(message = "O campo de telefone é obrigatório.")
    private String phone;

    @Schema(description = "Endereço do cliente.", example = "Avenida 1, 123")
    @Size(min = 5, message = "Endereço deve ter no mínimo 5 caracteres.")
    @NotBlank(message = "O campo de endereço é obrigatório.")
    private String address; 

}
