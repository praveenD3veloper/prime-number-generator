package com.prav.prime.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A model class representing a custom error response structure for REST APIs.
 * <p>
 * This class captures details such as the timestamp of the error,
 * the request URL, HTTP status code, status name, and a descriptive error message.
 * It is typically used in global exception handling to produce consistent error responses.
 *
 * <p>Example JSON:
 * <pre>
 * {
 *   "timestamp": "2025-07-06T12:34:56.789",
 *   "url": "/api/primes",
 *   "statusCode": 400,
 *   "statusName": "BAD_REQUEST",
 *   "message": "Invalid range provided"
 * }
 * </pre>
 *
 * <p>This class uses Lombok annotations to reduce boilerplate code.
 *
 * @author Praveenkumar Nagaraj
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    /**
     * The date and time when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The URL of the request that caused the error.
     */
    private String url;

    /**
     * The HTTP status code associated with the error (e.g., 400, 404, 500).
     */
    private int statusCode;

    /**
     * The textual name of the HTTP status (e.g., BAD_REQUEST, NOT_FOUND).
     */
    private String statusName;

    /**
     * A descriptive message explaining the error.
     */
    private String message;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomError other = (CustomError) obj;
        return statusCode == other.statusCode &&
                Objects.equals(timestamp, other.timestamp) &&
                Objects.equals(url, other.url) &&
                Objects.equals(statusName, other.statusName) &&
                Objects.equals(message, other.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, url, statusCode, statusName, message);
    }
    /**
     * Returns a string representation of the error containing only the URL and message.
     *
     * @return a simple string summary of the error
     */

    @Override
    public String toString() {
        return "ErrorInfo {"
                + "url: '"
                + url
                + "', message: '"
                + message
                + "'}";
    }
}
