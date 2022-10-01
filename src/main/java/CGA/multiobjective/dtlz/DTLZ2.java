package CGA.multiobjective.dtlz;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;
import CGA.problem.impl.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

public class DTLZ2 extends AbstractDoubleProblem {
    public DTLZ2() {
        this(12, 3);
    }

    public DTLZ2(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        this.setNumberOfVariables(numberOfVariables);
        this.setNumberOfObjectives(numberOfObjectives);
        this.setName("DTLZ2");
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

        int k;
        for(k = 0; k < numberOfVariables; ++k) {
            x[k] = (Double)solution.getVariableValue(k);
        }

        k = this.getNumberOfVariables() - this.getNumberOfObjectives() + 1;
        double g = 0.0D;

        int i;
        for(i = numberOfVariables - k; i < numberOfVariables; ++i) {
            g += (x[i] - 0.5D) * (x[i] - 0.5D);
        }

        for(i = 0; i < numberOfObjectives; ++i) {
            f[i] = 1.0D + g;
        }

        for(i = 0; i < numberOfObjectives; ++i) {
            int aux;
            for(aux = 0; aux < numberOfObjectives - (i + 1); ++aux) {
                f[i] *= Math.cos(x[aux] * 0.5D * 3.141592653589793D);
            }

            if (i != 0) {
                aux = numberOfObjectives - (i + 1);
                f[i] *= Math.sin(x[aux] * 0.5D * 3.141592653589793D);
            }
        }

        for(i = 0; i < numberOfObjectives; ++i) {
            solution.setObjective(i, f[i]);
        }

    }
}
