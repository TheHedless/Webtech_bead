package csendes.david.ser.service.mapper;

import csendes.david.ser.data.model.UserEntity;
import csendes.david.ser.service.dto.RegisterDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T13:46:44+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity registrationToEntity(RegisterDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setName( dto.getName() );
        userEntity.setUsername( dto.getUsername() );
        userEntity.setPassword( dto.getPassword() );
        userEntity.setEmail( dto.getEmail() );

        return userEntity;
    }
}
