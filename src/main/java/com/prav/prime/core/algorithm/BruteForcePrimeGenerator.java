package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BruteForcePrimeGenerator implements PrimeGenerator {
    /**
     * @param num is the range end for finding prime numbers
     * @return List of prime numbers found in the range
     */
    @Override
    public List<Integer> generatePrimeNumbersInRange(final int num) {
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
