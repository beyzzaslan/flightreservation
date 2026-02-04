package com.flight.flightreservation.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.security.core.parameters.P;

@Entity
@Table(name = "flights")
@Data
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDate;// kalkış tarihi
    private LocalDateTime arrivalDate;// varış tarihi
    private BigDecimal price;
    private int capacity;
}
