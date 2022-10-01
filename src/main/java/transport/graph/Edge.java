package transport.graph;

import org.jgrapht.graph.DefaultEdge;
import transport.ctm.model.Canal;

import java.util.*;

/**
 * @Title: Edge.java
 * @Package edu.jiaotong.transport.graph
 * @Description: 路段实体类
 * @author liangxiao.zhou
 * @date Jan 12, 2019 16:11:14 PM
 * @version V1.0
 */
public class Edge {
    /*路段编号*/
    private String label ;
    /*路段类型 0(normal)1(input)*/
   // private int type ;
    /*路段对应交叉口*/
    private String intType ;
    /*起始节点标识*/
    private String sourceVertex;
    /*终止节点*/
    private Vertex endVertex ;
    /*权重*/
    private double  weight ;
    /*临时变量*/
    public double tmpWeight;

    public double tmpRate;
    /*路段容纳最大车辆数*/
    private int cap ;
    /*车道最大流量*/
    private Double satRate ;
    /*路段输入元胞编号*/
    private int inCell ;
    /*路段起始元胞编号*/
    private int oCell;
    /*路段元胞数*/
    private int cellNum;
    /*路段尾元胞编号*/
    private int dCell;
    /*渠化子元胞*/
    private Map<String,Integer> dcMap;
    /*路段走向*/
    private String direct ;
    /*匝道Object*/
    private Ramp  ramp ;
    /*路段自由流行程时间*/
    private double freeTime;
    /*路段平均旅行时间*/
    private double travleTime;
    /*路段考察时间段平均行程速度*/
    private double PeriodAvg;
    /*车道数*/
    private  int roadNum;
    private double tempAvg;

    private double totalTime;
    private double vehNumber;

    private Map<Integer, Map<Integer,Integer>>
            avgMaps = new TreeMap<Integer,Map<Integer,Integer>>();

    private Map<Integer,Double> gMaps = new TreeMap<>();

    private Map<Integer,Double> steerMaps = new HashMap<>();

    private Map<String,Integer> reachMaps = new HashMap<>();

    private Map<Integer,Double> inputMaps = new HashMap<>();

    /**/
    private List<Double> lvs = new ArrayList<>();

    /*边中介中心性 该参数反映边在整个网络中的作用和影响力*/
    private double betCentrality;

    private double ecty ;

    private double etmp;

    /*无参构造器*/
    public Edge(){
        
    }
    public String getLabel() {
        return label;
    }

    public String getIntType() {
        return intType;
    }

    public String getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public double getWeight() {
        return weight;
    }

    public int getCap() {
        return cap;
    }

    public Double getSatRate() {
        return satRate;
    }

    public int getInCell() {
        return inCell;
    }

    public int getoCell() {
        return oCell;
    }

    public int getCellNum() {
        return cellNum;
    }

    public int getdCell() {
        return dCell;
    }


    public Map<String, Integer> getDcMap() {
        return dcMap;
    }

    public String getDirect() {
        return direct;
    }

    public Ramp getRamp() {
        return ramp;
    }

    public double getFreeTime() {
        return freeTime;
    }

    public double getTravleTime() {
        return travleTime;
    }

    public double getPeriodAvg() {
        return PeriodAvg;
    }

    public int getRoadNum() {
        return roadNum;
    }

    public double getTempAvg() {
        return tempAvg;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIntType(String intType) {
        this.intType = intType;
    }

    public void setSourceVertex(String sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public void setSatRate(Double satRate) {
        this.satRate = satRate;
    }

    public void setInCell(int inCell) {
        this.inCell = inCell;
    }

    public void setoCell(int oCell) {
        this.oCell = oCell;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public void setdCell(int dCell) {
        this.dCell = dCell;
    }

    public void setDcMap(Map<String, Integer> dcMap) {
        this.dcMap = dcMap;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public void setRamp(Ramp ramp) {
        this.ramp = ramp;
    }

    public void setFreeTime(double freeTime) {
        this.freeTime = freeTime;
    }

    public void setTravleTime(double travleTime) {
        this.travleTime = travleTime;
    }

    public void setPeriodAvg(double periodAvg) {
        PeriodAvg = periodAvg;
    }

    public void setRoadNum(int roadNum) {
        this.roadNum = roadNum;
    }

    public void setTempAvg(double tempAvg) {
        this.tempAvg = tempAvg;
    }

    public List<Double> getLvs() {
        return lvs;
    }

    public void setLvs(List<Double> lvs) {
        this.lvs = lvs;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getVehNumber() {
        return vehNumber;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public void setVehNumber(double vehNumber) {
        this.vehNumber = vehNumber;
    }

    public Map<Integer, Map<Integer,Integer>> getAvgMaps() {
        return avgMaps;
    }

    public void setAvgMaps(Map<Integer, Map<Integer, Integer>> avgMaps) {
        this.avgMaps = avgMaps;
    }

    public Map<Integer, Double> getgMaps() {
        return gMaps;
    }

    public void setgMaps(Map<Integer, Double> gMaps) {
        this.gMaps = gMaps;
    }

    public double getBetCentrality() {
        return betCentrality;
    }

    public void setBetCentrality(double betCentrality) {
        this.betCentrality = betCentrality;
    }


    public double getEcty() {
        return ecty;
    }

    public void setEcty(double ecty) {
        this.ecty = ecty;
    }

    public double getEtmp() {
        return etmp;
    }

    public void setEtmp(double etmp) {
        this.etmp = etmp;
    }

    public Map<Integer, Double> getSteerMaps() {
        return steerMaps;
    }

    public void setSteerMaps(Map<Integer, Double> steerMaps) {
        this.steerMaps = steerMaps;
    }

    public Map<String, Integer> getReachMaps() {
        return reachMaps;
    }

    public void setReachMaps(Map<String, Integer> reachMaps) {
        this.reachMaps = reachMaps;
    }

    public Map<Integer, Double> getInputMaps() {
        return inputMaps;
    }

    public void setInputMaps(Map<Integer, Double> inputMaps) {
        this.inputMaps = inputMaps;
    }

    public double getTmpWeight() {
        return tmpWeight;
    }

    public void setTmpWeight(double tmpWeight) {
        this.tmpWeight = tmpWeight;
    }

    public double getTmpRate() {
        return tmpRate;
    }

    public void setTmpRate(double tmpRate) {
        this.tmpRate = tmpRate;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "label='" + label + '\'' +
                ", intType='" + intType + '\'' +
                ", sourceVertex='" + sourceVertex + '\'' +
                ", endVertex=" + endVertex +
                ", weight=" + weight +
                ", cap=" + cap +
                ", satRate=" + satRate +
                ", inCell=" + inCell +
                ", oCell=" + oCell +
                ", cellNum=" + cellNum +
                ", dCell=" + dCell +
                ", dcMap=" + dcMap +
                ", direct='" + direct + '\'' +
                ", ramp=" + ramp +
                ", freeTime=" + freeTime +
                ", travleTime=" + travleTime +
                ", PeriodAvg=" + PeriodAvg +
                ", roadNum=" + roadNum +
                ", tempAvg=" + tempAvg +
                ", totalTime=" + totalTime +
                ", vehNumber=" + vehNumber +
                ", lvs=" + lvs +
                '}';
    }
}
