package homework1.src;

public class BenchmarkFindFirstInstance {

    private static final int RUNS = 1000;
    private static final int TARGET = 5;

    public static void main(String[] args) {

        runBenchmark("GridOne + MethodOne", true, true);
        runBenchmark("GridOne + MethodTwo", true, false);
        runBenchmark("GridTwo + MethodOne", false, true);
        runBenchmark("GridTwo + MethodTwo", false, false);
    }

    private static void runBenchmark(String label, boolean useGridOne, boolean useMethodOne) {

        int[][] grid = useGridOne
                ? FindFirstInstance.getGridOne()
                : FindFirstInstance.getGridTwo();

        // ---- Warm-up (important for JVM) ----
        for (int i = 0; i < 100; i++) {
            if (useMethodOne) {
                FindFirstInstance.findFirstInstanceOne(grid, TARGET);
            } else {
                FindFirstInstance.findFirstInstanceTwo(grid, TARGET);
            }
        }

        long totalTime = 0;

        // ---- Actual benchmark ----
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();

            if (useMethodOne) {
                FindFirstInstance.findFirstInstanceOne(grid, TARGET);
            } else {
                FindFirstInstance.findFirstInstanceTwo(grid, TARGET);
            }

            long end = System.nanoTime();
            totalTime += (end - start);
        }

        double avgMs = totalTime / (RUNS * 1_000_000.0);

        System.out.println(label + " average time: " + avgMs + " ms");
    }
}
