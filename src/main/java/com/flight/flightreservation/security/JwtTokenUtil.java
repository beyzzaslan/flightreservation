package com.flight.flightreservation.security;

import com.flightreservation.model.enums.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // yani bu class bir bean istediğin yere @autowired ile kullan

public class JwtTokenUtil {
    /*
     * 1️⃣ JWT üretmek (login sonrası)
     * 2️⃣ JWT üretmek (sadece userId ile)
     * 3️⃣ Token içinden userId almak
     * 4️⃣ Token içinden user bilgisi oluşturmak
     * 5️⃣ Token geçerli mi kontrol etmek
     */

    @Value("${jwt.secret.key")
    private String SECRET_KEY;

    @Value("${jwt.token.expiration.time}")
    private long TOKEN_EXPIRATION_TIME;

    public String generateJwtToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + TOKEN_EXPIRATION_TIME);
        return Jwts.builder().setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .claims("email", userDetails.getEmail())
                .claims("name", userDetails.getName())
                .claims("role", userDetails.getRole().name())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

    }

    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + TOKEN_EXPIRARION_TIME);// şu anki zaman + token süresi
        // jwt güvenliği için token süresi olmak zorunda
        return Jwts.builder().setSubject(Long.toString(userId)).setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

    }

    public Long getUserIdFromJwt(String token) {
        // token alır ve userId döndürür
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public JwtUserDetails getUserDetailsFromJwt(String token) {
        // bu metot token alır ve jwtuserdetails döndürür
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        // bu yukarıdaki kısım jwt yi okuyor ve içindeki bilgileri çıkarıyor

        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String phoneNumber = claims.get("phoneNumber", String.class);
        Role role = Role.fromString(claims.get("role", String.class));

        return new JwtUserDetails(userId, email, null, name, phoneNumber, role);

    }

    public boolean validateToken(String token){
        //bu metot token alır ve true false dönderir
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !isTokenExpired(token);//token süresi dolmuş mu
        }catch(SignatureException | MalformedJwtException | ExpiredJwsException | UnsupportedJwtException | IllegalArgumentException e)
        return false;
    }

    // tokenin süresinin dolup dolmadığını koontrol ederiz
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

}
