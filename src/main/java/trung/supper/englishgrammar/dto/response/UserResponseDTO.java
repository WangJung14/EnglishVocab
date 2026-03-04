package trung.supper.englishgrammar.dto.response;

import lombok.Builder;
import lombok.Data;
import trung.supper.englishgrammar.enums.MembershipType;
import trung.supper.englishgrammar.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String role;
    private String membershipType;
    private LocalDateTime membershipExpiresAt;
    private Boolean isActive;
}
