package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.UpdateUserRoleRequest;
import trung.supper.englishgrammar.dto.request.UpdateUserStatusRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.UserResponse;
import trung.supper.englishgrammar.services.IUserService;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final IUserService userService;

    @GetMapping
    public ApiRespone<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiRespone.<Page<UserResponse>>builder()
                .result(userService.getAllUsersPaginated(page, size))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{id}")
    public ApiRespone<UserResponse> getUserById(@PathVariable UUID id) {
        return ApiRespone.<UserResponse>builder()
                .result(userService.getUserById(id))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}/role")
    public ApiRespone<UserResponse> updateUserRole(@PathVariable UUID id, @RequestBody UpdateUserRoleRequest request) {
        return ApiRespone.<UserResponse>builder()
                .result(userService.updateUserRole(id, request))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}/status")
    public ApiRespone<UserResponse> updateUserStatus(@PathVariable UUID id,
            @RequestBody UpdateUserStatusRequest request) {
        return ApiRespone.<UserResponse>builder()
                .result(userService.updateUserStatus(id, request))
                .code(1000)
                .message("success")
                .build();
    }
}
