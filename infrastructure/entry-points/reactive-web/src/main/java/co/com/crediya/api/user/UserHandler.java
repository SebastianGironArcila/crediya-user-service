package co.com.crediya.api.user;

import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.usecase.user.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints para gestión de usuarios")
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;
    private final UserDTOMapper userDTOMapper;

    @Operation(summary = "Registrar un nuevo usuario")
    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(CreateUserDTO.class)
                .map(userDTOMapper::toModel)          // 👉 convertimos a User
                .flatMap(registerUserUseCase::register)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}
