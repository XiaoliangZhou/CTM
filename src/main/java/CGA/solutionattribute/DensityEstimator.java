package CGA.solutionattribute;

import java.util.List;

public interface DensityEstimator<S> extends SolutionAttribute<S, Double> {
    void computeDensityEstimator(List<S> var1);
}
