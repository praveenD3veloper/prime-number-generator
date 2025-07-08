package com.prav.prime.core;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BruteForcePrimeGenerator {
    /**
     *
     * @param num input for the range
     * @return list of primes
     */
    public List<Integer> findPrimeNumbersInRange(final int num) {
        return IntStream.rangeClosed(2, num)
                .filter(this::isPrime)
                .boxed()
                .collect(Collectors.toList());
    }

    private boolean isPrime(final int number) {
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
