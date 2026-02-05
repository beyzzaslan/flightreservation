package com.flight.flightreservation.repository;

import com.flight.flightreservation.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Kullanıcılar için: Sadece kalkış ve varış yerine göre uçuşları listele
    List<Flight> findByDepartureAirportAndArrivalAirport(String departure, String arrival);

    // Admin/Sistem için: Tekil olan uçuş numarasına göre uçuşu bul
    Optional<Flight> findByFlightNumber(String flightNumber);
}