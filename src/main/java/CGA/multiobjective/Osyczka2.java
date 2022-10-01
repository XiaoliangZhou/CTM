package CGA.multiobjective;


import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import util.NumberOfViolatedConstraints;
import util.OverallConstraintViolation;


import java.util.Arrays;
import java.util.List;


public class Osyczka2 extends AbstractDoubleProblem {
    public OverallConstraintViolation<Chrome> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<Chrome> numberOfViolatedConstraints;

    public Osyczka2() {
        this.setNumberOfVariables(6);
        this.setNumberOfObjectives(2);
        this.setNumberOfConstraints(6);
        this.setName("Osyczka2");
        List<Double> lowerLimit = Arrays.asList(0.0D, 0.0D, 1.0D, 0.0D, 1.0D, 0.0D);
        List<Double> upperLimit = Arrays.asList(10.0D, 10.0D, 5.0D, 6.0D, 5.0D, 10.0D);
        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
        this.overallConstraintViolationDegree = new OverallConstraintViolation();
        this.numberOfViolatedConstraints = new NumberOfViolatedConstraints();
    }

    public void evaluate(Chrome solution) {
        double[] fx = new double[this.getNumberOfObjectives()];
        double x1 = (Double)solution.getVariableValue(0);
        double x2 = (Double)solution.getVariableValue(1);
        double x3 = (Double)solution.getVariableValue(2);
        double x4 = (Double)solution.getVariableValue(3);
        double x5 = (Double)solution.getVariableValue(4);
        double x6 = (Double)solution.getVariableValue(5);
        fx[0] = -(25.0D * (x1 - 2.0D) * (x1 - 2.0D) + (x2 - 2.0D) * (x2 - 2.0D) + (x3 - 1.0D) * (x3 - 1.0D) + (x4 - 4.0D) * (x4 - 4.0D) + (x5 - 1.0D) * (x5 - 1.0D));
        fx[1] = x1 * x1 + x2 * x2 + x3 * x3 + x4 * x4 + x5 * x5 + x6 * x6;
        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
        this.evaluateConstraints(solution);
    }

    private void evaluateConstraints(Chrome solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];
        double x1 = (Double)solution.getVariableValue(0);
        double x2 = (Double)solution.getVariableValue(1);
        double x3 = (Double)solution.getVariableValue(2);
        double x4 = (Double)solution.getVariableValue(3);
        double x5 = (Double)solution.getVariableValue(4);
        double x6 = (Double)solution.getVariableValue(5);
        constraint[0] = (x1 + x2) / 2.0D - 1.0D;
        constraint[1] = (6.0D - x1 - x2) / 6.0D;
        constraint[2] = (2.0D - x2 + x1) / 2.0D;
        constraint[3] = (2.0D - x1 + 3.0D * x2) / 2.0D;
        constraint[4] = (4.0D - (x3 - 3.0D) * (x3 - 3.0D) - x4) / 4.0D;
        constraint[5] = ((x5 - 3.0D) * (x5 - 3.0D) + x6 - 4.0D) / 4.0D;
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
