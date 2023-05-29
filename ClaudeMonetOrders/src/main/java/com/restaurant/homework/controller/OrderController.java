package com.restaurant.homework.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurant.homework.model.*;
import com.restaurant.homework.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createorder")
    public ResponseEntity<Response> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @PostMapping(path = "/getorder")
    public ResponseEntity<Response> getOrder(@RequestBody GetOrderRequest orderRequest) throws JsonProcessingException {
        return ResponseEntity.ok(orderService.getOrder(orderRequest));
    }

}
