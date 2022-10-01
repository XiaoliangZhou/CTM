package CGA.problem.impl;

import CGA.Model.Chrome;

import java.util.List;

public abstract class AbstractDoubleProblem<S> extends AbstractGenericProblem<DoubleSolution> implements DoubleProblem {
    private List<Double> lowerLimit;
    private List<Double> upperLimit;

    public AbstractDoubleProblem() {
    }

    public Double getUpperBound(int index) {
        return (Double)this.upperLimit.get(index);
    }

    public Double getLowerBound(int index) {
        return (Double)this.lowerLimit.get(index);
    }

    protected void setLowerLimit(List<Double> lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    protected void setUpperLimit(List<Double> upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Chrome createSolution(int axis,int yaxis,int haxis) {
        return new Chrome(this,axis,yaxis,haxis);
    }
}