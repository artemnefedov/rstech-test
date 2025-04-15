package io.github.artemnefedov.rstech.dto;

import java.util.List;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        List<String> authorities
) {

}
