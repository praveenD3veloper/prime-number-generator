package com.prav.prime.service;

import com.prav.prime.core.PrimeGenerator;
import com.prav.prime.core.StrategySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrimeNumberService {

    /**
     * Bruteforce algorithm used to generate prime numbers.
     */
    @Autowired
    private StrategySelector strategySelector;

    /**
     *
     * @param range from controller layer
     * @param algorithm for strategy
     * @return result from core logic for generating prime list
     */
    public List<Integer> generatePrimeNumbersForRange(final int range, final String algorithm) {
        PrimeGenerator primeGenerator =  strategySelector.selectStrategy(algorithm);
        return primeGenerator.generatePrimeNumbersInRange(range);
    }
}
