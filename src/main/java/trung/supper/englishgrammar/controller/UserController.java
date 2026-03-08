package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.CreateUserRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
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

}
