package CGA.point.util.camparator;

import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

import java.util.Comparator;

public class PointDimensionComparator implements Comparator<Point> {
    private int index;

    public PointDimensionComparator(int index) {
        if (index < 0) {
            throw new JMetalException("The index value is negative");
        } else {
            this.index = index;
        }
    }

    public int compare(Point pointOne, Point pointTwo) {
        if (pointOne == null) {
            throw new JMetalException("PointOne is null");
        } else if (pointTwo == null) {
            throw new JMetalException("PointTwo is null");
        } else if (this.index >= pointOne.getDimension()) {
            throw new JMetalException("The index value " + this.index + " is out of range (0,  " + (pointOne.getDimension() - 1) + ")");
        } else if (this.index >= pointTwo.getDimension()) {
            throw new JMetalException("The index value " + this.index + " is out of range (0,  " + (pointTwo.getDimension() - 1) + ")");
        } else if (pointOne.getValue(this.index) < pointTwo.getValue(this.index)) {
            return -1;
        } else {
            return pointOne.getValue(this.index) > pointTwo.getValue(this.index) ? 1 : 0;
        }
    }
}
