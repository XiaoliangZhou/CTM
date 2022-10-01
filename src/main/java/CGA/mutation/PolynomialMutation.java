package CGA.mutation;

import CGA.Model.Chrome;
import CGA.problem.Problem;
import CGA.problem.impl.DoubleProblem;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import util.RandomGenerator;

public class PolynomialMutation {

    private double distributionIndex;
    private double mutationProbability;
    private RandomGenerator<Double> randomGenerator;

    public PolynomialMutation() {
        this(0.01D, 20.0D);
    }

    public PolynomialMutation(Problem<Chrome> problem, double distributionIndex) {
        this(1.0D / (double)problem.getNumberOfVariables(), distributionIndex);
    }

    public PolynomialMutation(double mutationProbability,double distributionIndex) {
        this(mutationProbability,distributionIndex,()->{
            return JMetalRandom.getInstance().nextDouble();
        });
    }

    public PolynomialMutation(double mutationProbability, double distributionIndex,  RandomGenerator<Double> randomGenerator) {
        this.distributionIndex = distributionIndex;
        this.mutationProbability = mutationProbability;
        this.randomGenerator = randomGenerator;
    }

    public Chrome execute(Chrome solution) throws JMetalException {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        } else {
            this.doMutation(this.mutationProbability, solution);
            return solution;
        }
    }

    public void doMutation(double mutationProbability, Chrome solution) {
        for(int i = 0; i < solution.getNumberOfVariables(); ++i) {
            if ((Double)this.randomGenerator.getRandomValue() <= mutationProbability) {
                double y = (Double)solution.getVariableValue(i);
                double yl = solution.getLowerBound(i);
                double yu = solution.getUpperBound(i);
                if (yl == yu) {
                    y = yl;
                } else {
                    double delta1 = (y - yl) / (yu - yl);
                    double delta2 = (yu - y) / (yu - yl);
                    double rnd = (Double)this.randomGenerator.getRandomValue();
                    double mutPow = 1.0D / (this.distributionIndex + 1.0D);
                    double deltaq;
                    double val;
                    double xy;
                    if (rnd <= 0.5D) {
                        xy = 1.0D - delta1;
                        val = 2.0D * rnd + (1.0D - 2.0D * rnd) * Math.pow(xy, this.distributionIndex + 1.0D);
                        deltaq = Math.pow(val, mutPow) - 1.0D;
                    } else {
                        xy = 1.0D - delta2;
                        val = 2.0D * (1.0D - rnd) + 2.0D * (rnd - 0.5D) * Math.pow(xy, this.distributionIndex + 1.0D);
                        deltaq = 1.0D - Math.pow(val, mutPow);
                    }

                    y += deltaq * (yu - yl);
                    y = this.repairSolutionVariableValue(y, yl, yu);
                }

                solution.setVariableValue(i, y);
            }
        }

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
