package io.github.artemnefedov.rstech.config.security;

import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Bean
    public AuthenticationManager authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder().encode("password"))
                        .authorities(
                                Role.USER.getAuthorities()
                                        .stream()
                                        .map(Authority::getAuthority)
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList())
                        )
                        .build()
        );
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("password"))
                        .authorities(
                                Role.ADMIN.getAuthorities()
                                        .stream()
                                        .map(Authority::getAuthority)
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList())
                        )
                        .build()
        );
        return manager;
    }
}
