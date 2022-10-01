package CGA.multiobjective.glt;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;

import java.util.ArrayList;
import java.util.List;

public class GLT2 extends AbstractDoubleProblem {
    public GLT2() {
        this(10);
    }

    public GLT2(int numberOfVariables) {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(2);
        this.setName("GLT2");
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
        solution.setObjective(0, (1.0D + this.g(solution)) * (1.0D - Math.cos(3.141592653589793D * (Double)solution.getVariableValue(0) / 2.0D)));
        solution.setObjective(1, (1.0D + this.g(solution)) * (10.0D - 10.0D * Math.sin((Double)solution.getVariableValue(0) * 3.141592653589793D / 2.0D)));
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
