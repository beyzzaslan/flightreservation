package com.flight.flightreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private String bookingNumber;
    private String flightNumber;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
}
