package transport.ctm.main;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.file.util.fileutils;
import transport.graph.*;
import transport.math.util.CellHandle;
import transport.math.util.MathSupplier;
import transport.math.util.randomSupplier;

import javax.management.StringValueExp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static transport.ctm.main.buildCtm.setIntPhase;

public class CtmTest {
    private static final String CPATH = "D:\\v.txt" ;
    private static final String CPATH1 = "D:\\netv.txt" ;
    private static  List ctms = constant.ctms;
    static List<Garage> lg = constant.lg;
    private static final int row  = 27;
    private static final int col  = 27;
    loadVehMap[][] odds = new loadVehMap[row][col];
    int[][] odd = CellHandle.zeros(row,col);
    List dd = constant.ctmls;

    @Test()
    public void testMain() throws IOException,Exception {
        int samplePeriod = 1;
        int sample= 1;
        int timToStep = 200;
        double maxTdf= 0.51;
        int maxStep = 600;
        int step = 1;
        int cycle = 101;
        double tdf = 0.01;
        double[][] avgs = new double[50][85];
        double[][] vgs = new double[60][85];
        double[][] cgs = new double[60][85];
        double[][] dgs = new double[60][85];
        double[][] hgs = new double[600][85];
        double[] ngs = new double[60];
        try {
            /*初始化路网*/
            loadNetworkData();
            /*初始化交叉口信息*/
            fileutils.loadIntData();
            /*相位设置、初始化*/
            setIntPhase(ctms);
            /*创建车库*/
            creatVehicleGarage(lg);
            int n=0,k=1;
            while(tdf<maxTdf){
                while(sample<=samplePeriod){
                    setPoissionDistrCar(tdf,odds); /*以泊松分布产生车辆*/
                    /*单源最短路径*/
                    DefaultGraphPath[][] gp = IniteGraph.AlgorithmOfShortestPath();
                    while (step<maxStep) {
                        /*交通流加载*/
                        //loadVehicle1(step,odds,gp);
                        loadVehicle(tdf,step,odd,gp);
                        /*车流传播*/
                        sim_03.ctmSimulat(step);
                        /*相位转换*/
                        stmSim.switchPhase(step);
                        /*fffff*/
                        //stmSim.roadInfoReady(step,ctms,lg);
                       /* if(step%10==0){
                            for (Edge le : constant.ctmls) {
                                int lid = Integer.parseInt(le.getLabel())-1;
                                double savg =  MathSupplier.div(le.getTempAvg(),step);
                                double avt =  MathSupplier.div(le.getPeriodAvg(),step);
                                vgs[k-1][lid]+= savg;
                                cgs[k-1][lid]+= avt;
                            }
                            //System.out.println(step);
                            ++k;
                        }*/
                        ++step;
                    }
                    k=1;
                    step=1;
                    ++sample;
                    cleanNetwokData();
                }
                int index = 50;
                Map<Integer,Double> gMaps ;
                for (Edge le : constant.ctmls) {
                     int lid = Integer.parseInt(le.getLabel())-1;
                     gMaps = getGMap(le);/*重新封装data*/
                     for(Map.Entry<Integer, Double> entry : gMaps.entrySet()){
                         System.out.println(entry.getKey()+"------"+ entry.getValue());
                     }
                     int timStep =1;
                     while (timStep<maxStep){
                         index=timStep;
                         Double vMaps = gMaps.get(index);
                         if(vMaps==null){
                            while (index>0){
                                index--;
                                Double sValue = gMaps.get(index);
                                if(sValue!=null){
                                    gMaps.put(timStep,Math.max(le.getFreeTime(),sValue-1));
                                    break;
                                }
                            }
                        }
                        if(gMaps.get(timStep)==null){
                            hgs[timStep-1][lid] = le.getFreeTime();
                        }else {
                            hgs[timStep-1][lid] = gMaps.get(timStep);
                        }
                        timStep++;
                    }
                    gMaps.clear();
                }
                double f0 = 0.0;
                for (int i = 0; i < hgs[0].length; i++) {
                    for (int j = 0; j < hgs.length; j++) {
                        if(j!=0&j%10!=0) f0+=hgs[j][i];
                        if(j!=0&j%10==0){
                            dgs[j/10-1][i] = MathSupplier.div(f0,10);
                            f0=0;
                        }
                        if(j==hgs.length - 1){
                            dgs[(j+1)/10-1][i] = MathSupplier.div(f0,10);
                        }
                    }
                }
                 /* for (int i = 0; i < vgs.length; i++) {
                    for (int j = 0; j < vgs[i].length; j++) {
                        double vc = MathSupplier.div(vgs[i][j],samplePeriod);
                        double vt = MathSupplier.div(cgs[i][j],samplePeriod);
                        vgs[i][j]=vc;
                        cgs[i][j]=vt;
                    }
                }*/
                sample=1;
                tdf=MathSupplier.add(tdf,0.01);
                ++n;
                /*考察时间段路段平均行程速度*/
                //getLaneAvgSpeed(samplePeriod, timStep, timToStep, avgs, n);
                /*清除路段数据*/
                netwokDataClear();
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        /*输出数据*/
        //printData(avgs);
        //printData(vgs);
        /*计算整体网络平均行程速度随仿真时步的变化曲线*/
        double nv=0.0;
        for (int i = 0; i < vgs.length; i++) {
            double cv=0.0 ;
            for (int j = 0; j < vgs[i].length; j++) {
                cv+= vgs[i][j];
            }
            ngs[i] = MathSupplier.div(cv,vgs[i].length);
        }
        //fileutils.writeToTxtBypath(CPATH,vgs);
        //fileutils.writeToTxtBypath(CPATH1,cgs);
        fileutils.writeToTxtBypath(CPATH1,hgs);
    }


    /**
     *
     * @return
     */
    protected List<Vertex> setNetVertex() {
        Vertex v = null;
        List<Vertex> ols = new ArrayList<Vertex>();
        for (int j = 1; j < 28; j++) {
            v = new Vertex(String.valueOf(j));
            ols.add(v);
        }
        return ols;
    }
    protected void creatVehicleGarage(List<Garage> lg) {
        Garage gg = null;
        for (int i = 0; i < 28; i++) {
            gg = new Garage();
            gg.setIntId(String.valueOf(i+1));
            lg.add(gg);
        }
    }

    protected  Map<Integer,Double> getGMap(Edge le){
        Map<Integer,Double> gMaps ;
        gMaps = le.getgMaps();
        Map<Integer,Map<Integer,Integer>> hMaps = le.getAvgMaps();
        for(Map.Entry<Integer, Map<Integer,Integer>> entry : hMaps.entrySet()){
            for(Map.Entry<Integer,Integer> maps :entry.getValue().entrySet()){
                /*  if(le.getLabel().equals("4")){
                      System.out.println(entry.getKey()+"------"+ maps.getValue()+"------"+maps.getKey());
                    }*/
                gMaps.put(entry.getKey(),MathSupplier.div(maps.getValue(),maps.getKey()));
            }
        };
        hMaps.clear();
        return gMaps;
    }

    /**
     *
     * @param samplePeriod
     * @param timStep
     * @param timToStep
     * @param avgs
     * @param n
     */
    @Deprecated
    protected void getLaneAvgSpeed(int samplePeriod, int timStep, int timToStep, double[][] avgs, int n) {
        for (Edge le : constant.ctmls) {
            int Index = Integer.parseInt(le.getLabel()) - 1;
            double pAvg = le.getPeriodAvg();
            /*算术平均*/
            double avgss = MathSupplier.
                    div(pAvg, MathSupplier.mul(timToStep - timStep + 1, samplePeriod));
            avgs[n - 1][Index] = avgss;
        }
    }

    /**
     * 加载路网数据
     * @return
     * @throws Exception
     */
    protected void loadNetworkData() throws Exception  {
        /*路网数据*/
        String path = fileutils.class.getClassLoader().getResource("sf_tab_5.txt").getPath();
        fileutils.readGraphDataFromTxtByPath(setNetVertex(), path);
        /*路网模型*/
        IniteGraph.setNetwork();
    }


    protected void printData(double[][] avgs) {
        for (int i = 0; i < avgs.length; i++) {
            for (int j = 0; j < 15; j++) {
                if(i==0&&j==0){
                    for (int k = 0; k < 15; k++) {
                        System.out.printf("%-10s",k+1);
                    }
                    System.out.println();
                }
                System.out.printf("%-10s",avgs[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 网络数据清洗
     */
    public static void netwokDataClear(){
        /*初始化路段*/
        restRoadData();
        /*初始化相位*/
        buildCtm.initiazePhase();
    }
    public static void cleanNetwokData(){
        /*初始化路段*/
        restRoadData1();
    }

    public static void restRoadData(){
        List<Edge> les = constant.ctmls;
        for (Edge e : les) {
            //clearEdc(e);
            e.setPeriodAvg(0.0);
            e.setTravleTime(e.getFreeTime());
            e.setWeight(e.getFreeTime());
        }
//       ctms.forEach((cc)->{
//            ctmCell c = (ctmCell)cc;
//            c.setC_n(0);
//            c.setPos_in(0.0);
//            c.setPos_out(0.0);
//            c.setF_l_in(0.0);
//            c.getClv().clear();
//        });
    }

    public static void restRoadData1(){
        List<Edge> les = constant.ctmls;
        for (Edge e : les) {
            clearSecAndEndCell(e);
            e.setTempAvg(0.0);
            e.setTravleTime(e.getFreeTime());
            e.setWeight(e.getFreeTime());
        }
        ctms.forEach((cc)->{
            ctmCell c = (ctmCell)cc;
            if(c.c_type==2){
                orginCell c1 = (orginCell)c;
                c1.setF_l_in(0.0);
            }
            c.setC_n(0);
            c.setPos_in(0.0);
            c.setPos_out(0.0);
            c.getC1().clear();
        });

    }
    /**
     * 清除尾元胞
     */
    protected static void clearSecAndEndCell(Edge e) {
        int sec;
        Canal cal;
        endCell edc;
        secCell stc;

        sec = Math.addExact(e.getInCell(), e.getCellNum()) - 1;
        stc = initCtm.getSecCell(ctms, sec);

        stc.setC_n(0);
        stc.setPos_in(0.0);
        stc.setPos_out(0.0);
        stc.setCur_alow_out(0.0);
        stc.getC1().clear();
        stc.getQueMap().clear();

        cal = stc.getCanal();
        cal.setS_n(0);
        cal.setT_n(0);
        cal.setR_n(0);
        cal.setL_n(0);
        cal.setD_n(0);

        for (Map.Entry<String, Integer> entry : e.getDcMap().entrySet()) {
            Integer c = entry.getValue();
            edc = initCtm.getEndCell(ctms, c);
            edc.setC_n(0);
            edc.setPos_in(0.0);
            edc.setPos_out(0.0);
            edc.getC1().clear();
            edc.getC2().clear();
        }
    }


    /**
     * 交通流加载
     * @param tdf
     * @param step
     * @param ods
     * @param gp
     */
    protected void loadVehicle(double tdf, int step, int[][] ods, DefaultGraphPath[][] gp) {
        int loadCar =  (int)(MathSupplier.mul(tdf,100));
        int jsd = MathSupplier.divide(100,loadCar);
        if(step<100+1){
            if (step % jsd == 0) {
                //stmSim.networkFlowLoading(step, ods, gp,ctms);
            }
        }
    }

    /**
     *
     * @param step
     * @param vMap
     * @param gp
     * @throws Exception
     */
    protected void loadVehicle1(int step,loadVehMap[][] vMap,DefaultGraphPath[][] gp) throws Exception {
        if(vMap!=null&gp!=null){
            //stmSim.networkFlowLoading1(step, vMap, gp,ctms);
        }
    }

    /**
     * 车辆产生服从泊松分布
     * @param tdf
     * @param odds
     */
    protected void setPoissionDistrCar(double tdf,loadVehMap[][] odds) {
        int loadCar =  (int)(MathSupplier.mul(tdf,100));
        loadVehMap odd;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(i!=j){
                    odd = randomSupplier.randmsetCar(loadCar);
                    odds[i][j] = odd;
                }
            }
        }
    }

    //@Test
    public void test1(){
        for (int j = 0; j < 30; j++) {
            for (int i = 0; i < 13; i++) {
                if(i==0&&j==0){
                    for (int k = 0; k < 13; k++) {
                        System.out.printf("%-10s",k+1);
                    }
                    System.out.println();
                }
                System.out.printf("%-10s","abc");
            }
            System.out.println();

        }

    }

}

