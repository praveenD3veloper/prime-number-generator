package com.prav.prime.controller;


import com.prav.prime.model.response.Result;
import com.prav.prime.service.PrimeNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class PrimeNumberControllerTest {
    @Mock
    private PrimeNumberService primeNumberService;

    @InjectMocks
    private PrimeNumberController primeNumberController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetPrimeNumbers_ValidInput() {
        int range = 10;
        String algorithm = "bruteforce";
        Result expectedResult = new Result(10, List.of(2, 3, 5, 7));

        when(primeNumberService.generatePrimeNumbersForRange(range)).thenReturn(List.of(2, 3, 5, 7));

        ResponseEntity<Result> response = primeNumberController.getPrimes(range);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getInitial() == 10;
        assert response.getBody().getPrimes().equals(Arrays.asList(2, 3, 5, 7));
    }

    @Test
    void testGetPrimeNumbers_InvalidRange() {
        int range = -5;
        String algorithm = "bruteforce";

        assertThrows(IllegalArgumentException.class, () -> primeNumberController.getPrimes(range));
    }

    @Test
    void testGetPrimeNumbers_ZeroRange() {
        int range = 0;
        assertThrows(IllegalArgumentException.class, () -> primeNumberController.getPrimes(range));
    }

    @Test
    void testGetPrimeNumbers_OneRange() {
        int range = 1;
        when(primeNumberService.generatePrimeNumbersForRange(range)).thenReturn(Collections.emptyList());

        ResponseEntity<Result> response = primeNumberController.getPrimes(range);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(range, response.getBody().getInitial());
        assertTrue(response.getBody().getPrimes().isEmpty());
    }

    @Test
    void testGetPrimeNumbers_LargeRange() {
        int range = 100_000;
        when(primeNumberService.generatePrimeNumbersForRange(range)).thenReturn(Collections.emptyList());

        ResponseEntity<Result> response = primeNumberController.getPrimes(range);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(range, response.getBody().getInitial());
    }

    @Test
    void testGetPrimeNumbers_ServiceReturnsEmptyList() {
        int range = 30;
        when(primeNumberService.generatePrimeNumbersForRange(range)).thenReturn(Collections.emptyList());

        ResponseEntity<Result> response = primeNumberController.getPrimes(range);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(range, response.getBody().getInitial());
        assertTrue(response.getBody().getPrimes().isEmpty());
    }
}