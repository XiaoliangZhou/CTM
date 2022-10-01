package CGA.multiobjective.glt;


import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;

import java.util.ArrayList;
import java.util.List;

public class GLT1 extends AbstractDoubleProblem {
    public GLT1() {
        this(10);
    }

    public GLT1(int numberOfVariables) {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(2);
        this.setName("GLT1");
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());
        lowerLimit.add(0.0D);
        upperLimit.add(1.0D);

        for(int i = 1; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(-1.0D);
            upperLimit.add(1.0D);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    public void evaluate(Chrome solution) {
        solution.setObjective(0, (1.0D + this.g(solution)) * (Double)solution.getVariableValue(0));
        solution.setObjective(1, (1.0D + this.g(solution)) * (2.0D - (Double)solution.getVariableValue(0) - Math.signum(Math.cos(6.283185307179586D * (Double)solution.getVariableValue(0)))));
    }

    private double g(Chrome solution) {
        double result = 0.0D;

        for(int i = 1; i < solution.getNumberOfVariables(); ++i) {
            double value = (Double)solution.getVariableValue(i) - Math.sin(6.283185307179586D * (Double)solution.getVariableValue(0) + (double)i * 3.141592653589793D / (double)solution.getNumberOfVariables());
            result += value * value;
        }

        return result;
    }
}
