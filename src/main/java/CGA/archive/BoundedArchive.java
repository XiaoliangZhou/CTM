package CGA.archive;

import java.util.Comparator;

public interface BoundedArchive<S> extends Archive<S> {

    int getMaxSize();

    Comparator<S> getComparator();

    void computeDensityEstimator();

    void sortByDensityEstimator();
}
