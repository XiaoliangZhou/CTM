package CGA.front.util;

import CGA.Model.Chrome;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

public class FrontNormalizer {
    private double[] maximumValues;
    private double[] minimumValues;

    public FrontNormalizer(List<? extends Chrome> referenceFront) {
        if (referenceFront == null) {
            throw new JMetalException("The reference front is null");
        } else {
            this.maximumValues = FrontUtils.getMaximumValues(new ArrayFront(referenceFront));
            this.minimumValues = FrontUtils.getMinimumValues(new ArrayFront(referenceFront));
        }
    }

    public FrontNormalizer(Front referenceFront) {
        if (referenceFront == null) {
            throw new JMetalException("The reference front is null");
        } else {
            this.maximumValues = FrontUtils.getMaximumValues(referenceFront);
            this.minimumValues = FrontUtils.getMinimumValues(referenceFront);
        }
    }

    public FrontNormalizer(double[] minimumValues, double[] maximumValues) {
        if (minimumValues == null) {
            throw new JMetalException("The array of minimum values is null");
        } else if (maximumValues == null) {
            throw new JMetalException("The array of maximum values is null");
        } else if (maximumValues.length != minimumValues.length) {
            throw new JMetalException("The length of the maximum array (" + maximumValues.length + ") is different from the length of the minimum array (" + minimumValues.length + ")");
        } else {
            this.maximumValues = maximumValues;
            this.minimumValues = minimumValues;
        }
    }

    public List<? extends Solution<?>> normalize(List<? extends Solution<?>> solutionList) {
        if (solutionList == null) {
            throw new JMetalException("The front is null");
        } else {
            Front normalizedFront = this.getNormalizedFront(new ArrayFront((Front) solutionList), this.maximumValues, this.minimumValues);
            return FrontUtils.convertFrontToSolutionList(normalizedFront);
        }
    }

    public Front normalize(Front front) {
        if (front == null) {
            throw new JMetalException("The front is null");
        } else {
            return this.getNormalizedFront(front, this.maximumValues, this.minimumValues);
        }
    }

    private Front getNormalizedFront(Front front, double[] maximumValues, double[] minimumValues) {
        if (front.getNumberOfPoints() == 0) {
            throw new JMetalException("The front is empty");
        } else if (front.getPoint(0).getDimension() != maximumValues.length) {
            throw new JMetalException("The length of the point dimensions (" + front.getPoint(0).getDimension() + ") is different from the length of the maximum array (" + maximumValues.length + ")");
        } else {
            Front normalizedFront = new ArrayFront(front);
            int numberOfPointDimensions = front.getPoint(0).getDimension();

            for(int i = 0; i < front.getNumberOfPoints(); ++i) {
                for(int j = 0; j < numberOfPointDimensions; ++j) {
                    if (maximumValues[j] - minimumValues[j] == 0.0D) {
                        throw new JMetalException("Maximum and minimum values of index " + j + " are the same: " + maximumValues[j]);
                    }

                    normalizedFront.getPoint(i).setValue(j, (front.getPoint(i).getValue(j) - minimumValues[j]) / (maximumValues[j] - minimumValues[j]));
                }
            }

            return normalizedFront;
        }
    }
}
