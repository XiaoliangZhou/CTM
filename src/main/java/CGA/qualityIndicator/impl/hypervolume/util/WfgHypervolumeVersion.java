package CGA.qualityIndicator.impl.hypervolume.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import CGA.front.Front;
import CGA.point.Point;
import CGA.point.impl.ArrayPoint;
import CGA.point.util.camparator.PointComparator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

public class WfgHypervolumeVersion {
    static final int OPT = 2;
    WfgHypervolumeFront[] fs;
    private Point referencePoint;
    boolean maximizing;
    private int currentDeep;
    private int currentDimension;
    private int maxNumberOfPoints;
    private Comparator<Point> pointComparator;

    public WfgHypervolumeVersion(int dimension, int maxNumberOfPoints) {
        this(dimension, maxNumberOfPoints, new ArrayPoint(dimension));
    }

    public WfgHypervolumeVersion(int dimension, int maxNumberOfPoints, Point referencePoint) {
        this.referencePoint = new ArrayPoint(referencePoint);
        this.maximizing = false;
        this.currentDeep = 0;
        this.currentDimension = dimension;
        this.maxNumberOfPoints = maxNumberOfPoints;
        this.pointComparator = new PointComparator();
        int maxd = this.maxNumberOfPoints - 2;
        this.fs = new WfgHypervolumeFront[maxd];

        for(int i = 0; i < maxd; ++i) {
            this.fs[i] = new WfgHypervolumeFront(maxNumberOfPoints, dimension);
        }

    }

    public double get2DHV(WfgHypervolumeFront front) {
        double hv = 0.0D;
        hv = Math.abs((front.getPoint(0).getValue(0) - this.referencePoint.getValue(0)) * (front.getPoint(0).getValue(1) - this.referencePoint.getValue(1)));

        for(int i = 1; i < front.getNumberOfPoints(); ++i) {
            hv += Math.abs((front.getPoint(i).getValue(0) - this.referencePoint.getValue(0)) * (front.getPoint(i).getValue(1) - front.getPoint(i - 1).getValue(1)));
        }

        return hv;
    }

    public double getInclusiveHV(Point point) {
        double volume = 1.0D;

        for(int i = 0; i < this.currentDimension; ++i) {
            volume *= Math.abs(point.getValue(i) - this.referencePoint.getValue(i));
        }

        return volume;
    }

    public double getExclusiveHV(WfgHypervolumeFront front, int point) {
        double volume = this.getInclusiveHV(front.getPoint(point));
        if (front.getNumberOfPoints() > point + 1) {
            this.makeDominatedBit(front, point);
            double v = this.getHV(this.fs[this.currentDeep - 1]);
            volume -= v;
            --this.currentDeep;
        }

        return volume;
    }

    public double getHV(WfgHypervolumeFront front) {
        front.sort(this.pointComparator);
        double volume;
        if (this.currentDimension == 2) {
            volume = this.get2DHV(front);
        } else {
            volume = 0.0D;
            --this.currentDimension;
            int numberOfPoints = front.getNumberOfPoints();

            for(int i = numberOfPoints - 1; i >= 0; --i) {
                volume += Math.abs(front.getPoint(i).getValue(this.currentDimension) - this.referencePoint.getValue(this.currentDimension)) * this.getExclusiveHV(front, i);
            }

            ++this.currentDimension;
        }

        return volume;
    }

    public void makeDominatedBit(WfgHypervolumeFront front, int p) {
        int z = front.getNumberOfPoints() - 1 - p;

        int i;
      /*  for(int i = 0; i < z; ++i) {
            for(i = 0; i < this.currentDimension; ++i) {
                Point point1 = front.getPoint(p);
                Point point2 = front.getPoint(p + 1 + i);
                double worseValue = this.worse(point1.getValue(i), point2.getValue(i), false);
                Point point3 = this.fs[this.currentDeep].getPoint(i);
                point3.setValue(i, worseValue);
            }
        }*/

        this.fs[this.currentDeep].setNumberOfPoints(1);

        for(i = 1; i < z; ++i) {
            int j = 0;
            boolean keep = true;

            Point t;
            while(j < this.fs[this.currentDeep].getNumberOfPoints() && keep) {
                switch(this.dominates2way(this.fs[this.currentDeep].getPoint(i), this.fs[this.currentDeep].getPoint(j))) {
                    case -1:
                        t = this.fs[this.currentDeep].getPoint(j);
                        this.fs[this.currentDeep].setNumberOfPoints(this.fs[this.currentDeep].getNumberOfPoints() - 1);
                        this.fs[this.currentDeep].setPoint(j, this.fs[this.currentDeep].getPoint(this.fs[this.currentDeep].getNumberOfPoints()));
                        this.fs[this.currentDeep].setPoint(this.fs[this.currentDeep].getNumberOfPoints(), t);
                        break;
                    case 0:
                        ++j;
                        break;
                    default:
                        keep = false;
                }
            }

            if (keep) {
                t = this.fs[this.currentDeep].getPoint(this.fs[this.currentDeep].getNumberOfPoints());
                this.fs[this.currentDeep].setPoint(this.fs[this.currentDeep].getNumberOfPoints(), this.fs[this.currentDeep].getPoint(i));
                this.fs[this.currentDeep].setPoint(i, t);
                this.fs[this.currentDeep].setNumberOfPoints(this.fs[this.currentDeep].getNumberOfPoints() + 1);
            }
        }

        ++this.currentDeep;
    }

