package CGA.front.impl;

import CGA.Model.Chrome;
import CGA.front.Front;
import CGA.point.Point;
import CGA.point.impl.ArrayPoint;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileinput.VectorFileUtils;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class ArrayFront implements Front {
    protected Point[] points;
    protected int numberOfPoints;
    private int pointDimensions;

    public ArrayFront() {
        this.points = null;
        this.numberOfPoints = 0;
        this.pointDimensions = 0;
    }

    public ArrayFront(List<? extends Solution<?>> solutionList) {
        if (solutionList == null) {
            throw new JMetalException("The list of solutions is null");
        } else if (solutionList.size() == 0) {
            throw new JMetalException("The list of solutions is empty");
        } else {
            this.numberOfPoints = solutionList.size();
            this.pointDimensions = ((Solution)solutionList.get(0)).getNumberOfObjectives();
            this.points = new Point[this.numberOfPoints];
            this.points = new Point[this.numberOfPoints];

            for(int i = 0; i < this.numberOfPoints; ++i) {
                Point point = new ArrayPoint(this.pointDimensions);

                for(int j = 0; j < this.pointDimensions; ++j) {
                    point.setValue(j, ((Solution)solutionList.get(i)).getObjective(j));
                }

                this.points[i] = point;
            }

        }
    }

    public ArrayFront(Front front) {
        if (front == null) {
            throw new JMetalException("The front is null");
        } else if (front.getNumberOfPoints() == 0) {
            throw new JMetalException("The front is empty");
        } else {
            this.numberOfPoints = front.getNumberOfPoints();
            this.pointDimensions = front.getPoint(0).getDimension();
            this.points = new Point[this.numberOfPoints];
            this.points = new Point[this.numberOfPoints];

            for(int i = 0; i < this.numberOfPoints; ++i) {
                this.points[i] = new ArrayPoint(front.getPoint(i));
            }

        }
    }

    public ArrayFront(int numberOfPoints, int dimensions) {
        this.numberOfPoints = numberOfPoints;
        this.pointDimensions = dimensions;
        this.points = new Point[this.numberOfPoints];

        for(int i = 0; i < this.numberOfPoints; ++i) {
            Point point = new ArrayPoint(this.pointDimensions);

            for(int j = 0; j < this.pointDimensions; ++j) {
                point.setValue(j, 0.0D);
            }

            this.points[i] = point;
        }

    }

    public ArrayFront(String fileName) throws FileNotFoundException {
        this();
        InputStream inputStream = null;

        try {
            URL url = VectorFileUtils.class.getClassLoader().getResource(fileName);
            if (url != null) {
                String uri = Paths.get(url.toURI()).toString();
                inputStream = this.createInputStream(uri);
            } else {
                inputStream = this.createInputStream(fileName);
            }
        } catch (URISyntaxException var13) {
            var13.printStackTrace();
        }

        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        List<Point> list = new ArrayList();
        int numberOfObjectives = 0;

        try {
            for(String aux = br.readLine(); aux != null; aux = br.readLine()) {
                StringTokenizer tokenizer = new StringTokenizer(aux);
                int i = 0;
                if (numberOfObjectives == 0) {
                    numberOfObjectives = tokenizer.countTokens();
                } else if (numberOfObjectives != tokenizer.countTokens()) {
                    throw new JMetalException("Invalid number of points read. Expected: " + numberOfObjectives + ", received: " + tokenizer.countTokens());
                }

                ArrayPoint point;
                for(point = new ArrayPoint(numberOfObjectives); tokenizer.hasMoreTokens(); ++i) {
                    double value = new Double(tokenizer.nextToken());
                    point.setValue(i, value);
                }

                list.add(point);
            }

            br.close();
        } catch (IOException var14) {
            throw new JMetalException("Error reading file", var14);
        } catch (NumberFormatException var15) {
            throw new JMetalException("Format number exception when reading file", var15);
        }

        this.numberOfPoints = list.size();
        this.points = new Point[list.size()];
        this.points = (Point[])list.toArray(this.points);
        if (this.numberOfPoints == 0) {
            this.pointDimensions = 0;
        } else {
            this.pointDimensions = this.points[0].getDimension();
        }

        for(int i = 0; i < this.numberOfPoints; ++i) {
            this.points[i] = (Point)list.get(i);
        }

    }

    public InputStream createInputStream(String fileName) throws FileNotFoundException {
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            inputStream = new FileInputStream(fileName);
        }

        return (InputStream)inputStream;
    }

    public int getNumberOfPoints() {
        return this.numberOfPoints;
    }

    public int getPointDimensions() {
        return this.pointDimensions;
    }

    public Point getPoint(int index) {
        if (index < 0) {
            throw new JMetalException("The index value is negative");
        } else if (index >= this.numberOfPoints) {
            throw new JMetalException("The index value (" + index + ") is greater than the number of points (" + this.numberOfPoints + ")");
        } else {
            return this.points[index];
        }
    }

    public void setPoint(int index, Point point) {
        if (index < 0) {
            throw new JMetalException("The index value is negative");
        } else if (index >= this.numberOfPoints) {
            throw new JMetalException("The index value (" + index + ") is greater than the number of points (" + this.numberOfPoints + ")");
        } else if (point == null) {
            throw new JMetalException("The point is null");
        } else {
            this.points[index] = point;
        }
    }

    public void sort(Comparator<Point> comparator) {
        Arrays.sort(this.points, 0, this.numberOfPoints, comparator);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ArrayFront that = (ArrayFront)o;
            if (this.numberOfPoints != that.numberOfPoints) {
                return false;
            } else if (this.pointDimensions != that.pointDimensions) {
                return false;
            } else {
                return Arrays.equals(this.points, that.points);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.points);
        result = 31 * result + this.numberOfPoints;
        result = 31 * result + this.pointDimensions;
        return result;
    }

    public String toString() {
        return Arrays.toString(this.points);
    }
}
