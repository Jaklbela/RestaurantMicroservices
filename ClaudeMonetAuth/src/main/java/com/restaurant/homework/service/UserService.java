package com.restaurant.homework.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.restaurant.homework.model.*;
import com.restaurant.homework.repository.UserRepository;
import com.restaurant.homework.security.JwtUtils;
import com.restaurant.homework.security.services.RestaurantUserDetails;
import com.restaurant.homework.util.BCrypt;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    private static final Response SIMPLE_SUCCESS_RESPONSE = createSimpleSuccessResponse();

    private static final int BCRYPT_ROUNDS = 13;

    @Value("${server.port}")
    private String serverPort;

    @Transactional
    public Response createUser(RegistrationRequest registrationRequest) {
        Response response = new Response();
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            response.setMessage(String.format(
                    "User with this username is already exists.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        if (!registrationRequest.getEmail().contains("@")) {
            response.setMessage(String.format(
                    "Wrong email address.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            response.setMessage(String.format(
                    "User with this email is already exists.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        if (registrationRequest.getUsername().isEmpty() || registrationRequest.getPassword().isEmpty()) {
            response.setMessage(String.format(
                    "Email or password is missing.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        RestaurantUser newRestaurantUser = new RestaurantUser();

        newRestaurantUser.setUsername(registrationRequest.getUsername());
        newRestaurantUser.setPassword_hash(BCrypt.hashpw(registrationRequest.getPassword(), BCrypt.gensalt(BCRYPT_ROUNDS)));
        newRestaurantUser.setEmail(registrationRequest.getEmail());
        newRestaurantUser.setCreated_at(new Timestamp(System.currentTimeMillis()));

        userRepository.save(newRestaurantUser);


        response.setMessage(String.format(
                "Hello, %s! " +
                        "Welcome to Claude Monet Restaurant.",
                newRestaurantUser.getUsername(),
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    @Transactional
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        if (loginRequest.getLogin().isEmpty() || loginRequest.getPassword().isEmpty()) {
            response.setMessage(String.format(
                    "Email or password is missing.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        Optional<RestaurantUser> userOptional = userRepository.findByEmail(loginRequest.getLogin());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUsername(loginRequest.getLogin());
            if (userOptional.isEmpty()) {
                response.setMessage(String.format(
                        "User with this login doesn't exist.",
                        serverPort
                ));

                response.setSuccess(false);
                response.setCode(401);
                return response;
            }
        }

        RestaurantUser restaurantUser = userOptional.get();

        if (!checkPassword(restaurantUser.getPassword_hash(), loginRequest.getPassword())) {
            response.setMessage(String.format(
                    "Wrong password.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        RestaurantUserDetails userDetails = (RestaurantUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        restaurantUser.setUpdated_at(new Timestamp(System.currentTimeMillis()));


        response.setMessage(String.format(
                "Welcome back, %s! Your token is: %s",
                restaurantUser.getUsername(),
                jwt,
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    @Transactional
    public Response information(InformationRequest informationRequest) throws JsonProcessingException {
        Response response = new Response();

        if (informationRequest.getToken().isEmpty() || informationRequest.getRole().isEmpty()) {
            response.setMessage(String.format(
                    "Token or role is missing.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        String username;

        try {
            username = jwtUtils.getUserNameFromJwtToken(informationRequest.getToken());
        } catch (MalformedJwtException e) {
            response.setMessage(String.format(
                    "Wrong token.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        Optional<RestaurantUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            response.setMessage(String.format(
                    "Wrong token.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        RestaurantUser restaurantUser = userOptional.get();

        if (!Objects.equals(restaurantUser.getRole(), informationRequest.getRole())) {
            response.setMessage(String.format(
                    "Wrong role.",
                    serverPort
            ));

            response.setSuccess(false);
            response.setCode(401);
            return response;
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(restaurantUser);

        response.setMessage(String.format(
                json,
                serverPort
        ));

        response.setSuccess(true);
        response.setCode(200);
        return response;
    }

    private static Response createSimpleSuccessResponse() {
        Response returnValue = new Response();

        returnValue.setSuccess(true);
        return returnValue;
    }

    public static boolean checkPassword(String passwordHash, String password) {
        String salt = passwordHash.substring(0, 30);

        String newHash = BCrypt.hashpw(password, salt);
        return passwordHash.equals(newHash);
    }
}
