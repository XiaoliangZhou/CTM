/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport.ctm.model;

import transport.graph.Vehicle;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Title: Edge.java
 * @Package du.jiaotong.transport.graph
 * @Description: 元胞实体类
 * @author liangxiao.zhou
 * @date JJan 15, 2019 16:11:14 PM
 * @version V3.0
 */
public class ctmCell implements Serializable {
    // 元胞主键
    public int c_id ;
    /*路段标识*/
    public String l_id ;
    /**
     * 元胞类型(0,普通元胞)
     *         (1,输入元胞)
     *         (2,起始元胞)
     *         (3,末尾元胞j)
     *         (4,元胞j-1)
     */
    public int c_type ;
    /*元胞饱和流率(veh/dT)*/
    public double c_rate ;
    /*元胞容纳能力(veh)*/
    public double c_cap ;
    /*第t时间步长元胞含有的车辆数(veh)*/
    public double c_n;
    /*元胞长度(m)*/
    public double c_lgth;
    /*元胞理论驶入流率(veh/sec)*/
    public double pos_in ;
    /*元胞理论驶出流率(veh/sec)*/
    public double pos_out ;
    /*元胞实际驶入流率(veh/sec)*/
    public double real_in ;
    /*元胞实际驶入流率(veh/sec)*/
    public double real_out ;
    /*当前时段元胞所含车辆对象*/
    public ArrayDeque<Vehicle> c1 = new ArrayDeque<Vehicle>();

    public int grindex;
    public int gcindex;

    public int getC_id() {
        return c_id;
    }

    public int getC_type() {
        return c_type;
    }

    public double getC_rate() {
        return c_rate;
    }

    public double getC_cap() {
        return c_cap;
    }

    public double getPos_in() {
        return pos_in;
    }

    public double getC_n() {
        return c_n;
    }

    public double getPos_out() {
        return pos_out;
    }

    public double getReal_in() {
        return real_in;
    }

    public double getReal_out() {
        return real_out;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public void setC_type(int c_type) {
        this.c_type = c_type;
    }

    public void setC_rate(double c_rate) { this.c_rate = c_rate; }

    public void setC_cap(double c_cap) {
        this.c_cap = c_cap;
    }

    public void setC_n(double c_n) {
        this.c_n = c_n;
    }

    public double getC_lgth() {
        return c_lgth;
    }

    public void setC_lgth(double c_lgth) {
        this.c_lgth = c_lgth;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    public void setPos_in(double pos_in) {
        this.pos_in = pos_in;
    }

    public void setPos_out(double pos_out) {
        this.pos_out = pos_out;
    }

    public void setReal_in(double real_in) {
        this.real_in = real_in;
    }

    public void setReal_out(double real_out) {
        this.real_out = real_out;
    }

    public ArrayDeque<Vehicle> getC1() {
        return c1;
    }

    public void setC1(ArrayDeque<Vehicle> c1) {
        this.c1 = c1;
    }

    public String getLaneId() {
        return l_id;
    }

    public void setLaneId(String l_id) {
        this.l_id = l_id;
    }

    public int getGrindex() {
        return grindex;
    }

    public int getGcindex() {
        return gcindex;
    }

    public void setGrindex(int grindex) {
        this.grindex = grindex;
    }

    public void setGcindex(int gcindex) {
        this.gcindex = gcindex;
    }

    @Override
    public String toString() {
        return "ctmCell{" +
                "c_id=" + c_id +
                ", l_id='" + l_id + '\'' +
                ", c_type=" + c_type +
                ", c_rate=" + c_rate +
                ", c_cap=" + c_cap +
                ", c_n=" + c_n +
                ", c_lgth=" + c_lgth +
                ", pos_in=" + pos_in +
                ", pos_out=" + pos_out +
                ", real_in=" + real_in +
                ", real_out=" + real_out +
                ", c1=" + c1 +
                '}';
    }
}
