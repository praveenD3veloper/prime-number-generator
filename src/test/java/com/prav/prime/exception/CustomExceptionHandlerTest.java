package com.prav.prime.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomExceptionHandlerTest {

    private final CustomExceptionHandler handler = new CustomExceptionHandler();

    @Test
    void testEquals_EqualObjects() {
        LocalDateTime timeStamp = LocalDateTime.of(2025, 7, 8, 12, 0);
        String url1 = "http://example.com";
        int statusCode1 = 400;
        String statusName1 = "Bad Request";
        String message1 = "Invalid input";

        LocalDateTime timeStamp2 = LocalDateTime.of(2025, 7, 8, 12, 0);
        String url2 = "http://example.com";
        int statusCode2 = 400;
        String statusName2 = "Bad Request";
        String message2 = "Invalid input";

        CustomError error1 = new CustomError(timeStamp, url1, statusCode1, statusName1, message1);
        CustomError error2 = new CustomError(timeStamp2, url2, statusCode2, statusName2, message2);

        assertEquals(error1, error2);
        assertEquals(error2, error1);
    }

    @Test
    void testEquals_DifferentObjects() {
        LocalDateTime timestamp1 = LocalDateTime.of(2025, 7, 8, 12, 0);
        String url1 = "http://example.com";
        int statusCode1 = 400;
        String statusName1 = "Bad Request";
        String message1 = "Invalid input";

        LocalDateTime timestamp2 = LocalDateTime.of(2026, 7, 8, 12, 0);
        String url2 = "http://another-example.com";
        int statusCode2 = 500;
        String statusName2 = "Internal Server Error";
        String message2 = "Something went wrong";

        CustomError error1 = new CustomError(timestamp1, url1, statusCode1, statusName1, message1);
        CustomError error2 = new CustomError(timestamp2, url2, statusCode2, statusName2, message2);

        assertNotEquals(error1, error2);
        assertNotEquals(error2, error1);
    }

    @Test
    void testToString() {
        LocalDateTime timestamp = LocalDateTime.now();
        String url = "http://example.com";
        int statusCode = 400;
        String statusName = "Bad Request";
        String message = "Invalid input";

        CustomError error = new CustomError(timestamp, url, statusCode, statusName, message);
        String expected = "ErrorInfo {url: '" + url + "', message: '" + message + "'}";

        assertEquals(expected, error.toString());
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime timestamp = LocalDateTime.now();
        String url = "http://localhost/test";
        int statusCode = 500;
        String statusName = "Internal Server Error";
        String message = "Something failed";

        CustomError error = new CustomError();
        error.setTimestamp(timestamp);
        error.setUrl(url);
        error.setStatusCode(statusCode);
        error.setStatusName(statusName);
        error.setMessage(message);

        assertEquals(timestamp, error.getTimestamp());
        assertEquals(url, error.getUrl());
        assertEquals(statusCode, error.getStatusCode());
        assertEquals(statusName, error.getStatusName());
        assertEquals(message, error.getMessage());
    }

    @Test
    void testHashCodeConsistencyForEqualObjects() {
        LocalDateTime timeStamp = LocalDateTime.of(2025, 7, 8, 12, 0);
        String url = "http://example.com";
        String message = "Invalid input";

        CustomError error1 = new CustomError(timeStamp, url, 400, "Bad Request", message);
        CustomError error2 = new CustomError(timeStamp, url, 400, "Bad Request", message);

        // .equals() considers them equal due to overridden equals (based on url + message by default)
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    void testInvalidInput_WithCustomMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/primes/abc"));
        when(request.getQueryString()).thenReturn(null);

        IllegalArgumentException ex = new IllegalArgumentException("custom validation failure");

        CustomError error = handler.invalidInput(request, ex);

        assertEquals("custom validation failure", error.getMessage());
        assertEquals(400, error.getStatusCode());
        assertEquals("Bad Request", error.getStatusName());
    }
    @Test
    void testInvalidInput_InvalidRangeMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/primes/-1"));
        when(request.getQueryString()).thenReturn(null);

        IllegalArgumentException ex = new IllegalArgumentException("invalid range");

        CustomError error = handler.invalidInput(request, ex);

        assertEquals("Invalid range provided!. Correct format is /primes/{range} where range is positive integer.",
                error.getMessage());
    }
    @Test
    void testNotANumberException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/primes/abc"));
        when(request.getQueryString()).thenReturn("algo=bruteforce");

        NumberFormatException ex = new NumberFormatException("For input string: abc");

        CustomError error = handler.notANumber(request, ex);

        assertEquals("http://localhost/primes/abc?algo=bruteforce", error.getUrl());
        assertEquals(400, error.getStatusCode());
        assertEquals("Bad Request", error.getStatusName());
        assertEquals("Provided range is not a number. Correct format is /primes/{range} where range is positive integer.",
                error.getMessage());
    }

    @Test
    void testRangeExceededException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/primes/1000000"));
        when(request.getQueryString()).thenReturn(null);

        RangeExceededException ex = new RangeExceededException("Range 1000000 exceeds maximum allowed range of 999999");

        CustomError error = handler.rangeExceeded(request, ex);

        assertEquals("http://localhost/primes/1000000", error.getUrl());
        assertEquals(400, error.getStatusCode());
        assertEquals("Bad Request", error.getStatusName());
        assertEquals("Range exceeds maximum allowed limit of 999999. Please provide a smaller range value.",
                error.getMessage());
    }

}