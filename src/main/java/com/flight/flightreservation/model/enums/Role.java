package com.flight.flightreservation.model.enums;

public enum Role {

    ADMIN, USER;

    public static Role fromString(String roleStr) {
        // Bu metodun tek bir görevi var: Dışarıdan gelen bir String veriyi alıp, bizim
        // güvenli Enum tipimize dönüştürmek.

        return Role.valueOfIgnoreCase(roleStr);
    }

    // Case-Insensitive (Büyük/Küçük Harf Duyarsızlığı) Mantığı
    public static Role valueOfIgnoreCase(String value) {
        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in enum Role");
    }
}