    public int getLessContributorHV(List<Solution<?>> solutionList) {
        WfgHypervolumeFront wholeFront = (WfgHypervolumeFront)this.loadFront(solutionList, -1);
        int index = 0;
        double contribution = 1.0D / 0.0;

        for(int i = 0; i < solutionList.size(); ++i) {
            double[] v = new double[((Solution)solutionList.get(i)).getNumberOfObjectives()];

            for(int j = 0; j < v.length; ++j) {
                v[j] = ((Solution)solutionList.get(i)).getObjective(j);
            }

            double aux = this.getExclusiveHV(wholeFront, i);
            if (aux < contribution) {
                index = i;
                contribution = aux;
            }

            HypervolumeContributionAttribute<Solution<?>> hvc = new HypervolumeContributionAttribute();
            hvc.setAttribute((Solution)solutionList.get(i), aux);
        }

        return index;
    }

    private Front loadFront(List<Solution<?>> solutionSet, int notLoadingIndex) {
        int numberOfPoints;
        if (notLoadingIndex >= 0 && notLoadingIndex < solutionSet.size()) {
            numberOfPoints = solutionSet.size() - 1;
        } else {
            numberOfPoints = solutionSet.size();
        }

        int dimensions = ((Solution)solutionSet.get(0)).getNumberOfObjectives();
        Front front = new WfgHypervolumeFront(numberOfPoints, dimensions);
        int index = 0;

        for(int i = 0; i < solutionSet.size(); ++i) {
            if (i != notLoadingIndex) {
                Point point = new ArrayPoint(dimensions);

                for(int j = 0; j < dimensions; ++j) {
                    point.setValue(j, ((Solution)solutionSet.get(i)).getObjective(j));
                }

                front.setPoint(index++, point);
            }
        }

        return front;
    }

    private double worse(double x, double y, boolean maximizing) {
        double result;
        if (maximizing) {
            if (x > y) {
                result = y;
            } else {
                result = x;
            }
        } else if (x > y) {
            result = x;
        } else {
            result = y;
        }

        return result;
    }

    int dominates2way(Point p, Point q) {
        for(int i = this.currentDimension - 1; i >= 0; --i) {
            int j;
            if (p.getValue(i) < q.getValue(i)) {
                for(j = i - 1; j >= 0; --j) {
                    if (q.getValue(j) < p.getValue(j)) {
                        return 0;
                    }
                }

                return -1;
            }

            if (q.getValue(i) < p.getValue(i)) {
                for(j = i - 1; j >= 0; --j) {
                    if (p.getValue(j) < q.getValue(j)) {
                        return 0;
                    }
                }

                return 1;
            }
        }

        return 2;
    }

    public static void main(String[] args) throws IOException, JMetalException {
        WfgHypervolumeFront front = new WfgHypervolumeFront();
        if (args.length == 0) {
            throw new JMetalException("Usage: WFGHV front [reference point]");
        } else {
            int dimensions = front.getPointDimensions();
            double[] points = new double[dimensions];
            int i;
            if (args.length == dimensions + 1) {
                for(i = 1; i <= dimensions; ++i) {
                    points[i - 1] = Double.parseDouble(args[i]);
                }
            } else {
                for(i = 1; i <= dimensions; ++i) {
                    points[i - 1] = 0.0D;
                }
            }

            Point referencePoint = new ArrayPoint(points);
            JMetalLogger.logger.info("Using reference point: " + referencePoint);
            WfgHypervolumeVersion wfghv = new WfgHypervolumeVersion(referencePoint.getDimension(), front.getNumberOfPoints(), referencePoint);
            System.out.println("HV: " + wfghv.getHV(front));
        }
    }
}
