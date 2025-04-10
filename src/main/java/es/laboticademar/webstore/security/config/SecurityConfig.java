package es.laboticademar.webstore.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthentication jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                // Recursos públicos
                .requestMatchers("/WEB-INF/**", "/css/**", "/images/**", "/js/**", "/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/login", "/product/**", "/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/authenticate", "/auth/register").permitAll()
                
                // Endpoints protegidos:
                // Sólo ADMIN tiene acceso a /admin/**
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // EMPLOYEE y ADMIN pueden acceder a /employee/**
                .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")
                // Los clientes (USER) pueden acceder a carrito, wishlist y perfil
                .requestMatchers("/cart", "/wishlist", "/profile").hasRole("USER")
                
                // El resto requiere autenticación
                .anyRequest().authenticated()
            )
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))
            )
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

