package trung.supper.englishgrammar.dto.response;

import lombok.Builder;
import lombok.Data;
import trung.supper.englishgrammar.enums.MembershipType;
import java.time.LocalDateTime;

@Data
@Builder
public class MembershipResponse {
    private MembershipType membershipType;
    private LocalDateTime membershipExpiresAt;
    private boolean isActive;
}
