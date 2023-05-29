package com.restaurant.homework.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.restaurant.homework.model.*;
import com.restaurant.homework.repository.DishRepository;
import com.restaurant.homework.repository.OrderRepository;
import com.restaurant.homework.util.EStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@EnableScheduling
@EnableAsync
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Value("${server.port}")
    private String serverPort;

    Random random = new Random(451);

    @Transactional
    public Response createOrder(CreateOrderRequest orderRequest) {
        Response response = new Response();

        Order newOrder = new Order();
        newOrder.setUser_id(orderRequest.getUser_id());
        newOrder.setStatus(EStatus.CREATED.toString());

        List<Dish> positions = dishRepository.findAll();

        for (var position : orderRequest.getPositions()) {
            Optional<Dish> dishOptional = positions.stream().filter(d -> d.getName().equals(position.getName())).findFirst();
            if (dishOptional.isEmpty()) {
                response.setMessage(String.format(
                        "Dish with name %s doesn't exist.",
                        position.getName(),
                        serverPort
                ));

                response.setSuccess(false);
                response.setCode(401);
                return response;
            }
            Dish dish = dishOptional.get();
            if (!dish.is_available() || dish.getQuantity() == 0) {
                response.setMessage(String.format(
                        "Position %s is unavailable.",
                        dish.getName(),
                        serverPort
                ));

                response.setSuccess(false);
                response.setCode(401);
                return response;
            }
            if (dish.getQuantity() < position.getQuantity()) {
                response.setMessage(String.format(
                        "Not enough products to create an order.",
                        serverPort
                ));

                response.setSuccess(false);
                response.setCode(401);
                return response;
            }
            newOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newOrder.setSpecial_requests(orderRequest.getSpecial_requests());

            dish.setQuantity(dish.getQuantity() - position.getQuantity());
        }

        orderRepository.save(newOrder);
        dishRepository.saveAll(positions);

        response.setMessage(String.format(
                "New order was created.",
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void processOrder() throws InterruptedException {
        Optional<Order> orderOptional = orderRepository.findTopByStatusOrderByCreatedAtAsc(EStatus.CREATED.toString());
        if (orderOptional.isEmpty()) {
            return;
        }

        Order order = orderOptional.get();
        order.setStatus(EStatus.COOKING.toString());
        orderRepository.save(order);

        Thread.sleep(1000L * (10 + Math.abs(random.nextInt()) % 60));

        order.setStatus(EStatus.FINISHED.toString());
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
    }

    public Response getOrder(GetOrderRequest orderRequest) throws JsonProcessingException {
        Response response = new Response();
        Optional<Order> orderOptional = orderRepository.findById(orderRequest.getOrder_id());
        if (orderOptional.isEmpty()) {
            response.setMessage(String.format(
                    "No order witch such id.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        Order order = orderOptional.get();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order);

        response.setMessage(String.format(
                json,
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }
}
