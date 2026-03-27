package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    public UserResponse getMyProfile(UUID userId);

    public List<UserResponse> getAllUsers();

    public UserResponse createUser(CreateUserRequest createUserRequest);

    public UserResponse searchUserByEmail(String email);

    public UserResponse updateMyProfile(String email, trung.supper.englishgrammar.dto.request.UpdateUserRequest request);

    public void changePassword(String email, trung.supper.englishgrammar.dto.request.ChangePasswordRequest request);

    public String uploadAvatar(String email, org.springframework.web.multipart.MultipartFile file);
}
