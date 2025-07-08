package com.prav.prime.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @NotNull(message = "Initial value cannot be null")
    @JsonProperty
    private long initial;

    @JsonProperty
    private List<Integer> primes;

    /**
     * explicit constructor added for serialization / deserialization.
     */
    public Result() {
    }

    /**
     *
     * @param initial
     * @param primeNumberList
     */
    public Result(final long initial, final List<Integer> primeNumberList) {
        this.initial = initial;
        this.primes = primeNumberList;
    }

}
