package transport.graph;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class loadVehMap extends LinkedHashMap implements Map{

    public static LinkedHashMap<Integer,Integer> loadMap = new LinkedHashMap();;

    public loadVehMap() {}

    public static LinkedHashMap<Integer, Integer> getLoadMap() {
        return loadMap;
    }

    public static void setLoadMap(LinkedHashMap<Integer, Integer> loadMap) {
        loadVehMap.loadMap = loadMap;
    }

}
