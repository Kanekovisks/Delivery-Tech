package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public interface ItemOrderedDTO {
    String getProductName();
    Integer getQuantity();
    BigDecimal getSubtotal();
}
