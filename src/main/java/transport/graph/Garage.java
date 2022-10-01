package transport.graph;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Garage {
    public String IntId;
    public double proVeh;
    public LinkedList<Vehicle> reachVeh = new LinkedList<>();
    public Map<String,List<Vehicle>> vesMap =
            new LinkedHashMap<String,List<Vehicle>>();

    public Garage() { }

    public String getIntId() {
        return IntId;
    }

    public double getProVeh() {
        return proVeh;
    }

    public LinkedList<Vehicle> getReachVeh() { return reachVeh; }

    public Map<String, List<Vehicle>> getVes() {
        return vesMap;
    }

    public void setIntId(String intId) {
        IntId = intId;
    }

    public void setProVeh(double proVeh) {
        this.proVeh = proVeh;
    }

    public void setReachVeh(LinkedList<Vehicle> reachVeh) { this.reachVeh = reachVeh; }

    public void setVes(Map<String, List<Vehicle>> vesMap) {
        this.vesMap = vesMap;
    }

    @Override
    public String toString() {
        return "Garage{" +
                "IntId='" + IntId + '\'' +
                ", proVeh=" + proVeh +
                ", reachVeh=" + reachVeh +
                ", vesMap=" + vesMap +
                '}';
    }
}
