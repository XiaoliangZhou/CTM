package transport.graph;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {
    /*车辆编号*/
    private String vehId;
    /*车辆行驶轨迹*/
    private DefaultGraphPath defaultGraphPath = new DefaultGraphPath() ;
    /*车辆所处当前路段的标识*/
    private String curPosLane;
    /*当前车辆所在路段元胞位置*/
    private int curCellPosition;
    /*进入路段时间*/
    private int extanceTime;
    /*车辆通过路段的时间*/
    private int travseTime ;
    /*车辆在下一时段进入下游元胞的标识，便于更新车辆*/
    //public int nextIntoCell;
    /*车辆转向*/
    private String steer ;
    /*车辆是否到达终点*/
    private boolean isReach;

    public Vehicle() {
    }

    public String getVehId() {
        return vehId;
    }


    public int getCurCellPosition() {
        return curCellPosition;
    }

    public int getExtanceTime() {
        return extanceTime;
    }

    public void setVehId(String vehId) {
        this.vehId = vehId;
    }

    public DefaultGraphPath getDefaultGraphPath() {
        return defaultGraphPath;
    }

    public void setDefaultGraphPath(DefaultGraphPath defaultGraphPath) {
        this.defaultGraphPath = defaultGraphPath;
    }

    public void setCurCellPosition(int curCellPosition) {
        this.curCellPosition = curCellPosition;
    }

    public void setExtanceTime(int extanceTime) {
        this.extanceTime = extanceTime;
    }

    public int getTravseTime() { return travseTime; }

    public void setTravseTime(int travseTime) {
        this.travseTime = travseTime;
    }

    public String getCurPosLane() {
        return curPosLane;
    }

    public void setCurPosLane(String curPosLane) {
        this.curPosLane = curPosLane;
    }

    public String getSteer() {
        return steer;
    }

    public void setSteer(String steer) {
        this.steer = steer;
    }

    public boolean isReach() {
        return isReach;
    }

    public void setReach(boolean reach) {
        isReach = reach;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehId='" + vehId + '\'' +
                ", defaultGraphPath=" + defaultGraphPath +
                ", curPosLane='" + curPosLane + '\'' +
                ", curCellPosition=" + curCellPosition +
                ", extanceTime=" + extanceTime +
                ", travseTime=" + travseTime +
                ", steer='" + steer + '\'' +
                ", isReach=" + isReach +
                '}';
    }
}
