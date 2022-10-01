package CGA.Model;

import CGA.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import util.JMetalRandom;

import java.util.*;

public abstract class ChromeBase<T, P extends Problem<?>> implements Solution<T>{
    String Id;
    protected P problem;
    private double[] objectives;
    private List<T> variables;
    protected Map<Object, Object> attributes;
    double[] f = new double[2];
    @Deprecated
    protected  JMetalRandom randomGenerator ;

    public ChromeBase() {
    }
    protected ChromeBase(P problem) {
        this.problem = problem;
        this.randomGenerator   = JMetalRandom.getInstance();
        this.attributes = new HashMap<>();
        this.objectives = new double[problem.getNumberOfObjectives()];
        this.variables = new ArrayList(problem.getNumberOfVariables());

        for(int i = 0; i < problem.getNumberOfVariables(); ++i) {
            this.variables.add(i, (T) null);
        }

    }

    protected void initializeObjectiveValues() {
        for(int i = 0; i < this.problem.getNumberOfObjectives(); ++i) {
            this.objectives[i] = 0.0D;
        }

    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public P getProblem() {
        return problem;
    }

    public double[] getObjectives() {
        return objectives;
    }

    public List<T> getVariables() {
        return variables;
    }

    public void setProblem(P problem) {
        this.problem = problem;
    }

    public void setObjectives(double[] objectives) {
        this.objectives = objectives;
    }

    public void setVariables(List<T> variables) {
        this.variables = variables;
    }

    public void setObjective(int index, double value) {
        this.objectives[index] = value;
    }

    public double getObjective(int index) {
        return this.objectives[index];
    }

    public T getVariableValue(int index) {
        return this.variables.get(index);
    }

    public void setVariableValue(int index, T value) {
        this.variables.set(index, value);
    }

    public int getNumberOfVariables() {
        return this.variables.size();
    }

    public int getNumberOfObjectives() {
        return this.objectives.length;
    }

    public void setAttribute(Object id, Object value) {
        this.attributes.put(id, value);
    }

    public Object getAttribute(Object id) {
        return this.attributes.get(id);
    }

    @Override
    public String toString() {
        return "ChromeBase{" +
                "objectives=" + Arrays.toString(objectives) +
                ", variables=" + variables +
                '}';
    }


    private boolean equalsIgnoringAttributes(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ChromeBase<?, ?> that = (ChromeBase)o;
            if (!Arrays.equals(this.objectives, that.objectives)) {
                return false;
            } else {
                return this.variables.equals(that.variables);
            }
        } else {
            return false;
        }
    }

    public boolean equals(Object o) {
        if (!this.equalsIgnoringAttributes(o)) {
            return false;
        } else {
            ChromeBase<?, ?> that = (ChromeBase)o;
            if (this.attributes.size() != that.attributes.size()) {
                return false;
            } else {
                Iterator var3 = this.attributes.keySet().iterator();

                while(var3.hasNext()) {
                    Object key = var3.next();
                    Object value = this.attributes.get(key);
                    Object valueThat = that.attributes.get(key);
                    if (value != valueThat) {
                        if (value == null) {
                            return false;
                        }

                        if (valueThat == null) {
                            return false;
                        }

                        boolean areAttributeValuesEqual;
                        if (value instanceof AbstractGenericSolution) {
                            areAttributeValuesEqual = ((ChromeBase)value).equalsIgnoringAttributes(valueThat);
                        } else {
                            areAttributeValuesEqual = !value.equals(valueThat);
                        }

                        if (!areAttributeValuesEqual) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    public int hashCode() {
        int result = Arrays.hashCode(this.objectives);
        result = 31 * result + this.variables.hashCode();
        result = 31 * result + this.attributes.hashCode();
        return result;
    }
}
