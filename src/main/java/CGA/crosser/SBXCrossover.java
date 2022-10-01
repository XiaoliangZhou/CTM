package CGA.crosser;

import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;
import util.JMetalRandom;
import util.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class SBXCrossover {

    private double distributionIndex;
    private double crossoverProbability;
    private RandomGenerator<Double> randomGenerator;

    public SBXCrossover(double distributionIndex, double crossoverProbability, RandomGenerator<Double> randomGenerator) {
        this.distributionIndex = distributionIndex;
        this.crossoverProbability = crossoverProbability;
        this.randomGenerator = randomGenerator;
    }

    public SBXCrossover(double distributionIndex, double crossoverProbability) {
        this(crossoverProbability, distributionIndex, () -> {
            return JMetalRandom.getInstance().nextDouble();
        });
    }
    public List<Chrome> execute(List<Chrome> solutions) {
        if (null == solutions) {
            throw new JMetalException("Null parameter");
        } else if (solutions.size() < 2) {
            throw new JMetalException("There must be two parents instead of " + solutions.size());
        } else {
            return this.doCrossover(this.crossoverProbability, solutions.get(0), solutions.get(1));
        }
    }

    public List<Chrome> doCrossover(double probability, Chrome parent1, Chrome parent2) {
        List<Chrome> offspring = new ArrayList(2);
        offspring.add((Chrome)parent1.copy());
        offspring.add((Chrome)parent2.copy());
        if ((Double)this.randomGenerator.getRandomValue() <= probability) {
            for(int i = 0; i < parent1.getNumberOfVariables(); ++i) {
                double valueX1 = (Double)parent1.getVariableValue(i);
                double valueX2 = (Double)parent2.getVariableValue(i);
                if ((Double)this.randomGenerator.getRandomValue() <= 0.5D) {
                    if (Math.abs(valueX1 - valueX2) > 1.0E-14D) {
                        double y1;
                        double y2;
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        double lowerBound = parent1.getLowerBound(i);;
                        double upperBound = parent1.getUpperBound(i);
                        double rand = (Double)this.randomGenerator.getRandomValue();
                        double beta = 1.0D + 2.0D * (y1 - lowerBound) / (y2 - y1);
                        double alpha = 2.0D - Math.pow(beta, -(this.distributionIndex + 1.0D));
                        double betaq;
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.distributionIndex + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.distributionIndex + 1.0D));
                        }

                        double c1 = 0.5D * (y1 + y2 - betaq * (y2 - y1));
                        beta = 1.0D + 2.0D * (upperBound - y2) / (y2 - y1);
                        alpha = 2.0D - Math.pow(beta, -(this.distributionIndex + 1.0D));
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.distributionIndex + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.distributionIndex + 1.0D));
                        }

                        double c2 = 0.5D * (y1 + y2 + betaq * (y2 - y1));
                        c1 = this.repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = this.repairSolutionVariableValue(c2, lowerBound, upperBound);
                        if ((Double)this.randomGenerator.getRandomValue() <= 0.5D) {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c2);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c1);
                        } else {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c1);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c2);
                        }
                    } else {
                        ((Chrome)offspring.get(0)).setVariableValue(i, valueX1);
                        ((Chrome)offspring.get(1)).setVariableValue(i, valueX2);
                    }
                } else {
                    ((Chrome)offspring.get(0)).setVariableValue(i, valueX2);
                    ((Chrome)offspring.get(1)).setVariableValue(i, valueX1);
                }
            }
        }

        return offspring;
    }
    public double repairSolutionVariableValue(double value, double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new JMetalException("The lower bound (" + lowerBound + ") is greater than the upper bound (" + upperBound + ")");
        } else {
            double result = value;
            if (value < lowerBound) {
                result = lowerBound;
            }

            if (value > upperBound) {
                result = upperBound;
            }

            return result;
        }
    }
}
