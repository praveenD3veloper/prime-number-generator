package com.prav.prime.controller.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    private static final long MIN_INTERVAL_BETWEEN_REQUESTS_MS = 1000L;

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
    @DisplayName("Should allow first request from new IP")
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
    @DisplayName("Should throttle requests that come too quickly")
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
    @DisplayName("Should reject when rate limit is exceeded")
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
    @DisplayName("Should allow request just below rate limit threshold")
    void testRateLimitJustBelowThreshold() throws Exception {
        when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);

        AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE - 1);
        when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertEquals(MAX_REQUESTS_PER_MINUTE, counter.get());
    }

    @Nested
    @DisplayName("IPv4 Address Tests")
    class IPv4Tests {

        @ParameterizedTest
        @ValueSource(strings = {
                "192.168.1.1",    // Private Class C
                "172.16.0.1",     // Private Class B
                "10.0.0.1",       // Private Class A
                "127.0.0.1",      // Loopback
                "169.254.1.1",    // Link-local
                "224.0.0.1",      // Multicast
                "8.8.8.8",        // Public DNS
                "203.0.113.1",    // Test network
                "198.51.100.1",   // Test network
                "0.0.0.0",        // All zeros
                "255.255.255.255" // Broadcast
        })
        @DisplayName("Should handle various IPv4 addresses correctly")
        void testVariousIPv4Addresses(String ipAddress) throws Exception {
            when(request.getRemoteAddr()).thenReturn(ipAddress);
            when(lastAccessCache.getIfPresent(ipAddress)).thenReturn(null);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq(ipAddress), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq(ipAddress), anyLong());
            assertEquals(1, counter.get());
        }

        @Test
        @DisplayName("Should handle rate limiting for private IP addresses")
        void testPrivateIPRateLimiting() throws Exception {
            String privateIP = "192.168.1.100";
            when(request.getRemoteAddr()).thenReturn(privateIP);
            when(lastAccessCache.getIfPresent(privateIP)).thenReturn(null);

            AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE);
            when(rateLimitCache.get(eq(privateIP), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertFalse(allowed);
            verify(response).setStatus(429);
            assertTrue(responseWriter.toString().contains("Rate limit exceeded"));
        }

        @Test
        @DisplayName("Should handle throttling for loopback addresses")
        void testLoopbackThrottling() throws Exception {
            String loopbackIP = "127.0.0.1";
            when(request.getRemoteAddr()).thenReturn(loopbackIP);

            long now = System.currentTimeMillis();
            when(lastAccessCache.getIfPresent(loopbackIP)).thenReturn(now);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq(loopbackIP), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertFalse(allowed);
            verify(response).setStatus(429);
            assertTrue(responseWriter.toString().contains("Too many requests too quickly"));
        }
    }

    @Nested
    @DisplayName("IPv6 Address Tests")
    class IPv6Tests {

        @ParameterizedTest
        @ValueSource(strings = {
                "2001:0db8:85a3:0000:0000:8a2e:0370:7334", // Full IPv6
                "2001:db8:85a3::8a2e:370:7334",            // Compressed
                "::1",                                      // Loopback
                "::ffff:192.0.2.1",                       // IPv4-mapped
                "2001:db8::1",                             // Documentation
                "fe80::1",                                 // Link-local
                "ff02::1",                                 // Multicast
                "::",                                      // All zeros
                "2001:0db8:0000:0000:0000:0000:0000:0001"  // Leading zeros
        })
        @DisplayName("Should handle various IPv6 addresses correctly")
        void testVariousIPv6Addresses(String ipAddress) throws Exception {
            when(request.getRemoteAddr()).thenReturn(ipAddress);
            when(lastAccessCache.getIfPresent(ipAddress)).thenReturn(null);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq(ipAddress), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq(ipAddress), anyLong());
            assertEquals(1, counter.get());
        }

        @Test
        @DisplayName("Should handle rate limiting for IPv6 addresses")
        void testIPv6RateLimiting() throws Exception {
            String ipv6Address = "2001:db8::1";
            when(request.getRemoteAddr()).thenReturn(ipv6Address);
            when(lastAccessCache.getIfPresent(ipv6Address)).thenReturn(null);

            AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE);
            when(rateLimitCache.get(eq(ipv6Address), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertFalse(allowed);
            verify(response).setStatus(429);
            assertTrue(responseWriter.toString().contains("Rate limit exceeded"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        @DisplayName("Should handle null and empty IP addresses")
        void testNullAndEmptyIPAddresses(String ipAddress) throws Exception {
            when(request.getRemoteAddr()).thenReturn(ipAddress);
            when(lastAccessCache.getIfPresent(ipAddress)).thenReturn(null);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq(ipAddress), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq(ipAddress), anyLong());
            assertEquals(1, counter.get());
        }

        @Test
        @DisplayName("Should handle malformed IP addresses")
        void testMalformedIPAddresses() throws Exception {
            String[] malformedIPs = {
                    "256.256.256.256",     // Invalid IPv4
                    "192.168.1",           // Incomplete IPv4
                    "192.168.1.1.1",       // Too many octets
                    "gggg::1",             // Invalid IPv6
                    "192.168.1.1:8080",    // With port
                    "not-an-ip",           // Text
                    "192.168.1.-1",        // Negative numbers
                    ""                     // Empty string
            };

            for (String malformedIP : malformedIPs) {
                when(request.getRemoteAddr()).thenReturn(malformedIP);
                when(lastAccessCache.getIfPresent(malformedIP)).thenReturn(null);
                AtomicInteger counter = new AtomicInteger(0);
                when(rateLimitCache.get(eq(malformedIP), any())).thenReturn(counter);

                boolean allowed = interceptor.preHandle(request, response, new Object());

                assertTrue(allowed, "Should allow malformed IP: " + malformedIP);
                verify(lastAccessCache).put(eq(malformedIP), anyLong());
                assertEquals(1, counter.get());

                // Reset mocks for next iteration
                reset(lastAccessCache);
            }
        }

        @Test
        @DisplayName("Should handle exactly at throttling boundary")
        void testThrottlingBoundary() throws Exception {
            long pastTime = System.currentTimeMillis() - MIN_INTERVAL_BETWEEN_REQUESTS_MS;
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(pastTime);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq("1.2.3.4"), anyLong());
            assertEquals(1, counter.get());
        }

        @Test
        @DisplayName("Should handle exactly at rate limit boundary")
        void testRateLimitBoundary() throws Exception {
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);

            AtomicInteger counter = new AtomicInteger(MAX_REQUESTS_PER_MINUTE - 1);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            assertEquals(MAX_REQUESTS_PER_MINUTE, counter.get());
        }

        @Test
        @DisplayName("Should handle cache returning null AtomicInteger")
        void testCacheReturnsNull() throws Exception {
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(null);

            // This should trigger the assertion in the actual code
            // In a real scenario, this would likely throw an AssertionError
            // but we're testing the behavior when cache returns null
            assertThrows(AssertionError.class, () -> {
                interceptor.preHandle(request, response, new Object());
            });
        }

        @Test
        @DisplayName("Should handle IOException when writing response")
        void testIOExceptionOnResponseWrite() throws Exception {
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(System.currentTimeMillis());
            when(response.getWriter()).thenThrow(new RuntimeException("IO Error"));

            assertThrows(RuntimeException.class, () -> {
                interceptor.preHandle(request, response, new Object());
            });
        }

        @Test
        @DisplayName("Should handle very high request counts")
        void testVeryHighRequestCounts() throws Exception {
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(null);

            AtomicInteger counter = new AtomicInteger(Integer.MAX_VALUE - 1);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertFalse(allowed);
            verify(response).setStatus(429);
            assertTrue(responseWriter.toString().contains("Rate limit exceeded"));
            assertEquals(Integer.MAX_VALUE, counter.get());
        }
    }

    @Nested
    @DisplayName("Concurrent Access Tests")
    class ConcurrentAccessTests {

        @Test
        @DisplayName("Should handle concurrent requests from same IP")
        void testConcurrentRequestsSameIP() throws Exception {
            String ip = "192.168.1.1";
            when(request.getRemoteAddr()).thenReturn(ip);
            when(lastAccessCache.getIfPresent(ip)).thenReturn(null);

            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq(ip), any())).thenReturn(counter);

            // Simulate concurrent access
            boolean allowed1 = interceptor.preHandle(request, response, new Object());
            boolean allowed2 = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed1);
            assertTrue(allowed2);
            assertEquals(2, counter.get());
        }

        @Test
        @DisplayName("Should handle multiple different IPs")
        void testMultipleDifferentIPs() throws Exception {
            String[] ips = {"192.168.1.1", "192.168.1.2", "10.0.0.1", "::1"};

            for (String ip : ips) {
                when(request.getRemoteAddr()).thenReturn(ip);
                when(lastAccessCache.getIfPresent(ip)).thenReturn(null);
                AtomicInteger counter = new AtomicInteger(0);
                when(rateLimitCache.get(eq(ip), any())).thenReturn(counter);

                boolean allowed = interceptor.preHandle(request, response, new Object());

                assertTrue(allowed, "Should allow request from IP: " + ip);
                assertEquals(1, counter.get());
            }
        }
    }

    @Nested
    @DisplayName("Time-based Tests")
    class TimeBasedTests {

        @Test
        @DisplayName("Should allow request after throttling period expires")
        void testAllowAfterThrottlingPeriod() throws Exception {
            long oldTime = System.currentTimeMillis() - (MIN_INTERVAL_BETWEEN_REQUESTS_MS + 100);
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(oldTime);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq("1.2.3.4"), anyLong());
            assertEquals(1, counter.get());
        }

        @Test
        @DisplayName("Should allow request exactly at minimum interval boundary")
        void testAllowAtExactMinimumIntervalBoundary() throws Exception {
            // Set time to be exactly 1000ms ago (exactly at the required interval)
            long exactTime = System.currentTimeMillis() - MIN_INTERVAL_BETWEEN_REQUESTS_MS;
            when(lastAccessCache.getIfPresent("1.2.3.4")).thenReturn(exactTime);
            AtomicInteger counter = new AtomicInteger(0);
            when(rateLimitCache.get(eq("1.2.3.4"), any())).thenReturn(counter);

            boolean allowed = interceptor.preHandle(request, response, new Object());

            assertTrue(allowed);
            verify(lastAccessCache).put(eq("1.2.3.4"), anyLong());
            assertEquals(1, counter.get());
        }
    }
}