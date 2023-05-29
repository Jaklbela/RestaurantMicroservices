package com.restaurant.homework.repository;

import com.restaurant.homework.model.RestaurantUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<RestaurantUser, Long> {

    Optional<RestaurantUser> findByUsername(String username);

    Optional<RestaurantUser> findByEmail(String email);
}
