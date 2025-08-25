package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record CreateUserDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthDate,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        String phone,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "El documento de identidad es obligatorio")
        String identityDocument,

        @NotNull(message = "El salario base es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a 0")
        @DecimalMax(value = "15000000.0", message = "El salario no puede superar los 15 millones")
        BigDecimal baseSalary,

        @NotBlank(message = "El rol es obligatorio")
        String roleName
) {
}
