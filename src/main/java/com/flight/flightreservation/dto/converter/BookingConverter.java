package com.flight.flightreservation.dto.converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.flight.flightreservation.dto.BookingDTO;
import com.flight.flightreservation.dto.BookingCreateDTO;
import com.flight.flightreservation.model.entity.Booking;

@Component
public class BookingConverter {
    private final ModelMapper modelMapper;

    public BookingConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    //Rezervasyonu Kullanıcıya Gösterme (Entity -> DTO)
    //Veritabanından bir rezervasyon kaydı çektik. Bunu kullanıcıya süslü bir paket (BookingDTO) içinde sunmamız lazım.
    public BookingDTO convertToData(Booking booking){
        return modelMapper.map(booking, BookingDTO.class);
    
    }

    //Yeni Rezervasyon Oluşturma (DTO -> Entity)
     public Booking convertToEntity(BookingCreateDTO bookingDTO) {
        return modelMapper.map(bookingDTO, Booking.class);
    }

}