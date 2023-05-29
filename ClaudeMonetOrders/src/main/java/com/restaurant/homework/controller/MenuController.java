package com.restaurant.homework.controller;

import com.restaurant.homework.model.Dish;
import com.restaurant.homework.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class MenuController {

    @Autowired
    private DishService dishService;

    @GetMapping("/menu")
    public List<Dish> getMenu() {
        return dishService.getAvailablePositions();
    }
}
