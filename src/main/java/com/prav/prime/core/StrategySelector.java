package com.prav.prime.core;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class StrategySelector {

    @Autowired
    @Qualifier("bruteForce")
    private PrimeGenerator bruteForce;

    @Autowired
    @Qualifier("sieveOfEratosthenes")
    private PrimeGenerator sieveOfEratosthenes;

    @Autowired
    @Qualifier("sieveOfEratosthenesInParallel")
    private PrimeGenerator sieveOfEratosthenesInParallel;

    private Map<String, Supplier<PrimeGenerator>> strategyMap;

    /**
     * post constructor initializer to initialize a map.
     * with all the available algorithms for strategy
     */
    @PostConstruct
    public void initializeStrategyMap() {
        strategyMap = new HashMap<>();
        strategyMap.put("bruteforce", () -> bruteForce);
        strategyMap.put("sieveoferatosthenes", () -> sieveOfEratosthenes);
        strategyMap.put("sieveoferatosthenesinparallel", () -> sieveOfEratosthenesInParallel);
    }

    /**
     *
     * @param requestedStrategy decides algorithm for generating primes
     * @return bean of the selected strategy / algorithm
     *
     *    If the provided algorithm is not matched then it proceeds uses default algo
     */
    public PrimeGenerator selectStrategy(final String requestedStrategy) {
        if (requestedStrategy == null) {
            return sieveOfEratosthenes;
        }
        Supplier<PrimeGenerator> strategySupplier = strategyMap.getOrDefault(requestedStrategy.toLowerCase(), () -> {
            return sieveOfEratosthenes;
        });

        return strategySupplier.get();
    }
}
