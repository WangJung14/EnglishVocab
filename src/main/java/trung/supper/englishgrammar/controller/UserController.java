package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import trung.supper.englishgrammar.dto.request.ChangePasswordRequest;
import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.MembershipResponse;
import trung.supper.englishgrammar.dto.response.UserResponse;
import trung.supper.englishgrammar.services.IUserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    ApiRespone<List<UserResponse>> getAllUsers() {

        return ApiRespone.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping
    ApiRespone<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.createUser(createUserRequest);

        return ApiRespone.<UserResponse>builder()
                .result(userResponse)
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{email}")
    ApiRespone<UserResponse> searchUserByEmail(@PathVariable String email) {
        UserResponse userResponse = userService.searchUserByEmail(email);
        return ApiRespone.<UserResponse>builder()
                .result(userResponse)
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/me")
    public ApiRespone<UserResponse> getMyProfile() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
        UserResponse userResponse = userService.searchUserByEmail(email);
        return ApiRespone.<UserResponse>builder()
                .result(userResponse)
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/me")
    public ApiRespone<UserResponse> updateMyProfile(
            @RequestBody trung.supper.englishgrammar.dto.request.UpdateUserRequest request) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
        UserResponse userResponse = userService.updateMyProfile(email, request);
        return ApiRespone.<UserResponse>builder()
                .result(userResponse)
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/me/password")
    public ApiRespone<String> changePassword(@RequestBody ChangePasswordRequest request) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
        userService.changePassword(email, request);
        return ApiRespone.<String>builder()
                .result("Change password successfully")
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiRespone<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
        String avatarUrl = userService.uploadAvatar(email, file);
        return ApiRespone.<String>builder()
                .result(avatarUrl)
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/me/membership")
    public ApiRespone<MembershipResponse> getMyMembership() {
        return ApiRespone.<MembershipResponse>builder()
                .result(userService.getMyMembership())
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping("/me/membership/upgrade")
    public ApiRespone<MembershipResponse> upgradeMembership() {
        return ApiRespone.<MembershipResponse>builder()
                .result(userService.upgradeMembership())
                .code(1000)
                .message("success")
                .build();
    }
}
