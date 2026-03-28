package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.DashboardResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IDashboardService;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/dashboard")
    public ApiRespone<DashboardResponse> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<DashboardResponse>builder()
                .result(dashboardService.getDashboard(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }
}
