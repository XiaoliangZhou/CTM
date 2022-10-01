package CGA.qualityIndicator.impl;

import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.naming.impl.SimpleDescribedEntity;
import CGA.qualityIndicator.QualityIndicator;
import org.uma.jmetal.util.JMetalException;


import java.io.FileNotFoundException;
import java.util.List;

public abstract class GenericIndicator<S> extends SimpleDescribedEntity implements QualityIndicator<List<S>, Double> {
    protected Front referenceParetoFront = null;

    public GenericIndicator() {
    }

    public GenericIndicator(String referenceParetoFrontFile) throws FileNotFoundException {
        this.setReferenceParetoFront(referenceParetoFrontFile);
    }

    public GenericIndicator(Front referenceParetoFront) {
        if (referenceParetoFront == null) {
            throw new GenericIndicator.NullParetoFrontException();
        } else {
            this.referenceParetoFront = referenceParetoFront;
        }
    }

    public void setReferenceParetoFront(String referenceParetoFrontFile) throws FileNotFoundException {
        if (referenceParetoFrontFile == null) {
            throw new GenericIndicator.NullParetoFrontException();
        } else {
            Front front = new ArrayFront(referenceParetoFrontFile);
            this.referenceParetoFront = front;
        }
    }

    public void setReferenceParetoFront(Front referenceFront) throws FileNotFoundException {
        if (referenceFront == null) {
            throw new GenericIndicator.NullParetoFrontException();
        } else {
            this.referenceParetoFront = referenceFront;
        }
    }

    public abstract boolean isTheLowerTheIndicatorValueTheBetter();

    private static class NullParetoFrontException extends JMetalException {
        public NullParetoFrontException() {
            super("The reference pareto front is null");
        }
    }
}
