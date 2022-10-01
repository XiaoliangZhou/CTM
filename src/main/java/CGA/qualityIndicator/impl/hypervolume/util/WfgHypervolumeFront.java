package CGA.qualityIndicator.impl.hypervolume.util;

import CGA.Model.Chrome;
import CGA.front.impl.ArrayFront;
import CGA.point.Point;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

public class WfgHypervolumeFront extends ArrayFront {
    public WfgHypervolumeFront() {
    }

    public WfgHypervolumeFront(List<? extends Chrome> solutionList) {
        super(solutionList);
    }

    public WfgHypervolumeFront(int numberOfPoints, int dimensions) {
        super(numberOfPoints, dimensions);
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public int getNumberOfPoints() {
        return this.numberOfPoints;
    }

    public Point getPoint(int index) {
        if (index < 0) {
            throw new JMetalException("The index value is negative");
        } else {
            return this.points[index];
        }
    }

    public void setPoint(int index, Point point) {
        if (index < 0) {
            throw new JMetalException("The index value is negative");
        } else if (point == null) {
            throw new JMetalException("The point is null");
        } else {
            this.points[index] = point;
        }
    }
}
