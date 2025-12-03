package csendes.david.ser.service.mapper;

import csendes.david.ser.data.model.UserEntity;
import csendes.david.ser.service.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "saves", ignore = true)
    @Mapping(target = "privilages", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity registrationToEntity(RegisterDto dto);
}
