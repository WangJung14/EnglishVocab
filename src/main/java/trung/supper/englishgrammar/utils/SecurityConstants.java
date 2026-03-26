package trung.supper.englishgrammar.utils;

public final class SecurityConstants {
    private SecurityConstants() {
        // Restrict instantiation
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 604800000L; // 7 days

    // Auth Routes
    public static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
}
