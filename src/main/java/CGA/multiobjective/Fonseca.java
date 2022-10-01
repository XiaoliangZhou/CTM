package CGA.multiobjective;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;

import java.util.ArrayList;
import java.util.List;

public class Fonseca extends AbstractDoubleProblem {
    public Fonseca() {
        this.setNumberOfVariables(3);
        this.setNumberOfObjectives(2);
        this.setNumberOfConstraints(0);
        this.setName("Fonseca");
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(-4.0D);
            upperLimit.add(4.0D);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    public void evaluate(Chrome solution) {
        int numberOfVariables = this.getNumberOfVariables();
        double[] f = new double[this.getNumberOfObjectives()];
        double[] x = new double[numberOfVariables];

        for(int i = 0; i < numberOfVariables; ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        double sum1 = 0.0D;

        for(int i = 0; i < numberOfVariables; ++i) {
            sum1 += StrictMath.pow(x[i] - 1.0D / StrictMath.sqrt((double)numberOfVariables), 2.0D);
        }

        double exp1 = StrictMath.exp(-1.0D * sum1);
        f[0] = 1.0D - exp1;
        double sum2 = 0.0D;

        for(int i = 0; i < numberOfVariables; ++i) {
            sum2 += StrictMath.pow(x[i] + 1.0D / StrictMath.sqrt((double)numberOfVariables), 2.0D);
        }

        double exp2 = StrictMath.exp(-1.0D * sum2);
        f[1] = 1.0D - exp2;
        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    }
}
