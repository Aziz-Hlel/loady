package com.technoshark.Loady.Config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter that validates Firebase JWT tokens and sets Spring Security
 * authentication context.
 * Runs once per request before UsernamePasswordAuthenticationFilter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CLAIMS_ROLES_KEY = "roles"; // Custom claim key for roles

    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (token != null) {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
                setAuthentication(decodedToken, request);
            }

        } catch (FirebaseAuthException e) {
            log.warn("Firebase token verification failed: {}", e.getMessage());
            // Don't throw here - let Spring Security handle unauthorized access
            // If no authentication is set, secured endpoints will return 403
        } catch (Exception e) {
            log.error("Unexpected error during Firebase authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from Authorization header.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Creates Spring Security authentication object and sets it in SecurityContext.
     */
    private void setAuthentication(FirebaseToken token, HttpServletRequest request) {
        // Extract custom roles from Firebase token claims (if present)
        List<SimpleGrantedAuthority> authorities = extractAuthorities(token);

        // Create authentication object with FirebaseToken as principal
        var authentication = new UsernamePasswordAuthenticationToken(
                token,
                null,
                authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("Authenticated user: {} (UID: {})", token.getEmail(), token.getUid());
    }

    /**
     * Extracts user roles from Firebase custom claims.
     * You can set custom claims in Firebase using Admin SDK:
     * admin.auth().setCustomUserClaims(uid, { roles: ['USER', 'ADMIN'] })
     */
    @SuppressWarnings("unchecked")
    private List<SimpleGrantedAuthority> extractAuthorities(FirebaseToken token) {
        Object rolesObject = token.getClaims().get(CLAIMS_ROLES_KEY);

        if (rolesObject instanceof List) {
            return ((List<String>) rolesObject).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }

        // Default role for authenticated users
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}