package CGA.multiobjective;



import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import util.NumberOfViolatedConstraints;
import util.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

public class Golinski extends AbstractDoubleProblem {
    public OverallConstraintViolation<Chrome> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<Chrome> numberOfViolatedConstraints;

    public Golinski() {
        this.setNumberOfVariables(7);
        this.setNumberOfObjectives(2);
        this.setNumberOfConstraints(11);
        this.setName("Golinski");
        List<Double> lowerLimit = Arrays.asList(2.6D, 0.7D, 17.0D, 7.3D, 7.3D, 2.9D, 5.0D);
        List<Double> upperLimit = Arrays.asList(3.6D, 0.8D, 28.0D, 8.3D, 8.3D, 3.9D, 5.5D);
        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
        this.overallConstraintViolationDegree = new OverallConstraintViolation();
        this.numberOfViolatedConstraints = new NumberOfViolatedConstraints();
    }

    public void evaluate(Chrome solution) {
        double x1 = (Double)solution.getVariableValue(0);
        double x2 = (Double)solution.getVariableValue(1);
        double x3 = (Double)solution.getVariableValue(2);
        double x4 = (Double)solution.getVariableValue(3);
        double x5 = (Double)solution.getVariableValue(4);
        double x6 = (Double)solution.getVariableValue(5);
        double x7 = (Double)solution.getVariableValue(6);
        double f1 = 0.7854D * x1 * x2 * x2 * (10.0D * x3 * x3 / 3.0D + 14.933D * x3 - 43.0934D) - 1.508D * x1 * (x6 * x6 + x7 * x7) + 7.477D * (x6 * x6 * x6 + x7 * x7 * x7) + 0.7854D * (x4 * x6 * x6 + x5 * x7 * x7);
        double aux = 745.0D * x4 / (x2 * x3);
        double f2 = Math.sqrt(aux * aux + 1.69E7D) / (0.1D * x6 * x6 * x6);
        solution.setObjective(0, f1);
        solution.setObjective(1, f2);
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
        double x7 = (Double)solution.getVariableValue(6);
        constraint[0] = -(1.0D / (x1 * x2 * x2 * x3) - 0.037037037037037035D);
        constraint[1] = -(1.0D / (x1 * x2 * x2 * x3 * x3) - 0.0025157232704402514D);
        constraint[2] = -(x4 * x4 * x4 / (x2 * x3 * x3 * x6 * x6 * x6 * x6) - 0.5181347150259068D);
        constraint[3] = -(x5 * x5 * x5 / (x2 * x3 * x7 * x7 * x7 * x7) - 0.5181347150259068D);
        constraint[4] = -(x2 * x3 - 40.0D);
        constraint[5] = -(x1 / x2 - 12.0D);
        constraint[6] = -(5.0D - x1 / x2);
        constraint[7] = -(1.9D - x4 + 1.5D * x6);
        constraint[8] = -(1.9D - x5 + 1.1D * x7);
        double aux = 745.0D * x4 / (x2 * x3);
        double f2 = Math.sqrt(aux * aux + 1.69E7D) / (0.1D * x6 * x6 * x6);
        constraint[9] = -(f2 - 1300.0D);
        double a = 745.0D * x5 / (x2 * x3);
        double b = 1.575E8D;
        constraint[10] = -(Math.sqrt(a * a + b) / (0.1D * x7 * x7 * x7) - 1100.0D);
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
