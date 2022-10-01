package CGA.qualityIndicator.impl;

import CGA.point.Point;
import CGA.point.impl.ArrayPoint;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontUtils;
import CGA.point.util.camparator.LexicographicalPointComparator;
import CGA.point.util.camparator.PointDimensionComparator;
import CGA.point.util.distance.EuclideanDistance;
import org.uma.jmetal.solution.Solution;


import java.io.FileNotFoundException;
import java.util.List;

public class GeneralizedSpread<S extends Solution<?>> extends GenericIndicator<S> {
    public GeneralizedSpread() {
    }

    public GeneralizedSpread(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile);
    }

    public GeneralizedSpread(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public Double evaluate(List<S> solutionList) {
        return this.generalizedSpread(new ArrayFront(solutionList), this.referenceParetoFront);
    }

    public double generalizedSpread(Front front, Front referenceFront) {
        int numberOfObjectives = front.getPoint(0).getDimension();
        Point[] extremeValues = new Point[numberOfObjectives];

        int numberOfPoints;
        for(numberOfPoints = 0; numberOfPoints < numberOfObjectives; ++numberOfPoints) {
            referenceFront.sort(new PointDimensionComparator(numberOfPoints));
            Point newPoint = new ArrayPoint(numberOfObjectives);

            for(int j = 0; j < numberOfObjectives; ++j) {
                newPoint.setValue(j, referenceFront.getPoint(referenceFront.getNumberOfPoints() - 1).getValue(j));
            }

            extremeValues[numberOfPoints] = newPoint;
        }

        numberOfPoints = front.getNumberOfPoints();
        front.sort(new LexicographicalPointComparator());
        if ((new EuclideanDistance()).compute(front.getPoint(0), front.getPoint(front.getNumberOfPoints() - 1)) == 0.0D) {
            return 1.0D;
        } else {
            double dmean = 0.0D;

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                dmean += FrontUtils.distanceToNearestPoint(front.getPoint(i), front);
            }

            dmean /= (double)numberOfPoints;
            double dExtrems = 0.0D;

            for(int i = 0; i < extremeValues.length; ++i) {
                dExtrems += FrontUtils.distanceToClosestPoint(extremeValues[i], front);
            }

            double mean = 0.0D;

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                mean += Math.abs(FrontUtils.distanceToNearestPoint(front.getPoint(i), front) - dmean);
            }

            return (dExtrems + mean) / (dExtrems + (double)numberOfPoints * dmean);
        }
    }

    public String getName() {
        return "GSPREAD";
    }

    public String getDescription() {
        return "Generalized Spread quality indicator";
    }

    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return true;
    }
}
