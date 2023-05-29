package com.restaurant.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "USER_DATA")
public class RestaurantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "USER_ID")
    private int user_id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD_HASH")
    private String password_hash;

    @Column(name = "ROLE")
    private String role = "customer";

    @Column(name = "CREATED_AT")
    private Timestamp created_at;

    @Column(name = "UPDATED_AT")
    private Timestamp updated_at;

}