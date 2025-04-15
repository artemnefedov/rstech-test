package io.github.artemnefedov.rstech.config.security;

import static io.github.artemnefedov.rstech.config.security.Authority.CREATE;
import static io.github.artemnefedov.rstech.config.security.Authority.DELETE;
import static io.github.artemnefedov.rstech.config.security.Authority.READ;
import static io.github.artemnefedov.rstech.config.security.Authority.UPDATE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationManager authenticationManager;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
            AuthenticationManager authenticationManager) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(HttpMethod.GET, "/api/v1/products**",
                                        "/api/v1/categories**")
                                .hasAuthority(READ.getAuthority())
                                .requestMatchers(HttpMethod.POST, "/api/v1/products**",
                                        "/api/v1/categories**")
                                .hasAuthority(CREATE.getAuthority())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products**",
                                        "/api/v1/categories**")
                                .hasAuthority(UPDATE.getAuthority())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products**",
                                        "/api/v1/categories**")
                                .hasAuthority(DELETE.getAuthority())
                                .requestMatchers("/js/**", "/favicon.ico")
                                .permitAll()
                                .requestMatchers("/", "**.html", "/categories", "/api/v1/auth/**",
                                        "/login")
                                .permitAll()
                                .requestMatchers(
                                        "/swagger.html",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/docs/**",
                                        "/v3/api-docs/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
