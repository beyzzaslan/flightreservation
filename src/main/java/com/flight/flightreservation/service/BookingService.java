package com.flight.flightreservation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.flightreservation.dto.BookingCreateDTO;
import com.flight.flightreservation.dto.BookingDTO;
import com.flight.flightreservation.dto.converter.BookingConverter;
import com.flight.flightreservation.exception.BookingException;
import com.flight.flightreservation.exception.FlightNotFoundException;
import com.flight.flightreservation.model.entity.Booking;
import com.flight.flightreservation.model.entity.Flight;
import com.flight.flightreservation.model.entity.User;
import com.flight.flightreservation.model.enums.Role;
import com.flight.flightreservation.repository.BookingRepository;
import com.flight.flightreservation.repository.FlightRepository;
import com.flight.flightreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingConverter bookingConverter;

    // rezervasyon oluşturma metodu
    @Transactional // transactional : Eğer rezervasyon sırasında bir hata oluşursa (örneğin
                   // kullanıcı kaydedildi ama bilet kaydedilemedi), veritabanında yarım yamalak
                   // veri kalmasın, yapılan tüm işlemleri geri alsın (rollback) diye kullanıyoruz.
    public BookingDTO createBooking(BookingCreateDTO bookingCreateDTO) {
        // uçuşu buluyouz
        Flight flight = flightRepository.findByFlightNumber(bookingCreateDTO.getFlightNumber())
                .orElseThrow(() -> new FlightNotFoundException(
                        "Flight not found with flight number : " + bookingCreateDTO.getFlightNumber()));

        // uçuşu bulduk ama önce uçakta yer var mı kontrolü yapmamız lazım
        if (!isFlightAvailable(flight)) {
            throw new BookingException("Flight is fully booked");
        }
        // eğer bilet alan kişi sistemde kayıtlı değilse onu o an otomatik kaydediyoruz
        User user = userRepository.findByEmail(bookingCreateDTO.getPassengerEmail())
                .orElseGet(() -> createUser(bookingCreateDTO));

        // orElseGet: Eğer kullanıcıyı bulamazsa createUser metodunu çalıştırıp yeni bir
        // kullanıcı oluşturuyor. Bu sayede "önce üye ol sonra bilet al" zorunluluğunu
        // ortadan kaldırmış, kullanıcı deneyimini artırmış.

        // 1. Yeni bir Booking (Entity) nesnesi oluşturuyoruz
        Booking booking = new Booking();
        booking.setUser(user); // Bileti alan yolcu
        booking.setFlight(flight); // Hangi uçuş olduğu
        booking.setBookingNumber(generateBookingNumber()); // Rastgele üretilen BN... numarası

        // 2. Veritabanına kalıcı olarak kaydediyoruz
        bookingRepository.save(booking);

        // 3. Entity'yi DTO'ya çeviriyoruz (Dönüşüm Başlıyor)
        BookingDTO bookingDTO = bookingConverter.convertToDto(booking);

        // 4. EKSİK OLAN KISIM: DTO içindeki yolcu bilgilerini dolduruyoruz
        bookingDTO.setPassengerName(user.getName());
        bookingDTO.setPassengerEmail(user.getEmail());
        bookingDTO.setPassengerPhone(user.getPhoneNumber());

        // 5. Tamamlanmış paketi (DTO) geri gönderiyoruz
        return bookingDTO;

    }

    public List<BookingDTO> getAllBookings() {
        // 1. Repository üzerinden tüm Booking (Entity) kayıtlarını çekiyoruz
        List<Booking> bookings = bookingRepository.findAll();
        // 2. Dönüş yapacağımız boş DTO listesini hazırlıyoruz
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        // 3. Her bir rezervasyon için döngü başlatıyoruz
        for (Booking booking : bookings) {

            // Entity'yi temel DTO'ya çeviriyoruz
            BookingDTO bookingDTO = bookingConverter.convertToDto(booking);
            // DTO'nun içindeki yolcu bilgilerini User entity'sinden çekerek dolduruyoruz
            bookingDTO.setPassengerName(booking.getUser().getName());
            bookingDTO.setPassengerEmail(booking.getUser().getEmail());
            bookingDTO.setPassengerPhone(booking.getUser().getPhoneNumber());

            bookingDTOS.add(bookingDTO);
        }
        return bookingDTOS;
    }

    // uçağın kapasitesinin dolup dolmadığını denetleyerek aşırı bilet satışını
    // (overbooking) engellemektir.

    private boolean isFlightAvailable(Flight flight) {
        // 1. Veritabanına gidip bu uçuş (flight) nesnesiyle ilişkili kaç adet booking
        // kaydı olduğunu sayıyoruz.
        int bookedSeats = bookingRepository.countByFlight(flight);
        // 2. Uçağın toplam kapasitesini, halihazırda satılmış bilet sayısıyla
        // kıyaslıyoruz.
        return flight.getCapacity() > bookedSeats;
    }

    // Biletlerin üzerine yazılacak olan, karmaşık ve profesyonel görünen o
    // benzersiz kodu burada üretiyoruz.
    private String generateBookingNumber() {
        Random random = new Random();
        // 1. 100,000 ile 999,999 arasında rastgele bir tam sayı üretiyoruz.
        int number = random.nextInt(90000) + 100000;
        // 2. Başına "BN" (Booking Number) ekliyoruz ve String.format ile 6 haneli
        // olmasını garantiliyoruz.
        return "BN" + String.format("%06d", number);

    }

    // Kullanıcı bilet alırken sistemde e-postası kayıtlı değilse, onu o an sisteme
    // kaydediyoruz bu yazdığımız aşağıdaki metotla
    private User createUser(BookingCreateDTO bookingDTO) {
        // 1. Yeni bir User entity nesnesi oluşturuyoruz.
        User newUser = new User();
        // 2. DTO'dan gelen yolcu bilgilerini yeni kullanıcı nesnemize aktarıyoruz.
        newUser.setName(bookingDTO.getPassengerName());
        newUser.setEmail(bookingDTO.getPassengerEmail());
        newUser.setPhoneNumber(bookingDTO.getPassengerPhone());
        // 3. Daha önce yazdığımız Role enum'ını kullanarak ona "USER" yetkisi
        // veriyoruz.
        newUser.setRole(Role.USER);
        // 4. Bu yeni kullanıcıyı veritabanına kaydedip, kaydedilmiş halini geri dönüyoruz.
        return userRepository.save(newUser);


    }
}