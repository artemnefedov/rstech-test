package io.github.artemnefedov.rstech.controller;

import io.github.artemnefedov.rstech.dto.AuthRequest;
import io.github.artemnefedov.rstech.dto.AuthResponse;
import io.github.artemnefedov.rstech.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Auth rest controller",
        description = "REST-контроллер, отвечающий за аунтентификацию, проверку и обновление токенов"
)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Token",
            description = "Метод аунтентификации и получения access и refresh токенов, и списка authorities"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Успешная аунтентификация"),
                    @ApiResponse(responseCode = "401", description = "Ошибка аунтентификации"),
            }
    )
    @PostMapping
    public ResponseEntity<AuthResponse> token(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Успешная проверка токена"),
                    @ApiResponse(responseCode = "401", description = "Ошибка проверки токена"),
            }
    )
    @Operation(summary = "Validate", description = "Метод для проверки валидности access и refresh токенов")
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(HttpServletRequest req) {
        return ResponseEntity.ok(authService.validate(req));
    }

    @Operation(summary = "Refresh", description = "Метод для обновления access токена по refresh токену")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest req) {
        return ResponseEntity.ok(authService.refresh(req));
    }
}
