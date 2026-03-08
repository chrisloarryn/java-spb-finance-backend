package accounttransaction.error;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import accounttransaction.exceptions.ApiErrorResponse;
import accounttransaction.exceptions.BadRequestException;
import accounttransaction.exceptions.ControllerExceptionHandler;
import accounttransaction.exceptions.InsuficientBalanceException;
import accounttransaction.exceptions.UnparseableDateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerExceptionHandlerTests {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleInsufficientBalanceReturnsA422Payload() {
        ResponseEntity<Object> response = handler.handleInsufficientBalance(new InsuficientBalanceException("Insufficient balance"));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        ApiErrorResponse body = assertInstanceOf(ApiErrorResponse.class, response.getBody());
        assertEquals("The account does not have enough balance to complete the transaction.", body.getMessage());
    }

    @Test
    void handleUnparseableDateReturnsABadRequestPayload() {
        ResponseEntity<Object> response = handler.handleUnparseableDate(new UnparseableDateException("Date format is not valid"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse body = assertInstanceOf(ApiErrorResponse.class, response.getBody());
        assertTrue(body.getMessage().contains("yyyy-MM-dd"));
    }

    @Test
    void handleHttpMessageNotReadableSanitizesTheInvalidType() {
        InvalidFormatException cause = InvalidFormatException.from(
                null,
                "Cannot deserialize value of type `java.util.UUID` from String \"abc\": not a valid UUID",
                "abc",
                UUID.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "JSON parse error",
                cause,
                new EmptyHttpInputMessage());

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse body = assertInstanceOf(ApiErrorResponse.class, response.getBody());
        assertTrue(body.getMessage().contains("abc"));
        assertEquals("The id must use the format 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx'", body.getErrors().getFirst());
    }

    @Test
    void handleBadRequestExceptionReturnsTheOriginalPayload() {
        ResponseEntity<?> response = handler.handleBadRequestException(new BadRequestException("invalid", "400"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BadRequestException body = assertInstanceOf(BadRequestException.class, response.getBody());
        assertEquals("invalid", body.getMessage());
        assertEquals("400", body.getCode());
    }

    private static final class EmptyHttpInputMessage implements HttpInputMessage {

        @Override
        public ByteArrayInputStream getBody() {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public HttpHeaders getHeaders() {
            return HttpHeaders.EMPTY;
        }
    }
}
