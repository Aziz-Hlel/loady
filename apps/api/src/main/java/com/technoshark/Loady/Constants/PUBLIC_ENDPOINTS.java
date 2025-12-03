package com.technoshark.Loady.Constants;

public final class PUBLIC_ENDPOINTS {
    public static final String[] AUTH_WHITELIST = {
            "/auth/**",
            "/public/**",
            "/health",
            "/actuator/health", // For production health checks
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs" // Without trailing slash
    };
}
