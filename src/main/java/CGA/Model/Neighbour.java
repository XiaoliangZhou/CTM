package CGA.Model;

public class Neighbour {

    private String nId;
    private double Pl;
    private double Pr;
    private double fitness;
    private double x1;
    private double x2;

    public Neighbour() {
    }

    public Neighbour(String nId, double pl, double pr, double fitness, double x1, double x2, String gene) {
        this.nId = nId;
        Pl = pl;
        Pr = pr;
        this.fitness = fitness;
        this.x1 = x1;
        this.x2 = x2;
    }

    public String getnId() {
        return nId;
    }

    public double getPl() {
        return Pl;
    }

    public double getPr() {
        return Pr;
    }

    public double getFitness() {
        return fitness;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }


    public void setnId(String nId) {
        this.nId = nId;
    }

    public void setPl(double pl) {
        Pl = pl;
    }

    public void setPr(double pr) {
        Pr = pr;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    @Override
    public String toString() {
        return "Neighbour{" +
                "nId='" + nId + '\'' +
                ", fitness=" + fitness +
                ", Pl=" + Pl +
                ", Pr=" + Pr +
                ", x1=" + x1 +
                ", x2=" + x2 +
                '}';
    }
}
