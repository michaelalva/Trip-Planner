package com.tco.misc;

import java.util.*;
import java.util.concurrent.*;

class MonteCarlo {

    private final static long flips = 350000000L;
    private final static int cores = Runtime.getRuntime().availableProcessors();

    public static void main(String[] argv) {
        long total = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(cores);

        try {
            List<Callable<Long>> threads = new ArrayList<>();
            for (int i = 0; i < cores; i++) {
                threads.add(new Coin(flips / cores, i));
            }

            List<Future<Long>> results = executorService.invokeAll(threads);
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                throw new RuntimeException("Executor did not terminate in time.");
            }

            for (Future<Long> result : results) {
                total += result.get(10, TimeUnit.SECONDS);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread execution was interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("A task failed during execution", e);
        } catch (TimeoutException e) {
            throw new RuntimeException("A task timed out", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }

        System.out.printf("Heads: %d/%d\n", total, flips);
    }

    public static class Coin implements Callable<Long> {
        private final long flips;
        private final Random rand;

        public Coin(long flips, long seed) {
            this.flips = flips;
            this.rand = new Random(seed);
        }

        @Override
        public Long call() {
            long heads = 0;
            for (long i = 0; i < flips; i++) {
                if (rand.nextBoolean()) heads++;
            }
            return heads;
        }
    }
}
