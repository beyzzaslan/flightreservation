package com.flight.flightreservation.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.flight.flightreservation.model.entity.User;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(unique = true, nullable = false)
    private String bookingNumber; // Rezervasyon numarası - Unique olmalı

    private LocalDateTime bookingDate = LocalDateTime.now(); // Rezervasyon tarihi - Otomatik

    // İLİŞKİLER (N-1)

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="flight_id", nullable = false)
    private Flight flight;
}
