package com.technoshark.Loady.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.technoshark.Loady.Constants.PUBLIC_ENDPOINTS;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring Security configuration for Firebase JWT authentication.
 * 
 * Key behaviors:
 * 1. All requests pass through FirebaseAuthFilter
 * 2. Public endpoints (PUBLIC_ENDPOINTS) → no authentication required
 * 3. All other endpoints → authentication REQUIRED by default
 * 4. @PreAuthorize annotations provide additional role-based access control
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // ⭐ Enables @PreAuthorize, @Secured, etc.
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseAuthFilter firebaseAuthFilter;
    private final FirebaseAuthenticationEntryPoint firebaseAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // CORS configuration
                .cors(withDefaults())

                // Stateless session management (no server-side sessions)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ⭐ Custom entry point for proper 401 JSON responses
                .exceptionHandling(exception -> exception.authenticationEntryPoint(firebaseAuthenticationEntryPoint))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(PUBLIC_ENDPOINTS.AUTH_WHITELIST).permitAll()

                        // CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ⭐ ALL OTHER ENDPOINTS REQUIRE AUTHENTICATION BY DEFAULT
                        .anyRequest().authenticated())

                // Add Firebase filter before Spring's authentication filter
                .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow localhost with any port (dev environment)
        config.setAllowedOriginPatterns(List.of("http://localhost:[*]"));

        // In production, use specific origins:
        // config.setAllowedOrigins(List.of("https://yourdomain.com"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}