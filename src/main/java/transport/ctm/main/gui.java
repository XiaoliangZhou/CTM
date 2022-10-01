package transport.ctm.main;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import transport.ctm.model.intersection;
import transport.ctm.util.constant;

import transport.file.util.fileutils;
import transport.graph.*;
import transport.math.util.MathSupplier;
import transport.math.util.randomSupplier;


import java.awt.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;


import javax.swing.*;

import static transport.ctm.main.buildCtm.*;
import static transport.ctm.main.simAaction.*;
import static transport.ctm.main.stmSim.roadInfoReady;
import static transport.ctm.main.stmSim.switchPhase;
import static transport.file.util.fileutils.loadIntData;

public class gui {

    static final int row1  = 27;
    static final int col1  = 27;
    static List ctms = constant.ctms;
    static List<Garage> lg = constant.lg;
    static final String CPATH = "D:\\v.txt" ;
    static final String EDGE_CENT_CTPATH = "D:\\e_btc.txt" ;
    static final String VETEX_CENT_CTPATH = "D:\\v_btc.txt" ;
    static final String VETEX_COS_CTPATH = "D:\\v_cos.txt" ;
    static final String CPATH1 = "D:\\netv.txt" ;

    static final Map<String,Double> loadMaps = new HashMap<>();
    static List<Edge> edges = constant.ctmls;

    static int samplePeriod = 1;
    static int sample = 1;
    static int timsStep =10;
    static int timToStep = 200;
    static double maxTdf =0.11;
    static int maxStep =112 ;
    static  int step = 1;
    static int cycle = 101;
    static double tdf = 0.10;
    static int baseCycle = 16;

    static int popSize =50;
    static int a = 3, b = 6;
    static double yta1 = 20;
    static double yta2 = 20;
    static double pc=1.0;
    static double pm=1/100;
    static int maxGen=100;
    static int tour = 2;
    static int x_num = 4;

    private gui_2 gui2 = new gui_2();
    private List<ArrayList<IntegerSolution>> rankedSubPopulations;
    private transport.math.util.randomSupplier randomSupplier = new randomSupplier();
    private  double lowerBound = 0;
    private  double upperBound = 1;

  /*  static double[][] avgs = new double[50][85];
    static double[][] avgs1 = new double[50][85];
    static double[][] vgs = new double[72][85];
    static double[][] cgs = new double[72][85];
    static double[][] dgs = new double[60][85];
    static double[][] hgs = new double[600][85];
    static double[] ngs = new double[60];*/
    static DefaultGraphPath[][] gp = new DefaultGraphPath[row1][col1];

    private static Container contentPane;
    private JButton btn1;
    private JButton btn2;
    private static JButton btn3;
    private JLabel label1;
    private int row=52;
    private int col=39;
    private int [][]pa;
    private static JPanel p,p1,p2;
    private int num=0;
    private static JButton[][] btns;
    private static JLabel[][] labels;
    private int number[][];
    private static Graphics2D jg;

    static Thread thread1 = null;
    static Thread thread2 = null;
    static boolean suspended1=false;
    static boolean suspended2=false;

