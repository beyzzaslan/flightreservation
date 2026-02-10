package com.flight.flightreservation.service;

import org.springframework.stereotype.Service;
import com.flight.flightreservation.dto.converter.FlightConverter; // benim yazdığın converter
import com.flight.flightreservation.dto.FlightDTO;
import com.flight.flightreservation.model.entity.Flight;
import com.flight.flightreservation.repository.FlightRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final olan alanlar için otomatik constructor oluşturur.
public class FlightService {

    private final FlightRepository flightRepository;// Servisimizin içine arşivciyi yerleştiriyoruz.
    private final FlightConverter flightConverter;// Entity-Dto dönüşümü yapmak için kullanılır

    // İlk olarak uçuş ekleme metodu yazalım
    // "Admin Uçuş Ekleme" senaryosunu koda döküyoruz. Burada kritik olan şey dışarıdan bir FlightDTO alıp, onu veritabanına kaydetmek için Flight entity'sine çevirmektir.

    public FlightDTO addFlight(FlightDTO flightDTO) {
        Flight flight = flightConverter.convertToEntity(flightDTO);// dto yu entitye çeviriyoruz
        return flightRepository.save(flight);// veritabanına kaydediyoruz
    }

    // Bir kullanıcı belirli bir ID ile uçuş aradığında, o ID veritabanında olmayabilir. Bu durumda "Null" dönmek yerine, hata fırlatmak en profesyonel yaklaşımdır.
    public Flight getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with by id : " + id));
        return flight;
    }

    //Hiçbir filtreleme yapmadan veritabanındaki tüm uçuşları getiriyoruz.
    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    } 


    // Şimdi ise uçuşları güncellemeye çalışıyoruz.

    public Flight updateFlight(Long id, FlightDTO flightDTO){
        //Önce güncellenecek uçuşu ID ile veritabanından bul
        Flight flight = getFlightById(id);

        //Bilgileir güncelliyoruz
        flight.setFlightNumber(flightDTO.getFlightNumber());//flightDTO dan gelenleri okuyup sonra flight nesnesine yazıyoruz yani set ediyoruz
        flight.setCapacity(flightDTO.getCapacity());
        flight.setDepartureAirport(flightDTO.getDepartureAirport());
        flight.setArrivalAirport(flightDTO.getArrivalAirport());
        flight.setDepartureDate(flightDTO.getDepartureDate());
        flight.setArrivalDate(flightDTO.getArrivalDate());
        flight.setPrice(flightDTO.getPrice());
        //şimdide update ettiğimiz haliyle tekrar kaydediyoruz
        return flightRepository.save(flight);//satabaseye tekrar kaydettik

    }

    // admin bir uçuşu iptal etmek yada silmek isterse bu metodu kullanacaktır.
    public void deleteFlight(Long id){
        flightRepository.deleteById(id);
    }



    // şimdi de Kullanıcı "İstanbul'dan Ankara'ya uçuşları gör" dediğinde, bu verileri veritabanından süzüp getirmemiz gerekir.
    //  Ancak profesyonel bir yaklaşımda, ham veriyi (Entity) değil, paketlenmiş veriyi (DTO) dönmelisiniz.


    //yani önce gelen parametreleri kontrol edecez yani doğrulama yapacağız
    //sonra repositoryde eşleşen uçuşları liste olarak çekeceğiz
    //sonra bu listeyi converter ile DTO listesine çevirip kullanıcıya sunacağız

    public List<FlightDTO> searchFlights(String departure, String arrival){
        //önce kapıdaki güvenliği (validasyonu) çağırıyoruz
        validateSearchParameters(departure, arrival);
        //veritabanından filtreli aramayı yapıyoruz
        List<Flight> flights = flightRepository.findByDepartureAirportAndArrivalAirport(departure, arrival);

        //bulunan uçuşları kullanıcıya göndermek üzere DTO'ya çeviriyoruz
        return flights.stream().map(flightConverter::convertToDto).toList();
    }

    private void validateSearchParameters(String departure, String arrival){
        if(departure == null || departure.isBlank()){
            throw new IllegalArgumentException("Kalkış havalimanı bilgisi boş olamaz");
        }
        if(arrival == null || arrival.isBlank()){
            throw new IllegalArgumentException("Varış havalimanı bilgisi boş olamaz");
        }
    }




}
