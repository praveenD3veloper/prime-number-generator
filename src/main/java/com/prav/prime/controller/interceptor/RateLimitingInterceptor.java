package com.prav.prime.controller.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Interceptor to apply basic rate limiting and enforce throttling per client IP address.
 *  * - Rate limiting: restricts number of requests per minute.
 *  * - Throttling: enforces minimum time between requests.
 *
 * <p>This interceptor uses a {@link com.github.benmanes.caffeine.cache.Cache} to track the number of
 * requests from each client IP within a 1-minute window. If the number of requests exceeds
 * {@link #MAX_REQUESTS_PER_MINUTE}, the request is rejected with HTTP 429 (Too Many Requests).
 *
 * <p>Configured via {@link com.prav.prime.config.WebConfig}.
 *
 * @author Praveenkumar Nagaraj
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    /**
     * The maximum number of allowed requests per minute per IP address.
     */
    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final long MIN_INTERVAL_BETWEEN_REQUESTS_MS = 1000; // 0.5 second

    /**
     * A Caffeine cache to track request counts for each client IP.
     */
    private final Cache<String, AtomicInteger> rateLimitCache;

    /**
     * Cache to store the timestamp of the last request for throttling control.
     */
    private final Cache<String, Long> lastAccessCache;

    /**
     * Intercepts incoming requests to check and enforce rate limiting.
     *
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @param handler  the chosen handler to execute
     * @return {@code true} if the request should proceed; {@code false} if rate limited
     * @throws Exception in case of any I/O error writing to the response
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final  HttpServletResponse response,
                             final Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        if (!isThrottlingAllowed(clientIp, response)) {
            return false;
        }

        return isRateLimitAllowed(clientIp, response);
    }
    /**
     * Checks if the request complies with throttling rules.
     *
     * @param clientIp IP address of the client
     * @param response HttpServletResponse to write throttling error if needed
     * @return true if allowed, false if throttled
     */
    private boolean isThrottlingAllowed(final String clientIp,
                                        final HttpServletResponse response) throws Exception {
        long now = System.currentTimeMillis();
        Long lastAccessTime = lastAccessCache.getIfPresent(clientIp);

        if (lastAccessTime != null && now - lastAccessTime < MIN_INTERVAL_BETWEEN_REQUESTS_MS) {
            log.warn("Throttled request from IP {} ({}ms interval)", clientIp, now - lastAccessTime);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests too quickly. Please wait.");
            return false;
        }

        lastAccessCache.put(clientIp, now);
        return true;
    }

    /**
     * Checks if the request complies with rate limit rules.
     *
     * @param clientIp IP address of the client
     * @param response HttpServletResponse to write rate limit error if needed
     * @return true if allowed, false if rate limited
     */
    private boolean isRateLimitAllowed(final String clientIp,
                                       final HttpServletResponse response) throws Exception {
        AtomicInteger requestCount = rateLimitCache.get(clientIp, k -> new AtomicInteger(0));
        assert requestCount != null;
        if (requestCount.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return false;
        }

        return true;
    }
}
