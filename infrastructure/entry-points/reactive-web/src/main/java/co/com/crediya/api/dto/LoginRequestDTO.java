package co.com.crediya.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Formato de email inválido") String email,
        @NotBlank(message = "La contraseña es requerida") String password
) {}