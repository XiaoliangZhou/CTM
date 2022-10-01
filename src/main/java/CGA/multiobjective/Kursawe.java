package CGA.multiobjective;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;

import java.util.ArrayList;
import java.util.List;

public class Kursawe extends AbstractDoubleProblem {
    public Kursawe() {
        this(3);
    }

    public Kursawe(Integer numberOfVariables) {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(2);
        this.setName("Kursawe");
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(-5.0D);
            upperLimit.add(5.0D);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    public void evaluate(Chrome solution) {
        double[] fx = new double[this.getNumberOfObjectives()];
        double[] x = new double[this.getNumberOfVariables()];

        int var;
        for(var = 0; var < solution.getNumberOfVariables(); ++var) {
            x[var] = (Double)solution.getVariableValue(var);
        }

        fx[0] = 0.0D;

        for(var = 0; var < solution.getNumberOfVariables() - 1; ++var) {
            double xi = x[var] * x[var];
            double xj = x[var + 1] * x[var + 1];
            double aux = -0.2D * Math.sqrt(xi + xj);
            fx[0] += -10.0D * Math.exp(aux);
        }

        fx[1] = 0.0D;

        for(var = 0; var < solution.getNumberOfVariables(); ++var) {
            fx[1] += Math.pow(Math.abs(x[var]), 0.8D) + 5.0D * Math.sin(Math.pow(x[var], 3.0D));
        }

        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }
}
