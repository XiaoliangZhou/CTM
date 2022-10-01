package CGA.point.util.camparator;

import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

import java.util.Comparator;

public class LexicographicalPointComparator implements Comparator<Point> {
    public LexicographicalPointComparator() {
    }

    public int compare(Point pointOne, Point pointTwo) {
        if (pointOne == null) {
            throw new JMetalException("PointOne is null");
        } else if (pointTwo == null) {
            throw new JMetalException("PointTwo is null");
        } else {
            int index;
            for(index = 0; index < pointOne.getDimension() && index < pointTwo.getDimension() && pointOne.getValue(index) == pointTwo.getValue(index); ++index) {
            }

            int result = 0;
            if (index < pointOne.getDimension() && index < pointTwo.getDimension()) {
                if (pointOne.getValue(index) < pointTwo.getValue(index)) {
                    result = -1;
                } else if (pointOne.getValue(index) > pointTwo.getValue(index)) {
                    result = 1;
                }
            } else {
                result = 0;
            }

            return result;
        }
    }
}
