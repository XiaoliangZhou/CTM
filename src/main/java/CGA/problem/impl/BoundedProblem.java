package CGA.problem.impl;

import CGA.problem.Problem;

public interface BoundedProblem<T extends Number, S> extends Problem<S> {
    T getLowerBound(int var1);

    T getUpperBound(int var1);
}
