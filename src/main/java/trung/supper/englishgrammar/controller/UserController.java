package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;
import trung.supper.englishgrammar.services.IUserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    ApiRespone<List<UserResponseDTO>> getAllUsers() {

        return ApiRespone.<List<UserResponseDTO>>builder()
                .result(userService.getAllUsers())
                .code(1000)
                .message("success")
                .build();
    }

}
