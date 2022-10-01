package CGA.point.util.camparator;

import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

import java.util.Comparator;

public class PointComparator implements Comparator<Point> {
    private boolean maximizing = true;

    public PointComparator() {
    }

    public void setMaximizing() {
        this.maximizing = true;
    }

    public void setMinimizing() {
        this.maximizing = false;
    }

    public int compare(Point pointOne, Point pointTwo) {
        if (pointOne == null) {
            throw new JMetalException("PointOne is null");
        } else if (pointTwo == null) {
            throw new JMetalException("PointTwo is null");
        } else if (pointOne.getDimension() != pointTwo.getDimension()) {
            throw new JMetalException("Points have different size: " + pointOne.getDimension() + " and " + pointTwo.getDimension());
        } else {
            for(int i = pointOne.getDimension() - 1; i >= 0; --i) {
                if (this.isBetter(pointOne.getValue(i), pointTwo.getValue(i))) {
                    return -1;
                }

                if (this.isBetter(pointTwo.getValue(i), pointOne.getValue(i))) {
                    return 1;
                }
            }

            return 0;
        }
    }

    private boolean isBetter(double v1, double v2) {
        if (this.maximizing) {
            return v1 > v2;
        } else {
            return v2 > v1;
        }
    }
}
