package com.restaurant.homework.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurant.homework.model.*;
import com.restaurant.homework.service.DishService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/manage")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishs() {
        return ResponseEntity.ok(dishService.findAll());
    }

    @GetMapping("/get/{dishId}")
    public ResponseEntity<Response> getDish(@PathVariable final Integer dishId) throws JsonProcessingException {
        return ResponseEntity.ok(dishService.get(dishId));
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> createDish(@RequestBody DishRequest dishRequest) {
        final Integer createdDishId = dishService.create(dishRequest);
        return new ResponseEntity<>(createdDishId, HttpStatus.CREATED);
    }

    @PutMapping("/update/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable final Integer dishId,
                                           @RequestBody DishRequest dishRequest) throws ChangeSetPersister.NotFoundException {
        dishService.update(dishId, dishRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable final Integer dishId) {
        dishService.delete(dishId);
        return ResponseEntity.noContent().build();
    }

}
