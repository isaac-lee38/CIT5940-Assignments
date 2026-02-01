package homework1.src;

import java.util.Random;
import java.util.Arrays;

public class BenchmarkMergeSort {

    // Configuration
    private static final int ARRAY_SIZE = 1000;
    private static final int NUM_WARMUP = 100;
    private static final int NUM_MEASURE = 100;
    
    public static void main(String[] args) {
        System.out.println("--- Starting Benchmark ---");
        System.out.println("Array Size: " + ARRAY_SIZE);
        System.out.println("Warmup Runs: " + NUM_WARMUP);
        System.out.println("Measured Runs: " + NUM_MEASURE);
        System.out.println("--------------------------");

        // --- WARMUP PHASE ---
        // We run the code multiple times to allow the Java Virtual Machine (JVM)
        // to optimize (JIT compile) the "hot" code paths before we start timing.
        System.out.print("Warming up...");
        for (int i = 0; i < NUM_WARMUP; i++) {
            int[] original = generateRandomArray(ARRAY_SIZE);
            
            // Warmup Standard MergeSort
            int[] data1 = original.clone();
            int[] buffer = new int[data1.length];
            MergeSort.mergeSort(data1, buffer, 0, data1.length - 1);

            // Warmup Javier MergeSort
            int[] data2 = original.clone();
            JavierMergeSort.mergeSort(data2);
        }
        System.out.println(" Done.");

        // --- MEASUREMENT PHASE ---
        long totalTimeStandard = 0;
        long totalTimeJavier = 0;

        for (int i = 0; i < NUM_MEASURE; i++) {
            // Generate fresh data for this iteration
            int[] original = generateRandomArray(ARRAY_SIZE);
            
            // 1. Test Standard MergeSort
            int[] data1 = original.clone();
            int[] buffer = new int[data1.length];
            
            long start1 = System.nanoTime();
            MergeSort.mergeSort(data1, buffer, 0, data1.length - 1);
            long end1 = System.nanoTime();
            totalTimeStandard += (end1 - start1);

            // 2. Test JavierMergeSort
            int[] data2 = original.clone();
            
            long start2 = System.nanoTime();
            JavierMergeSort.mergeSort(data2);
            long end2 = System.nanoTime();
            totalTimeJavier += (end2 - start2);
            
            // Optional: Validate sorting logic to ensure fairness
            // if (!Arrays.equals(data1, new JavierMergeSort().mergeSort(original.clone()))) {
            //    System.err.println("Mismatch detected!");
            // }
        }

        // --- RESULTS ---
        double avgStandard = totalTimeStandard / (double) NUM_MEASURE;
        double avgJavier = totalTimeJavier / (double) NUM_MEASURE;

        System.out.println("\n--- Results (Average per run) ---");
        System.out.printf("Standard Recursive MergeSort: %.2f ns (%.4f ms)%n", 
                          avgStandard, avgStandard / 1_000_000.0);
        
        System.out.printf("Javier (Queue) MergeSort:     %.2f ns (%.4f ms)%n", 
                          avgJavier, avgJavier / 1_000_000.0);

        System.out.println("\n--- Comparison ---");
        if (avgStandard < avgJavier) {
            double factor = avgJavier / avgStandard;
            System.out.printf("Standard Recursive is %.2fx FASTER than Javier's approach.%n", factor);
        } else {
            double factor = avgStandard / avgJavier;
            System.out.printf("Javier's approach is %.2fx FASTER than Standard Recursive.%n", factor);
        }
    }

    // Helper method to generate random int arrays
    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(5000);
        }
        return arr;
    }
}