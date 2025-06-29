package es.laboticademar.webstore.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Usamos esto en lugar de @Autowired para inyecciones finales
public class SecurityConfig {

    private final JwtAuthentication jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(requests -> requests
                .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()

                // --- ENDPOINTS PÚBLICOS ---
                .requestMatchers(
                    // Recursos estáticos
                    "/css/**", 
                    "/images/**", 
                    "/js/**",
                    "/public/**",
                    // Páginas y APIs públicas
                    "/",
                    "/login",
                    "/register",
                    "/cart",
                    "/wishlist",
                    "/product/**", "/api/product/**",
                    "/auth/**" 
                ).permitAll()

                // --- ENDPOINTS PROTEGIDOS ---
                .requestMatchers("/profile/**").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // --- REGLA FINAL ---
                // Cualquier otra petición que no coincida con las anteriores debe estar autenticada.
                .anyRequest().authenticated()
            )

            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("jwtToken")
                .logoutSuccessUrl("/")
                .permitAll()
            )

            .exceptionHandling(handling -> handling
                // Esto se activa cuando un usuario NO logueado intenta acceder a una ruta protegida.
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendRedirect("/login")
                )
            );

        return http.build();
    }
}
