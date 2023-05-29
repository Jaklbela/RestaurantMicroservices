package com.restaurant.homework.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurant.homework.model.*;
import com.restaurant.homework.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Response> registration(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.createUser(registrationRequest));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping(path = "/info")
    public ResponseEntity<Response> information(@RequestBody InformationRequest informationRequest) throws JsonProcessingException {
        return ResponseEntity.ok(userService.information(informationRequest));
    }
}
