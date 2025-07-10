package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BruteForceTest {

    private final PrimeGenerator bruteForce = new BruteForce();

    @Test
    void testGeneratePrimeNumbersInRange_UpTo10() {
        List<Integer> expected = List.of(2, 3, 5, 7);
        List<Integer> actual = bruteForce.generatePrimeNumbersInRange(10);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimeNumbersInRange_SinglePrime() {
        List<Integer> expected = List.of(2);
        List<Integer> actual = bruteForce.generatePrimeNumbersInRange(2);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimeNumbersInRange_UpTo1_ShouldReturnEmpty() {
        List<Integer> actual = bruteForce.generatePrimeNumbersInRange(1);
        assertEquals(List.of(), actual);
    }

    @Test
    void testGeneratePrimeNumbersInRange_WithNonPrimes() {
        List<Integer> actual = bruteForce.generatePrimeNumbersInRange(4); // 2, 3 are primes; 4 is not
        assertEquals(List.of(2, 3), actual);
    }

    @Test
    void testGeneratePrimeNumbersInRange_LargePrime() {
        List<Integer> actual = bruteForce.generatePrimeNumbersInRange(29);
        List<Integer> expected = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        // Exclude 9, 15, 21, 25, 27
        assertEquals(expected, actual);
    }
}
