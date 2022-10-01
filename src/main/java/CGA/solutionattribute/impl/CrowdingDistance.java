package CGA.solutionattribute.impl;

import CGA.Model.Chrome;
import CGA.comparator.ObjectiveComparator;
import CGA.solutionattribute.DensityEstimator;
import CGA.solutionattribute.GenericSolutionAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrowdingDistance<S extends Chrome> extends GenericSolutionAttribute<S, Double> implements DensityEstimator<S> {
    public CrowdingDistance() {
    }
    /**
     * @计算拥挤度
     * @param solutionList
     */
    public void computeDensityEstimator(List<S> solutionList) {
        int size = solutionList.size();
        if (size != 0) {
            if (size == 1) {
                ((Chrome)solutionList.get(0)).setNd(1.0D / 0.0);
            } else if (size == 2) {
                ((Chrome)solutionList.get(0)).setNd(1.0D / 0.0);
                ((Chrome)solutionList.get(1)).setNd(1.0D / 0.0);
            } else {
                List<Chrome> front = new ArrayList(size);
                Iterator var4 = solutionList.iterator();

                while(var4.hasNext()) {
                    Chrome solution = (Chrome)var4.next();
                    front.add(solution);
                }

                for(int i = 0; i < size; ++i) {
                    ((Chrome)front.get(i)).setNd(0.0D);
                }

                int numberOfObjectives = ((Chrome)solutionList.get(0)).getNumberOfObjectives();

                for(int i = 0; i < numberOfObjectives; ++i) {
                    Collections.sort(front, new ObjectiveComparator(i));
                    double objetiveMinn = ((Chrome)front.get(0)).getObjective(i);
                    double objetiveMaxn = ((Chrome)front.get(front.size() - 1)).getObjective(i);
                    ((Chrome)front.get(0)).setNd(1.0D / 0.0);
                    ((Chrome)front.get(size - 1)).setNd(1.0D / 0.0);

                    for(int j = 1; j < size - 1; ++j) {
                        double distance = ((Chrome)front.get(j + 1)).getObjective(i) - ((Chrome)front.get(j - 1)).getObjective(i);
                        distance /= objetiveMaxn - objetiveMinn;
                        distance += (Double)((Chrome)front.get(j)).getNd();
                        ((Chrome)front.get(j)).setNd(distance);
                    }
                }

            }
        }
    }

    public Object getAttributeIdentifier() {
        return this.getClass();
    }
}
