package com.flight.flightreservation.service;

import com.flight.flightreservation.dto.UserLoginDTO;
import com.flight.flightreservation.dto.UserRegistrationDTO;
import com.flight.flightreservation.dto.converter.UserRegistrationConverter;
import com.flight.flightreservation.exception.AuthException; // Eksikti, eklendi
import com.flight.flightreservation.exception.BusinessRuleException; // Eksikti, eklendi
import com.flight.flightreservation.model.entity.User;
import com.flight.flightreservation.model.enums.Role; // Eksikti, eklendi
import com.flight.flightreservation.repository.UserRepository;
import com.flight.flightreservation.security.JwtTokenUtil; // Eksikti, eklendi
import com.flight.flightreservation.security.JwtUserDetails; // Eksikti, eklendi
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // Eksikti, eklendi
import org.springframework.security.core.AuthenticationException; // Eksikti, eklendi
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Lazy // UserService, FlightService'e bağımlı olabilir ve FlightService de
      // UserService'e bağımlı olabilir. Bu durumda birbirlerini çağırırken sonsuz
      // döngüye girebilirler. @Lazy anotasyonu, Spring'in bu sınıfları ihtiyaç
      // duyulana kadar oluşturmasını engeller ve böylece bu tür döngüleri önler.
public class UserService implements UserDetailsService {
    // UserDetailsService Spring'e "Kullanıcıyı nasıl bulacağını ben sana
    // öğreteceğim" demektir.

    private final UserRepository userRepository;
    private final UserRegistrationConverter userRegistrationConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil; // kimlik kartı token basmak için

    private AuthenticationManager authenticationManager; // Giriş bilgilerini kontrol eden ana mekanizma.

    @Autowired // AuthenticationManager'ı güvenli bir şekilde içeri almak için setter injection
               // kullandık.
    public void setAuthenticationManager(AuthenticationManager authenticationManager) { // Yazım hatası düzeltildi
        this.authenticationManager = authenticationManager;
    }

    public String login(UserLoginDTO userLoginDTO) {
        // Kullanıcı bir kez giriş yaptıktan sonra, her seferinde kullanıcı adını ve
        // şifresini göndermez. Bunun yerine senin ona verdiğin o String (Token)
        // değerini gönderir. Backend bu "String"e bakarak "Tamam, bu kişi giriş yapmış
        // biri, geçebilir" der.

        // Adım 1 (Kimlik Doğrulama): authenticationManager.authenticate metoduna email
        // ve şifreyi veriyoruz. Bu arkadaş arka planda veritabanına gidip şifreleri
        // karşılaştırır.

        // Adım 2 (Hata Yakalama): Eğer şifre yanlışsa AuthException fırlatıyoruz.

        // Adım 3 (Token Üretimi): Eğer giriş başarılıysa, jwtTokenUtil ile kullanıcıya
        // özel bir JWT Token (karakter dizisi) üretip dönüyoruz.

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
            return jwtTokenUtil.generateJwtToken(authentication);

        } catch (AuthenticationException e) {
            throw new AuthException("Hatalı email veya giriş");
        }
    }

    public User registerUser(UserRegistrationDTO userRegistrationDTO) {
        // Burda aynı emaile sahip iki kişi olmaısn diye mail kontrolü yapıyoruz
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new BusinessRuleException("Bu e posta adresi zaten kayıtlı.");
        }
        
        // Gelen kayıt paketini (DTO), veritabanı nesnesine (User) çeviriyoruz.
        User user = userRegistrationConverter.convertToUser(userRegistrationDTO); // Yazım hatası düzeltildi
        
        // Kullanıcının gerçek şifresini asla kaydetmeyiz. passwordEncoder.encode ile
        // onu tanınmaz hale getiriyoruz.
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    // ID ile bulma ve Listeleme)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Kullanıcı bulunamadı "));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Security bağlantısı
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email bulunamadı " + email));

        return new JwtUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getRole());
    }
}