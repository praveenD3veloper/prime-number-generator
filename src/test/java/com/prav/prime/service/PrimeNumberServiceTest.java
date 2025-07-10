package com.prav.prime.service;

import com.prav.prime.core.PrimeGenerator;
import com.prav.prime.core.StrategySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrimeGeneratorServiceTest {

    @Mock
    private StrategySelector strategySelector;

    @Mock
    private PrimeGenerator primeGenerator;

    @InjectMocks
    private PrimeNumberService primeNumberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindPrimeNumbersInRange_ValidInput() {
        int range = 10;
        String algorithm = "bruteforce";
        List<Integer> expectedPrimeNumbers = List.of(2,3,5,7); // Initialize with expected data

        when(strategySelector.selectStrategy(algorithm)).thenReturn(primeGenerator);
        when(primeGenerator.generatePrimeNumbersInRange(range)).thenReturn(expectedPrimeNumbers);

        List<Integer> primeNumbers = primeNumberService.generatePrimeNumbersForRange(range, algorithm);

        assertNotNull(primeNumbers);
        assertEquals(expectedPrimeNumbers, primeNumbers);
    }

}

