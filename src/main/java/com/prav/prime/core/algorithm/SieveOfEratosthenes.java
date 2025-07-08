package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Uses sieves to generate primes in range.
 */
public class SieveOfEratosthenes implements PrimeGenerator {
    /**
     * overriding method for this algo.
     */
    @Override
    public List<Integer> generatePrimeNumbersInRange(final int n) {
        boolean[] isPrime = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= n; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        return IntStream.rangeClosed(2, n)
                .filter(i -> isPrime[i])
                .boxed()
                .collect(Collectors.toList());
    }
}
