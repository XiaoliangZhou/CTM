package transport.ctm.main;

import org.apache.commons.lang3.StringUtils;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.graph.*;
import transport.math.util.MathSupplier;

import java.math.BigDecimal;
import java.util.*;

public class sim_04 {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    private static final int DEFAUTL_SCALE = 3;
    /*拥挤传播速度(m/sec)*/
    private static final double  ctm_w_vf = 5.6 ;
    /*自由流速度(m/sec)*/
    private static final double  vf = 14.0 ;
    /*时间步长(sec)*/
    private static final double  dt = 5 ;
    private static double N,N1,N2,N3 ;
    private static double ctm_w_gth = 0.0 ;
    private static double ctm_vf_gth = 0.0 ;
    private static int c1,c2,c3,c4;
    private static double fmin,f,f1,f2,f3,f4;
    private static double b1,b2,b3,b4;
    private static List<ctmCell> ctms = constant.ctms;
    private static List<ctmLinks> clks = constant.clks;
    private  static int curStep;
    /**
     *
     * 节点、路段模型
     * @Title: ctmSiulat
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return Edge   返回类型
     * @throws
     */
    public static void ctmSimulat(int step){
        curStep=step;
        if (!ctms.isEmpty()) {
            ctms.forEach((cc)->{
                double c_l = cc.c_lgth;
                switch(cc.c_type){
                    case 0 : case 3:
                        /*当前元胞的驶入流率*/
                        cc.pos_in = min(cc.c_rate*dt, mul(ctm_w_vf*(cc.c_cap/c_l - cc.c_n/c_l),dt));
                        /*当前元胞的驶出流率*/
                        cc.pos_out =min(cc.c_rate*dt, cc.c_n);
                       /* if(cc.c_type==3){
                            endCell edc = (endCell)cc;
                            if(edc.getC_dir().equals("1")&edc.getLaneId().equals("32_1")){
                                System.out.println(step+"    "+cc.pos_in+"   "+cc.pos_out+"   "+cc.real_in+"   "+cc.c_n);
                            }

                        }*/
                        cc.real_in = 0.0;
                        cc.real_out = 0.0;
                        break ;
                    case 1:
                        /*输入元胞*/
                        cc.c_n = cc.c_n + cc.c_rate;
                        cc.pos_in = 0;
                        /*发送流率*/
                        cc.pos_out =  cc.c_n ;


                        cc.real_in = 0.0;
                        cc.real_out = 0.0;
                        break;
                    case 2:
                        /*起始元胞*/
                        cc = (orginCell)cc;

                        /*当前元胞的驶入流率*/
                        cc.pos_in = min(cc.c_rate*dt, mul(ctm_w_vf*(cc.c_cap/c_l - cc.c_n/c_l),dt));
                        /*当前元胞的驶出流率*/
                        cc.pos_out =min(cc.c_rate*dt, cc.c_n);

                        cc.real_in = 0.0;
                        cc.real_out = 0.0;

                        ((orginCell) cc).f_l_in = 0.0;
                        ((orginCell) cc).f_l_in_1 = 0.0;
                        break;
                }
            });
        }
        for (ctmLinks clk : clks) {
            if (clk.getAccess() == 0) {
                continue;
            }
            switch (clk.getType()) {
                case 0: case 3:
                    c1 = clk.getCells()[0]; /*车流传递*/
                    c2 = clk.getCells()[1];

                    ctmCell c10 = ctms.get(c1);
                    ctmCell c20 = ctms.get(c2);

                    f = min(c10.pos_out, c20.pos_in);
                    c10.real_out = f;
                    c20.real_in  = f;

                    break;
                case 1:
                    /*合流*/
                    c1 = clk.getCells()[0]; /*车流传递*/
                    c2 = clk.getCells()[1];

                    endCell c431 = (endCell)ctms.get(c1);
                    orginCell c432 = (orginCell) ctms.get(c2);
                   /* if(c431.getLaneId().equals("32_1")){
                        System.out.println(step+"    "+c431.pos_out+"   "+c431.c_n);
                    }*/
                    f = min(c431.pos_out, c432.pos_in);
                   // System.out.println(step+"   "+c431.getLaneId()+"  "+"pos_out = "+c431.pos_out);
                    c431.real_out = f;
                    c432.real_in = f;

                    break;
                default:
                    break;
            }
        }
        /*更新路段车流*/
        updateRoadFlux(step);
    }

