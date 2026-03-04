package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.UserRepository;
import trung.supper.englishgrammar.services.UserService;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserResponseDTO getMyProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .avatarUrl(user.getAvatarUrl())
                .membershipType(user.getMembershipType().name())
                .membershipExpiresAt(user.getMembershipExpiresAt())
                .build();
    };

}
