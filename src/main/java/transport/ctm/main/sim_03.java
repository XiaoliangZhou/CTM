package transport.ctm.main;

import org.apache.commons.lang3.StringUtils;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.graph.*;
import transport.math.util.MathSupplier;

import java.util.*;

public class sim_03 {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    private static final int DEFAUTL_SCALE = 3;
    /*拥挤传播速度(m/sec)*/
    private static final double  ctm_w_vf = 6.0 ;
    /*自由流速度(m/sec)*/
    private static final double  vf = 13.8888889 ;
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
                switch(cc.c_type){
                    case 0:case 3 :case 4 :
                        /*当前元胞的驶入流率*/
                        cc.pos_in = min(cc.c_rate,mul(ctm_w_vf,(div(cc.c_cap,cc.c_lgth)-div(cc.c_n,cc.c_lgth))));
                        /*当前元胞的驶出流率*/
                        cc.pos_out =min(cc.c_rate, mul(vf,div(cc.c_n,cc.c_lgth)));

                        cc.real_in = 0.0;
                        cc.real_out = 0.0;
                        break ;
                    case 1:
                        cc.c_n = add(cc.c_n,cc.c_rate);
                        cc.pos_in = 0;
                        cc.pos_out = mul(vf,div(cc.c_n,cc.c_lgth));

                        cc.real_in = 0.0;
                        cc.real_out = 0.0;
                        break;
                    case 2:
                        cc = (orginCell)cc;

                        /*当前元胞的驶入流率*/
                        cc.pos_in = min(cc.c_rate,mul(ctm_w_vf,(div(cc.c_cap,cc.c_lgth)-div(cc.c_n,cc.c_lgth))));
                        /*当前元胞的驶出流率*/
                        cc.pos_out =min(cc.c_rate,mul(vf,div(cc.c_n,cc.c_lgth)));

                        cc.real_in = 0.0;
                        cc.real_out = 0.0;

                        ((orginCell) cc).f_l_in = 0.0;
                        break;
                }
            });
        }
        for (ctmLinks clk : clks) {
            if (clk.getAccess() == 0) {
                continue;
            }
            switch (clk.getType()) {
                case 0:
                    c1 = clk.getCells()[0]; /*车流传递*/
                    c2 = clk.getCells()[1];

                    ctmCell c10 = ctms.get(c1);
                    ctmCell c20 = ctms.get(c2);

                    f = min(c10.pos_out, c20.pos_in);

                    c10.real_out = f;
                    c20.real_in  = f;

                    N = Math.round(mul(f,dt));
                    if(f>0){
                        updateRoadVehicle(c10, c20,N);/*更新vehicle object*/
                    }
                    break;
                case 3:
                    c1 = clk.getCells()[0]; /*车流传递*/
                    c2 = clk.getCells()[1];

                    ctmCell c31 = ctms.get(c1);
                    orginCell c32 = (orginCell) ctms.get(c2);

                    f = min(c31.pos_out, c32.pos_in);

                    c31.real_out = f;
                    c32.real_in = f + c32.f_l_in;

                    N = Math.round(mul(f,dt));
                    if(f>0){
                        updateNodeVehicle(c31, c32,N);/*更新vehicle object*/
                    }
                    break;
                case 1:
                    /*合流*/
                    c1 = clk.getCells()[0];
                    c2 = clk.getCells()[1];
                    c3 = clk.getCells()[2];
                    b1 = clk.getKp()[0];
                    b2 = clk.getKp()[1];

                    endCell c11 = (endCell) ctms.get(c1);
                    endCell c12 = (endCell) getCtmCell(clk);
                    orginCell c13 = (orginCell) ctms.get(c3);

                    if (c13.pos_in >= c11.pos_out + c12.pos_out) {
                        f1 = c11.pos_out;
                        f2 = c12.pos_out;
                    } else {
                        f1 =roude(median(c11.pos_out, sub(c13.pos_in, c12.pos_out), b1 * c13.pos_in));
                        /*f2 = median(c21.pos_out, sub(c31.pos_in, c11.pos_out), b2 * c31.pos_in);*/
                        f2 = sub(c13.pos_in, f1);
                    }
                    c11.real_out = f1;
                    c12.real_out = f2;

                    c13.real_in = f1 + f2;
                    c13.f_l_in = f1 + f2;

                    /*重新更新元胞驶入流率(veh/sec)*/
                    updateRightFlow(c13);

                    N1 = Math.round(mul(f1,dt));
                    N2 = Math.round(mul(f2,dt));

                    updateMerVehicle1(c11,c12,c13,N1,N2);

                    break;
                case 2:
                    /*分流*/
                    int length = clk.getCells().length;
                    switch (length){
                        case 2:
                            c1 = clk.getCells()[0];
                            c2 = clk.getCells()[1];
                            if(clk.getFlag()==1){
                                /*单车道分流*/
                                secCell r1 = initCtm.getSecCell(ctms,c1);
                                endCell r2 = (endCell) ctms.get(c2);
                                f = min(r1.pos_out, r2.pos_in);

                                r1.real_out = f;
                                r2.real_in = f ;

                                N = Math.round(mul(f,dt));
                                /*单车道分流只有一个子元胞*/
                                if(f>0){
                                    updateSingleLaneVehicle(r1, r2,N);/*更新vehicle object*/
                                }
                            }else{
                                /*处理右转 、左转相位*/
                                b1 = clk.getKp()[0];
                                endCell r1 = initCtm.getEndCell(ctms,c1);
                                orginCell r2 =(orginCell) ctms.get(c2);

                                f = min(r1.pos_out, r2.pos_in);
                                r1.real_out = f;
                                r2.real_in = f ;
                                r2.f_l_in = f;

                                /*重新更新元胞驶入流率(veh/sec)*/
                                updateRightFlow(r2);
                                f = mul(f,dt);
                                updateSingleRoadVehicle(r1, r2,f);/*更新vehicle object*/

                            }
                            break;
                        case 3:
                            c1 = clk.getCells()[0];
                            c2 = clk.getCells()[1];
                            c3 = clk.getCells()[2];
                            b1 = clk.getKp()[0];
                            b2 = clk.getKp()[1];
                            secCell c211 = (secCell) ctms.get(c1);
                            endCell c221 = (endCell) ctms.get(c2);
                            endCell c231 = (endCell) ctms.get(c3);

                            if(b1!=0&&b2!=0){
                                f = roude(medmin(c211.pos_out, div(c221.pos_in,b1),div(c231.pos_in,b2)));
                            }
                            if(b1==0){
                                f = roude(min(c211.pos_out,c231.pos_in));
                            }
                            if(b2==0){
                                f = roude(min(c211.pos_out,c221.pos_in));
                            }

                            f1 = mul(f , b1);
                            f2 = sub(f,f1);

                            c211.real_out = f;
                            c221.real_in  = f1;
                            c231.real_in  = f2;

                            N1 = Math.round(mul(f1,dt));
                            N2 = Math.round(mul(f2,dt));

                            /*车辆传播*/
                            if(f>0){
                                updateDiVehicle1(c211,c221,c231,N1,N2);
                            }

                            break;
                        case 4:
                            /*十字型交叉口*/
                            c1 = clk.getCells()[0];
                            c2 = clk.getCells()[1];
                            c3 = clk.getCells()[2];
                            c4 = clk.getCells()[3];

                            b1 = clk.getKp()[0];/*左转*/
                            b2 = clk.getKp()[1];/*直行*/
                            b3 = clk.getKp()[2];/*右转*/

                            secCell c41 = (secCell) ctms.get(c1);
                            endCell c42 = (endCell) ctms.get(c2);
                            endCell c43 = (endCell) ctms.get(c3);
                            endCell c44 = (endCell) ctms.get(c4);
                            if (b1==0&b2==0&b3==0) {
                                f = 0.0;
                            }
                            if (b2==0&b3==0&b1>0) {
                                f = roude(min(c41.pos_out,div(c42.pos_in,b1)));
                            }
                            if(b1==0&b3==0&b2>0){
                                f = roude(min(c41.pos_out,div(c43.pos_in,b2)));
                            }
                            if(b1==0&b2==0&b3>0){
                                f = roude(min(c41.pos_out,div(c44.pos_in,b3)));
                            }
                            if(b2>0&b3>0&b2+b3==1){
                                f = roude(medmin(c41.pos_out,div(c43.pos_in,b2),div(c44.pos_in,b3)));
                            }
                            if(b1>0&b3>0&b1+b3==1){
                                f = roude(medmin(c41.pos_out,div(c42.pos_in,b1),div(c44.pos_in,b3)));
                            }
                            if(b1>0&b2>0&b1+b2==1){
                                f = roude(medmin(c41.pos_out,div(c42.pos_in,b1),div(c43.pos_in,b2)));
                            }
                            if(b1>0&b2>0&b3>0&b1+b2+b3==1){
                                fmin =medmin(div(c42.pos_in,b1),div(c43.pos_in,b2),div(c44.pos_in,b3));
                                f =roude(Math.min(c41.pos_out,fmin));
                            }

                            f1 = mul(f, b1);
                            f2 = mul(f, b2);
                            if(f1+f2>f&f>0){
                                f2 = sub(f,f1);
                            }
                            f3 = sub(f,add(f1,f2));

                            c41.real_out = f;
                            c42.real_in  = f1;
                            c43.real_in  = f2;
                            c44.real_in  = f3;

                            N1 = Math.round(mul(f1,dt));
                            N2 = Math.round(mul(f2,dt));
                            N3 = Math.round(mul(f3,dt));

                            /*车辆传播*/
                            if(f>0){
                                updateTTOfDivVehicle(c41,c42,c43,c44,N1,N2,N3);
                            }
                            break;
                    }
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
            ctm.c_n = ctm.c_n + mul(ctm.real_in,dt) - mul(ctm.real_out,dt);

            if(type==0||type==1||type==2){
                size = ctm.getC1().size();
                if(ctm.c_n != size){
                    System.out.println(" [CURSTEP:"+ step + ", ERROR CID:" + ctm.c_id + ", CTYPE:" + ctm.c_type +"]");
                }
            }
            if(type==3){
                endCell edc = (endCell) ctm;
                size = edc.getC1().size()+edc.getC2().size();
                if(ctm.c_n != size){
                    System.out.println(" [CURSTEP:"+ step + ", ERROR CID:" + ctm.c_id + ", CTYPE:" + ctm.c_type +"]");
                }
            }
            if(type==4){
                secCell sec = (secCell) ctm;
                size = sec.getC1().size()+stmSim.getVehicleCount(sec);
                if(sec.c_n != size){
                    System.out.println(" [CURSTEP:"+ step + ", ERROR CID:" + sec.c_id + ", CTYPE:" + sec.c_type +"]");
                }
            }
            if(ctm.c_type == 1) ctm.setC_rate(0.0);
        });
    }

    /**
     *   节点合流 获取C1 或者 C2
     *    C3----> -->---C2
     *         /
     *      C1/(R C1 路段首元胞)
     *
     *        |
     *        C3(路段起始元胞)
     *       / \
     *      /   C2(内部元胞)
     *     C1(内部元胞)
     *
     * @param clk
     * @return
     */
    protected static ctmCell getCtmCell(ctmLinks clk) {
        boolean fag = clk.getIntType()==4&&(clk.getCurPhase() == 2||clk.getCurPhase() == 4);
        ctmCell  c21 = null ;
        if(fag)  c21 = ctms.get(c2);
        if(!fag) c21 = initCtm.getEndCell(ctms,c2);
        return c21;
    }

    /**
     *
     * @param cc
     */
    private static void updateRightFlow(orginCell cc) {
        double posCn = cc.c_n + mul(cc.real_in,dt);
        cc.pos_in = min(cc.c_rate,mul(ctm_w_vf,div(sub(cc.c_cap,posCn),cc.c_lgth)));
    }
    /**
     *
     * (secCell)C1--->---->C2(endCell)
     * @param c1
     * @param c2
     * @param f
     */
    private static void updateSingleLaneVehicle(secCell c1, endCell c2,double f) {
        String r2;
        int idx=0;
        int N = (int)Math.round(f);
        if(N<=c1.getC_n()){
            r2 = c2.getC_dir();
            Iterator<Map.Entry<String, ArrayDeque<Vehicle>>> it = c1.getQueMap().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = it.next();
                String k = (String)entry.getKey();
                ArrayDeque<Vehicle> dq1 = (ArrayDeque<Vehicle>) entry.getValue(); /*排队车辆*/
                if(dq1.size()>0){
                    /*所有车辆按先进先出排成一队*/
                    if(k.equals(r2)){
                        while(idx<N){
                            offerQueue1(c1.getCanal(),c2,dq1);
                            idx++;
                        }
                    }
                }
                if(dq1.size()==0) it.remove();
            }
        }
    }
    /**
     * ---->C1()---->C2()--->
     * 更新交叉口车流
     * @param c1 endcell 路段末尾元胞
     * @param c2 orgincell 交叉口进口元胞
     * @param f
     */
    private static void updateSingleRoadVehicle(endCell c1, orginCell c2,double f) {
        Edge edge;
        double Time = 0.0,T= 0.0 ,Num = 0.0;

        edge = initCtm.getEdgeByEndCell(c1);/*获取元胞所在的路段对象*/
        T = edge.getTotalTime();
        Num = edge.getVehNumber();
        Map<Integer,Map<Integer,Integer>> gMaps = edge.getAvgMaps();
        int cnt=1;
        int N = (int)Math.round(f);
        int startIndex = 0;
        if(N<=c1.getC_n()){
            while (startIndex<N){
                double t=0.0;
                if(c1.getC2().size()>0){
                    Vehicle v = c1.getC2().poll();
                    if(v!=null){
                        v.setTravseTime(curStep-v.getExtanceTime());/*车辆通过路段时间*/
                        Time+=v.getTravseTime();
                       if(gMaps.get(v.getExtanceTime())== null){
                            Map<Integer,Integer> tMap = new HashMap<>();
                            tMap.put(cnt,v.getTravseTime());
                            gMaps.put(v.getExtanceTime(),tMap);
                        }else{
                            Map<Integer,Integer> sMap = gMaps.get(v.getExtanceTime());
                            int c=0,k =0,vl=0;
                            for(Map.Entry<Integer, Integer> entry : sMap.entrySet()){
                                k = entry.getKey();
                                vl = entry.getValue();
                                c=k;
                                c++;
                                vl+=v.getTravseTime();
                            }
                            sMap.put(c,vl);
                            sMap.remove(k);
                        }
                        v.setCurCellPosition(c2.getC_id());
                        v.setCurPosLane(c2.getLaneId());
                        v.setExtanceTime(curStep);
                        c2.c1.offer(v);
                    }
                }
                startIndex++;
            }
        }
        Num+=N;
        T+=Time;
        edge.setTotalTime(T);
        edge.setVehNumber(Num);

    }

    /**
     * ---->C1---->C2--->
     * 更新路段车流
     * @param c1
     * @param c2
     * @param f
     */
    private static void updateNodeVehicle(ctmCell c1, orginCell c2,double f) {
        int N = (int)Math.round(f);
        int startIndex = 0;
        if(N<=c1.getC_n()){
            while (startIndex<N){
                if(c1.getC1().size()>0){
                    Vehicle v = c1.getC1().poll();
                    if(v!=null){
                        v.setCurCellPosition(c2.getC_id());
                        v.setCurPosLane(c2.getLaneId());
                        c2.getC1().offer(v);
                    }
                }
                startIndex++;
            }
        }
    }


    /**
     * ---->C1---->C2--->
     * 更新路段车流
     * @param c1
     * @param c2
     * @param f
     */
    private static void updateRoadVehicle(ctmCell c1, ctmCell c2,double f) {
        int N = (int)Math.round(f);
        int startIndex = 0;
        if(N<=c1.getC_n()){
            while (startIndex<N){
                if(c1.getC1().size()>0){
                    Vehicle v = c1.getC1().poll();
                    if(v!=null){
                        v.setCurCellPosition(c2.getC_id());
                        v.setCurPosLane(c2.getLaneId());
                        c2.getC1().offer(v);
                    }
                }
                startIndex++;
            }
        }
    }

    /**
     * 十字型交叉口路段尾元胞分流
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     */
    private static void updateTTOfDivVehicle(secCell c1,
                                             endCell c2,
                                             endCell c3,
                                             endCell c4,double f1,double f2,double f3) {
        /*获取当前路段*/
        Edge e;
        Vehicle v ;
        int N1 = (int)f1,N2 = (int)f2,N3 = (int)f3;
        String r1 ,r2, r3, r4;/*车辆转向*/
        Canal cal = c1.getCanal();
        int idx0 = 0 , idx1 = 0 ,idx2 = 0 ;

        r2 = c2.getC_dir();
        r3 = c3.getC_dir();
        r4 = c4.getC_dir();


        Iterator<Map.Entry<String,ArrayDeque<Vehicle>>> it = c1.getQueMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = it.next();
            String k = (String)entry.getKey();
            ArrayDeque<Vehicle> dq1 = (ArrayDeque<Vehicle>) entry.getValue(); /*排队车辆*/
            if(dq1.size()>0){
                if(k.equals(r2)){
                    while(idx0<N1){
                        offerQueue1(cal,c2,dq1);
                        idx0++;
                    }
                }
                if(k.equals(r3)){
                    while(idx1<N2){
                        offerQueue1(cal,c3,dq1);
                        idx1++;
                    }
                }
                if(k.equals(r4)){
                    while(idx2<N3){
                        offerQueue1(cal,c4,dq1);
                        idx2++;
                    }
                }
            }
            if(dq1.size()==0) it.remove();
        }
    }

    /**
     *
     * @param c1
     * @param c2
     * @param c3
     * @param f1
     * @param f2
     */
    private static void updateMerVehicle1(endCell c1, endCell c2, orginCell c3, double f1, double f2) {
        if (f1==0&f2!=0) {
             updateSingleRoadVehicle(c2,c3,f2);
        }else if(f1!=0&f2==0) {
             updateSingleRoadVehicle(c1,c3,f1);
        }else if (f1!=0&f2!=0) {
             updateDoubleMerVehicle1(c1,c2,c3,f1,f2);
        }
    }

    /**
     * 交叉口元胞合流
     * @param c1
     * @param c2
     * @param c3
     * @param f1
     * @param f2
     */
    private static void updateDoubleMerVehicle1(endCell c1, endCell c2, orginCell c3, double f1, double f2) {
        int r1 ,r2 ;
        r1 = strToInt(c1.getC_dir());
        r2 = strToInt(c2.getC_dir());

        if((r1==0 & r2==2) || (r1==0 & r2==1)){
            /*(S,R)直行优先、(S,L)直行优先*/
            updateSingleRoadVehicle(c1,c3,f1);
            updateSingleRoadVehicle(c2,c3,f2);
        }
        if(r1==1 & r2==2){
            /*(L,R)右转优先*/
            updateSingleRoadVehicle(c2,c3,f2);
             updateSingleRoadVehicle(c1,c3,f1);
        }
    }

    /**
     *
     * @param c1
     * @param c2
     * @param c3
     * @param f1
     * @param f2
     */
    private static void updateDiVehicle1(secCell c1,
                                         endCell c2,
                                         endCell c3, double f1,double f2) {
        /*获取当前路段*/
        Edge e;
        Ramp ramp;/*匝道对象*/
        Vehicle v ;
        String r1 ,r2, r3 ;/*车辆转向*/
        int idx0 = 0 , idx1 = 0 ,idx2 =0;
        Canal cal = c1.getCanal();
        int N1 = (int)f1 ,N2 = (int)f2;

        r2 = c2.getC_dir();
        r3 = c3.getC_dir();

        Iterator<Map.Entry<String,ArrayDeque<Vehicle>>> it = c1.getQueMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = it.next();
            String k = (String)entry.getKey();
            ArrayDeque<Vehicle> dq1 = (ArrayDeque<Vehicle>) entry.getValue(); /*排队车辆*/
            if(dq1.size()>0){
                if(k.equals(r2)){
                    if(dq1.size()<N1){
                        System.out.println("error!" + dq1.size() +"--N1 "+ N1);
                    }
                    while(idx0<N1){
                        offerQueue1(cal,c2,dq1);
                        idx0++;
                    }
                }
                if(k.equals(r3)){
                    if(dq1.size()<N2){
                        System.out.println("error!" + dq1.size() +"--N2 "+ N2);
                    }
                    while(idx1<N2){
                        offerQueue1(cal,c3,dq1);
                        idx1++;
                    }
                }
            }
            if(dq1.size()==0) it.remove();
        }
    }
    /**
     * 节点车辆更新
     * @param c
     * @param dq
     */
    private static void offerQueue1(Canal cal,ctmCell c, ArrayDeque<Vehicle> dq) {
        Vehicle v;
        if(dq.size()>0){
            v = dq.peek();
            if(v!=null){
                v = dq.poll();
                c.getC1().offer(v);  /*进入相应队列*/
                v.setCurCellPosition(c.c_id);
                v.setCurPosLane(c.getLaneId()); /*车辆所在路段位置*/

                List<String> ds = new ArrayList<>();
                LinkedList<DefaultWeightLabelEdge> lds = v.getDefaultGraphPath().getPaths();
                lds.forEach((de)->{
                    ds.add(de.getLabel());
                });
                if(!ds.contains(c.getLaneId())){
                    System.out.println();
                }
                updateCanal(cal, v);
            }
        }
    }

    /**
     *
     * @param v
     * @param lds
     */
    private static void restVehPath(Vehicle v, String lds) {
        LinkedList dphs = v.getDefaultGraphPath().getPaths();
        Iterator<DefaultWeightLabelEdge> dls = dphs.iterator();
        while (dls.hasNext()){
            DefaultWeightLabelEdge  dfed = dls.next();
            if(dfed.getLabel().equals(lds)){
                dls.remove();
            }
        }
    }

    /**
     * 更新分流比例
     * @param cal
     * @param v
     */
    private static void updateCanal(Canal cal, Vehicle v) {
        String vster = v.getSteer();
        if(vster.equals("0")) cal.setS_n(cal.getS_n()-1);
        if(vster.equals("2")) cal.setR_n(cal.getR_n()-1);
        if(vster.equals("1")) cal.setL_n(cal.getL_n()-1);
        if(vster.equals("3")) cal.setD_n(cal.getD_n()-1);
        cal.setT_n(cal.getT_n());

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
        return MathSupplier.mul(v1,v2);
    }
    public static double roude(double v1) {
        return MathSupplier.roude(v1);
    }
    public static void main(String[] args) {
        //System.out.println(medmin(div(4,0.059),div(2,0.706),div(7,0.235)));
        System.out.println(mul(15,div(4,150)));
    }
}



