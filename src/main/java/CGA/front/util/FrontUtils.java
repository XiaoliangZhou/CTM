package CGA.front.util;

import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.point.Point;
import CGA.point.PointSolution;
import CGA.point.util.distance.EuclideanDistance;
import CGA.point.util.distance.PointDistance;
import org.uma.jmetal.util.JMetalException;


import java.util.ArrayList;
import java.util.List;

public class FrontUtils {
    public FrontUtils() {
    }

    public static double[] getMaximumValues(Front front) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else if (front.getNumberOfPoints() == 0) {
            throw new FrontUtils.EmptyFrontException();
        } else {
            int numberOfObjectives = front.getPoint(0).getDimension();
            double[] maximumValue = new double[numberOfObjectives];

            int i;
            for(i = 0; i < numberOfObjectives; ++i) {
                maximumValue[i] = -1.0D / 0.0;
            }

            for(i = 0; i < front.getNumberOfPoints(); ++i) {
                for(int j = 0; j < numberOfObjectives; ++j) {
                    if (front.getPoint(i).getValue(j) > maximumValue[j]) {
                        maximumValue[j] = front.getPoint(i).getValue(j);
                    }
                }
            }

            return maximumValue;
        }
    }

    public static double[] getMinimumValues(Front front) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else if (front.getNumberOfPoints() == 0) {
            throw new FrontUtils.EmptyFrontException();
        } else {
            int numberOfObjectives = front.getPoint(0).getDimension();
            double[] minimumValue = new double[numberOfObjectives];

            int i;
            for(i = 0; i < numberOfObjectives; ++i) {
                minimumValue[i] = 1.7976931348623157E308D;
            }

            for(i = 0; i < front.getNumberOfPoints(); ++i) {
                for(int j = 0; j < numberOfObjectives; ++j) {
                    if (front.getPoint(i).getValue(j) < minimumValue[j]) {
                        minimumValue[j] = front.getPoint(i).getValue(j);
                    }
                }
            }

            return minimumValue;
        }
    }

    public static double distanceToNearestPoint(Point point, Front front) {
        return distanceToNearestPoint(point, front, new EuclideanDistance());
    }

    public static double distanceToNearestPoint(Point point, Front front, PointDistance distance) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else if (front.getNumberOfPoints() == 0) {
            throw new FrontUtils.EmptyFrontException();
        } else if (point == null) {
            throw new JMetalException("The point is null");
        } else {
            double minDistance = 1.7976931348623157E308D;

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                double aux = distance.compute(point, front.getPoint(i));
                if (aux < minDistance && aux > 0.0D) {
                    minDistance = aux;
                }
            }

            return minDistance;
        }
    }

    public static double distanceToClosestPoint(Point point, Front front) {
        return distanceToClosestPoint(point, front, new EuclideanDistance());
    }

    public static double distanceToClosestPoint(Point point, Front front, PointDistance distance) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else if (front.getNumberOfPoints() == 0) {
            throw new FrontUtils.EmptyFrontException();
        } else if (point == null) {
            throw new JMetalException("The point is null");
        } else {
            double minDistance = distance.compute(point, front.getPoint(0));

            for(int i = 1; i < front.getNumberOfPoints(); ++i) {
                double aux = distance.compute(point, front.getPoint(i));
                if (aux < minDistance) {
                    minDistance = aux;
                }
            }

            return minDistance;
        }
    }

    public static Front getInvertedFront(Front front) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else if (front.getNumberOfPoints() == 0) {
            throw new FrontUtils.EmptyFrontException();
        } else {
            int numberOfDimensions = front.getPoint(0).getDimension();
            Front invertedFront = new ArrayFront(front.getNumberOfPoints(), numberOfDimensions);

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                for(int j = 0; j < numberOfDimensions; ++j) {
                    if (front.getPoint(i).getValue(j) <= 1.0D && front.getPoint(i).getValue(j) >= 0.0D) {
                        invertedFront.getPoint(i).setValue(j, 1.0D - front.getPoint(i).getValue(j));
                    } else if (front.getPoint(i).getValue(j) > 1.0D) {
                        invertedFront.getPoint(i).setValue(j, 0.0D);
                    } else if (front.getPoint(i).getValue(j) < 0.0D) {
                        invertedFront.getPoint(i).setValue(j, 1.0D);
                    }
                }
            }

            return invertedFront;
        }
    }

    public static double[][] convertFrontToArray(Front front) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else {
            double[][] arrayFront = new double[front.getNumberOfPoints()][];

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                arrayFront[i] = new double[front.getPoint(i).getDimension()];

                for(int j = 0; j < front.getPoint(i).getDimension(); ++j) {
                    arrayFront[i][j] = front.getPoint(i).getValue(j);
                }
            }

            return arrayFront;
        }
    }

    public static List<PointSolution> convertFrontToSolutionList(Front front) {
        if (front == null) {
            throw new FrontUtils.NullFrontException();
        } else {
            int solutionSetSize = front.getNumberOfPoints();
            int numberOfObjectives;
            if (front.getNumberOfPoints() == 0) {
                numberOfObjectives = 0;
            } else {
                numberOfObjectives = front.getPoint(0).getDimension();
            }

            List<PointSolution> solutionSet = new ArrayList(solutionSetSize);

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                PointSolution solution = new PointSolution(numberOfObjectives);

                for(int j = 0; j < numberOfObjectives; ++j) {
                    solution.setObjective(j, front.getPoint(i).getValue(j));
                }

                solutionSet.add(solution);
            }

            return solutionSet;
        }
    }

    private static class EmptyFrontException extends JMetalException {
        public EmptyFrontException() {
            super("The front is empty");
        }
    }

    private static class NullFrontException extends JMetalException {
        public NullFrontException() {
            super("The front is null");
        }
    }
}
