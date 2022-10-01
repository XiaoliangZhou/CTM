/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport.ctm.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Title: ctm_links.java
 * @Package edu.jiaotong.transport.ctm.model
 * @Description: 元胞链接类
 * @author liangxiao.zhou
 * @date Jan 15, 2019 16:11:14 PM
 * @version V1.0
 */
public class ctmLinks  implements Serializable {
    /*链接标识*/
    private String intLabel;
    /*交叉口类型*/
    private int flag ;
    private int intType;
    // 元胞链接类型
    private int type;
    // 相邻元胞传输编号
    private int[] cells;
    // 元胞分流比例 分流(直行) 合流(左转)
    private double[]  kp;
    // 是否可以访问
    private int access;
    // 该链接所处相位
    private int curPhase;
    /*该链接对应转向*/
    private String[] ster ;

    private  String steer;
    private String l_id;
    // no constructor
    public ctmLinks() {

    }

    public String getInt_label() {
        return intLabel;
    }

    public int getIntType() {
        return intType;
    }

    public int getType() {
        return type;
    }

    public int[] getCells() {
        return cells;
    }

    public double[] getKp() {
        return kp;
    }

    public int getAccess() {
        return access;
    }

    public int getCurPhase() {
        return curPhase;
    }

    public String[] getSter() {
        return ster;
    }

    public void setInt_label(String int_label) {
        this.intLabel = int_label;
    }

    public void setIntType(int intType) {
        this.intType = intType;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCells(int[] cells) {
        this.cells = cells;
    }

    public void setKp(double[] kp) {
        this.kp = kp;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setCurPhase(int curPhase) {
        this.curPhase = curPhase;
    }

    public void setSter(String[] ster) {
        this.ster = ster;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    public String getSteer() {
        return steer;
    }

    public void setSteer(String steer) {
        this.steer = steer;
    }

    @Override
    public String toString() {
        return "ctmLinks{" +
                "intLabel='" + intLabel + '\'' +
                ", flag=" + flag +
                ", intType=" + intType +
                ", type=" + type +
                ", cells=" + Arrays.toString(cells) +
                ", kp=" + Arrays.toString(kp) +
                ", access=" + access +
                ", curPhase=" + curPhase +
                ", ster=" + Arrays.toString(ster) +
                ", l_id='" + l_id + '\'' +
                '}';
    }
}
