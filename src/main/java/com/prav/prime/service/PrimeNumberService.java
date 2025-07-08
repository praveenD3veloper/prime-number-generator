package com.prav.prime.service;

import com.prav.prime.core.BruteForcePrimeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrimeNumberService {

    /**
     * Bruteforce algorithm used to generate prime numbers.
     */
    @Autowired
    private BruteForcePrimeGenerator bruteForcePrimeGenerator;

    /**
     *
     * @param range from controller layer
     * @return result from core logic for generating prime list
     */
    public List<Integer> generatePrimeNumbersForRange(final int range) {
        return bruteForcePrimeGenerator.findPrimeNumbersInRange(range);
    }
}
