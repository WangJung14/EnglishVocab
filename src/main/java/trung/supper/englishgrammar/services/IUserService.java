package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.ChangePasswordRequest;
import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserRoleRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserStatusRequest;
import trung.supper.englishgrammar.dto.response.MembershipResponse;
import trung.supper.englishgrammar.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    public UserResponse getMyProfile(UUID userId);

    public List<UserResponse> getAllUsers();

    public UserResponse createUser(CreateUserRequest createUserRequest);

    public UserResponse searchUserByEmail(String email);

    public UserResponse updateMyProfile(String email, UpdateUserRequest request);

    public void changePassword(String email, ChangePasswordRequest request);

    public String uploadAvatar(String email, MultipartFile file);

    public MembershipResponse getMyMembership();

    public MembershipResponse upgradeMembership();

    public Page<UserResponse> getAllUsersPaginated(int page, int size);

    public UserResponse getUserById(UUID id);

    public UserResponse updateUserRole(UUID id, UpdateUserRoleRequest request);

    public UserResponse updateUserStatus(UUID id, UpdateUserStatusRequest request);
}