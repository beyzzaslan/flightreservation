package com.flight.flightreservation.service;

import org.springframework.stereotype.Service;

import com.flight.flightreservation.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;//Servisimizin içine arşivciyi yerleştiriyoruz.


}
