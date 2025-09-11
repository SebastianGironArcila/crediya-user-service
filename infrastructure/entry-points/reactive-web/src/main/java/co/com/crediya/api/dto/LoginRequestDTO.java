package co.com.crediya.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginRequestDTO(
        @Email(message = "Inválid format email") String email,
        @NotBlank(message = "Password is required") String password
) {}