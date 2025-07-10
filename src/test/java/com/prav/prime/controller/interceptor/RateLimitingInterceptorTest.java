package com.prav.prime.controller.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitingInterceptorTest {

    private Cache<String, AtomicInteger> rateLimitCache;
    private Cache<String, Long> lastAccessCache;
    private RateLimitingInterceptor interceptor;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private StringWriter responseWriter;

    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    @BeforeEach
    void setup() {
        rateLimitCache = mock(Cache.class);
        lastAccessCache = mock(Cache.class);
        interceptor = new RateLimitingInterceptor(rateLimitCache, lastAccessCache);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        try {
            when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        } catch (Exception e) {
            fail("Setup failure: " + e.getMessage());
        }

        when(request.getRemoteAddr()).thenReturn("1.2.3.4");
    }

    @Test
    void testAllowRequestFirstTime() throws Exception {
        when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);
        AtomicInteger counter = new AtomicInteger(0);
        when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        verify(lastAccessCache).put(eq("1.2.3.4"), anyLong());
        assertEquals(1, counter.get());
    }

    @Test
    void testThrottleTooSoon() throws Exception {
        long now = System.currentTimeMillis();
        when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(now);
        AtomicInteger counter = new AtomicInteger(0);
        when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(response).setStatus(429);
        assertTrue(responseWriter.toString().contains("Too many requests too quickly"));
    }

    @Test
    void testRateLimitExceeded() throws Exception {
        when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);

        AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE);
        when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(response).setStatus(429);
        assertTrue(responseWriter.toString().contains("Rate limit exceeded"));
    }

    @Test
    void testRateLimitJustBelowThreshold() throws Exception {
        when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);

        AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE - 1);
        when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertEquals(MAX_REQUESTS_PER_MINUTE, counter.get());
    }

}
