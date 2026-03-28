package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.AdminStatsResponse;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.ContentStatsResponse;
import trung.supper.englishgrammar.dto.response.UserGrowthResponse;
import trung.supper.englishgrammar.services.IAdminStatsService;

import java.util.List;

@RestController
@RequestMapping("/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminStatsController {

    private final IAdminStatsService adminStatsService;

    @GetMapping
    public ApiRespone<AdminStatsResponse> getOverviewStats() {
        return ApiRespone.<AdminStatsResponse>builder()
                .result(adminStatsService.getOverviewStats())
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/users")
    public ApiRespone<List<UserGrowthResponse>> getUserGrowth(
            @RequestParam(defaultValue = "30") int days) {
        return ApiRespone.<List<UserGrowthResponse>>builder()
                .result(adminStatsService.getUserGrowth(days))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/content")
    public ApiRespone<List<ContentStatsResponse>> getContentStats(
            @RequestParam(defaultValue = "10") int limit) {
        return ApiRespone.<List<ContentStatsResponse>>builder()
                .result(adminStatsService.getContentStats(limit))
                .code(1000)
                .message("success")
                .build();
    }
}
