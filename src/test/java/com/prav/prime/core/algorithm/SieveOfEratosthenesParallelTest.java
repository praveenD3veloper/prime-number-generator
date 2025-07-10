package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SieveOfEratosthenesParallelTest {

    private final PrimeGenerator sieveParallel = new SieveOfEratosthenesParallel();

    @Test
    void testGeneratePrimes_UpTo10() {
        List<Integer> expected = List.of(2, 3, 5, 7);
        List<Integer> actual = sieveParallel.generatePrimeNumbersInRange(10);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimes_UpTo1_ShouldReturnEmpty() {
        List<Integer> actual = sieveParallel.generatePrimeNumbersInRange(1);
        assertEquals(List.of(), actual);
    }

    @Test
    void testGeneratePrimes_UpTo2() {
        List<Integer> actual = sieveParallel.generatePrimeNumbersInRange(2);
        assertEquals(List.of(2), actual);
    }

    @Test
    void testGeneratePrimes_UpTo29() {
        List<Integer> expected = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        List<Integer> actual = sieveParallel.generatePrimeNumbersInRange(29);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimes_AroundChunkBoundaries() {
        // Forces chunk division; MAX_CHUNKS = 6 so this range > 6 should trigger multithreading
        List<Integer> actual = sieveParallel.generatePrimeNumbersInRange(50);
        List<Integer> expected = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47);
        assertEquals(expected, actual);
    }

    @Test
    void testGeneratePrimes_LargeInput() {
        // Basic sanity check for larger range
        List<Integer> primes = sieveParallel.generatePrimeNumbersInRange(100);
        // Should include some known primes near the end
        assertEquals(true, primes.contains(97));
        assertEquals(false, primes.contains(100));
    }
}
