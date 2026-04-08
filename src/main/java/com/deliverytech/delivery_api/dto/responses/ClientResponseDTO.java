package com.deliverytech.delivery_api.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address; 
    private Boolean active;

    private Boolean isActive(){
        return active;
    };
}