    /**
     *
     */
    public  void simDataReadyInfo1() throws Exception{
        /*路网数据加载*/
        try {
            /*初始化路网*/
            loadNetworkData();
        }catch (Exception e2 ){e2.printStackTrace(); }
        /*初始化交叉口信息*/
        loadIntData();
        /*相位设置、初始化*/
        setIntPhase(ctms);
        /*创建车库*/
        creatVehicleGarage(lg);
        /*加载交叉口进口道输入流量*/
        initLoadMap1();

        while (sample <= samplePeriod) {
            /*************************************种群初始化 开始*******************************/
            List<IntegerSolution> chrs = constant.chrs;
            for (int i = 0; i < chrs.size(); i++) {
                IntegerSolution chr = chrs.get(i);
                stmSingleCycle(chr);
            }

             /*************************************种群初始化 结束********************************/
            int popSize = chrs.size();
            /*非支配排序*/
            gui2.computeRanking(chrs);
            /*拥挤度计算*/
            chrs = gui2.crowdingDistance(chrs.size());
            int gstep=0;
            while (gstep<maxGen){
                /*二进制竞赛选择(k取了pop/2，所以选两次)*/
                List<IntegerSolution> chds = tournamentSelection(chrs,tour);
                /*模拟二进制交叉与多项式变异*/
                List<IntegerSolution> off_sprs =  SBXAndMutation(chds);
                /*精英保留*/
                System.out.println(gstep);
                if(gstep%100==0){
                    System.out.println(gstep + "gen is completed!");
                }
                chrs = saveStrategyOfElite(popSize,chrs,off_sprs);
                gstep++;
            }
            chrs.forEach((c)->{
                System.out.println(c.getObjective()[0]+"    "+c.getObjective()[1]+"   "+c.getX());
            });

            ++sample;
        }

        sample = 1;
        /*清除路段数据*/
        netwokDataClear();

    }
    /**
     *
     *@ 二进制竞标赛
     * @param chrs
     * @return
     */
    public static List<IntegerSolution> binaryTournamentSelection(List<IntegerSolution> chrs) throws Exception{
        int length = chrs.size();
        int n = MathSupplier.divide(length, 2);
        List<IntegerSolution> newchrs1 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String cId="";
            Integer[] cIndex = findDiffChromIndex(chrs);/*随机选取两个父代个体、保证选出的是不同个体*/

            IntegerSolution c1 = chrs.get(cIndex[0]);
            IntegerSolution c2 = chrs.get(cIndex[cIndex.length - 1]);

            int rk1 = c1.getRank();
            int rk2 = c2.getRank();

            if (rk1!=rk2) {
                cId = (rk1 < rk2) ? c1.getId() : c2.getId();
            }
            /*如果等级相同，则按拥挤度排序，拥挤度大的被选中*/
            if(rk1==rk2){
                double nd1 = c1.getNd();
                double nd2 = c2.getNd();
                if (nd1 != nd2) cId = (nd1 > nd2) ? c1.getId() : c2.getId();
                if (nd1 == nd2) cId = (Math.random() > 0.5) ? c1.getId() : c2.getId();/*等级相同、拥挤度相同 则随机选择其中一个*/
            }
            newchrs1.add((IntegerSolution) getChromeById(chrs,cId).clone());
        }
        return newchrs1;
    }
    /**
     *
     * @param chrs
     * @param Id
     * @return
     */
    public static IntegerSolution getChromeById(List<IntegerSolution> chrs,String Id){
        if(!chrs.isEmpty()){
            for (IntegerSolution c : chrs) {
                if(c.getId().equals(Id)){
                    return c;
                }
            }
        }
        return null;
    }
    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    protected  List<IntegerSolution> tournamentSelection(List<IntegerSolution> chrss,int tour) throws Exception {
        List<IntegerSolution> chds = new ArrayList<>();
        for (int i = 0; i < tour; i++) {
            List<IntegerSolution> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
        }
        return chds;
    }
    /**
     * @随机选取两个父代个体、保证选出的是不同个体
     * @param olist
     * @return
     */
    protected static Integer[] findDiffChromIndex(List<IntegerSolution> olist) {
        int tour = 2;/*二进制*/
        Set<Integer> cdx = new TreeSet<>();
        int h = cdx.size();
        while(cdx.size()<tour){
            int cdx1 = new Random().nextInt(olist.size());
            cdx.add(cdx1);
            while (cdx.size()==tour){
                int m=0;
                int[] c = new int[]{-1,-1};
                Iterator<Integer> it = cdx.iterator();
                while (it.hasNext()){
                    int index = it.next();
                    c[m] =Integer.parseInt(olist.get(index).getId());
                    m++;
                }
                if(c[0]==c[1]){
                    int cdx2 = new Random().nextInt(olist.size());
                    cdx.remove(((TreeSet<Integer>) cdx).last());
                    cdx.add(cdx2);
                }else {
                    break;
                }
            }
        }

        return cdx.toArray(new Integer[cdx.size()]);
    }
    /**
     * @产生新父代个体(精英保留策略)
     * @param popSize 种群大小
     * @param fchrs 父代个体
     * @param cchrs 子代个体
     * @return
     * @throws Exception
     */
    public  List<IntegerSolution> saveStrategyOfElite(int popSize,List<IntegerSolution> fchrs,List<IntegerSolution> cchrs) throws Exception{
        List<IntegerSolution> chroms = new ArrayList<>();
        /*1、父子代合并*/
        List<IntegerSolution> fc_lists = addAll(fchrs,cchrs);
        /*2、快速非支配排序*/
        gui2.computeRanking(fc_lists);
        /*3、拥挤度计算*/
        int pop = fc_lists.size();
        List<IntegerSolution> n_fc_lists =  gui2.crowdingDistance(pop);
        /*4、产生新父代个体*/
        /********************************精英保留策略***************************************/
        /*(1) 根据pareto等级从高到低进行排序*/
        popSort(n_fc_lists);
        List<IntegerSolution> rk_lists = deepCopyed(n_fc_lists);
        /*(2) 求出最高的pareto等级*/
        int max_rank = rk_lists.get(rk_lists.size() - 1).getRank();
        /*(3) 根据排序后的顺序，将等级相同的种群整个放入父代种群中，直到某一层不能全部放下为止*/
        int pre_index=0;
        for (int i = 0; i <= max_rank; i++) {
            /*寻找当前等级i个体的最大索引*/
            int cur_index = gui2.getMaxIndex(rk_lists, i) + 1;
            if (cur_index > popSize) {
                /*剩余群体数*/
                int rem_index = popSize - pre_index;
                /*等级为i的个体*/
                List<IntegerSolution> tmp_lists = gui2.copyChrome(rk_lists, pre_index, cur_index);
                /*根据拥挤度从大到小排序*/
                gui2.popSortByNd(tmp_lists);
                /*填满父代*/
                chroms = deepCopy(tmp_lists, chroms, 0, rem_index);
                return chroms;
            }
            if (cur_index < popSize) {
                /*将所有等级为i的个体直接放入父代种群*/
                chroms = deepCopy(rk_lists, chroms, chroms.size(), cur_index);
            }
            if (cur_index == popSize) {
                chroms = deepCopy(rk_lists, chroms, chroms.size(), cur_index);/*将所有等级为i的个体直接放入父代种群*/
                return chroms;
            }
            pre_index = cur_index;
        }
        return chroms;
    }
    /**
     *
     * @param srcs
     * @param start
     * @param end
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<IntegerSolution> deepCopy(List<IntegerSolution> srcs,List<IntegerSolution> targets,int start,int end) throws Exception{
        if(!(start<0||start>=end)){
            if(!srcs.isEmpty()){
                for (int i = start; i < end; i++) {
                    targets.add((IntegerSolution) srcs.get(i));
                }
            }
        }
        return targets;
    }

    /**
     * @ 合并集合
     * @param chrs
     * @param off_sprs
     * @return
     */
    public static List<IntegerSolution> addAll(List<IntegerSolution> chrs,List<IntegerSolution> off_sprs) throws CloneNotSupportedException{
        List<IntegerSolution> com_list = new ArrayList<>();
        addList(chrs, com_list);
        addList(off_sprs, com_list);
        return com_list;

    }
    /**
     *
     * @param olist
     * @return
     * @throws Exception
     */
    public  List<IntegerSolution> SBXAndMutation(List<IntegerSolution> olist) throws Exception{
        List<IntegerSolution> offspring = new ArrayList<>();
        int size = olist.size();
        for (int i = 0; i < size; i++) {
            /*确定两个父代个体不是同一个*/
            IntegerSolution parent1 = olist.get(0);
            IntegerSolution parent2 = olist.get(size-1);


            //IntegerSolution off_1 = (IntegerSolution) parent1.copy();/*子代个体*/
            //IntegerSolution off_2 = (IntegerSolution) parent2.copy();

            //gui2.SBXCroosover(parent1,parent2,off_1,off_2);
            /*二进制交叉*/
            offspring = gui2.doCrossover(pc,parent1,parent2);
            /*变异*/
            gui2.doMutation(pm,offspring, offspring.get(0));
            gui2.doMutation(pm,offspring, offspring.get(1));

            //gui2.polynomialMutation(offspring,off_1);
            //gui2.polynomialMutation(offspring,off_2);
        }
        return offspring;
    }
    /**
     *
     * @param chrs
     * @param com_list
     * @throws CloneNotSupportedException
     */
    protected static void addList(List<IntegerSolution> chrs, List<IntegerSolution> com_list) throws CloneNotSupportedException {
        if (!chrs.isEmpty()) {
            for (IntegerSolution c : chrs) {
                IntegerSolution chr = (IntegerSolution) c.clone();
                chr.setRank(0);
                chr.setNd(0);
                /* chr.setNd(new double[]{0, 0});*/
                chr.setId(String.valueOf(com_list.size()));
                com_list.add(chr);
            }
        }
    }
    /**
     * 种群排序
     * @param chrs
     */
    public static void popSort(List<IntegerSolution> chrs) {
        Collections.sort(chrs, new Comparator<IntegerSolution>() {
            @Override
            public int compare(IntegerSolution o1, IntegerSolution o2) {
                if(o1.getRank()>o2.getRank()){
                    return 1;
                }else if(o1.getRank()<o2.getRank()){
                    return -1;
                }else {
                    return 0;
                }

            }
        });
    }
    /**
     * 深度拷贝
     * @param srcs
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<IntegerSolution> deepCopyed(List<IntegerSolution> srcs) throws Exception{
        List<IntegerSolution> n_c_lists = new ArrayList<>();
        for (IntegerSolution src : srcs) {
            n_c_lists.add((IntegerSolution) src);
        }
        return n_c_lists;
    }
    /**
     *
     */
    public static  class RunThread1 implements Runnable {
        public RunThread1() { }
        @Override
        public void run() {
            synchronized (thread1) {
                try {
                    try{

                    }catch (Exception d3){}
                    if (suspended1) {
                        thread1.wait();
                    }
                } catch (InterruptedException e) {
                }
            }

          /*  System.out.print(tdf+"   ");*/
          /*  constant.ctmls.forEach((e) -> {
                if(e.getLabel().equals("25")||e.getLabel().equals("32")||e.getLabel().equals("43")||e.getLabel().equals("48")){
                    double v = e.getSteerMaps().get(1);
                    System.out.println(e.getLabel()+"  "+v +"     ");

                    //System.out.println();
                }
            });
            String intlabel = "10";
            intersection intsect = stmSim.getIntByName(intlabel);
            System.out.println(intsect.getSumDelay() +"--"+intsect.getSumVeh());*/

            try{
            }catch (Exception ex){ex.printStackTrace();}

            thread1=null;
            thread2=null;
            /*初始化路段*/
            restRoadData1();
            /*初始化相位*/
            initiazePhase();

            samplePeriod = 1;
            sample = 1;
            maxStep = 16;
        }


    }

    public static void stmSingleCycle(IntegerSolution chr) {
        while(step<maxStep){
            thread2 = new Thread(new RunThread2(chr));
            try {
                thread2.run();
            } catch (Exception e) {
                break;
            }
        }
        step = 1;
        /*初始化相位*/
        initiazePhase();
        cleanNetwokData();
    }


    /**
     *
     */
    public static class RunThread2 implements Runnable {
        IntegerSolution chr;
        public RunThread2(IntegerSolution chr) {
            this.chr = chr;
        }
        @Override
        public void run() {
            while (sample<=samplePeriod) {
                synchronized (thread2) {
                    try {
                        try{
                            if(step<=maxStep){
                                thirdExcute(chr);
                            }else{
                                break;
                            }
                        }catch (Exception d3){
                            System.out.println(d3.getMessage());
                        }
                        if (suspended2) {
                            thread2.wait();
                        }
                    } catch (InterruptedException e) {
                        System.out.println("当前线程被中断");
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     */
    protected static void secondExcute(IntegerSolution chr) {
        while (sample <= samplePeriod) {
            setPoissionDistrCar(tdf, odds); /*以泊松分布产生车辆*/
            while(step<maxStep){
                thread2 = new Thread(new gui.RunThread2(chr));
                try {
                    thread2.run();
                } catch (Exception e) {
                    break;
                }
            }
            step = 1;
            ++sample;
            cleanNetwokData();
        }
        sample = 1;
        double crf = tdf;
        tdf = MathSupplier.add(tdf, 0.01);
        /*清除路段数据*/
        netwokDataClear();
    }

    protected static void thirdExcute(IntegerSolution chr) throws Exception{
        int t=1;
        String intlabel = "10";
        int cycle = baseCycle;
        intersection intsect = stmSim.getIntByName(intlabel);
        /*交通流加载*/
        fluxLoadStyle();
        /*车流传播*/
        /*统计第i个周期初始时段交叉口进口道滞留车辆数*/
        if(step%baseCycle==1){
            stmSim.calBaseDelay();
        }
        /*车流传递*/
        sim_04.ctmSimulat(step);
        /*相位转换*/
        switchPhase(step,chr);
        /*fffff*/
        roadInfoReady(step,ctms, lg);


        if(step%cycle==0){
            /*计算目标函数值*/
            double f1 = (intsect.getSumDelay()/(intsect.getBaseDelay()+intsect.getSumVeh()))*5;
            double f2 = intsect.getQueueLength()/4;
            /*设置*/
            chr.setObjective(new double[]{f1,f2});
            //System.out.println(step/cycle+"   "+intsect.getBaseDelay()+"    " +intsect.getSumDelay()+"    " +intsect.getSumVeh()+"    " +f1+"    "+f2);
           // System.out.println(f1+"    "+f2);
            t++;
            cycle=baseCycle*t;
            intsect.setSumDelay(0);
            intsect.setSumVeh(0);
            intsect.setBaseDelay(0);
            intsect.setQueueLength(0);

            /**/
        }
        //System.out.println(step);
        ++step;
    }
    /**
     *
     * @param samplePeriod
     * @param timStep
     * @param timToStep
     * @param avgs1
     * @param tdf
     */
    @Deprecated
    protected static void getLaneAvgSpeed1(int samplePeriod, int timStep, int timToStep, double[][] avgs1, double tdf) {
        int rowindex = (int)(MathSupplier.mul(tdf,100));
        for (Edge le : constant.ctmls) {
            int Index = Integer.parseInt(le.getLabel());
            double pAvg = le.getTempAvg();
            /*算术平均*/
            double avgss = MathSupplier.
                    div(pAvg, MathSupplier.mul(timToStep - timStep + 1, samplePeriod));
            avgs1[rowindex - 1][Index] = avgss;
        }
    }

    /**
     *
     */
    public static void initLoadMap1() throws Exception{
        List<Edge> ctmls = constant.ctmls;

        String e25_0 = "25_0";
        String e25_1 = "25_1";
        String e32_0 = "32_0";
        String e32_1 = "32_1";
        String e43_0 = "43_0";
        String e43_1 = "43_1";
        String e48_0 = "48_0";
        String e48_1 = "48_1";

        Edge l_25_0 = initCtm.findEdgByName(ctmls,e25_0);
        Edge l_25_1 = initCtm.findEdgByName(ctmls,e25_1);
        Edge l_32_0 = initCtm.findEdgByName(ctmls,e32_0);
        Edge l_32_1 = initCtm.findEdgByName(ctmls,e32_1);
        Edge l_43_0 = initCtm.findEdgByName(ctmls,e43_0);
        Edge l_43_1 = initCtm.findEdgByName(ctmls,e43_1);
        Edge l_48_0 = initCtm.findEdgByName(ctmls,e48_0);
        Edge l_48_1 = initCtm.findEdgByName(ctmls,e48_1);

        Map<Integer,Double> m250 =  l_25_0.getInputMaps();
        Map<Integer,Double> m251 =  l_25_1.getInputMaps();
        Map<Integer,Double> m320 =  l_32_0.getInputMaps();
        Map<Integer,Double> m321 =  l_32_1.getInputMaps();
        Map<Integer,Double> m430 =  l_43_0.getInputMaps();
        Map<Integer,Double> m431 =  l_43_1.getInputMaps();
        Map<Integer,Double> m480 =  l_48_0.getInputMaps();
        Map<Integer,Double> m481 =  l_48_1.getInputMaps();


        BufferedReader reader = null;
        try {
            Map<Integer,Map<String,Double>> map25 = new HashMap<>();
            String p_25 = fileutils.class.getClassLoader().getResource("ldf_25").getPath();
            reader = new BufferedReader(new FileReader(p_25));
            Gson gs1 = new Gson();
            map25 =  gs1.fromJson(reader,Map.class);

            Iterator<Map.Entry<Integer,Map<String, Double>>> it25 = map25.entrySet().iterator();
            while (it25.hasNext()) {
                Map.Entry<Integer, Map<String,Double>> entry = it25.next();
                Integer key =Integer.parseInt(String.valueOf(entry.getKey()));
                Map<String,Double> lmaps = entry.getValue();
                lmaps.forEach((k1,v1)->{
                    if(k1.equals(e25_0)){
                        m250.put(key,v1);
                    }else{
                        m251.put(key,v1);
                    }
                });
            }


            Map<Integer,Map<String,Double>> map32 = new HashMap<>();
            String p_32 = fileutils.class.getClassLoader().getResource("ldf_32").getPath();
            reader = new BufferedReader(new FileReader(p_32));
            Gson gs2 = new GsonBuilder().create();
            map32 = gs2.fromJson(reader,map32.getClass());


            Iterator<Map.Entry<Integer,Map<String, Double>>> it32 = map32.entrySet().iterator();
            while (it32.hasNext()) {
                Map.Entry<Integer, Map<String,Double>> entry = it32.next();
                Integer key =Integer.parseInt(String.valueOf(entry.getKey()));
                Map<String,Double> lmaps = entry.getValue();
                lmaps.forEach((k,v)->{
                    if(k.equals(e32_0)){
                        m320.put(key,v);
                    }else{
                        m321.put(key,v);
                    }
                });
            }


            Map<Integer,Map<String,Double>> map43 = new HashMap<>();
            String p_43 = fileutils.class.getClassLoader().getResource("ldf_43").getPath();
            reader = new BufferedReader(new FileReader(p_43));
            Gson gs3 = new GsonBuilder().create();
            map43 = gs3.fromJson(reader,map43.getClass());

            Iterator<Map.Entry<Integer,Map<String, Double>>> it43 = map43.entrySet().iterator();
            while (it43.hasNext()) {
                Map.Entry<Integer, Map<String,Double>> entry = it43.next();
                Integer key =Integer.parseInt(String.valueOf(entry.getKey()));
                Map<String,Double> lmaps = entry.getValue();
                lmaps.forEach((k,v)->{
                    if(k.equals(e43_0)){
                        m430.put(key,v);
                    }else{
                        m431.put(key,v);
                    }
                });
            }


            Map<Integer,Map<String,Double>> map48 = new HashMap<>();
            String p_48 = fileutils.class.getClassLoader().getResource("ldf_48").getPath();
            reader = new BufferedReader(new FileReader(p_48));
            Gson gs4 = new GsonBuilder().create();
            map48 = gs4.fromJson(reader,map48.getClass());


            Iterator<Map.Entry<Integer,Map<String, Double>>> it48 = map48.entrySet().iterator();
            while (it48.hasNext()) {
                Map.Entry<Integer, Map<String,Double>> entry = it48.next();
                Integer key =Integer.parseInt(String.valueOf(entry.getKey()));
                Map<String,Double> lmaps = entry.getValue();
                lmaps.forEach((k,v)->{
                    if(k.equals(e48_0)){
                        m480.put(key,v);
                    }else{
                        m481.put(key,v);
                    }
                });
            }


            /*关闭流*/
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    /**
     * 交叉口进口道输入流量(veh/time step)
     * @param loadMaps
     */
    public static void initLoadMap(Map<String,Double> loadMaps){
      /*  loadMaps.put("25_0",0.5888);
        loadMaps.put("25_1",0.3333);
        loadMaps.put("32_0",0.6597);
        loadMaps.put("32_1",1.0972);
        loadMaps.put("43_0",0.7111);
        loadMaps.put("43_1",0.7292);
        loadMaps.put("48_0",0.8542);
        loadMaps.put("48_1",0.6930);*/

        loadMaps.put("25_0",4.24);
        loadMaps.put("25_1",2.40);
        loadMaps.put("32_0",4.75);
        loadMaps.put("32_1",7.90);
        loadMaps.put("43_0",5.12);
        loadMaps.put("43_1",5.25);
        loadMaps.put("48_0",6.15);
        loadMaps.put("48_1",4.99);
    }
    /**
     *
     * @throws Exception
     */
    private static void fluxLoadStyle() throws Exception {
        /*1、服从泊松分布*/
        loadVehicle1(step,loadMaps);


    }
    /*1、种群初始化 */
    /**
     * 在 n*n*n的二维网格中，每个网格代表一个个体V(i,j,k)
     */
    public   List<IntegerSolution> initPop1(){
        List<IntegerSolution> chroms = new ArrayList<>(popSize);
        /*初始化种群*/
        for(int i=0;i<popSize;i++){
            IntegerSolution chr = new IntegerSolution();
            List<Integer> x = chr.getX();

            for (int h = 0; h < x_num; h++) {
                int v = a + (int)(Math.random() * (b - a + 1));
                chr.setVariableValue(h,v);
            }

            chr.setId(String.valueOf(i));
            chroms.add(chr);
        }
        return chroms;
    }
    @Test
    public void NsGAII_test() throws IOException,Exception {

        long startTime = System.currentTimeMillis();
        //获取开始时间
        constant.chrs = initPop1();
        simDataReadyInfo1();    //测试的代码段

        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

}