    /**
     * 更新路段车流
     * @param step
     */
    protected static void updateRoadFlux(int step) {
        ctms.forEach((ctm)->{
            int size=0;
            int type = ctm.getC_type();
            //ctm.c_n = sub(add(ctm.c_n,ctm.real_in),ctm.real_out);
        /*    if(ctm.getC_type()==3&ctm.getLaneId().equals("32_1")){
                System.out.println(step+"    "+ctm.c_id+"   "+ctm.c_n+"   "+ctm.real_in+"   "+ctm.real_out);

            }*/
            ctm.c_n =ctm.c_n+ctm.real_in-ctm.real_out;

           /* if(ctm.c_id==15){
                System.out.println(step+"  "+ctm.l_id+"   "+ctm.pos_in+"  "+ctm.real_in+"   "+ctm.c_n);
            }*/
            if(ctm.c_type == 1) ctm.setC_rate(0.0);
        });
    }

    /**
     * 返回三个数的最大值
     * @Title: medmin
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param a
     * @param b
     * @param c
     * @return double   返回类型
     * @throws
     */
    private static double medmax(double a,double b,double c){
        return MathSupplier.medmax(a,b,c);
    }
    /**
     * 返回三个数的最小值
     * @Title: medmin
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param a
     * @param b
     * @param c
     * @return double   返回类型
     * @throws
     */
    private static double medmin(double a,double b,double c){
        return MathSupplier.medmin(a,b,c);
    }
    /**
     * 取中间值
     * @Title: median
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param a
     * @param b
     * @param c
     * @return double   返回类型
     * @throws
     */
    private static double median(double a,double b,double c){
        return MathSupplier.median(a,b,c);
    }
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
     * @Title: div
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param v1  被除数
     * @param v2  除数
     * @return double   返回类型
     * @throws
     */
    private static double div(double v1, double v2) {
        return MathSupplier.div(v1, v2, 4);
    }
    /**
     * 提供精确的减法运算
     * @Title: sub
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param v1  被减数
     * @param v2  减数
     * @return double   返回类型
     * @throws
     */
    private static double sub(double v1, double v2) {
        return MathSupplier.sub(v1,v2);
    }
    /**
     * 重写Math.min方法
     */
    private static double min(double v1, double v2){
        return Math.min(v1,v2);
    }
    /**
     * 重写Math.round
     */
    private static double round(double v){
        return Math.round(v);
    }

    /**
     * 重写add
     * @param v1
     * @param v2
     * @return
     */
    private static double add(double v1,double v2){
        return MathSupplier.add(v1,v2);
    }
    /**
     * 重写Integer.parseInt()方法
     * @param str
     * @return
     */
    private static int strToInt(String str){
        if(StringUtils.isNotEmpty(str)){
            return Integer.parseInt(str);
        }
        return -1;
    }
    /**
     * 提供精确的乘法运算
     * @Title: mul
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param v1 被乘数
     * @param v2 乘数
     * @return double   返回类型
     * @throws
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        //return b1.multiply(b2).doubleValue();
        return  b1.multiply(b2).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double roude(double v1) {
        return MathSupplier.roude(v1);
    }
    public static void main(String[] args) {
        //System.out.println(medmin(div(4,0.059),div(2,0.706),div(7,0.235)));
        //System.out.println(mul(15,div(4,150)));
        //System.out.println(round(2.4259));
        System.out.println(div(0.8526,70));
    }
}



