package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.response.UserStatsResponse;

import java.util.UUID;

public interface IUserStatsService {
    UserStatsResponse getStats(UUID userId);
}
