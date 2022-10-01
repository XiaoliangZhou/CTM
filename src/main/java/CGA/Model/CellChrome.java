package CGA.Model;

import java.util.ArrayList;
import java.util.List;

public class CellChrome implements Cloneable {

    String id;
    double x1;
    double x2;
    double maxNghFitness;/*个体V(i,j)所有邻居中最大适应度*/
    /*个体适应度*/
    double fitness;
    /*中心元胞邻居*/
    List<Neighbour> olcs = new ArrayList<>();

    public CellChrome() {
    }

    public CellChrome(String id, double x1, double x2, double maxNghFitness, double fitness, List<Neighbour> olcs) {
        this.id = id;
        this.x1 = x1;
        this.x2 = x2;
        this.maxNghFitness = maxNghFitness;
        this.fitness = fitness;
        this.olcs = olcs;
    }

    public String getId() {
        return id;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getMaxNghFitness() {
        return maxNghFitness;
    }

    public double getFitness() {
        return fitness;
    }

    public List<Neighbour> getOlcs() {
        return olcs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setMaxNghFitness(double maxNghFitness) {
        this.maxNghFitness = maxNghFitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setOlcs(List<Neighbour> olcs) {
        this.olcs = olcs;
    }

    @Override
    public Object clone(){
        CellChrome idd = null;
        try {
            idd = (CellChrome) super.clone();
        } catch (CloneNotSupportedException e) { e.printStackTrace(); }
        idd.id=this.id;
        idd.x1 = this.x1;
        idd.x2 = this.x2;
        idd.fitness= this.fitness;
        idd.olcs=this.olcs;
        return idd;
    }
    @Override
    public String toString() {
        return "CellChrome{" +
                "id='" + id + '\'' +
                ", fitness=" + fitness +
                ", x1=" + x1 +
                ", x2=" + x2 +
                ", maxNghFitness=" + maxNghFitness +
                ", olcs=" + olcs +
                '}';
    }
}
