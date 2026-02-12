package com.flight.flightreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) //// 1. Spring'e bu hata için 401 (Unauthorized) kodu kullanmasını söylüyoruz
public class AuthException extends RuntimeException {
    // 2. Sadece hata mesajı ile fırlatmak için
    public AuthException(String message) {
        super(message);
    }

    // 3. Başka bir hatadan kaynaklanan (zincirleme) durumlar için
    public AuthException(String message, Throwable cause) {

        super(message, cause);
    }
}
