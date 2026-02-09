package com.flight.flightreservation.service;

import org.springframework.stereotype.Service;
import com.flight.flightreservation.dto.converter.FlightConverter; // benim yazdığın converter
import com.flight.flightreservation.dto.FlightDTO;
import com.flight.flightreservation.model.entity.Flight;
import com.flight.flightreservation.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final olan alanlar için otomatik constructor oluşturur.
public class FlightService {

    private final FlightRepository flightRepository;// Servisimizin içine arşivciyi yerleştiriyoruz.
    private final FlightConverter flightConverter;// Entity-Dto dönüşümü yapmak için kullanılır

    // İlk olarak uçuş ekleme metodu yazalım
    // "Admin Uçuş Ekleme" senaryosunu koda döküyoruz. Burada kritik olan şey;
    // dışarıdan bir FlightDTO alıp, onu veritabanına kaydetmek için Flight
    // entity'sine çevirmektir.

    public FlightDTO addFlight(FlightDTO flightDTO) {
        Flight flight = flightConverter.convertToEntity(flightDTO);// dto yu entitye çeviriyoruz
        return flightRepository.save(flight);// veritabanına kaydediyoruz
    }

    // Bir kullanıcı belirli bir ID ile uçuş aradığında, o ID veritabanında
    // olmayabilir. Bu durumda "Null" dönmek yerine, hata fırlatmak en profesyonel
    // yaklaşımdır.
    public Flight getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with by id : " + id));
        return flight;
    }


    









}
