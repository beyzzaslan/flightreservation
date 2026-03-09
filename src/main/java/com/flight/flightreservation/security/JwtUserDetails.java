package com.flight.flightreservation.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.flightreservation.model.enums.Role;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private Role role;
    // kullanıcı ile ilgili bilgileir burada topluyoruz

    // şimdi constructor yazıyoruz
    public JwtUserDetails(Long id, String email, String password, String name, String phoneNumber, Role role) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
        // kullanıcıya tek rol veriyorum ondan singletonList kullanıyorum

    }

    @Override
    public String getPassword() {
        return password;
        // Spring Security kullanıcı doğrularken şifreyi buradan alır.
    }

    @Override
    public String getUsername() {
        return email;
        // kullanıcı login olurken emailini kullanacak o yüzden username olarak emaili
        // döndürüyoruz

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;

        //Kullanıcının hesabı süresi dolmuş mu?
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
        //Şifre süresi dolmuş mu?;

    }

    @Override 
    public boolean isAccountNonLocked(){
        return true;
        //Kullanıcı aktif mi, pasif mi?
    }

}
