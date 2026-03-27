package trung.supper.englishgrammar.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXIST(1001, "User does not exist", HttpStatus.NOT_FOUND),
    USER_EXISTED(1002, "User already exists", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Account or password is incorrect", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH(1005, "Confirm password does not match", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(1006, "Password is too weak (need at least 8 characters)", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1007, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND(2001, "Resource not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
