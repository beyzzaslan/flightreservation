package com.flight.flightreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        // Dışarıdan bir hata mesajı alıyoruz (Örn: "Uçuş saati geçmiş!").
        super(message);
        // super(message): Aldığımız bu mesajı, miras aldığımız RuntimeException
        // sınıfına gönderiyoruz ki Java'nın kendi hata mekanizması bu mesajı tanısın.
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
