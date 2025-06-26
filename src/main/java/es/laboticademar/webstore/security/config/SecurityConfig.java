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
                .requestMatchers("/WEB-INF/views/**", "/css/**", "/images/**", "/js/**", "/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/login", "/register", "/cart", "/product/**", "/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/authenticate", "/auth/register", "/auth/logout").permitAll()
                
                // Endpoints protegidos:
                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                .requestMatchers("/employee/**")
                    .hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers("/profile/**")
                    .hasAnyRole("USUARIO", "ADMIN")
                
                // El resto requiere autenticación
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("jwtToken")
                .logoutSuccessUrl("/")
                .permitAll()
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

