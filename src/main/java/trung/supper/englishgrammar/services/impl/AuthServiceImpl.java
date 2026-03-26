package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trung.supper.englishgrammar.dto.request.LoginRequest;
import trung.supper.englishgrammar.dto.request.RegisterRequest;
import trung.supper.englishgrammar.dto.response.AuthResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.models.RefreshToken;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.IRefreshTokenRepository;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.sercurity.JwtService;
import trung.supper.englishgrammar.services.IAuthService;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IRefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(trung.supper.englishgrammar.enums.Role.STUDENT)
                .membershipType(trung.supper.englishgrammar.enums.MembershipType.FREE)
                .isActive(true)
                .build();
        userRepository.save(user);

        return buildAuthTokens(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        return buildAuthTokens(user);
    }

    @Override
    public void logout(String refreshTokenStr) {
        refreshTokenRepository.findByToken(refreshTokenStr)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public AuthResponse refresh(String refreshTokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired");
        }

        CustomUserDetails userDetails = new CustomUserDetails(token.getUser());
        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenStr)
                .build();
    }

    private AuthResponse buildAuthTokens(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(604800000L)) // 7 days hardcoded refresh policy
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
