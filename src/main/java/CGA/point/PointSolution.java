package CGA.point;



import org.uma.jmetal.solution.Solution;

import java.util.*;

public class PointSolution implements Solution<Double> {
    private int numberOfObjectives;
    private double[] objectives;
    protected Map<Object, Object> attributes;

    public PointSolution(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
        this.objectives = new double[numberOfObjectives];
        this.attributes = new HashMap();
    }

    public PointSolution(Point point) {
        this.numberOfObjectives = point.getDimension();
        this.objectives = new double[this.numberOfObjectives];

        for(int i = 0; i < this.numberOfObjectives; ++i) {
            this.objectives[i] = point.getValue(i);
        }

    }

    public PointSolution(Solution<?> solution) {
        this.numberOfObjectives = solution.getNumberOfObjectives();
        this.objectives = new double[this.numberOfObjectives];

        for(int i = 0; i < this.numberOfObjectives; ++i) {
            this.objectives[i] = solution.getObjective(i);
        }

    }

    public PointSolution(PointSolution point) {
        this(point.getNumberOfObjectives());

        for(int i = 0; i < this.numberOfObjectives; ++i) {
            this.objectives[i] = point.getObjective(i);
        }

    }

    public void setObjective(int index, double value) {
        this.objectives[index] = value;
    }

    public double getObjective(int index) {
        return this.objectives[index];
    }

    public double[] getObjectives() {
        return this.objectives;
    }

    public List<Double> getVariables() {
        return Collections.emptyList();
    }

    public Double getVariableValue(int index) {
        return null;
    }

    public void setVariableValue(int index, Double value) {
    }

    public String getVariableValueString(int index) {
        return null;
    }

    public int getNumberOfVariables() {
        return 0;
    }

    public int getNumberOfObjectives() {
        return this.numberOfObjectives;
    }

    public PointSolution copy() {
        return new PointSolution(this);
    }

    public void setAttribute(Object id, Object value) {
        this.attributes.put(id, value);
    }

    public Object getAttribute(Object id) {
        return this.attributes.get(id);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PointSolution that = (PointSolution)o;
            if (this.numberOfObjectives != that.numberOfObjectives) {
                return false;
            } else {
                return Arrays.equals(this.objectives, that.objectives);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.objectives);
    }

    public String toString() {
        return Arrays.toString(this.objectives);
    }

    public Map<Object, Object> getAttributes() {
        return this.attributes;
    }
}
