package com.prav.prime.core;

import java.util.List;

public interface PrimeGenerator {
    /**
     * @param range is the range end for finding prime numbers
     * @return List of prime numbers found in the range
     */
    List<Integer> generatePrimeNumbersInRange(int range);
}
