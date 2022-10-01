
package CGA.Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chromosome implements Cloneable,Comparable<Chromosome>{
    String id;
    double x;
    /*适应度*/
    double fitness;
    double Pl;
    double Pr;
    String gene;
    /*中心元胞邻居*/
    List<Neighbour> olcs = new ArrayList<>();

    public Chromosome(String id,double x,double fitness, double pl, double pr, String gene, List<Neighbour> olcs) {
        this.id=id;
        this.x = x;
        this.fitness = fitness;
        Pl = pl;
        Pr = pr;
        this.gene = gene;
        this.olcs = olcs;
    }

    public Chromosome() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getFitness() {
        return fitness;
    }

    public double getPl() {
        return Pl;
    }

    public double getPr() {
        return Pr;
    }

    public String getGene() {
        return gene;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setPl(double pl) {
        Pl = pl;
    }

    public void setPr(double pr) {
        Pr = pr;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public List<Neighbour> getOlcs() {
        return olcs;
    }

    public void setOlcs(List<Neighbour> olcs) {
        this.olcs = olcs;
    }

    @Override
    public Object clone(){
        Chromosome idd = null;
        try {
            idd = (Chromosome) super.clone();
        } catch (CloneNotSupportedException e) { e.printStackTrace(); }
        idd.gene = this.gene;
        return idd;
    }

    @Override
    public int compareTo(Chromosome c) {
        double cr = c.getFitness();
        double ct = this.getFitness();
        if(cr>ct){
            return 1;
        }else if(cr==ct){
            return 0;
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "id='" + id + '\'' +
                ", fitness=" + fitness +
                ", Pl=" + Pl +
                ", Pr=" + Pr +
                ", gene='" + gene + '\'' +
                ", olcs=" + olcs +
                '}';
    }
}
