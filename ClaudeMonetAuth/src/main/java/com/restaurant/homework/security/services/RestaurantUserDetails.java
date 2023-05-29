package com.restaurant.homework.security.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.restaurant.homework.model.RestaurantUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RestaurantUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private int user_id;

    private String username;

    private String email;

    @JsonIgnore
    private String password_hash;

    private String role = "customer";

    private Timestamp created_at;

    private Timestamp updated_at;

    private Collection<? extends GrantedAuthority> authorities;

    public RestaurantUserDetails(int user_id, String username, String email, String password_hash, String role,
                           Timestamp created_at, Timestamp updated_at,
                           Collection<? extends GrantedAuthority> authorities) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.authorities = authorities;
    }

    public static RestaurantUserDetails build(RestaurantUser user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        return new RestaurantUserDetails(
                user.getUser_id(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword_hash(),
                user.getRole(),
                user.getCreated_at(),
                user.getUpdated_at(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public int getId() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password_hash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RestaurantUserDetails user = (RestaurantUserDetails) o;
        return Objects.equals(user_id, user.user_id);
    }
}