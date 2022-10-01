package CGA.multiobjective.zdt;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import CGA.problem.impl.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

public class ZDT1 extends AbstractDoubleProblem {
    public ZDT1() {
        this(30);
    }

    public ZDT1(Integer numberOfVariables) {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(2);
        this.setName("ZDT1");
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(0.0D);
            upperLimit.add(1.0D);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }
    public void evaluate(Chrome solution) {
        double[] f = new double[this.getNumberOfObjectives()];
        f[0] = (Double)solution.getVariableValue(0);
        double g = this.evalG(solution);
        double h = this.evalH(f[0], g);
        f[1] = h * g;
        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    }

    protected double evalG(Chrome solution) {
        double g = 0.0D;

        for(int i = 1; i < solution.getNumberOfVariables(); ++i) {
            g += (Double)solution.getVariableValue(i);
        }

        double constant = 9.0D / (double)(solution.getNumberOfVariables() - 1);
        return constant * g + 1.0D;
    }

    protected double evalH(double f, double g) {
        double h = 1.0D - Math.sqrt(f / g);
        return h;
    }


}
