package com.flight.flightreservation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.flight.flightreservation.model.entity.Booking;
import com.flight.flightreservation.model.entity.Flight;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

        int countByFlight(Flight flight);

}
