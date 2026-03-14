package com.flight.flightreservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.flightreservation.dto.FlightDTO;
import com.flight.flightreservation.model.entity.Flight;
import com.flight.flightreservation.service.FlightService;

@RestController
@RequestMapping("/api/flights")

public class FlightController {
    // amaç uçuş arama , silme , ekleme ve güncelleme işlemlerini yapacağız
    // mantık basit request al serviceye gönder ve sonucu dön

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
        // spring bu controlleri oluştururken flightService nesnesini içine verir ve
        // buna da dependency injection denir

    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam("departure") String departureAirport,
            @RequestParam("arrival") String arrivalAirpport) {

        List<FlightDTO> flights = flightService.searchFlights(departureAirport, arrivalAirpport);
        return ResponseEntity.ok(flights);
        // yani controller diyor ki ben arama işini serviceye veriyorum

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addFlights")
    public ResponseEntity<Flight> addFlight(@RequestBody FlightDTO flightDTO) {
        // BU UÇUŞ EKLEMEYİ SADECE ADMIN YAPABİLİR ONDAN PREAUTHORİZE VERİYORUZ
        Flight flight = flightService.addFlight(flightDTO);
        return ResponseEntity.ok(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flightDTO) {
        Flight flight = flightService.updateFlight(id, flightDTO);
        return ResponseEntity.ok(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok("Flight with id " + id + "has been successfully deleted.");

    }

}
