package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    public UserResponse getMyProfile(UUID userId);

    public List<UserResponse> getAllUsers();

    public UserResponse createUser(CreateUserRequest createUserRequest);

}
