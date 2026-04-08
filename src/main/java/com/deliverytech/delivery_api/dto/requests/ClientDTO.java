package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClientDTO {
    @NotBlank(message = "O campo de nome é obrigatório.")
    private String name;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O campo de e-mail é obrigatório.")
    private String email;

    @Pattern(regexp = "^(?:\\+55\\s?)?(?:\\([1-9]{2}\\)|[1-9]{2})\\s?(?:9\\d{4}|\\d{4})[-\\s]?\\d{4}$", 
    message = "Formato de telefone inválido. Use (XX) XXXXX-XXXX.")
    @NotBlank(message = "O campo de telefone é obrigatório.")
    private String phone;

    @Size(min = 5, message = "Endereço deve ter no mínimo 5 caracteres.")
    @NotBlank(message = "O campo de endereço é obrigatório.")
    private String address; 

}
