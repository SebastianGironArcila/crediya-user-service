package co.com.crediya.api.dto;


import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record UserDTO (
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        String identityDocument,
        BigDecimal baseSalary,
        String roleName
){
}
