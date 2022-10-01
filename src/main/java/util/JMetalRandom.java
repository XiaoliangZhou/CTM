package util;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import java.io.Serializable;

public class JMetalRandom implements Serializable {
    private static JMetalRandom instance;
    private PseudoRandomGenerator randomGenerator = new JavaRandomGenerator();

    private JMetalRandom() {
    }

    public static JMetalRandom getInstance() {
        if (instance == null) {
            instance = new JMetalRandom();
        }

        return instance;
    }

    public void setRandomGenerator(PseudoRandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public PseudoRandomGenerator getRandomGenerator() {
        return this.randomGenerator;
    }

    public int nextInt(int lowerBound, int upperBound) {
        return this.randomGenerator.nextInt(lowerBound, upperBound);
    }

    public double nextDouble() {
        return this.randomGenerator.nextDouble();
    }

    public double nextDouble(double lowerBound, double upperBound) {
        return this.randomGenerator.nextDouble(lowerBound, upperBound);
    }

    public void setSeed(long seed) {
        this.randomGenerator.setSeed(seed);
    }

    public long getSeed() {
        return this.randomGenerator.getSeed();
    }

    public String getGeneratorName() {
        return this.randomGenerator.getName();
    }
}
