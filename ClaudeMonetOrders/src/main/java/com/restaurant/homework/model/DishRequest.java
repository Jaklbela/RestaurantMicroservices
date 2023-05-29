package com.restaurant.homework.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class DishRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private int quantity;

    private boolean is_available;

}
