package CGA.archive.impl;


import CGA.Model.Chrome;
import CGA.archive.BoundedArchive;
import CGA.comparator.CrowdingDistanceComparator;
import CGA.comparator.ObjectiveComparator;

import java.util.*;


public abstract class AbstractBoundedArchive<S> implements BoundedArchive<S> {

    private Comparator<Chrome> crowdingDistanceComparator = new CrowdingDistanceComparator();
    protected List<Chrome> archive;
    protected int maxSize;

    public boolean add(Chrome solution) throws CloneNotSupportedException{
        boolean success = addArchive(solution);
        if (success) {
            this.prune();
        }
        return success;
    }

    public void prune() {
        if (this.archive.size() > 100) {
            this.computeDensityEstimator(this.archive);
            Chrome worst = findWorstSolution(this.archive,this.crowdingDistanceComparator);
            this.archive.remove(worst);
        }

    }
    public  Chrome findWorstSolution(Collection<Chrome> solutionList, Comparator<Chrome> comparator) {
        if (solutionList != null && !solutionList.isEmpty()) {
            Chrome worstKnown = solutionList.iterator().next();
            Iterator var4 = solutionList.iterator();

            while(var4.hasNext()) {
                Chrome candidateSolution = (Chrome) var4.next();
                if (comparator.compare(worstKnown, candidateSolution) < 0) {
                    worstKnown = candidateSolution;
                }
            }
            return worstKnown;
        } else {
            throw new IllegalArgumentException("No solution provided: " + solutionList);
        }
    }
    public boolean addArchive(Chrome solution) {
        boolean solutionInserted = false;
        if (this.archive.size() == 0) {
            this.archive.add(solution);
            solutionInserted = true;
            return solutionInserted;
        } else {
            Iterator<Chrome> iterator = this.archive.iterator();
            boolean isDominated = false;
            boolean isContained = false;

            while(!isDominated && !isContained && iterator.hasNext()) {
                Chrome listIndividual = (Chrome)iterator.next();
                int flag = this.dominanceTest(solution, listIndividual);
                if (flag == -1) {
                    iterator.remove();
                } else if (flag == 1) {
                    isDominated = true;
                } else if (flag == 0) {
                    int equalflag = this.equalcompare(solution, listIndividual);
                    if (equalflag == 0) {
                        isContained = true;
                    }
                }
            }

            if (!isDominated && !isContained) {
                this.archive.add(solution);
                solutionInserted = true;
            }

            return solutionInserted;
        }
    }

    public int equalcompare(Chrome solution1, Chrome solution2) {
        if (solution1 == null) {
            return 1;
        } else if (solution2 == null) {
            return -1;
        } else {
            boolean dominate1 = false;
            boolean dominate2 = false;

            for(int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
                double value1 = solution1.getObjective(i);
                double value2 = solution2.getObjective(i);
                byte flag;
                if (value1 < value2) {
                    flag = -1;
                } else if (value1 > value2) {
                    flag = 1;
                } else {
                    flag = 0;
                }

                if (flag == -1) {
                    dominate1 = true;
                }

                if (flag == 1) {
                    dominate2 = true;
                }
            }

            if (!dominate1 && !dominate2) {
                return 0;
            } else if (dominate1) {
                return -1;
            } else if (dominate2) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    /**
     * @计算拥挤度
     * @param solutionList
     */
    public void computeDensityEstimator(List<Chrome> solutionList) {
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
    private int dominanceTest(Chrome solution1, Chrome solution2) {
        int bestIsOne = 0;
        int bestIsTwo = 0;


        for(int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
            double value1 = solution1.getObjective(i);
            double value2 = solution2.getObjective(i);
            if (value1 != value2) {
                if (value1 < value2) {
                    bestIsOne = 1;
                }

                if (value2 < value1) {
                    bestIsTwo = 1;
                }
            }
        }

        byte result;
        if (bestIsOne > bestIsTwo) {
            result = -1;
        } else if (bestIsTwo > bestIsOne) {
            result = 1;
        } else {
            result = 0;
        }

        return result;
    }

}
