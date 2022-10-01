package CGA.Model;

import CGA.MocGAs.Pareto;
import CGA.problem.impl.DoubleProblem;
import CGA.problem.impl.DoubleSolution;
import util.JMetalRandom;

import java.io.*;
import java.util.*;

public  class Chrome extends ChromeBase<Double, DoubleProblem> implements DoubleSolution {
    int rank ;/*pareto等级*/
    double nd = 0;/*个体拥挤度*/
    public boolean isCurChr=false;
    List<Double> x = new ArrayList<>(Cont.X_NUM);
    Pareto pareto = new Pareto();
    int axis;
    int yaxis;
    int haxis;
    public Chrome() {
        for (int i = 0; i < Cont.X_NUM; i++) {
            x.add(0.0);
        }
    }

    public int getHaxis() {
        return haxis;
    }

    public void setHaxis(int haxis) {
        this.haxis = haxis;
    }

    public String getId() {
        return Id;
    }

    public List<Double> getX() {
        return x;
    }

    public int getAxis() {
        return axis;
    }

    public int getYaxis() {
        return yaxis;
    }

    public void setAxis(int axis) {
        this.axis = axis;
    }

    public void setYaxis(int yaxis) {
        this.yaxis = yaxis;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public void setId(String id) {
        Id = id;
    }


    public int getRank() {
        return rank;
    }

    public double[] getF() {
        return f;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setF(double[] f) {
        this.f = f;
    }

    public Pareto getPareto() {
        return pareto;
    }

    public double getNd() {
        return nd;
    }

    public void setNd(double nd) {
        this.nd = nd;
    }

    public void setPareto(Pareto pareto) {
        this.pareto = pareto;
    }

    public boolean isCurChr() {
        return isCurChr;
    }

    public void setCurChr(boolean curChr) {
        isCurChr = curChr;
    }


    public Chrome(DoubleProblem problem,int axis,int yaxis,int haxis) {
        super(problem);
        this.initializeOtherValues(axis,yaxis,haxis);
        this.initializeDoubleVariables(JMetalRandom.getInstance());
        this.initializeObjectiveValues();
    }

    public Chrome(Chrome solution) {
        super(solution.problem);
        this.setAxis(solution.getAxis());
        this.setYaxis(solution.getYaxis());
        this.setHaxis(solution.getHaxis());
        this.setNd(solution.getNd());
        this.setRank(solution.getRank());
        int i;
        for(i = 0; i < this.getNumberOfVariables(); ++i) {
            this.setVariableValue(i, solution.getVariableValue(i));
        }
        for(i = 0; i < this.getNumberOfObjectives(); ++i) {
            this.setObjective(i, solution.getObjective(i));
        }
        this.attributes = new HashMap(solution.attributes);
    }
    public Chrome copy()   {
      return new Chrome(this);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return null;
    }


    private void initializeOtherValues(int axis,int yaxis,int haxis) {
        this.setAxis(axis);
        this.setYaxis(yaxis);
        this.setHaxis(haxis);
        this.setRank(-1);
    }
    private void initializeDoubleVariables(JMetalRandom randomGenerator) {
        for(int i = 0; i < ((DoubleProblem)this.problem).getNumberOfVariables(); ++i) {
            Double value = randomGenerator.nextDouble(this.getLowerBound(i), this.getUpperBound(i));
            this.setVariableValue(i, value);
        }

    }
    @Override
    public Double getLowerBound(int index) {
        return (Double)((DoubleProblem)this.problem).getLowerBound(index);
    }

    @Override
    public Double getUpperBound(int index) {
        return (Double)((DoubleProblem)this.problem).getUpperBound(index);
    }

    public String getVariableValueString(int index) {
        return ((Double)this.getVariableValue(index)).toString();
    }
    @Override
    public String toString() {
        return "Chrome{" +
                ", axis=" + axis +
                ", yaxis=" + yaxis +
                ", haxis=" + haxis +
                ", rank=" + rank +
                ", nd=" + nd +
                ", objectives=" + Arrays.toString(this.getObjectives()) +
                ", variables=" + this.getVariables().toString() +
                '}';
    }
}
