package transport.graph;


import java.util.*;

public class DefaultGraphPath {
    public String startVertex;
    public String endVertex;
    public LinkedList<DefaultWeightLabelEdge> paths = new LinkedList<>() ;
    public double weight ;


    public DefaultGraphPath() {}

    public String getStartVertex() {
        return startVertex;
    }

    public String getEndVertex() {
        return endVertex;
    }

    public LinkedList<DefaultWeightLabelEdge> getPaths() {
        return paths;
    }

    public double getWeight() {
        return weight;
    }

    public void setStartVertex(String startVertex) {
        this.startVertex = startVertex;
    }

    public void setEndVertex(String endVertex) {
        this.endVertex = endVertex;
    }

    public void setPaths(LinkedList<DefaultWeightLabelEdge> paths) {
        this.paths = paths;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "DefaultGraphPath{" +
                "startVertex='" + startVertex + '\'' +
                ", endVertex='" + endVertex + '\'' +
                ", paths=" + paths +
                '}';
    }
}
