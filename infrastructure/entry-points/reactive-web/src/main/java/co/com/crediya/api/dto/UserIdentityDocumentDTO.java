package co.com.crediya.api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserIdentityDocumentDTO(

        @NotBlank(message = "Identity document cannot be blank")
        @Pattern(regexp = "\\d{6,10}", message = "Identity document must be 6-10 digits")
        String identityDocument
) {


}
