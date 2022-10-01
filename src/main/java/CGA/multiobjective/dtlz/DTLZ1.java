package CGA.multiobjective.dtlz;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

public class DTLZ1 extends AbstractDoubleProblem {
    public DTLZ1() {
        this(7, 3);
    }

    public DTLZ1(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setName("DTLZ1");
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
        int numberOfVariables = this.getNumberOfVariables();
        int numberOfObjectives = this.getNumberOfObjectives();
        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];
        int k = this.getNumberOfVariables() - this.getNumberOfObjectives() + 1;

        for(int i = 0; i < numberOfVariables; ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        double g = 0.0D;

        int i;
        for(i = numberOfVariables - k; i < numberOfVariables; ++i) {
            g += (x[i] - 0.5D) * (x[i] - 0.5D) - Math.cos(62.83185307179586D * (x[i] - 0.5D));
        }

        g = 100.0D * ((double)k + g);

        for(i = 0; i < numberOfObjectives; ++i) {
            f[i] = (1.0D + g) * 0.5D;
        }

        for(i = 0; i < numberOfObjectives; ++i) {
            int aux;
            for(aux = 0; aux < numberOfObjectives - (i + 1); ++aux) {
                f[i] *= x[aux];
            }

            if (i != 0) {
                aux = numberOfObjectives - (i + 1);
                f[i] *= 1.0D - x[aux];
            }
        }

        for(i = 0; i < numberOfObjectives; ++i) {
            solution.setObjective(i, f[i]);
        }

    }
}
