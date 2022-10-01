package CGA.Model;

import java.util.ArrayList;
import java.util.List;

public class ThreeCellChrom {

    String id;
    double x;
    double y;
    double z;
    double maxNghFitness;/*个体V(i,j)所有邻居中最大适应度*/
    /*个体适应度*/
    double fitness;
    /*中心元胞邻居*/
    List<Neighbour> olcs = new ArrayList<>();

    public ThreeCellChrom(String id, double x, double y, double z, double maxNghFitness, double fitness, List<Neighbour> olcs) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxNghFitness = maxNghFitness;
        this.fitness = fitness;
        this.olcs = olcs;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
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
    public String toString() {
        return "ThreeCellChrom{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", maxNghFitness=" + maxNghFitness +
                ", fitness=" + fitness +
                ", olcs=" + olcs +
                '}';
    }
}
