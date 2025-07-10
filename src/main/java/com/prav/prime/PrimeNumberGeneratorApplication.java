package com.prav.prime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point for the Prime Number Generator Spring Boot application.
 * <p>
 * This class boots up the Spring context and launches the embedded server.
 * It is annotated with {@link SpringBootApplication}, which is a convenience annotation
 * that adds {@code @Configuration}, {@code @EnableAutoConfiguration}, and {@code @ComponentScan}.
 * </p>
 */
@SpringBootApplication
@EnableCaching
public class PrimeNumberGeneratorApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(final String[] args) {
        SpringApplication.run(PrimeNumberGeneratorApplication.class, args);
    }

}
