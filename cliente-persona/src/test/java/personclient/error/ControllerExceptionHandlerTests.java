package personclient.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import personclient.exceptions.BadRequestException;
import personclient.exceptions.ControllerExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ControllerExceptionHandlerTests {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleBadRequestExceptionReturnsTheOriginalPayload() {
        ResponseEntity<Object> response = handler.handleBadRequestException(new BadRequestException("invalid", "400"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        BadRequestException body = assertInstanceOf(BadRequestException.class, response.getBody());
        assertEquals("invalid", body.getMessage());
        assertEquals("400", body.getCode());
    }
}
