package io.github.artemnefedov.rstech.service;

import io.github.artemnefedov.rstech.dto.AuthRequest;
import io.github.artemnefedov.rstech.dto.AuthResponse;
import io.github.artemnefedov.rstech.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authManager,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        try {
            userDetailsService.loadUserByUsername(authRequest.username());

            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(),
                            authRequest.password())
            );
        } catch (Exception e) {
            throw new AuthException("Неверное имя пользователя или пароль");
        }

        return new AuthResponse(
                jwtService.generateToken(authRequest.username()),
                jwtService.generateRefreshToken(authRequest.username()),
                userDetailsService
                        .loadUserByUsername(authRequest.username())
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    public Boolean validate(HttpServletRequest req) {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthException("Invalid token");
        }
        String token = header.substring(7);
        return jwtService.isTokenValid(token, jwtService.extractUsername(token));
    }

    public AuthResponse refresh(HttpServletRequest req) {
        if (!validate(req)) {
            throw new AuthException("Invalid refresh token");
        }

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid refresh token");
        }

        String refreshToken = header.substring(7);

        String username = jwtService.extractUsername(refreshToken);

        return new AuthResponse(
                jwtService.generateToken(username),
                jwtService.generateRefreshToken(username),
                userDetailsService
                        .loadUserByUsername(username)
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
