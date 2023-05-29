package com.restaurant.homework.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;


@Data
@Entity
@Table(name = "DISH")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "DISH_ID")
    private int dish_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String decription;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "IS_AVAILABLE")
    private boolean is_available;

    @Column(name = "CREATED_AT")
    private Timestamp created_at;

    @Column(name = "UPDATED_AT")
    private Timestamp updated_at;

}
