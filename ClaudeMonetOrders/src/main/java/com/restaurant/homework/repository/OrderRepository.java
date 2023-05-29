package com.restaurant.homework.repository;

import com.restaurant.homework.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    Optional<Order> findTopByStatusOrderByCreatedAtAsc(String status);
}
