package CGA.comparator;

import CGA.Model.Chrome;


import java.io.Serializable;
import java.util.Comparator;

public class RankingAndCrowdingDistanceComparator<S extends Chrome> implements Comparator<S>, Serializable {
    private final Comparator<Chrome> rankComparator = new RankingComparator();
    private final Comparator<Chrome> crowdingDistanceComparator = new CrowdingDistanceComparator();

    public RankingAndCrowdingDistanceComparator() {
    }

    public int compare(Chrome solution1, Chrome solution2) {
        int result = this.rankComparator.compare(solution1, solution2);
        if (result == 0) {
            result = this.crowdingDistanceComparator.compare(solution1, solution2);
        }

        return result;
    }
}
