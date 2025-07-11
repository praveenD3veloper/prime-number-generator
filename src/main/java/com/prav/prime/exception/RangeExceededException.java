package com.prav.prime.exception;

/**
 * Custom exception thrown when the requested range exceeds the maximum allowed limit.
 * <p>
 * This exception is used to indicate that a user has requested a range that is too large
 * for the prime number generation service to handle efficiently or safely.
 */
public class RangeExceededException extends RuntimeException {

    /**
     * Constructs a new RangeExceededException with the specified detail message.
     *
     * @param message the detail message explaining why the range was exceeded
     */
    public RangeExceededException(final String message) {
        super(message);
    }

    /**
     * Constructs a new RangeExceededException with the specified detail message and cause.
     *
     * @param message the detail message explaining why the range was exceeded
     * @param cause   the cause of this exception
     */
    public RangeExceededException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
