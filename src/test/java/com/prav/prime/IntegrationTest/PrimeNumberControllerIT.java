package com.prav.prime.IntegrationTest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PrimeNumberControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetPrimeNumbers_ValidInput_AcceptXML() throws Exception {
        int range = 10;
        String algorithm = "bruteforce";

        mockMvc.perform(MockMvcRequestBuilders.get("/primes/{range}", range)
                        .param("algorithm", algorithm)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(getExpectedXmlString(range)))
                .andReturn();
    }

    @Test
    void testGetPrimeNumbers_InvalidRange() throws Exception {
        int range = -5;
        String algorithm = "bruteforce";

        mockMvc.perform(MockMvcRequestBuilders.get("/primes/{range}", range)
                        .param("algorithm", algorithm)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusName", Matchers.equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", Matchers.equalTo("Invalid range provided!. Correct format is /primes/{range} where range is positive integer.")));
    }

    @Test
    public void testGetPrimeNumbersIntegration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/primes/10").param("algorithm", "sieveoferatosthenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.initial", Matchers.is(10)))
                .andExpect(jsonPath("$.primes", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.primes", Matchers.contains(2, 3, 5, 7)))
                .andReturn();
    }
    @Test
    void testGetPrimeNumbers_NotANumber() throws Exception {

        String algorithm = "bruteforce";

        mockMvc.perform(MockMvcRequestBuilders.get("/primes/a")
                        .param("algorithm", algorithm)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusName", Matchers.equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", Matchers.equalTo("Provided range is not a number. Correct format is /primes/{range} where range is positive integer.")));
    }

    @Test
    void testGetPrimeNumbers_InvalidPath() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/primes/")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    private String getExpectedXmlString(int range) {
        return String.format(
                "<Result>"+
                        "<initial>%d</initial>" +
                        "<primes>"+
                        "<primes>2</primes>" +
                        "<primes>3</primes>" +
                        "<primes>5</primes>" +
                        "<primes>7</primes>" +
                        "</primes>" +
                        "</Result>", range);
    }
}
