package com.prav.prime.core;

import com.prav.prime.core.algorithm.BruteForcePrimeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {

    /**
     *
     * @return bruteForce bean
     */
    @Bean
    public PrimeGenerator bruteForce() {
        return new BruteForcePrimeGenerator();
    }
}
