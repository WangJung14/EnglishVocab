package trung.supper.englishgrammar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.LoginRequest;
import trung.supper.englishgrammar.dto.request.RegisterRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.AuthResponse;
import trung.supper.englishgrammar.services.IAuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for identifying interacting users, controlling login/registration workflows.")
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "Register a new User", description = "Registers user. Publicly accessible without Bearer token constraints.")
    @PostMapping("/register")
    public ApiRespone<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.register(request))
                .message("Successfully registered")
                .build();
    }

    @Operation(summary = "Authenticate System User", description = "Supplies long lived and short lived token responses upon valid credentials.")
    @PostMapping("/login")
    public ApiRespone<AuthResponse> login(@RequestBody LoginRequest request) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.login(request))
                .message("Successfully logged in")
                .build();
    }

    @Operation(summary = "Rotation of JWT", description = "Refresh stateless JWT Session safely mapping valid refresh tokens mapping to persistent databases.")
    @PostMapping("/refresh-token")
    public ApiRespone<AuthResponse> refreshToken(@RequestParam String token) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.refresh(token))
                .message("Token refreshed")
                .build();
    }

    @Operation(summary = "Dispose Session Context", description = "Deactivates and strips session tokens mapping invalidation logic resolving the JWT security domain context.")
    @SecurityRequirement(name = "bearerAuth") // Example explicit requirement enforcing bearer injection UI locking
                                              // feature on Swagger dashboard
    @PostMapping("/logout")
    public ApiRespone<String> logout(@RequestParam String token) {
        authService.logout(token);
        return ApiRespone.<String>builder()
                .result("Logout successful")
                .build();
    }
}
