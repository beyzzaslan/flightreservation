package com.flight.flightreservation.model.entity;

import com.flight.flightreservation.model.enums.Role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;
    private String password;
    private String role;
}
