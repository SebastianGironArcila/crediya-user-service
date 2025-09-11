package co.com.crediya.api.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponseDTO(
        String token
) {
}
