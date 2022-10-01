package CGA.solutionattribute;

import java.util.List;

public interface Ranking<S> extends SolutionAttribute<S, Integer> {
    Ranking<S> computeRanking(List<S> var1);

    List<S> getSubfront(int var1);

    int getNumberOfSubfronts();
}
