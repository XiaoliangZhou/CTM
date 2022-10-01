package CGA.MocGAs;

import CGA.Model.Cont;

import java.util.Arrays;

public class ChromeBase implements Cloneable {
    String Id;
    int index=0;
    double[] f = new double[Cont.F_NUM];

    public ChromeBase() {
    }

    public String getId() {
        return Id;
    }

    public double[] getF() {
        return f;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setF(double[] f) {
        this.f = f;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChromeBase{" +
                "Id='" + Id + '\'' +
                ", index=" + index +
                ", f=" + Arrays.toString(f) +
                '}';
    }
}
