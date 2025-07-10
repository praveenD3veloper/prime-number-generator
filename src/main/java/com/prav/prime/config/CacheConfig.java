package com.prav.prime.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * CacheConfig configures a {@link CacheManager} backed by Caffeine for use in the Spring context.
 * <p>
 * This configuration enables caching support in Spring and defines a Caffeine-based cache named {@code primeList}.
 * The cache is initialized with specific capacity, size limits, and expiration policies.
 * </p>
 *
 * <p><b>Cache Settings: from application properties</b></p>
 * <ul>
 *   <li>Initial capacity: 100 entries</li>
 *   <li>Maximum size: 1000 entries</li>
 *   <li>Expire after write: 30 minutes</li>
 *   <li>Statistics recording: enabled</li>
 * </ul>
 *
 * @author Praveenkumar Nagaraj
 */

@Configuration
@EnableCaching
public class CacheConfig {

    private static final int MAX_SIZE = 1000;

    /**
     * Defines the {@link CacheManager} bean that uses Caffeine for caching.
     *
     * @return a {@link CaffeineCacheManager} configured with cache settings for {@code primeList}
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("primeList");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                        .recordStats());
        return cacheManager;
    }

    /**
     * Creates a Caffeine cache bean used for rate limiting, where each entry expires
     * 1 minute after its creation or last update.
     *
     * <p>The cache is configured with a maximum size of 1000 entries to prevent
     * unbounded memory usage. Each entry maps a client identifier (e.g., IP address)
     * to an {@link AtomicInteger} used for counting requests.
     *
     * @return a {@link com.github.benmanes.caffeine.cache.Cache} instance
     *         with specified expiration and size constraints
     */
    @Bean
    public Cache<String, AtomicInteger> rateLimitCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE)
                .build();
    }
    /**
     * Creates a Caffeine cache bean for tracking the timestamp of the last access per client IP.
     * <p>
     * This cache is used to implement throttling logic by storing the last request time
     * for each client. Entries expire automatically after 1 minute.
     *
     * @return a {@code Cache<String, Long>} instance with expiration and size limits
     */
    @Bean
    public Cache<String, Long> lastAccessCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE)
                .build();
    }
}
