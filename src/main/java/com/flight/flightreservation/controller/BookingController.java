package com.flight.flightreservation.controller;
import com.flight.flightreservation.dto.BookingCreateDTO;
import com.flight.flightreservation.dto.BookingDTO;
import com.flight.flightreservation.exception.BusinessRuleException;
import com.flight.flightreservation.exception.FlightNotFoundException;
import com.flight.flightreservation.exception.UserNotFoundException;
import com.flight.flightreservation.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController //json alır ve json dönderir
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
        //BookingController çalışmak için BookingService’e ihtiyaç duyar

    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO) { //Client’tan gelen JSON veri, BookingCreateDTO nesnesine çevrilir.
        try {
            BookingDTO booking = bookingService.createBooking(bookingCreateDTO);
            return ResponseEntity.ok(booking);
        } catch (FlightNotFoundException | UserNotFoundException | BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping //tüm rezervasyonları listelemek için kullanılır
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}

// BookingController, rezervasyon oluşturma ve rezervasyon listeleme isteklerini
// BookingService üzerinden yönetir.
