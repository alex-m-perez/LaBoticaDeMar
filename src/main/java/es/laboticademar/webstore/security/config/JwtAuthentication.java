package es.laboticademar.webstore.security.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthentication extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String correoUsuario = null;

        // Extraer la cookie "jwtToken"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt != null) {
            try {
                correoUsuario = jwtService.extractUsername(jwt);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // El token está expirado
                SecurityContextHolder.clearContext();
                // Opcional: Eliminar la cookie expiradas del navegador
                Cookie expiredCookie = new Cookie("jwtToken", null);
                expiredCookie.setPath("/");
                expiredCookie.setHttpOnly(true);
                expiredCookie.setSecure(true);
                expiredCookie.setMaxAge(0);
                response.addCookie(expiredCookie);
            }
        }

        // Si no hay token o este fue inválido, el usuario se queda sin autenticar;
        // AnonymousAuthenticationFilter (configurado por defecto en Spring Security) asigna el rol "ROLE_ANONYMOUS".
        if (correoUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(correoUsuario);
            if (jwt != null && jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }


}