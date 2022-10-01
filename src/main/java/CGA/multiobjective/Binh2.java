package CGA.multiobjective;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import util.NumberOfViolatedConstraints;
import util.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

public class Binh2 extends AbstractDoubleProblem {
    public OverallConstraintViolation<Chrome> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<Chrome> numberOfViolatedConstraints;

    public Binh2() {
        this.setNumberOfVariables(2);
        this.setNumberOfObjectives(2);
        this.setNumberOfConstraints(2);
        this.setName("Binh2");
        List<Double> lowerLimit = Arrays.asList(0.0D, 0.0D);
        List<Double> upperLimit = Arrays.asList(5.0D, 3.0D);
        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
        this.overallConstraintViolationDegree = new OverallConstraintViolation();
        this.numberOfViolatedConstraints = new NumberOfViolatedConstraints();
    }

    public void evaluate(Chrome solution) {
        double[] fx = new double[this.getNumberOfObjectives()];
        double[] x = new double[this.getNumberOfVariables()];

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        fx[0] = 4.0D * x[0] * x[0] + 4.0D * x[1] * x[1];
        fx[1] = (x[0] - 5.0D) * (x[0] - 5.0D) + (x[1] - 5.0D) * (x[1] - 5.0D);
        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
        this.evaluateConstraints(solution);
    }

    private void evaluateConstraints(Chrome solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];
        double x0 = (Double)solution.getVariableValue(0);
        double x1 = (Double)solution.getVariableValue(1);
        constraint[0] = -1.0D * (x0 - 5.0D) * (x0 - 5.0D) - x1 * x1 + 25.0D;
        constraint[1] = (x0 - 8.0D) * (x0 - 8.0D) + (x1 + 3.0D) * (x1 + 3.0D) - 7.7D;
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

