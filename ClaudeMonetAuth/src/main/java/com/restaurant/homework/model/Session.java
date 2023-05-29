package com.restaurant.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "SESSION")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "SESSION_ID")
    private int session_id;

    @Column(name = "USER_ID")
    private int user_id;

    @Column(name = "SESSION_TOKEN")
    private String session_token;

    @Column(name = "EXPIRES_AT")
    private Timestamp expires_at;

}

