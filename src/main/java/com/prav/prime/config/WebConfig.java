package com.prav.prime.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.prav.prime.controller.interceptor.RateLimitingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Web configuration class that registers custom interceptors for handling HTTP requests.
 *
 * <p>This configuration registers a {@link RateLimitingInterceptor} to apply rate limiting logic
 * on the endpoint(s) under the path <code>/primes/**</code>. It uses a Caffeine cache
 * to store and throttle request counts per client IP address.
 *
 * @author Praveenkumar Nagaraj
 */

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * A Caffeine cache to track request counts per IP address for rate limiting.
     */
    private final Cache<String, AtomicInteger> rateLimitCache;

    /**
     * Cache to store the timestamp of the last request for throttling control.
     */
    private final Cache<String, Long> lastAccessCache;

    /**
     * Registers the {@link RateLimitingInterceptor} for all paths matching <code>/primes/**</code>.
     *
     * @param registry the {@link InterceptorRegistry} to which interceptors can be added
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitingInterceptor(rateLimitCache, lastAccessCache))
                .addPathPatterns("/primes/**");
    }
}
