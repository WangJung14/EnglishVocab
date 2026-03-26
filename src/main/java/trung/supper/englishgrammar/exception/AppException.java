package trung.supper.englishgrammar.exception;

import lombok.Getter;
import lombok.Setter;
import trung.supper.englishgrammar.enums.ErrorCode;

@Getter
@Setter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
