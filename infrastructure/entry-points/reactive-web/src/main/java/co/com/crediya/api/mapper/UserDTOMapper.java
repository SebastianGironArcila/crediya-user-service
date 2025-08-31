package co.com.crediya.api.mapper;


import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.dto.UserDTO;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    User toModel(CreateUserDTO createUserDTO);

    @Mapping(source = "roleId", target = "roleName")
    UserDTO toResponse(User user);

}
