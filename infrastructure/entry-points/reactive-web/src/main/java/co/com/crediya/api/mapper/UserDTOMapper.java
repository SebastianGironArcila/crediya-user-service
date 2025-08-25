package co.com.crediya.api.mapper;


import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    User toModel(CreateUserDTO createUserDTO);

}
