package com.tco.misc;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

public class TestMonteCarlo {

    @Test
    @DisplayName("zack722: Coin should flip correct number of times and result is within valid range")
    public void testCoinFlipInRange() throws Exception {
        long flips = 1_000_000L;
        MonteCarlo.Coin coin = new MonteCarlo.Coin(flips, 42);
        long result = coin.call();
        assertTrue(result >= 0 && result <= flips, "Result should be between 0 and total flips");
    }

    @Test
    @DisplayName("zack722: Multiple Coin instances with same seed should return same result")
    public void testCoinDeterminism() throws Exception {
        long flips = 100_000L;
        long seed = 12345;

        Callable<Long> coin1 = new MonteCarlo.Coin(flips, seed);
        Callable<Long> coin2 = new MonteCarlo.Coin(flips, seed);

        assertEquals(coin1.call(), coin2.call(), "Results from same seed should be equal");
    }

    @Test
    @DisplayName("zack722: Coin with 0 flips should return 0")
    public void testCoinZeroFlips() throws Exception {
        MonteCarlo.Coin coin = new MonteCarlo.Coin(0, 999);
        assertEquals(0L, coin.call(), "0 flips should return 0 heads");
    }
    @Test
    @DisplayName("cougar: testing to make sure the coin produces different results")
    public void testDifferentResults() throws Exception{
        MonteCarlo.Coin coin1 = new MonteCarlo.Coin(1000,1);
        MonteCarlo.Coin coin2 = new MonteCarlo.Coin(1000,2);
        int trials = 10_000;
        int differentResults = IntStream.range(0,trials).parallel().map(i->{
            try {
                return coin1.call().equals(coin2.call()) ? 0 : 1;
            } catch(Exception e){
                return 0;
            }
        })
        .sum();

        double probability = (double) differentResults / trials;
        assertTrue (probability > 0.95);
    }
    @Test
    @DisplayName("bryce24: MonteCarlo should distribute flips evenly among threads")
    public void testMonteCarloThreadDistribution() throws Exception {
    int cores = Runtime.getRuntime().availableProcessors();
    long flipsPerThread = 1_000_000L;
    long totalFlips = flipsPerThread * cores;
    
    Set<Callable<Long>> threads = new HashSet<>();
    for (int i = 0; i < cores; i++) {
        threads.add(new MonteCarlo.Coin(flipsPerThread, i));
    }

    ExecutorService executorService = Executors.newFixedThreadPool(cores);
    List<Future<Long>> results = executorService.invokeAll(threads);
    executorService.shutdown();

    long totalHeads = 0;
    for (Future<Long> result : results) {
        totalHeads += result.get();
    }

    assertEquals(totalFlips, flipsPerThread * cores, "Total flips should match the expected amount.");
    assertTrue(totalHeads >= 0 && totalHeads <= totalFlips, "Total heads should be within the valid range.");
}


    


}
