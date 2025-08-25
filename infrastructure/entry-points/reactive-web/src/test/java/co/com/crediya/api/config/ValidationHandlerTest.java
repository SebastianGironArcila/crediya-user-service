package co.com.crediya.api.config;

import co.com.crediya.api.dto.CreateUserDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class ValidationHandlerTest {

    private ValidationHandler validationHandler;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        validationHandler = new ValidationHandler(validator);
    }

    @Test
    void shouldPassValidationWhenDtoIsValid() {
        CreateUserDTO validDto = CreateUserDTO.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("3001234567")
                .email("juan.perez@test.com")
                .identityDocument("100200300")
                .baseSalary(BigDecimal.valueOf(1500000))
                .roleName("ADMINISTRADOR")
                .build();

        StepVerifier.create(validationHandler.validate(validDto))
                .expectNext(validDto)
                .verifyComplete();
    }

    @Test
    void shouldFailValidationWhenFirstNameIsBlank() {
        CreateUserDTO invalidDto = CreateUserDTO.builder()
                .firstName("") // invalido
                .lastName("Pérez")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("3001234567")
                .email("juan.perez@test.com")
                .identityDocument("100200300")
                .baseSalary(BigDecimal.valueOf(1500000))
                .roleName("ADMINISTRADOR")
                .build();

        StepVerifier.create(validationHandler.validate(invalidDto))
                .expectErrorSatisfies(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
                    ConstraintViolationException ex = (ConstraintViolationException) throwable;
                    Assertions.assertThat(ex.getConstraintViolations())
                            .anyMatch(v -> v.getPropertyPath().toString().equals("firstName") &&
                                    v.getMessage().equals("El nombre es obligatorio"));
                })
                .verify();
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        CreateUserDTO invalidDto = CreateUserDTO.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("3001234567")
                .email("invalid-email") // invalido
                .identityDocument("100200300")
                .baseSalary(BigDecimal.valueOf(1500000))
                .roleName("ADMINISTRADOR")
                .build();

        StepVerifier.create(validationHandler.validate(invalidDto))
                .expectErrorSatisfies(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
                    ConstraintViolationException ex = (ConstraintViolationException) throwable;
                    Assertions.assertThat(ex.getConstraintViolations())
                            .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
                                    v.getMessage().equals("Formato de email inválido"));
                })
                .verify();
    }
}
