package com.prav.prime.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RangeExceededExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String msg = "Range too large";
        RangeExceededException ex = new RangeExceededException(msg);
        assertEquals(msg, ex.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String msg = "Range too large";
        Throwable cause = new RuntimeException("cause");
        RangeExceededException ex = new RangeExceededException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testThrowException() {
        assertThrows(RangeExceededException.class, () -> {
            throw new RangeExceededException("Exceeded");
        });
    }
}
