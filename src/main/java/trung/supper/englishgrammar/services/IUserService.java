package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.ChangePasswordRequest;
import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserRequest;
import trung.supper.englishgrammar.dto.response.MembershipResponse;
import trung.supper.englishgrammar.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    public UserResponse getMyProfile(UUID userId);

    public List<UserResponse> getAllUsers();

    public UserResponse createUser(CreateUserRequest createUserRequest);

    public UserResponse searchUserByEmail(String email);

    public UserResponse updateMyProfile(String email, UpdateUserRequest request);

    public void changePassword(String email, ChangePasswordRequest request);

    public String uploadAvatar(String email, org.springframework.web.multipart.MultipartFile file);

    public MembershipResponse getMyMembership();

    public MembershipResponse upgradeMembership();

}