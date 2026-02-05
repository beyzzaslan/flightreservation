package com.flight.flightreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.flightreservation.model.entity.User;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Özel bir sorgu: Kullanıcıyı email adresiyle bulmak için (Login için gerekecek)

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
