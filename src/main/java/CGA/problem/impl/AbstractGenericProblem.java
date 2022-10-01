package CGA.problem.impl;

import CGA.problem.Problem;

public abstract class AbstractGenericProblem<S> implements Problem<S> {
    private int numberOfVariables = 0;
    private int numberOfObjectives = 0;
    private int numberOfConstraints = 0;
    private String name = null;

    public AbstractGenericProblem() {
    }

    public int getNumberOfVariables() {
        return this.numberOfVariables;
    }

    public int getNumberOfObjectives() {
        return this.numberOfObjectives;
    }

    public int getNumberOfConstraints() {
        return this.numberOfConstraints;
    }

    public String getName() {
        return this.name;
    }

    protected void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    protected void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    protected void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
