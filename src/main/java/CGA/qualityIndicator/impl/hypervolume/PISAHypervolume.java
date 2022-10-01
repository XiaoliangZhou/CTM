package CGA.qualityIndicator.impl.hypervolume;

import CGA.Model.Chrome;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontNormalizer;
import CGA.front.util.FrontUtils;
import CGA.point.Point;
import CGA.qualityIndicator.impl.Hypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.HypervolumeContributionComparator;
import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PISAHypervolume<S extends Chrome> extends Hypervolume<S> {
    private static final double DEFAULT_OFFSET = 100.0D;
    private double offset = 100.0D;

    public PISAHypervolume() {
    }

    public PISAHypervolume(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile);
    }

    public PISAHypervolume(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public Double evaluate(List<S> paretoFrontApproximation) {
        if (paretoFrontApproximation == null) {
            throw new JMetalException("The pareto front approximation is null");
        } else {
            return this.hypervolume(new ArrayFront(paretoFrontApproximation), this.referenceParetoFront);
        }
    }

    private boolean dominates(double[] point1, double[] point2, int noObjectives) {
        int betterInAnyObjective = 0;

        int i;
        for(i = 0; i < noObjectives && point1[i] >= point2[i]; ++i) {
            if (point1[i] > point2[i]) {
                betterInAnyObjective = 1;
            }
        }

        return i >= noObjectives && betterInAnyObjective > 0;
    }

    private void swap(double[][] front, int i, int j) {
        double[] temp = front[i];
        front[i] = front[j];
        front[j] = temp;
    }

    private int filterNondominatedSet(double[][] front, int noPoints, int noObjectives) {
        int n = noPoints;

        for(int i = 0; i < n; ++i) {
            int j = i + 1;

            while(j < n) {
                if (this.dominates(front[i], front[j], noObjectives)) {
                    --n;
                    this.swap(front, j, n);
                } else {
                    if (this.dominates(front[j], front[i], noObjectives)) {
                        --n;
                        this.swap(front, i, n);
                        --i;
                        break;
                    }

                    ++j;
                }
            }
        }

        return n;
    }

    private double surfaceUnchangedTo(double[][] front, int noPoints, int objective) {
        if (noPoints < 1) {
            new JMetalException("run-time error");
        }

        double minValue = front[0][objective];

        for(int i = 1; i < noPoints; ++i) {
            double value = front[i][objective];
            if (value < minValue) {
                minValue = value;
            }
        }

        return minValue;
    }

    private int reduceNondominatedSet(double[][] front, int noPoints, int objective, double threshold) {
        int n = noPoints;

        for(int i = 0; i < n; ++i) {
            if (front[i][objective] <= threshold) {
                --n;
                this.swap(front, i, n);
            }
        }

        return n;
    }

    public double calculateHypervolume(double[][] front, int noPoints, int noObjectives) {
        double volume = 0.0D;
        double distance = 0.0D;

        double tempDistance;
        for(int n = noPoints; n > 0; n = this.reduceNondominatedSet(front, n, noObjectives - 1, tempDistance)) {
            int nonDominatedPoints = this.filterNondominatedSet(front, n, noObjectives - 1);
            double tempVolume;
            if (noObjectives < 3) {
                if (nonDominatedPoints < 1) {
                    new JMetalException("run-time error");
                }

                tempVolume = front[0][0];
            } else {
                tempVolume = this.calculateHypervolume(front, nonDominatedPoints, noObjectives - 1);
            }

            tempDistance = this.surfaceUnchangedTo(front, n, noObjectives - 1);
            volume += tempVolume * (tempDistance - distance);
            distance = tempDistance;
        }

        return volume;
    }

    private double hypervolume(Front front, Front referenceFront) {
        Front invertedFront = FrontUtils.getInvertedFront(front);
        int numberOfObjectives = referenceFront.getPoint(0).getDimension();
        return this.calculateHypervolume(FrontUtils.convertFrontToArray(invertedFront), invertedFront.getNumberOfPoints(), numberOfObjectives);
    }

    public String getDescription() {
        return "PISA implementation of the hypervolume quality indicator";
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public List<S> computeHypervolumeContribution(List<S> solutionList, List<S> referenceFrontList) {
        if (solutionList.size() > 1) {
            Front front = new ArrayFront(solutionList);
            Front referenceFront = new ArrayFront(referenceFrontList);
            double[] maximumValues = FrontUtils.getMaximumValues(referenceFront);
            double[] minimumValues = FrontUtils.getMinimumValues(referenceFront);
            FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues);
            Front normalizedFront = frontNormalizer.normalize(front);
            double[] offsets = new double[maximumValues.length];

            for(int i = 0; i < maximumValues.length; ++i) {
                offsets[i] = this.offset / (maximumValues[i] - minimumValues[i]);
            }

            Front invertedFront = FrontUtils.getInvertedFront(normalizedFront);

            int i;
            for(int i1 = 0; i1 < invertedFront.getNumberOfPoints(); ++i1) {
                Point point = invertedFront.getPoint(i1);

                for(i = 0; i < point.getDimension(); ++i) {
                    point.setValue(i, point.getValue(i) + offsets[i]);
                }
            }

            HypervolumeContributionAttribute<S> hvContribution = new HypervolumeContributionAttribute();
            double[] contributions = this.hvContributions(FrontUtils.convertFrontToArray(invertedFront));

            for(i = 0; i < contributions.length; ++i) {
                hvContribution.setAttribute((S) solutionList.get(i), contributions[i]);
            }

            Collections.sort(solutionList, new HypervolumeContributionComparator());
        }

        return solutionList;
    }

    public double getOffset() {
        return this.offset;
    }

    private double[] hvContributions(double[][] front) {
        int numberOfObjectives = front[0].length;
        double[] contributions = new double[front.length];
        double[][] frontSubset = new double[front.length - 1][front[0].length];
        LinkedList<double[]> frontCopy = new LinkedList();
        Collections.addAll(frontCopy, front);
        double[][] totalFront = (double[][])frontCopy.toArray(frontSubset);
        double totalVolume = this.calculateHypervolume(totalFront, totalFront.length, numberOfObjectives);

        for(int i = 0; i < front.length; ++i) {
            double[] evaluatedPoint = (double[])frontCopy.remove(i);
            frontSubset = (double[][])frontCopy.toArray(frontSubset);
            double hv = this.calculateHypervolume(frontSubset, frontSubset.length, numberOfObjectives);
            double contribution = totalVolume - hv;
            contributions[i] = contribution;
            frontCopy.add(i, evaluatedPoint);
        }

        return contributions;
    }
}
