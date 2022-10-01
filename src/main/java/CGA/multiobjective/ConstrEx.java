package CGA.multiobjective;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import util.NumberOfViolatedConstraints;
import util.OverallConstraintViolation;


import java.util.Arrays;
import java.util.List;

public class ConstrEx extends AbstractDoubleProblem {
    public OverallConstraintViolation<Chrome> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<Chrome> numberOfViolatedConstraints;

    public ConstrEx() {
        this.setNumberOfVariables(2);
        this.setNumberOfObjectives(2);
        this.setNumberOfConstraints(2);
        this.setName("ConstrEx");
        List<Double> lowerLimit = Arrays.asList(0.1D, 0.0D);
        List<Double> upperLimit = Arrays.asList(1.0D, 5.0D);
        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
        this.overallConstraintViolationDegree = new OverallConstraintViolation();
        this.numberOfViolatedConstraints = new NumberOfViolatedConstraints();
    }

    public void evaluate(Chrome solution) {
        double[] f = new double[this.getNumberOfObjectives()];
        f[0] = (Double)solution.getVariableValue(0);
        f[1] = (1.0D + (Double)solution.getVariableValue(1)) / (Double)solution.getVariableValue(0);
        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
        this.evaluateConstraints(solution);
    }

    private void evaluateConstraints(Chrome solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];
        double x1 = (Double)solution.getVariableValue(0);
        double x2 = (Double)solution.getVariableValue(1);
        constraint[0] = x2 + 9.0D * x1 - 6.0D;
        constraint[1] = -x2 + 9.0D * x1 - 1.0D;
        double overallConstraintViolation = 0.0D;
        int violatedConstraints = 0;

        for(int i = 0; i < this.getNumberOfConstraints(); ++i) {
            if (constraint[i] < 0.0D) {
                overallConstraintViolation += constraint[i];
                ++violatedConstraints;
            }
        }

        this.overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        this.numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
