package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.UserStatsResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IUserStatsService;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserStatsController {

    private final IUserStatsService userStatsService;

    @GetMapping("/stats")
    public ApiRespone<UserStatsResponse> getStats(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<UserStatsResponse>builder()
                .result(userStatsService.getStats(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }
}
