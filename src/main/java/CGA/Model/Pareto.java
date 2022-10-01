package CGA.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Pareto implements Cloneable {
    int n;
    Set<Integer> pset = new TreeSet<>();

    public Pareto() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getN() {
        return n;
    }

    public Set<Integer> getPset() {
        return pset;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setPset(Set<Integer> pset) {
        this.pset = pset;
    }

    @Override
    public String toString() {
        return "Pareto{" +
                "n=" + n +
                ", pset=" + pset +
                '}';
    }
}
