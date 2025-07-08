package com.prav.prime.core.algorithm;

import com.prav.prime.core.PrimeGenerator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SieveOfEratosthenesParallel implements PrimeGenerator {

    private static final int MAX_CHUNKS = 6;

    /**
     *
     * @param n is the range end for finding prime numbers
     *          uses parallel execution algo to reduce processing time
     * @return list of prime numbers in range
     */

    @Override
    public List<Integer> generatePrimeNumbersInRange(final int n) {
        int numberOfChunks = MAX_CHUNKS;
        int chunkSize = (n + numberOfChunks - 1) / numberOfChunks;

        BitSet isPrime = new BitSet(n + 1);
        isPrime.set(2, n + 1);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfChunks);
        List<Future<Void>> futures = new ArrayList<>();

        Object lock = new Object(); // Lock for synchronized access to shared data

        for (int chunk = 0; chunk < numberOfChunks; chunk++) {
            int start = chunk * chunkSize;
            int end = Math.min((chunk + 1) * chunkSize, n + 1);

            futures.add(executor.submit(() -> {
                sieveChunk(isPrime, start, end, lock);
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        return IntStream.rangeClosed(2, n)
                .filter(i -> {
                    synchronized (lock) {
                        return isPrime.get(i);
                    }
                })
                .boxed()
                .collect(Collectors.toList());
    }

    private void sieveChunk(final BitSet isPrime, final int start, final int end, final Object lock) {
        for (int p = 2; p * p < end; p++) {
            if (isPrime.get(p)) {
                int firstMultiple = Math.max(2 * p, (start + p - 1) / p * p);
                for (int i = firstMultiple; i < end; i += p) {
                    synchronized (lock) {
                        isPrime.clear(i);
                    }
                }
            }
        }
    }

}
