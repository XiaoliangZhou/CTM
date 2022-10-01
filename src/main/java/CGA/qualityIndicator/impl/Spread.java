package CGA.qualityIndicator.impl;

import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.point.util.camparator.LexicographicalPointComparator;
import CGA.point.util.distance.EuclideanDistance;
import CGA.point.util.distance.PointDistance;
import org.uma.jmetal.solution.Solution;

import java.io.FileNotFoundException;
import java.util.List;

public class Spread<S extends Solution<?>> extends GenericIndicator<S> {
    public Spread() {
    }

    public Spread(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile);
    }

    public Spread(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public Double evaluate(List<S> solutionList) {
        return this.spread(new ArrayFront(solutionList), this.referenceParetoFront);
    }

    public double spread(Front front, Front referenceFront) {
        PointDistance distance = new EuclideanDistance();
        front.sort(new LexicographicalPointComparator());
        referenceFront.sort(new LexicographicalPointComparator());
        double df = distance.compute(front.getPoint(0), referenceFront.getPoint(0));
        double dl = distance.compute(front.getPoint(front.getNumberOfPoints() - 1), referenceFront.getPoint(referenceFront.getNumberOfPoints() - 1));
        double mean = 0.0D;
        double diversitySum = df + dl;
        int numberOfPoints = front.getNumberOfPoints();

        int i;
        for(i = 0; i < numberOfPoints - 1; ++i) {
            mean += distance.compute(front.getPoint(i), front.getPoint(i + 1));
        }

        mean /= (double)(numberOfPoints - 1);
        if (numberOfPoints <= 1) {
            return 1.0D;
        } else {
            for(i = 0; i < numberOfPoints - 1; ++i) {
                diversitySum += Math.abs(distance.compute(front.getPoint(i), front.getPoint(i + 1)) - mean);
            }

            return diversitySum / (df + dl + (double)(numberOfPoints - 1) * mean);
        }
    }

    public String getName() {
        return "SPREAD";
    }

    public String getDescription() {
        return "Spread quality indicator";
    }

    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return true;
    }
}
