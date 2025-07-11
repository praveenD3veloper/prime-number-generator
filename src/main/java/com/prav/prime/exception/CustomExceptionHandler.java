package com.prav.prime.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for REST API errors related to prime number generation.
 * <p>
 * This class intercepts specific exceptions and converts them into
 * structured {@link CustomError} responses to be returned to the client.
 * It ensures that invalid user input is handled consistently across endpoints.
 *
 * <p>Handled exceptions:
 * <ul>
 *     <li>{@link NumberFormatException} — when the input is not a number</li>
 *     <li>{@link IllegalArgumentException} — for validation failures like invalid range</li>
 * </ul>
 *
 * <p>Annotated with {@link RestControllerAdvice} to apply globally across all controllers.
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    /**
     * Handles {@link NumberFormatException} when non-numeric input is passed in the path or query parameters.
     *
     * @param req the HTTP request causing the error
     * @param e   the thrown exception
     * @return a structured {@link CustomError} response with a 400 BAD_REQUEST status
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError notANumber(final HttpServletRequest req, final NumberFormatException e) {
        log.error("Input Range is not a number = {}", reqUrl(req));
        return new CustomError(LocalDateTime.now(), req.getRequestURL().toString(),
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Provided range is not a number. Correct format is /primes/{range} where range is positive integer.");
    }

    /**
     * Handles {@link IllegalArgumentException}, typically thrown for invalid input or validation failures.
     *
     * @param req the HTTP request causing the error
     * @param e   the thrown exception
     * @return a structured {@link CustomError} response with a 400 BAD_REQUEST status
     */
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError invalidInput(final HttpServletRequest req, final IllegalArgumentException e) {
        if (e.getMessage().equals("invalid range")) {
            log.error("Invalid range provided = {}", reqUrl(req));
            return new CustomError(LocalDateTime.now(), reqUrl(req),
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Invalid range provided!. Correct format is /primes/{range} where range is positive integer.");
        }
        return new CustomError(LocalDateTime.now(),  reqUrl(req), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }
    /**
     * Handles {@link RangeExceededException} when the requested range exceeds the maximum allowed limit.
     *
     * @param req the HTTP request causing the error
     * @param e   the thrown exception
     * @return a structured {@link CustomError} response with a 400 BAD_REQUEST status
     */
    @ExceptionHandler(RangeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError rangeExceeded(final HttpServletRequest req, final RangeExceededException e) {
        return new CustomError(LocalDateTime.now(), reqUrl(req),
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Range exceeds maximum allowed limit of 999999. Please provide a smaller range value.");
    }

    /**
     * Utility method to construct the full request URL including query parameters.
     *
     * @param req the incoming HTTP request
     * @return the full request URL as a string
     */
    private String reqUrl(final HttpServletRequest req) {
        String queryParams = req.getQueryString() == null ? "" : "?" + req.getQueryString();
        return req.getRequestURL().append(queryParams).toString();
    }
}
