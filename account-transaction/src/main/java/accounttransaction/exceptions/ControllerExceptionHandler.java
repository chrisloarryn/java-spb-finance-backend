package accounttransaction.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Order(1)
@ResponseBody
public class ControllerExceptionHandler {
    private static final Map<String, String> typeToErrorMessage = new HashMap<>();

    static {
        typeToErrorMessage.put("java.time.LocalDate", "The date must use the format 'yyyy-MM-dd'");
        typeToErrorMessage.put("java.util.UUID", "The id must use the format 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx'");
        typeToErrorMessage.put("java.lang.Double", "The amount must be a decimal number");
    }

    @ExceptionHandler(InsuficientBalanceException.class)
    public ResponseEntity<Object> handleInsufficientBalance(InsuficientBalanceException ex) {
        String message = "The account does not have enough balance to complete the transaction.";
        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                message,
                Collections.singletonList(ex.getMessage())
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnparseableDateException.class)
    public ResponseEntity<Object> handleUnparseableDate(UnparseableDateException ex) {
        String message = "The date could not be parsed. Make sure it uses the format 'yyyy-MM-dd'.";
        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                Collections.singletonList(ex.getMessage())
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String readableMessage = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        String captureField = extractFieldName(readableMessage);
        String errorType = extractErrorType(readableMessage);

        String errorMessage = typeToErrorMessage.getOrDefault(errorType, "The request contains fields with an invalid or unexpected format. Review the API contract.");

        List<String> errors = Collections.singletonList(errorMessage);

        ApiErrorResponse apiError = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request format error: " + captureField,
                errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private String extractFieldName(String message) {
        Pattern pattern = Pattern.compile("from String \"(.*?)\":");
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private String extractErrorType(String message) {
        Pattern pattern = Pattern.compile("type `(.+?)`");
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        BadRequestException body = BadRequestException.builder()
                .message(ex.getMessage())
                .code(ex.getCode())
                .build();

        return builder.body(body);
    }
}
