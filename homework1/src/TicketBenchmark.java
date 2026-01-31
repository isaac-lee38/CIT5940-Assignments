package homework1.src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TicketBenchmark {

    private static final int WARMUP_RUNS = 10000;
    private static final int MEASURE_RUNS = 100;

    public static void main(String[] args) {
        System.out.println("--- Starting JVM Warmup (10,000 iterations) ---");
        warmup();
        System.out.println("--- Warmup Complete ---\n");

        System.out.println("Results (Average of 100 runs):");
        System.out.println("Format: [Type] [Size] | Create: X ms | Process: Y ms | Total: Z ms");
        System.out.println("------------------------------------------------------------------");

        runFullSuite("ArrayList", 50, "Short");
        runFullSuite("LinkedList", 50, "Short");
        runFullSuite("ArrayList", 20000, "Long");
        runFullSuite("LinkedList", 20000, "Long");
    }

    private static void runFullSuite(String type, int count, String label) {
        double totalCreateNanos = 0;
        double totalProcessNanos = 0;

        for (int i = 0; i < MEASURE_RUNS; i++) {
            List<String> queue = type.equals("ArrayList") ? new ArrayList<>() : new LinkedList<>();

            // 1. Measure Creation
            long startCreate = System.nanoTime();
            for (int j = 1; j <= count; j++) {
                queue.add("Ticket #" + j);
            }
            totalCreateNanos += (System.nanoTime() - startCreate);

            // 2. Measure Processing (remove(0))
            long startProcess = System.nanoTime();
            while (!queue.isEmpty()) {
                queue.remove(0);
            }
            totalProcessNanos += (System.nanoTime() - startProcess);
        }

        printMetrics(type, label, count, totalCreateNanos / MEASURE_RUNS, totalProcessNanos / MEASURE_RUNS);
    }

    private static void printMetrics(String type, String label, int count, double avgCreate, double avgProcess) {
        double createMs = avgCreate / 1_000_000.0;
        double processMs = avgProcess / 1_000_000.0;
        double totalMs = createMs + processMs;

        System.out.printf("%-10s %-5s (%d) | Create: %8.4f ms | Process: %8.4f ms | Total: %8.4f ms\n",
                type, label, count, createMs, processMs, totalMs);
    }

    private static void warmup() {
        // Run both types through enough iterations to trigger JIT compilation
        for (int i = 0; i < WARMUP_RUNS; i++) {
            List<String> al = new ArrayList<>();
            List<String> ll = new LinkedList<>();
            for (int j = 0; j < 100; j++) {
                al.add("w"); ll.add("w");
            }
            while (!al.isEmpty()) { al.remove(0); ll.remove(0); }
        }
    }
}