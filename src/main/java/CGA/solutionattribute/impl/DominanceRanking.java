package CGA.solutionattribute.impl;

import CGA.Model.Chrome;
import CGA.comparator.DominanceComparator;
import CGA.comparator.impl.OverallConstraintViolationComparator;
import CGA.solutionattribute.GenericSolutionAttribute;
import CGA.solutionattribute.Ranking;
import org.uma.jmetal.util.JMetalException;

import java.util.*;

public class DominanceRanking<S extends Chrome> extends GenericSolutionAttribute<S, Integer> implements Ranking<S> {
    private Comparator<S> dominanceComparator;
    private final Comparator<S> CONSTRAINT_VIOLATION_COMPARATOR = new OverallConstraintViolationComparator();
    private List<ArrayList<S>> rankedSubPopulations;

    public DominanceRanking(Comparator<S> comparator) {
        this.dominanceComparator = comparator;
        this.rankedSubPopulations = new ArrayList();
    }

    public DominanceRanking() {
        this((Comparator)(new DominanceComparator()));
    }

    public DominanceRanking(Object id) {
        super(id);
        this.rankedSubPopulations = new ArrayList();
    }

    public Ranking<S> computeRanking(List<S> solutionSet) {
        List<S> population = solutionSet;
        int[] dominateMe = new int[solutionSet.size()];
        List<List<Integer>> iDominate = new ArrayList(solutionSet.size());
        ArrayList<List<Integer>> front = new ArrayList(solutionSet.size() + 1);

        int flagDominate;
        for(flagDominate = 0; flagDominate < population.size() + 1; ++flagDominate) {
            front.add(new LinkedList());
        }

        for(flagDominate = 0; flagDominate < population.size(); ++flagDominate) {
            iDominate.add(new LinkedList());
            dominateMe[flagDominate] = 0;
        }

        int i;
        int var10002;
        for(i = 0; i < population.size() - 1; ++i) {
            for(int q = i + 1; q < population.size(); ++q) {
                flagDominate = CONSTRAINT_VIOLATION_COMPARATOR.compare(solutionSet.get(i), solutionSet.get(q));
                if (flagDominate == 0) {
                    flagDominate = this.dominanceComparator.compare(solutionSet.get(i), solutionSet.get(q));
                }

                if (flagDominate == -1) {
                    ((List)iDominate.get(i)).add(q);
                    var10002 = dominateMe[q]++;
                } else if (flagDominate == 1) {
                    ((List)iDominate.get(q)).add(i);
                    var10002 = dominateMe[i]++;
                }
            }
        }

        for(i = 0; i < population.size(); ++i) {
            if (dominateMe[i] == 0) {
                ((List)front.get(0)).add(i);
                ((Chrome)solutionSet.get(i)).setRank(0);
            }
        }

        i = 0;

        int index;
        Iterator it1;
        while(((List)front.get(i)).size() != 0) {
            ++i;
            it1 = ((List)front.get(i - 1)).iterator();

            while(it1.hasNext()) {
                Iterator it2 = ((List)iDominate.get((Integer)it1.next())).iterator();

                while(it2.hasNext()) {
                    index = (Integer)it2.next();
                    var10002 = dominateMe[index]--;
                    if (dominateMe[index] == 0) {
                        ((List)front.get(i)).add(index);
                        ((Chrome)solutionSet.get(index)).setRank(i);
                    }
                }
            }
        }

        this.rankedSubPopulations = new ArrayList();

        for(index = 0; index < i; ++index) {
            this.rankedSubPopulations.add(index, new ArrayList(((List)front.get(index)).size()));
            it1 = ((List)front.get(index)).iterator();

            while(it1.hasNext()) {
                ((ArrayList)this.rankedSubPopulations.get(index)).add(solutionSet.get((Integer)it1.next()));
            }
        }

        return this;
    }

    public List<S> getSubfront(int rank) {
        if (rank >= this.rankedSubPopulations.size()) {
            throw new JMetalException("Invalid rank: " + rank + ". Max rank = " + (this.rankedSubPopulations.size() - 1));
        } else {
            return (List)this.rankedSubPopulations.get(rank);
        }
    }

    public int getNumberOfSubfronts() {
        return this.rankedSubPopulations.size();
    }
}
