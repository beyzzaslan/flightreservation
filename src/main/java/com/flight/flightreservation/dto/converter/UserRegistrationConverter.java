package com.flight.flightreservation.dto.converter;

import com.flight.flightreservation.dto.UserRegistrationDTO;
import com.flight.flightreservation.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationConverter {

    private final ModelMapper modelMapper;

    public UserRegistrationConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //Neden sadece tek yönlü yazdık? Çünkü kullanıcı kayıt bilgilerini veritabanına atmak için bu converter'ı kullanırız. Kullanıcının şifresini içeren bu DTO'yu genellikle geri dışarı (Frontend'e) göndermeyiz. O yüzden genelde tek yönlü (DTO -> Entity) olması yeterlidir.
    public User convertToEntity(UserRegistrationDTO userRegistrationDTO){
        return modelMapper.map(userRegistrationDTO, User.class);
    }
}