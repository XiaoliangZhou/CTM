package transport.ctm.main;

import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.file.util.fileutils;
import transport.graph.*;
import transport.math.util.CellHandle;
import transport.math.util.MathSupplier;
import transport.math.util.randomSupplier;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static transport.ctm.main.buildCtm.setIntPhase;

public  class simAaction   {

    static final int row  = 27;
    static final int col  = 27;
    static List ctms = constant.ctms;
    static List<Garage> lg = constant.lg;
    static final String CPATH = "D:\\v.txt" ;
    static final String CPATH1 = "D:\\netv.txt" ;
    static final List<Integer> ods = new ArrayList<>();
    static int[][] odd = CellHandle.zeros(row,col);
    static loadVehMap[][] odds = new loadVehMap[row][col];
    List dd = constant.ctmls;


    public static void start() throws Exception{
         int samplePeriod = 5;
         int sample= 1;
         int timsStep=25;
         int timToStep = 74;
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
                    setUniformDistrCar(tdf,ods);
                    while (step<maxStep) {
                        /*单源最短路径*/
                        DefaultGraphPath[][] gp = IniteGraph.AlgorithmOfShortestPath();
                        /*交通流加载*/
                        //loadVehicle1(step,odds,gp);
                       // loadVehicle(step, cycle, odd, gp);
                        /*车流传播*/
                        sim_03.ctmSimulat(step);
                        /*相位转换*/
                        stmSim.switchPhase(step);
                        /*fffff*/
                        stmSim.roadInfoReady(step, ctms, lg);
                        if(step%10==0){
                            for (Edge le : constant.ctmls) {
                                int lid = Integer.parseInt(le.getLabel())-1;
                                double savg =  MathSupplier.div(le.getTempAvg(),step);
                                double avt =  MathSupplier.div(le.getPeriodAvg(),step);
                                vgs[k-1][lid]+= savg;
                                cgs[k-1][lid]+= avt;
                            }
                            System.out.println(step);
                            ++k;
                        }
                        ++step;
                        //System.out.println("step=" + step);
                    }
                    k=1;
                    step=1;
                    ++sample;
                    cleanNetwokData();
                }
               /* int index = 50;
                Map<Integer,Double> gMaps ;
                for (Edge le : constant.ctmls) {
                    int lid = Integer.parseInt(le.getLabel())-1;
                    gMaps = getGMap(le);*//*重新封装data*//*
                   *//* for(Map.Entry<Integer, Double> entry : gMaps.entrySet()){
                        System.out.println(entry.getKey()+"------"+ entry.getValue());
                    }*//*
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
                }*/
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
                System.out.println("tdf=" + tdf);
                ++n;
                /*考察时间段路段平均行程速度*/
                //getLaneAvgSpeed(samplePeriod, timsStep, timToStep, avgs, n);
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
        //fileutils.writeToTxtBypath(CPATH,avgs);
        fileutils.writeToTxtBypath(CPATH,vgs);
        //fileutils.writeToTxtBypath(CPATH1,cgs);
        //fileutils.writeToTxtBypath(CPATH1,hgs);
    }

    protected static int excuteSimution(int step, int cycle, double tdf, DefaultGraphPath[][] gp) {
        /*交通流加载*/
        //loadVehicle1(step,odds,gp);
        //loadVehicle(tdf,step,odd,gp);
        /*车流传播*/
        sim_03.ctmSimulat(step);
        /*相位转换*/
        stmSim.switchPhase(step);
        /*fffff*/
        //stmSim.roadInfoReady(step,ctms,lg);
        ++step;
        System.out.println("step="+ step);
        return step;
    }
  public void start(Runnable run){
    }

    /**
     *
     * @return
     */
    protected static List<Vertex> setNetVertex() {
        List<Vertex> ols = new ArrayList<Vertex>();
        Vertex v9 = new Vertex("9");
        ols.add(v9);
        Vertex v10 = new Vertex("10");
        ols.add(v10);
        Vertex v11 = new Vertex("11");
        ols.add(v11);
        Vertex v15 = new Vertex("15");
        ols.add(v15);
        Vertex v16 = new Vertex("16");
        ols.add(v16);
        return ols;
    }
    public static void creatVehicleGarage(List<Garage> lg) {
        Garage gg = null;
        for (int i = 0; i < 28; i++) {
            gg = new Garage();
            gg.setIntId(String.valueOf(i+1));
            lg.add(gg);
        }
    }

    /**
     *
     * @param le
     * @return
     */
    protected static  Map<Integer,Double> getGMap(Edge le){
        Map<Integer,Double> gMaps ;
        gMaps = le.getgMaps();
        Map<Integer,Map<Integer,Integer>> hMaps = le.getAvgMaps();
        for(Map.Entry<Integer, Map<Integer,Integer>> entry : hMaps.entrySet()){
            for(Map.Entry<Integer,Integer> maps :entry.getValue().entrySet()){
                  /*if(le.getLabel().equals("15")){
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
     * @param tdf
     */
    @Deprecated
    protected static void getLaneAvgSpeed(int samplePeriod, int timStep, int timToStep, double[][] avgs, double tdf) {
        int rowindex = (int)(tdf*100);
        System.out.println("rowindex="+rowindex);
        for (Edge le : constant.ctmls) {
            int Index = Integer.parseInt(le.getLabel());
            double pAvg = le.getPeriodAvg();
            /*算术平均*/
            double avgss = MathSupplier.
                    div(pAvg, MathSupplier.mul(timToStep - timStep + 1, samplePeriod));
            avgs[rowindex - 1][Index] = avgss;
        }
    }
    /**
     * 加载路网数据
     * @return
     * @throws Exception
     */
    protected static void loadNetworkData() throws Exception  {
        /*路网数据*/
        String path = fileutils.class.getClassLoader().getResource("sf_tab_6.txt").getPath();
        fileutils.readGraphDataFromTxtByPath(setNetVertex(), path);
        /*路网模型*/
        IniteGraph.setNetwork();
        /*初始化路段输入流量*/
    }
    public static void initLoadMap(){

    }
    /**
     * 打印数据
     * @param avgs
     */
    protected static void printData(double[][] avgs) {
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

    private static void restRoadData(){
        List<Edge> les = constant.ctmls;
        for (Edge e : les) {
            e.setPeriodAvg(0.0);
            e.setTravleTime(e.getFreeTime());
            e.setWeight(e.getFreeTime());
            e.setEtmp(0.0);


          /*  for (Map.Entry<Integer, Double> entry : e.getSteerMaps().entrySet()) {
                e.getSteerMaps().put(entry.getKey(),0.0);
            }*/
          /*  for (Map.Entry<String, Integer> entry : e.getReachMaps().entrySet()) {
                e.getReachMaps().put(entry.getKey(),0);
            }*/
        }
        /*clear btmaps*/
        for (Map.Entry<String, Double> entry : constant.btMaps.entrySet()) {
            constant.btMaps.put(entry.getKey(),0.0);
        }
        /*clear btmaps*/
        for (Map.Entry<String, Double> entry : constant.cosMaps.entrySet()) {
            constant.cosMaps.put(entry.getKey(),0.0);
        }
    }

    public static void restRoadData1(){
        List<Edge> les = constant.ctmls;
        for (Edge e : les) {
            //clearSecAndEndCell(e);
            e.setTempAvg(0.0);
            e.setTravleTime(e.getFreeTime());
            e.setWeight(e.getFreeTime());
            e.setEcty(0);
        }
        ctms.forEach((cc)->{
            ctmCell c = (ctmCell)cc;
            if(c.c_type==2){
                orginCell c1 = (orginCell)c;
                c1.setF_l_in(0.0);
            }
            c.setC_n(0);
            c.setReal_in(0);
            c.setReal_out(0);
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
     *
     * @param step
     * @throws Exception
     */
    protected static void loadVehicle1(int step,Map<String,Double> loadMaps) throws Exception {
        stmSim.networkFlowLoading100(step, loadMaps,ctms);
    }

    /**
     * 车辆产生服从泊松分布
     * @param tdf
     * @param odds
     */
    protected static void setPoissionDistrCar(double tdf,loadVehMap[][] odds) {
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
    /**
     * 车辆产生服从均匀分布
     * @param tdf
     * @param odCars
     */
    protected static void setUniformDistrCar(double tdf,List<Integer> odCars) {
        int loadCar =  (int)(MathSupplier.mul(tdf,100));
        odCars = randomSupplier.randomCommon(1,100,loadCar);
    }

    public static void main(String[] args) throws Exception {
        start();
    }

}
