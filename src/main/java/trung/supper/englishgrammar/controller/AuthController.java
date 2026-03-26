package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.LoginRequest;
import trung.supper.englishgrammar.dto.request.RegisterRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.AuthResponse;
import trung.supper.englishgrammar.services.IAuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ApiRespone<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.register(request))
                .message("Successfully registered")
                .build();
    }

    @PostMapping("/login")
    public ApiRespone<AuthResponse> login(@RequestBody LoginRequest request) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.login(request))
                .message("Successfully logged in")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiRespone<AuthResponse> refreshToken(@RequestParam String token) {
        return ApiRespone.<AuthResponse>builder()
                .result(authService.refresh(token))
                .message("Token refreshed")
                .build();
    }

    @PostMapping("/logout")
    public ApiRespone<String> logout(@RequestParam String token) {
        authService.logout(token);
        return ApiRespone.<String>builder()
                .result("Logout successful")
                .build();
    }
}
