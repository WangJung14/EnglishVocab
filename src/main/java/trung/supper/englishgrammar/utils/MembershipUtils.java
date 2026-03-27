package trung.supper.englishgrammar.utils;

import trung.supper.englishgrammar.models.User;
import java.time.LocalDateTime;

public class MembershipUtils {
    private MembershipUtils() { }

    public static LocalDateTime calculateNewExpiry(User user) {
        if (user.getMembershipExpiresAt() != null && user.getMembershipExpiresAt().isAfter(LocalDateTime.now())) {
            return user.getMembershipExpiresAt().plusDays(MembershipConstants.UPGRADE_DURATION_DAYS);
        }
        return LocalDateTime.now().plusDays(MembershipConstants.UPGRADE_DURATION_DAYS);
    }
}
