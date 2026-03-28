package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.response.AdminStatsResponse;
import trung.supper.englishgrammar.dto.response.ContentStatsResponse;
import trung.supper.englishgrammar.dto.response.UserGrowthResponse;

import java.util.List;

public interface IAdminStatsService {
    AdminStatsResponse getOverviewStats();
    List<UserGrowthResponse> getUserGrowth(int days);
    List<ContentStatsResponse> getContentStats(int limit);
}
