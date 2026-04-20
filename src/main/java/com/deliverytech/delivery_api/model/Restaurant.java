package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery_api.enums.RestaurantCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant")

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private RestaurantCategory category;
    private String address;
    private String phone;
    private BigDecimal rating;
    private BigDecimal deliveryFee;
    private Boolean active; 

   @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy= "restaurant", fetch=FetchType.LAZY)
    private List<Order> products = new ArrayList<>();

    @OneToMany(mappedBy= "restaurant", fetch=FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
