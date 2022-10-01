package CGA.point.util.distance;

import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

public class DominanceDistance implements PointDistance {
    public DominanceDistance() {
    }

    public double compute(Point a, Point b) {
        if (a == null) {
            throw new JMetalException("The first point is null");
        } else if (b == null) {
            throw new JMetalException("The second point is null");
        } else if (a.getDimension() != b.getDimension()) {
            throw new JMetalException("The dimensions of the points are different: " + a.getDimension() + ", " + b.getDimension());
        } else {
            double distance = 0.0D;

            for(int i = 0; i < a.getDimension(); ++i) {
                double max = Math.max(b.getValue(i) - a.getValue(i), 0.0D);
                distance += Math.pow(max, 2.0D);
            }

            return Math.sqrt(distance);
        }
    }
}
