package com.prav.prime.core;

import com.prav.prime.core.algorithm.BruteForce;
import com.prav.prime.core.algorithm.SieveOfEratosthenes;
import com.prav.prime.core.algorithm.SieveOfEratosthenesParallel;
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
        return new BruteForce();
    }

    /**
     *
     * @return sieve of eratosthenes bean
     */
    @Bean
    public PrimeGenerator sieveOfEratosthenes() {
        return new SieveOfEratosthenes();
    }

    /**
     *
     * @return sieve of eratosthenes parallel execution bean
     */
    @Bean
    public PrimeGenerator sieveOfEratosthenesInParallel() {
        return new SieveOfEratosthenesParallel();
    }

}
