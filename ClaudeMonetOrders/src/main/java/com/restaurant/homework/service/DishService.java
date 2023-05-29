package com.restaurant.homework.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.restaurant.homework.model.*;
import com.restaurant.homework.repository.DishRepository;
import com.restaurant.homework.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Value("${server.port}")
    private String serverPort;

    public List<Dish> findAll() {
        final List<Dish> dishes = dishRepository.findAll();
        return dishes.stream().toList();
    }

    public Response get(final Integer dishId) throws JsonProcessingException {
        Response response = new Response();
        Optional<Dish> dishOptional = dishRepository.findById(dishId);
        if (dishOptional.isEmpty()) {
            response.setMessage(String.format(
                    "Dish with this id doesn't exist.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        Dish dish = dishOptional.get();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(dish);

        response.setMessage(String.format(
                json,
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    public Integer create(DishRequest dishRequest) {
        final Dish dish = new Dish();
        dish.setDish_id(dish.getDish_id());
        dish.setCreated_at(new Timestamp(System.currentTimeMillis()));
        dish.set_available(dishRequest.is_available());
        dish.setDecription(dishRequest.getDescription());
        dish.setName(dishRequest.getName());
        dish.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        dish.setPrice(dishRequest.getPrice());
        dish.setQuantity(dishRequest.getQuantity());
        return dishRepository.save(dish).getDish_id();
    }

    public void update(final Integer dishId, DishRequest dishRequest) throws ChangeSetPersister.NotFoundException {
        final Dish dish = dishRepository.findById(dishId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        dish.set_available(dishRequest.is_available());
        dish.setDecription(dishRequest.getDescription());
        dish.setName(dishRequest.getName());
        dish.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        dish.setPrice(dishRequest.getPrice());
        dish.setQuantity(dishRequest.getQuantity());
        dishRepository.save(dish);
    }

    public void delete(final Integer dishId) {
        dishRepository.deleteById(dishId);
    }

    public List<Dish> getAvailablePositions() {
        return dishRepository.findAll().stream().filter(s -> s.is_available() && s.getQuantity() > 0).toList();
    }
}
