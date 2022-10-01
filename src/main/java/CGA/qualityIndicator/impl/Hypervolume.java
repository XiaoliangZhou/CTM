package CGA.qualityIndicator.impl;

import CGA.front.Front;


import java.io.FileNotFoundException;
import java.util.List;

public abstract class Hypervolume<S> extends GenericIndicator<S> {
    public Hypervolume() {
    }

    public Hypervolume(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile);
    }

    public Hypervolume(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public abstract List<S> computeHypervolumeContribution(List<S> var1, List<S> var2);

    public abstract double getOffset();

    public abstract void setOffset(double var1);

    public String getName() {
        return "HV";
    }

    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return false;
    }
}
