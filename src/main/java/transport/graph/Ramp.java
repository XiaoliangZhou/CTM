package transport.graph;

import jdk.nashorn.internal.runtime.arrays.ArrayData;

import java.util.ArrayDeque;

public class Ramp {
    /*路段id*/
    private String id;
    /*假设路段出口设有匝道 存放终点车辆*/
    private ArrayDeque<Vehicle> rque = new ArrayDeque<>();

    public Ramp(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayDeque<Vehicle> getRque() {
        return rque;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRque(ArrayDeque<Vehicle> rque) {
        this.rque = rque;
    }
    @Override
    public String toString() {
        return "Ramp{" +
                "id='" + id + '\'' +
                ", rque=" + rque +
                '}';
    }
}
