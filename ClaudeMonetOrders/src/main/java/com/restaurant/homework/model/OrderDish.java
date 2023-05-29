package com.restaurant.homework.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "ORDER_DISH")
public class OrderDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "ORDER_DISH_ID")
    private int order_dish_id;

    @Column(name = "ORDER_ID")
    private int order_id;

    @Column(name = "DISH_ID")
    private int dish_id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

}
