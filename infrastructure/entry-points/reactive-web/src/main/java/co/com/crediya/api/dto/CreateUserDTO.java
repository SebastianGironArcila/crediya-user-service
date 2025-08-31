package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record CreateUserDTO(

        @NotBlank(message = "FirstName is required")
        String firstName,

        @NotBlank(message = "LastName is required")
        String lastName,

        @NotNull(message = "BirthDate is required")
        LocalDate birthDate,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid format email")
        String email,

        @NotBlank(message = "IdentityDocument is requited")
        String identityDocument,

        @NotNull(message = "BaseSalary is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "BaseSalary must be greater than 0.")
        @DecimalMax(value = "15000000.0", message = "BaseSalary cannot exceed 15 million.")
        BigDecimal baseSalary,

        @NotBlank(message = "Role is required")
        String roleName
) {
}
