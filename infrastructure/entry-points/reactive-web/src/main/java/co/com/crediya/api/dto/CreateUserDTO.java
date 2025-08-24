package co.com.crediya.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Builder
public record CreateUserDTO(

        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        Double baseSalary
) {

}
