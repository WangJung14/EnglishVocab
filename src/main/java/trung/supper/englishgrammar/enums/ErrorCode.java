package trung.supper.englishgrammar.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXIST(1001, "User does not exist", HttpStatus.NOT_FOUND),
    USER_EXISTED(1002, "User already exists", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Tài khoản hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH(1005, "Mật khẩu xác nhận không khớp", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(1006, "Mật khẩu quá yếu (cần tối thiểu 8 ký tự)", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1007, "Lỗi tải file lên hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
