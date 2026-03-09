package com.flight.flightreservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component // spring bu classı bean olarak yönetsin diye @Component ekliyoruz
public class JwtTokenFilter extends OncePerRequestFilter {
    // her request için bu filter bi kere çalışsın

    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        // spring bu util classı filter'a verir
    }

    @Override
    // her rewuest geldiğinde bu metod çalışır
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = extractJwtFromRequest(request);// Request header içindeki Authorization değerinden JWT’yi ayıklar
        if (token != null && jwtTokenUtil.validateToken(token)) {
            // yani token hala geçerli ise
            JwtUserDetails userDetails = jwtTokenUtil.getUserDetailsFromJwt(token);
            // token içindeki bilgileri kullanarak bir UserDetails nesnesi oluşturur

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                // bu nesne Spring Security’nin Authentication nesnesidir ve kullanıcı doğrulama
                // bilgilerini
                SecurityContextHolder.getContext().setAuthentication(auth);// Bu request artık authenticated user ile
                                                                           // geliyor.
                /*
                 * Bu requestteki kullanıcı doğrulandı. Bu kullanıcı budur.
                 * UsernamePasswordAuthenticationToken:
                 * principal → userDetails
                 * credentials → null
                 * authorities → roller
                 */
            }

            chain.doFilter(request, response);// Filter işini bitirdi, request sonraki katmana geçsin.

        }
    }
       //Bu metodun işi sadece header’dan token almak
        private String extractJwtFromRequest(HttpServletRequest request){
            String bearerToken = request.getHeader("Authorization");
            if(bearerToken != null && bearerToken.startsWith("Bearer ")){
                return bearerToken.substring(7);
                // "Bearer " ifadesi 7 karakter olduğu için substring ile token kısmını alıyoruz
            }
            return null;    
        }
}

/*
extractJwtFromRequest(request)
Header’dan tokenı alır.

validateToken(token)
Token sahte mi, süresi dolmuş mu kontrol eder.

getUserDetailsFromJwt(token)
Token içinden kullanıcıyı çıkarır.

UsernamePasswordAuthenticationToken
Spring Security için authentication nesnesi oluşturur.

SecurityContextHolder.getContext().setAuthentication(auth)
Kullanıcıyı o request için sisteme giriş yapmış hale getirir.

chain.doFilter(...)
Request devam eder.

*/
