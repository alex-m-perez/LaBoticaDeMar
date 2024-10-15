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

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthentication jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Asegúrate de que CSRF está desactivado
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/auth/**").permitAll() // Permite el acceso sin autenticación
                .requestMatchers("/**").permitAll() // Permite el acceso sin autenticación
                .requestMatchers("/css/**").permitAll() // Permite el acceso sin autenticación
                .requestMatchers(HttpMethod.GET).permitAll() // Permite el acceso sin autenticación
                .anyRequest().authenticated() // Requiere autenticación para otras solicitudes
            )
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura la política de sesión
            )
            .authenticationProvider(authenticationProvider) // Configura el proveedor de autenticación
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro JWT

        return http.build();
    }
}

