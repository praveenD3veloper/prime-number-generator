package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SieveOfEratosthenesTest {

    private final PrimeGenerator sieve = new SieveOfEratosthenes();

    @Test
    void testGeneratePrimes_UpTo10() {
        List<Integer> expected = List.of(2, 3, 5, 7);
        assertEquals(expected, sieve.generatePrimeNumbersInRange(10));
    }

    @Test
    void testGeneratePrimes_UpTo2() {
        List<Integer> expected = List.of(2);
        assertEquals(expected, sieve.generatePrimeNumbersInRange(2));
    }

    @Test
    void testGeneratePrimes_UpTo1_ShouldBeEmpty() {
        List<Integer> expected = List.of();
        assertEquals(expected, sieve.generatePrimeNumbersInRange(1));
    }

    @Test
    void testGeneratePrimes_UpTo0_ShouldBeEmpty() {
        List<Integer> expected = List.of();
        assertEquals(expected, sieve.generatePrimeNumbersInRange(0));
    }

    @Test
    void testGeneratePrimes_UpTo29() {
        List<Integer> expected = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        List<Integer> actual = sieve.generatePrimeNumbersInRange(29);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimes_CompositeOnly() {
        List<Integer> actual = sieve.generatePrimeNumbersInRange(4); // should exclude 4
        List<Integer> expected = List.of(2, 3);
        assertEquals(expected, actual);
    }
}
