package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.LoginRequest;
import trung.supper.englishgrammar.dto.request.RegisterRequest;
import trung.supper.englishgrammar.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshTokenStr);

    void logout(String refreshTokenStr);
}
