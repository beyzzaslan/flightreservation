package com.flight.flightreservation.config;

import com.flight.flightreservation.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //bu metota uygulamanın güvenlik kurallarını tanımlayacağız
        http
        .csrf(csrf -> csrf.disable())//csrf korumasını devre dışı bırakıyoruz
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/swagger-resources/**").permitAll() //swagger erişimi için izin veriyoruz ve permitAll() ile giriş yapmadan erişebiliyoruz
            .requestMatchers("/api/users/login","/api/users/register").permitAll() // Kullanıcı daha login olmadan login endpoint’ine girebilmelidir.
            .requestMatchers("/api/flights/search").permitAll()//uçuş arama herkesin yapacağı bir iş olabilir
            .requestMatchers(HttpMethod.POST,"/api/bookings/create").permitAll() //Bu endpoint sadece POST ise eşleşsin diyorsun.
            .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasRole("ADMIN")  // Bu endpointlere sadece admin girsin diyorsun.                   .requestMatchers(HttpMethod.POST, "/api/flights/addFlights").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/flights/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/flights/{id}").hasRole("ADMIN")
            .anyRequest().authenticated()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)  //Benim JWT filter’ım, Spring’in standart authentication filter’ından önce çalışsın.

        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//kullanıcı şifrelerii düz metin olarak saklanmaz
        
    }


}
