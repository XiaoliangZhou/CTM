package util;


import algorithm.Algorithm;
import org.uma.jmetal.util.JMetalException;

public class AlgorithmRunner {
    private long computingTime;

    private AlgorithmRunner(AlgorithmRunner.Executor execute) {
        this.computingTime = execute.computingTime;
    }

    public long getComputingTime() {
        return this.computingTime;
    }

    public static class Executor {
        Algorithm<?> algorithm;
        long computingTime;

        public Executor(Algorithm algorithm) {
            this.algorithm = algorithm;
        }

        public AlgorithmRunner execute() {
            long initTime = System.currentTimeMillis();
            Thread thread = new Thread(this.algorithm);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException var5) {
                throw new JMetalException("Error in thread.join()", var5);
            }

            this.computingTime = System.currentTimeMillis() - initTime;
            return new AlgorithmRunner(this);
        }
    }
}
