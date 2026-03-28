package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.response.DashboardResponse;

import java.util.UUID;

public interface IDashboardService {
    DashboardResponse getDashboard(UUID userId);
}
