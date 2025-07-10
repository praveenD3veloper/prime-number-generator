package com.prav.prime.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
}
