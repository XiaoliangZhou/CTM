package CGA.qualityIndicator.impl;

import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontUtils;
import org.uma.jmetal.solution.Solution;

import java.io.FileNotFoundException;
import java.util.List;

public class InvertedGenerationalDistance<S extends Solution<?>> extends GenericIndicator<S> {
    private double pow;

    public InvertedGenerationalDistance() {
        this.pow = 2.0D;
    }

    public InvertedGenerationalDistance(String referenceParetoFrontFile, double p) throws FileNotFoundException {
        super(referenceParetoFrontFile);
        this.pow = 2.0D;
        this.pow = p;
    }

    public InvertedGenerationalDistance(String referenceParetoFrontFile) throws FileNotFoundException {
        this(referenceParetoFrontFile, 2.0D);
    }

    public InvertedGenerationalDistance(Front referenceParetoFront) {
        super(referenceParetoFront);
        this.pow = 2.0D;
    }

    public Double evaluate(List<S> solutionList) {
        return this.invertedGenerationalDistance(new ArrayFront(solutionList), this.referenceParetoFront);
    }

    public double invertedGenerationalDistance(Front front, Front referenceFront) {
        double sum = 0.0D;

        for(int i = 0; i < referenceFront.getNumberOfPoints(); ++i) {
            sum += Math.pow(FrontUtils.distanceToClosestPoint(referenceFront.getPoint(i), front), this.pow);
        }

        sum = Math.pow(sum, 1.0D / this.pow);
        return sum / (double)referenceFront.getNumberOfPoints();
    }

    public String getName() {
        return "IGD";
    }

    public String getDescription() {
        return "Inverted generational distance quality indicator";
    }

    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return true;
    }
}
