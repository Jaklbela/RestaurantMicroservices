package com.restaurant.homework.repository;

import com.restaurant.homework.model.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends CrudRepository<Dish, Integer> {

    List<Dish> findAll();
}
