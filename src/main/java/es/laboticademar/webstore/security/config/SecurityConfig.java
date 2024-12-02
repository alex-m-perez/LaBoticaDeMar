package es.laboticademar.webstore.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
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
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF
            .authorizeHttpRequests(requests -> requests
                // Permitir el acceso sin autenticación a las rutas públicas
                .requestMatchers("/WEB-INF/**", "/css/**", "/images/**", "/js/**", "/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/product/**", "/auth/**").permitAll()
                // Proteger las rutas que requieren autenticación
                .requestMatchers("/wishlist", "/cart", "/profile").authenticated()
                .anyRequest().authenticated() // Otras rutas requieren autenticación
            )
            // Redirigir al login si el usuario no está autenticado
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/auth/login"))
            )
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura la política de sesión como stateless
            )
            .authenticationProvider(authenticationProvider) // Configura el proveedor de autenticación
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro JWT

        return http.build();
    }

}

