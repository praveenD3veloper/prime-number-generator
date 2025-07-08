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
    private PrimeGenerator bruteForcePrimeFinder;

    private Map<String, Supplier<PrimeGenerator>> strategyMap;

    /**
     * post constructor initializer to initialize a map.
     * with all the available algorithms for strategy
     */
    @PostConstruct
    public void initializeStrategyMap() {
        strategyMap = new HashMap<>();
        strategyMap.put("bruteforce", () -> bruteForcePrimeFinder);
    }

    /**
     *
     * @param requestedStrategy decides algorithm for generating primes
     * @return bean of the selected strategy / algorithm
     *
     *    If the provided algorithm is not matched then it proceeds uses default algo
     */
    public PrimeGenerator selectStrategy(final String requestedStrategy) {
        Supplier<PrimeGenerator> strategySupplier = strategyMap.getOrDefault(requestedStrategy.toLowerCase(), () -> {
            return bruteForcePrimeFinder;
        });

        return strategySupplier.get();
    }
}
