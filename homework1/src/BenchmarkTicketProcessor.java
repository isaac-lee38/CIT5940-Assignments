package homework1.src;

import java.util.*;

public class BenchmarkTicketProcessor {

    public static void main(String[] args) {

        benchmark("ArrayList + Short Queue", () -> {
            List<String> q = new ArrayList<>();
            createShortQueue(q);
            processQueue(q);
        });

        benchmark("ArrayList + Long Queue", () -> {
            List<String> q = new ArrayList<>();
            createLongQueue(q);
            processQueue(q);
        });

        benchmark("LinkedList + Short Queue", () -> {
            List<String> q = new LinkedList<>();
            createShortQueue(q);
            processQueue(q);
        });

        benchmark("LinkedList + Long Queue", () -> {
            List<String> q = new LinkedList<>();
            createLongQueue(q);
            processQueue(q);
        });
    }

    // ---- Core benchmark helper ----
    private static void benchmark(String label, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();

        double ms = (end - start) / 1_000_000.0;
        System.out.printf("%-30s : %.3f ms%n", label, ms);
    }

    // ---- Queue processor ----
    private static void processQueue(List<String> queue) {
        while (!queue.isEmpty()) {
            queue.remove(0);
        }
    }

    // ---- Queue creators ----
    private static void createShortQueue(List<String> queue) {
        for (int i = 1; i <= 50; i++) {
            queue.add("Ticket #" + i);
        }
    }

    private static void createLongQueue(List<String> queue) {
        for (int i = 1; i <= 20_000; i++) {
            queue.add("Ticket #" + i);
        }
    }
}
