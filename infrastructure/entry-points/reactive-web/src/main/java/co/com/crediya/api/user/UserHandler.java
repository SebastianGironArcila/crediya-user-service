package co.com.crediya.api.user;

import co.com.crediya.api.config.ValidationHandler;
import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints para gestión de usuarios")
@Slf4j
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;
    private final UserDTOMapper userDTOMapper;
    private final ValidationHandler validationHandler;

    @Operation(summary = "Registrar un nuevo usuario")
    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(CreateUserDTO.class)
                .flatMap(validationHandler::validate)
                .flatMap(dto -> {
                    User user = userDTOMapper.toModel(dto);
                    return registerUserUseCase.register(user, dto.roleName());
                })
                .flatMap(user -> ServerResponse.ok().bodyValue(user));

    }
}
