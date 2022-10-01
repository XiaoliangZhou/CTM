package CGA.Model;

import java.util.HashSet;
import java.util.Set;

public class ParetoRank {
    /*等级编号*/
    int rank;
    Set<Integer> set = new HashSet<>();

    public ParetoRank() {
    }

    public int getRank() {
        return rank;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setSet(Set<Integer> set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "ParetoRank{" +
                "rank=" + rank +
                ", set=" + set +
                '}';
    }
}
