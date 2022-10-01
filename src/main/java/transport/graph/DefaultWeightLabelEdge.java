package transport.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import transport.ctm.util.constant;

public class DefaultWeightLabelEdge extends DefaultWeightedEdge {

    /*路段标识*/
    private String label ;
    /*路段自由流时间*/
    private double freeTime;
    /*车辆到达下一路段标识*/
    //private String nextRoadLabel;


    public DefaultWeightLabelEdge(String label) {
        this.label = label;
    }
    public DefaultWeightLabelEdge(String label,double freeTime) {
        this.label = label;
        this.freeTime = freeTime;
    }
    public String getLabel() {
        return label;
    }
    @Override
    public String getSource() {
        return super.getSource().toString();
    }
    @Override
    public String getTarget() { return super.getTarget().toString(); }

    @Override
    public double getWeight() {
        return super.getWeight();
    }

    public double getFreeTime() {
        return freeTime;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFreeTime(double freeTime) {
        this.freeTime = freeTime;
    }
/*  @Override
    public String toString() {
        return "DefaultWeightLabelEdge{" +
                "label='" + label + '\'' +
                '}';
    }*/
}
