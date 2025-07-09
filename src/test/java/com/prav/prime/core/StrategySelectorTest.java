package com.prav.prime.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class StrategySelectorTest {

    @Mock
    private PrimeGenerator bruteForce;

    @Mock
    private PrimeGenerator sieveOfEratosthenes;

    @Mock
    private PrimeGenerator sieveOfEratosthenesInParallel;

    @InjectMocks
    private StrategySelector strategySelector;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        strategySelector.initializeStrategyMap();

    }

    @Test
    public void testSelectStrategyWithValidStrategy() {
        String validStrategy = "bruteforce";
        when(bruteForce.generatePrimeNumbersInRange(anyInt())).thenReturn(Arrays.asList(2, 3, 5, 7));

        List<Integer> primeNumbers = bruteForce.generatePrimeNumbersInRange(10);
        PrimeGenerator selectedStrategy = strategySelector.selectStrategy(validStrategy);

        assertEquals(bruteForce, selectedStrategy);
        verify(bruteForce, times(1)).generatePrimeNumbersInRange(anyInt());
        verifyNoMoreInteractions(sieveOfEratosthenes, sieveOfEratosthenesInParallel);
    }

    @Test
    public void testSelectStrategyWithNullStrategy() {
        PrimeGenerator selectedStrategy = strategySelector.selectStrategy(null);

        assertEquals(sieveOfEratosthenes, selectedStrategy);
        verifyNoMoreInteractions(bruteForce, sieveOfEratosthenesInParallel);
    }

    @Test
    public void testSelectStrategyWithInvalidStrategy() {
        String invalidStrategy = "invalid";
        PrimeGenerator selectedStrategy = strategySelector.selectStrategy(invalidStrategy);

        assertEquals(sieveOfEratosthenes, selectedStrategy);
        verifyNoMoreInteractions(bruteForce, sieveOfEratosthenesInParallel);
    }

    @Test
    public void testSelectParallelStrategy() {
        when(sieveOfEratosthenesInParallel.generatePrimeNumbersInRange(20)).thenReturn(List.of(2, 3, 5, 7, 11, 13, 17, 19));

        PrimeGenerator selectedStrategy = strategySelector.selectStrategy("sieveOfEratosthenesInParallel");
        List<Integer> result = selectedStrategy.generatePrimeNumbersInRange(20);

        assertEquals(sieveOfEratosthenesInParallel, selectedStrategy);
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19), result);

        verify(sieveOfEratosthenesInParallel).generatePrimeNumbersInRange(20);
        verifyNoMoreInteractions(bruteForce, sieveOfEratosthenes);
    }
}