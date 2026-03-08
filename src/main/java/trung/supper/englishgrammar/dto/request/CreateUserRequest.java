package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @Email(message = "invalid email")
    private String email;
    @Size(min = 8, max = 30, message = "PASSWORD_INVALID")
    private String password;
    private String reTypePassword;
    @Size(min = 10, max = 11, message = "PHONENUMBER_INVALID")
    private String phoneNumber;
    private String firstName;
    private String lastName;

}
