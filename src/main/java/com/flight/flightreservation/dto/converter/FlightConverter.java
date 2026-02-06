package com.flight.flightreservation.dto.converter;

import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.flight.flightreservation.dto.FlightDTO;
import com.flight.flightreservation.model.entity.Flight;

@Component // Sen bir sınıfa @Component yazdığında, Spring'e şunu demiş olursun: "Bu sınıfı
           // sen yönet, lazım olduğunda içinden bir tane üret ve ben istediğimde bana ver

public class FlightConverter {

    private final ModelMapper modelMapper;// "kopyala-yapıştır" robotumuz. private yaptık çünkü sadece bu sınıfın içinde
                                          // kullanılsın istiyoruz.

    // Değişkeni yazdık ama içi boş. Spring'e demeliyiz ki: "Bu sınıfı çalıştırırken
    // bana bir tane ModelMapper robotu ver."
    public FlightConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Veritabanından bir uçuş çektik, bunu kullanıcıya göndereceğiz. Ama
    // kullanıcıya "Entity" değil, süslü bir "DTO" paketinde göndermeliyiz.
    public FlightDTO convertToDto(Flight flight) {    // Veritabanından Kullanıcıya (Entity -> DTO)

        return modelMapper.map(flight, FlightDTO.class);
        //modelMapper.map komutu şuna yarar: flight (Entity) içindeki verileri al, FlightDTO kalıbına dök ve bana bir DTO nesnesi ver.
    }


    //Bir de tersi lazım. Kullanıcı yeni bir uçuş eklemek istediğinde bize bir paket (DTO) gönderecek. Bizim bunu veritabanının anlayacağı Flight nesnesine çevirmemiz lazım.
    //Kullanıcıdan Veritabanına (DTO -> Entity)
    public Flight convertToEntity(FlightDTO flightDTO) {

        return modelMapper.map(flightDTO, Flight.class);
    }
    
}