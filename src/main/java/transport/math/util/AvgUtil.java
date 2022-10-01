package transport.math.util;

import org.jgrapht.Graph;
import transport.ctm.main.initCtm;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.graph.DefaultGraphPath;
import transport.graph.DefaultWeightLabelEdge;
import transport.graph.Edge;
import transport.graph.Vehicle;

import java.math.BigDecimal;
import java.util.*;

public class AvgUtil {

    static int s1, s2;
    static double T, length, avg, tra, jam, k, c_length, kb;
    ;

    public static List ctms = constant.ctms;
    private static final int DEFAUTL_SCALE = 3;
    private static final Graph<String, DefaultWeightLabelEdge> g = constant.graph;
    /**
     * @param step
     * @param e
     */
    public static int getRoadAvg(int step, Edge e, int count, intersection intsec) {
        int s1 = 1;
        int s2 = 200;
        double T, length, v1;
        double jam = 0.2000, k, k0, k1;
        double tra = 0.0, tra1 = 0, freeTime;
        int t1 = 0, t2 = 0, t3 = 0, t4 = 0;
        double avg = 0.0, freeAvg = 50.0, avg0 = 0;
        double l_a = 70;
        double d_l_a = 80;

        double tt = 0.0;

        int sec;
        ctmCell ctc;
        endCell edc;
        secCell stc;
        ArrayDeque<Vehicle> dq;


        int startIndex, toIndex;
        toIndex = e.getdCell();
        startIndex = e.getoCell();

        int incel = e.getInCell();
       // ctmCell inc = initCtm.getInCell(ctms,incel);



        /*统计路段流储出量*/
        String eVertex = e.getEndVertex().getLabel();
        /*路段的阻塞密度*/
        jam = mul(jam, e.getRoadNum());

        int n3 = 0, n84 = 0, nd = 0;
        /*统计路段流入量*/
        Map<Integer,Double> steermaps =e.getSteerMaps();
        while (startIndex <= toIndex) {
            ctc = (ctmCell) ctms.get(startIndex);
            tt = ctc.getC_n();

            /*统计路段流入量*/
            if(eVertex.equals(intsec.getNlabel())){
               // System.out.println(step+"   "+ctc.getC_id()+"    "+ctc.getC_cap()+"   "+ctc.getC_n()+"  "+(int)mul(ctc.real_out,5));
                if (ctc.c_type == 2) {
                    int sveh = intsec.getSumVeh();
                    sveh+=ctc.real_in;;
                    intsec.setSumVeh(sveh);
                }
            }
          /*  if(eVertex.equals(intsec.getNlabel())){
                if(!edc.getC_dir().equals("2")){
                    *//*统计车流转向*//*
                    Map<Integer,Double> steermaps =e.getSteerMaps();
                    int output = (int) mul(edc.real_out, 5);
                    *//*累积流出量*//*
                    steermaps.put(1, steermaps.get(1) + output);
                    int sveh = intsec.getSumVeh();
                    sveh+=output;
                    td+=output;s
                    intsec.setSumVeh(sveh);
                    // System.out.println(step+"---"+e.getLabel()+"---"+edc.getC_dir()+"---"+output);

                }
            }*/
            t1 += tt;
            ++startIndex;
        }

        /*终点车辆*/
        dq = e.getRamp().getRque();
        t4 = dq.size();
        if (t4 > 0) {
            dq.clear();
        }

        T = t1 + t3; /*路段总车辆数*/
        count += T;

        length = (e.getCellNum() - 1) * l_a + d_l_a;/*路段长度*/
        k = MathSupplier.div(T, length);
        double k_limt = mul(0.4, jam);
        if (k < k_limt) {
            //tra1 = mul(e.getFreeTime(),Math.pow(Math.E,div(k,jam)));
            tra = mul(e.getFreeTime(), div(jam, jam - k));
        } else {
            tra = div(mul(2.0, e.getFreeTime()), Math.log(div(jam, k)));
        }
        e.setWeight(tra);/*路段阻抗*/
        //e.setTmpWeight(tra);
        if (T == 0) {
            /*自由流时间*/
            g.setEdgeWeight(e.getSourceVertex(), e.getEndVertex().label, tra);

        }
        if (T > 0) {
            g.setEdgeWeight(e.getSourceVertex(), e.getEndVertex().label, tra);

        }
        //e.setWeight(trvTime);/*路段阻抗*/
        //System.out.println(nt +"------------"+tra);
        if (step % 10 == 0) {
            //System.out.println(tra +"-----"+nt);
            //System.out.println("-------------------"+step +"-----------------------");
            //System.out.print("road"+e.getLabel()+" : "+ T);
            //System.out.println("\r\n");

        }
      /*  if (k < k_limt) {
            avg = mul(freeAvg, sub(1, div(k, jam)));*//*路段平均行程速度*//*
        } else {
            avg = mul(div(freeAvg, 2), Math.log(div(jam, k)));
        }*/
        //double v0 = e.getTempAvg();
        //v0 += tra/e.getFreeTime();
       // v0 += avg;
        //e.setTempAvg(v0);

        /*考察时间段内路段平均速度 线性密度模型*/
      /*  if (step >= s1 & step <= s2) {
            *//*平均行程速度*//*
            e.setPeriodAvg(e.getPeriodAvg() + avg);

        }*/
        /*考察时间段内路段平均速度 本文改进模型*/
      /*  if (step >= s1 & step <= s2) {
            double v0 = e.getTempAvg();
            v0 += avg;
            e.setTempAvg(v0);
        }*/

        return count;
    }

    /**
     *
     * @param e
     * @param intsec
     */
    public static void getBaseFlow(Edge e, intersection intsec) {

        int t = 0;
        double tt = 0.0;

        int edc_index;
        ctmCell ctc;

        edc_index = e.getdCell();

        int startIndex, toIndex;
        toIndex = edc_index;
        startIndex = e.getoCell();

        /*统计路段流储出量*/
        String eVertex = e.getEndVertex().getLabel();
        /*统计路段流入量*/
        if(eVertex.equals(intsec.getNlabel())){
            /*统计路段流入量*/
            Map<Integer,Double> steermaps =e.getSteerMaps();
            while (startIndex <= toIndex) {
                ctc = (ctmCell) ctms.get(startIndex);
                tt = ctc.getC_n();
                t += tt;
                ++startIndex;
            }
            /*路段总车辆数*/
            double bsDelay = intsec.getBaseDelay();
            bsDelay+=t;
            intsec.setBaseDelay(bsDelay);
            //System.out.println(e.getLabel()+"   "+t);
        }


    }
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return double   返回类型
     * @throws
     * @Title: div
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private static double div(double v1, double v2) {
        return MathSupplier.div(v1, v2, DEFAUTL_SCALE);
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return double   返回类型
     * @throws
     * @Title: sub
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return double   返回类型
     * @throws
     * @Title: mul
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static double mul(double v1, double v2) {
        return MathSupplier.mul3(v1, v2);
    }


    public static void main(String[] args) {
        //load();
        System.out.println(div(0.520, 0.533));

        System.out.println(mul(15.0, sub(1, div(0.520, 0.533))));
    }

}
