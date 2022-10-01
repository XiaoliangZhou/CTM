package CGA.qualityIndicator.impl;

import CGA.Model.Chrome;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontUtils;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

import java.io.FileNotFoundException;
import java.util.List;

public class GenerationalDistance<S extends Chrome> extends GenericIndicator<S> {
    private double pow;

    public GenerationalDistance() {
        this.pow = 2.0D;
    }

    public GenerationalDistance(String referenceParetoFrontFile, double p) throws FileNotFoundException {
        super(referenceParetoFrontFile);
        this.pow = 2.0D;
        this.pow = p;
    }

    public GenerationalDistance(String referenceParetoFrontFile) throws FileNotFoundException {
        this(referenceParetoFrontFile, 2.0D);
    }

    public GenerationalDistance(Front referenceParetoFront) {
        super(referenceParetoFront);
        this.pow = 2.0D;
    }

    public Double evaluate(List<S> solutionList) {
        if (solutionList == null) {
            throw new JMetalException("The pareto front approximation is null");
        } else {
            return this.generationalDistance(new ArrayFront(solutionList), this.referenceParetoFront);
        }
    }

    public double generationalDistance(Front front, Front referenceFront) {
        double sum = 0.0D;

        for(int i = 0; i < front.getNumberOfPoints(); ++i) {
            sum += Math.pow(FrontUtils.distanceToClosestPoint(front.getPoint(i), referenceFront), this.pow);
        }

        sum = Math.pow(sum, 1.0D / this.pow);
        return sum / (double)front.getNumberOfPoints();
    }

    public String getName() {
        return "GD";
    }

    public String getDescription() {
        return "Generational distance quality indicator";
    }

    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return true;
    }
}
