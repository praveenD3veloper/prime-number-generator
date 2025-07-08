package com.prav.prime.controller;

import com.prav.prime.model.response.Result;
import com.prav.prime.service.PrimeNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/primes")
@Slf4j
public class PrimeNumberController {

    /**
     * service layer dependency.
     */
    @Autowired
    private PrimeNumberService primeNumberService;

    /**
     *
     * @param range to generate prime numbers
     * @return list of prime numbers
     */
    @GetMapping(path = "/{range}" ,
            produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Result> getPrimes(@PathVariable("range") final int range) {
        if (range <= 0) {
            log.error("Invalid range provided in the request = {}", range);
            throw new IllegalArgumentException("invalid range");
        }

        Result result = new Result(range, primeNumberService.generatePrimeNumbersForRange(range));
       return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
