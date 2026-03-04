package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.UserResponseDTO;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // hard code vi chua thiet lap Sercurity de lay ID

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser() {
        UUID currentUserId = UUID.fromString("your-uuid-here");
        UserResponseDTO response = userService.getMyProfile(currentUserId);
        return ResponseEntity.ok(response);
    }
}
