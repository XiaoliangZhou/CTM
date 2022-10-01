package CGA.problem.impl;

import org.uma.jmetal.solution.Solution;

public interface DoubleSolution {
    Double getLowerBound(int var1);

    Double getUpperBound(int var1);
}
