package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import trung.supper.englishgrammar.dto.request.ChangePasswordRequest;
import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.response.MembershipResponse;
import trung.supper.englishgrammar.dto.response.UserResponse;
import trung.supper.englishgrammar.enums.MembershipType;
import trung.supper.englishgrammar.enums.Role;
import trung.supper.englishgrammar.mapper.UserMapper;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.services.ICloudinaryService;
import trung.supper.englishgrammar.services.IUserService;
import trung.supper.englishgrammar.utils.MembershipUtils;
import trung.supper.englishgrammar.dto.request.UpdateUserRoleRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserStatusRequest;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final ICloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getMyProfile(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .avatarUrl(user.getAvatarUrl())
                .membershipType(user.getMembershipType().name())
                .membershipExpiresAt(user.getMembershipExpiresAt())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        // TODO: Implement password encoding with BCryptPasswordEncoder
        // String encodedPassword =
        // bCryptPasswordEncoder.encode(createUserRequest.getPassword());
        String encodedPassword = createUserRequest.getPassword();

        User newUser = User.builder()
                .email(createUserRequest.getEmail())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .passwordHash(encodedPassword)
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .role(Role.STUDENT) // Default role
                .membershipType(MembershipType.FREE) // Default membership
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);
        return userMapper.toUserResponseDTO(savedUser);
    }

    @Override
    public UserResponse searchUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponse updateMyProfile(String email,
            trung.supper.englishgrammar.dto.request.UpdateUserRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        userMapper.updateUser(user, request);
        user = userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (request.getNewPassword().length() < 8) {
            throw new AppException(ErrorCode.WEAK_PASSWORD);
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(String email, MultipartFile file) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        try {
            Map<?, ?> result = cloudinaryService.uploadFile(file, "avatars");
            String avatarUrl = (String) result.get("secure_url");
            if (avatarUrl == null)
                avatarUrl = (String) result.get("url");

            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);
            return avatarUrl;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public MembershipResponse getMyMembership() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toMembershipResponse(user);
    }

    @Override
    public MembershipResponse upgradeMembership() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        user.setMembershipType(MembershipType.MEMBERSHIP);
        user.setMembershipExpiresAt(MembershipUtils.calculateNewExpiry(user));

        userRepository.save(user);
        return userMapper.toMembershipResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(userMapper::toUserResponseDTO);
    }

    @Override
    public UserResponse getUserById(java.util.UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponse updateUserRole(java.util.UUID id, UpdateUserRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        user.setRole(request.getRole());
        userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponse updateUserStatus(java.util.UUID id, UpdateUserStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        user.setIsActive(request.getIsActive());
        userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }
}
