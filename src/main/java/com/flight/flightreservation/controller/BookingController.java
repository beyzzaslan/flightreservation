package com.flight.flightreservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO) {

        try {
            BookingDTO booking = bookingService.createBooking(bookingCreateDTO);
            return ResponseEntity.ok(booking);
        } catch (FlightNotFoundException | UserNotFoundException | BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO){
        try{
            BookingDTO booking = bookingService.createBooking(bookingCreateDTO);
            return ResponseEntity.ok(booking);
        }
        catch(FlightNotFoundException | UserNotFoundException BusinessRuleException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}

// BookingController, rezervasyon oluşturma ve rezervasyon listeleme isteklerini
// BookingService üzerinden yönetir.
