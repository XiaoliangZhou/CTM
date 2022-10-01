/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport.ctm.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: intersection.java
 * @Package edu.jiaotong.transport.ctm.model;
 * @Description: 交叉口
 * @author liangxiao.zhou
 * @date Jan 15, 2019 16:11:14 PM
 * @version V2.0
 */
public class intersection implements Serializable {
    // 节点标识 1、2、3..
    public String nlabel;
    // 存放相位
    public Map<String, List<String[]>> phaseMap;
    // 节点内部元胞
    public int[] icells;
    // 相位对应链接编号
    public LinkedHashMap<Integer,int[]> phases;
    // 相位标识
    public int phase;
    // 交叉口类型 T型(0 ╠、1 ╦、2 ╩、3 ╣) 十字形(╬) 4 五叉路口 5 
    public String intStyle;

    public double sumDelay;

    public double baseDelay;

    public double queueLength;

    public double capcity;

    public int sumVeh;

    public intersection() {
    }

    public String getNlabel() {
        return nlabel;
    }

    public Map<String, List<String[]>> getPhaseMap() {
        return phaseMap;
    }

    public int[] getIcells() {
        return icells;
    }

    public LinkedHashMap<Integer, int[]> getPhases() {
        return phases;
    }

    public int getPhase() {
        return phase;
    }

    public String getIntStyle() {
        return intStyle;
    }

    public void setNlabel(String nlabel) {
        this.nlabel = nlabel;
    }

    public void setPhaseMap(Map<String, List<String[]>> phaseMap) {
        this.phaseMap = phaseMap;
    }

    public void setIcells(int[] icells) {
        this.icells = icells;
    }

    public void setPhases(LinkedHashMap<Integer, int[]> phases) {
        this.phases = phases;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setIntStyle(String intStyle) {
        this.intStyle = intStyle;
    }


    public double getSumDelay() {
        return sumDelay;
    }

    public void setSumDelay(double sumDelay) {
        this.sumDelay = sumDelay;
    }

    public int getSumVeh() {
        return sumVeh;
    }

    public void setSumVeh(int sumVeh) {
        this.sumVeh = sumVeh;
    }

    public double getBaseDelay() {
        return baseDelay;
    }

    public void setBaseDelay(double baseDelay) {
        this.baseDelay = baseDelay;
    }

    public double getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(double queueLength) {
        this.queueLength = queueLength;
    }

    public void setCapcity(double capcity) {
        this.capcity = capcity;
    }

    public double getCapcity() {
        return capcity;
    }

    @Override
    public String toString() {
        return "intersection{" +
                "nlabel='" + nlabel + '\'' +
                ", phaseMap=" + phaseMap +
                ", icells=" + Arrays.toString(icells) +
                ", phases=" + phases +
                ", phase=" + phase +
                ", intStyle='" + intStyle + '\'' +
                '}';
    }
}