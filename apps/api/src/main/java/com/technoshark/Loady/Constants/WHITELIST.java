package com.technoshark.Loady.Constants;

import java.util.List;

public final class WHITELIST {
    public static final List<String> AUTH_WHITELIST = List.of(
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/error",

            "/auth/**",
            "/public/**",
            "/health",
            "/");
}
