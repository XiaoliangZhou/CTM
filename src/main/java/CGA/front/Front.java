package CGA.front;


import CGA.point.Point;

import java.io.Serializable;
import java.util.Comparator;

public interface Front extends Serializable {
    int getNumberOfPoints();

    int getPointDimensions();

    Point getPoint(int var1);

    void setPoint(int var1, Point var2);

    void sort(Comparator<Point> var1);
}
