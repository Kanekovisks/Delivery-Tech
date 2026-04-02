package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public interface SalesPerRestaurant {
    String getRestaurantName();
    BigDecimal getTotalSales();
}
