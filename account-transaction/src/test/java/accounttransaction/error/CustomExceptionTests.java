package accounttransaction.error;

import accounttransaction.exceptions.CustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionTests {

    @Test
    void constructorStoresMessageAndCode() {
        CustomException exception = new CustomException("invalid payload", "400");

        assertEquals("invalid payload", exception.getMessage());
        assertEquals("400", exception.getCode());
    }
}
