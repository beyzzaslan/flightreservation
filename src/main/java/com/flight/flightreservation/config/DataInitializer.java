package com.flight.flightreservation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flight.flightreservation.model.entity.User;
import com.flight.flightreservation.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init() {

        return args -> {
            User adminUser = new User();
            adminUser.setUsername("Beyza");
            adminUser.setEmail("beyza@gmail.com");
            adminUser.setPhoneNumber("1234");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);

            System.out.println("Admin kullanıcı oluşturuldu.");
        };
    }
}
/*
Uygulama başlar
↓
DataInitializer çalışır
↓
Admin kullanıcı oluşturulur
↓
Database'e kaydedilir 

*/