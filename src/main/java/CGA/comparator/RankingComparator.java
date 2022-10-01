package CGA.comparator;

import CGA.Model.Chrome;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import java.io.Serializable;
import java.util.Comparator;

public class RankingComparator<S extends Chrome> implements Comparator<S>, Serializable {
    private Ranking<S> ranking = new DominanceRanking();

    public RankingComparator() {
    }

    public int compare(Chrome solution1, Chrome solution2) {
        byte result;
        if (solution1 == null) {
            if (solution2 == null) {
                result = 0;
            } else {
                result = 1;
            }
        } else if (solution2 == null) {
            result = -1;
        } else {
            int rank1 = 2147483647;
            int rank2 = 2147483647;
            if (solution1.getRank() != -1.0D) {
                rank1 = solution1.getRank();
            }

            if (solution2.getRank() != -1.0D) {
                rank2 =solution2.getRank();
            }

            if (rank1 < rank2) {
                result = -1;
            } else if (rank1 > rank2) {
                result = 1;
            } else {
                result = 0;
            }
        }

        return result;
    }
}
