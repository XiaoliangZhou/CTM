package transport.ctm.model;

import transport.graph.Vehicle;

import javax.naming.directory.SearchResult;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class secCell extends ctmCell implements Serializable {
    private int lkid;
    /*车流去向*/
    private String[] cdir;
    /*排队车辆*/
    protected Map<String, ArrayDeque<Vehicle>> queMap = new LinkedHashMap<String, ArrayDeque<Vehicle>>();
    /*出口渠化*/
    protected  Canal canal = new Canal();
    /*当前时段尾元胞允许最大发送量*/
    public  double cur_alow_out;

    public Map<String, ArrayDeque<Vehicle>> getQueMap() {
        return queMap;
    }

    public Canal getCanal() {
        return canal;
    }

    public double getCur_alow_out() {
        return cur_alow_out;
    }

    public void setQueMap(Map<String, ArrayDeque<Vehicle>> queMap) {
        this.queMap = queMap;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public void setCur_alow_out(double cur_alow_out) {
        this.cur_alow_out = cur_alow_out;
    }

    public String[] getCdir() {
        return cdir;
    }

    public void setCdir(String[] cdir) {
        this.cdir = cdir;
    }

    public int getLkid() {
        return lkid;
    }

    public void setLkid(int lkid) {
        this.lkid = lkid;
    }

    @Override
    public String toString() {
        return "secCell{" +
                "lkid=" + lkid +
                ", cdir=" + Arrays.toString(cdir) +
                ", queMap=" + queMap +
                ", canal=" + canal +
                ", cur_alow_out=" + cur_alow_out +
                '}';
    }
}
