package com.prav.prime.controller;

import com.prav.prime.model.response.Result;
import com.prav.prime.service.PrimeNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
     * @param algorithm (optional) the name of the algorithm to use for prime generation;
     *  if not specified, a default algorithm will be used
     * @return list of prime numbers
     */
    @GetMapping(path = "/{range}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<Result> getPrimes(@PathVariable("range") final int range,
                                            @RequestParam(value = "algorithm", required = false)
                                            final String algorithm) {
        if (range <= 0) {
            log.error("Invalid range provided in the request = {}", range);
            throw new IllegalArgumentException("invalid range");
        }
        log.info("Received Request with range = {}, algorithm = {}", range, algorithm);

        Result result = new Result(range, primeNumberService.generatePrimeNumbersForRange(range, algorithm));
        log.info("Responded to the request with range = {} , algorithm = {} with response = {}",
                range, algorithm, result.getPrimes().toString());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
