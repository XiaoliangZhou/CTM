package transport.ctm.model;

import transport.graph.Vehicle;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

public class endCell extends ctmCell implements Serializable {
    /*渠化子元胞转向代码(0,straight)/(1,left)/(2,right)*/
    protected String c_dir;

    protected   ArrayDeque<Vehicle> c2 = new ArrayDeque<Vehicle>();

    private boolean isAmplify = false;
    @Override
    public double getC_cap() {
        return super.getC_cap();
    }

    public endCell() {
    }

    public String getC_dir() {
        return c_dir;
    }

    public void setC_dir(String c_dir) {
        this.c_dir = c_dir;
    }

    public ArrayDeque<Vehicle> getC2() {
        return c2;
    }

    public void setC2(ArrayDeque<Vehicle> c2) {
        this.c2 = c2;
    }


    public boolean isAmplify() {
        return isAmplify;
    }

    public void setAmplify(boolean amplify) {
        isAmplify = amplify;
    }

    @Override
    public String toString() {
        return "endCell{" +
                "c_dir='" + c_dir + '\'' +
                ", c2=" + c2 +
                '}';
    }
}
