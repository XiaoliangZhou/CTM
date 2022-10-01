package transport.ctm.main;


import CGA.comparator.ObjectiveComparatorII;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.uma.jmetal.util.JMetalException;
import transport.ctm.model.intersection;
import transport.ctm.util.constant;
import transport.file.util.fileutils;
import transport.graph.DefaultGraphPath;
import transport.graph.Edge;
import transport.graph.Garage;
import transport.math.util.MathSupplier;
import transport.math.util.randomSupplier;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;

import static transport.ctm.main.buildCtm.initiazePhase;
import static transport.ctm.main.buildCtm.setIntPhase;
import static transport.ctm.main.simAaction.*;
import static transport.ctm.main.stmSim.roadInfoReady;
import static transport.ctm.main.stmSim.switchPhase;
import static transport.file.util.fileutils.loadIntData;

public class gui_4 {

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
    static int maxStep =48 ;
    static  int step = 1;
    static int cycle = 16;
    static double tdf = 0.10;
    static int baseCycle = 16;

    static int popSize = 7;
    static int a = 3, b = 6;
    static double yta1 = 20;
    static double yta2 = 20;
    static double pc=1.0;
    static double pm=1/125;
    static int maxGen=100;
    static int tour = 2;
    static int x_num = 4;

    private List<ArrayList<IntegerSolution>> rankedSubPopulations;
    private transport.math.util.randomSupplier randomSupplier = new randomSupplier();
    private  double lowerBound = 3;
    private  double upperBound = 6;

    /* static double[][] avgs = new double[50][85];
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
        //creatVehicleGarage(lg);
        /*加载交叉口进口道输入流量*/
        initLoadMap1();

        while (sample <= samplePeriod) {

            /*************************************初始化目标函数值 开始*******************************/
            IntegerSolution[][] chroms = constant.d2_chrs;
            for (int i = 1; i < chroms[0].length-1; i++) {
                for (int j = 1; j < chroms[i].length - 1; j++) {
                    IntegerSolution chr = chroms[i][j];
                    stmSingleCycle(chr);
                }
            }
            /*2、产生一个存放pareto解集空种群*/
            List<IntegerSolution>  p_f_lists = new ArrayList<>(popSize*popSize);
            IntegerSolution[][] aux_pop = createNewPop(chroms,popSize);
            /*************************************种群初始化 结束********************************/
            int gstep=0;
            while(gstep<maxGen){
                for (int i = 1; i < chroms[0].length-1; i++) {
                    for (int j = 1; j < chroms[i].length - 1; j++) {
                        IntegerSolution chr = chroms[i][j];/*种群个体*/
                        chr.setCurChr(true);
                        List<IntegerSolution> cur_neigh_lists = ruleOfMooreType(chroms,i,j);
                        /*二进制选择，从周围邻居中选出两个个体作为父本*/
                        List<IntegerSolution> t_f_lists = tournamentSelection(cur_neigh_lists,tour);
                        List<IntegerSolution> offspring = SBXAndMutation(t_f_lists);

                        IntegerSolution c = getBestSolutions(offspring,chr);
                        if(!c.getId().equals(chr.getId())){
                            IntegerSolution c0 = (IntegerSolution) chr.copy();
                            /*子代占优，子代提花当前个体*/
                            c0.setObjective(c.getObjective());
                            c0.setX(c.getX());
                            c0.setCurChr(false);

                            p_f_lists = insertParetoFront(p_f_lists,c);
                            aux_pop[i][j]=c0;
                        }else{
                            chr.setCurChr(false);
                            aux_pop[i][j]=chr;
                        }

                    }
                }
                chroms= aux_pop;/*异步更新种群*/
                /*反馈机制*/
                System.out.println(gstep + "  gen is completed!");
         /*   if(step%100==0){
                System.out.println(step + "  gen is completed!");
            }*/
                feedBackStrategy(chroms,p_f_lists);
                gstep++;

            }
           p_f_lists.forEach((c)->{
                double[] fx = c.getObjective();
                System.out.println(fx[0]+"  "+fx[1]+"  "+ c.getX().toString());
            });
            ++sample;
        }

        sample = 1;
        /*清除路段数据*/
        netwokDataClear();

    }

    /**
     * @创建一个空的存放Pareto解集空种群
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  IntegerSolution[][] createNewPop(IntegerSolution[][] oldPops,int popSize) throws Exception{
        int length = popSize+2;
        IntegerSolution[][] newPops = new  IntegerSolution[length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                newPops[i][j]=oldPops[i][j];
            }
        }
        for (int i = 1; i < newPops[0].length-1; i++) {
            for (int j = 1; j <newPops[i].length-1; j++) {
                newPops[i][j]=null;

            }
        }
        return newPops;
    }
    /**
     *
     * @param chroms
     * @param i
     * @param j
     */
    public static List<IntegerSolution> ruleOfMooreType(IntegerSolution[][] chroms, int i, int j) throws Exception {
        IntegerSolution child;
        List<IntegerSolution> olist = new ArrayList<>();
        for (int k = i - 1; k <= i + 1; k++) {
            for (int m = j - 1; m <= j + 1; m++) {
                child = chroms[k][m];
                olist.add(child);
            }
        }
        return olist;
    }
    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<IntegerSolution> tournamentSelection(List<IntegerSolution> chrss,int n_arity) throws Exception {

        List<IntegerSolution> chds = new ArrayList<>();
        int start = 0;
        while (start<n_arity){
            List<IntegerSolution> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
            ++start;
        }
        return chds;
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
        for (int i = 0; i < 1; i++) {
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
                if (nd1 == nd2) cId = c1.getId();/*等级相同、拥挤度相同 则随机选择其中一个*/
            }
            newchrs1.add((IntegerSolution) findChromeById(chrs,cId));
        }
        return newchrs1;
    }
    /**
     *
     * @param chrs
     * @param Id
     * @return
     */
    public static IntegerSolution findChromeById(List<IntegerSolution> chrs,String Id){
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
                String[] c = new String[]{"",""};
                Iterator<Integer> it = cdx.iterator();
                while (it.hasNext()){
                    int index = it.next();
                    c[m] =olist.get(index).getId();
                    m++;
                }
                if(c[0].equals(c[1])){
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
     *
     * @param p_f_lists
     * @param chr
     * @throws Exception
     */
    public  List<IntegerSolution> insertParetoFront(List<IntegerSolution> p_f_lists,IntegerSolution chr) throws Exception{

        int maxLimit = popSize *popSize;
        int N=20;
        if(chr!=null){
            p_f_lists.add(chr);
        }

        if(p_f_lists.size()<=maxLimit){
            return p_f_lists;
        }else{
            while (p_f_lists.size()>maxLimit){
                int popSize = p_f_lists.size();

                this.computeRanking(p_f_lists);
                p_f_lists = this.crowdingDistance(popSize);


                int worstRank = p_f_lists.get(p_f_lists.size() - 1).getRank();
                int pre_index1=0;

                /*寻找当前等级i个体的最大索引*/
                int cur_index = getMaxIndex1(p_f_lists, worstRank)+1;
                /*等级为i的个体*/
                List<IntegerSolution> tmp_lists = copyChrome(p_f_lists, cur_index, p_f_lists.size());
                /*根据拥挤度从大到小排序*/
                popSortByNd(tmp_lists);

                IntegerSolution c = tmp_lists.get(tmp_lists.size()-1);
                p_f_lists.remove(c);


            }
            return p_f_lists;
        }

    }
    /**
     *
     * @param offsprings
     * @param f_chr
     * @return
     * @throws Exception
     */
    public  IntegerSolution getBestSolutions(List<IntegerSolution> offsprings,IntegerSolution f_chr) throws Exception {
        offsprings.add((IntegerSolution) f_chr.copy());
        int size = offsprings.size();

        this.computeRanking(offsprings);
        offsprings = this.crowdingDistance(size);

        IntegerSolution c_chr = campreOperator(offsprings);/*选出优秀子代*/

        return c_chr;
    }
    /**
     * @非支配排序
     * @param solutionSet
     * @return
     */
    public List<ArrayList<IntegerSolution>> computeRanking(List<IntegerSolution> solutionSet){

        List<IntegerSolution> population = solutionSet;
        int[] dominateMe = new int[solutionSet.size()];
        List<List<Integer>> iDominate = new ArrayList(solutionSet.size());
        ArrayList<List<Integer>> front = new ArrayList(solutionSet.size() + 1);

        int flagDominate;
        for(flagDominate = 0; flagDominate < population.size() + 1; ++flagDominate) {
            front.add(new LinkedList());
        }

        for(flagDominate = 0; flagDominate < population.size(); ++flagDominate) {
            iDominate.add(new LinkedList());
            dominateMe[flagDominate] = 0;
        }

        int i;
        int var10002;
        for(i = 0; i < population.size() - 1; ++i) {
            for(int q = i + 1; q < population.size(); ++q) {
                flagDominate = this.compare(solutionSet.get(i), solutionSet.get(q));
                if (flagDominate == -1) {
                    ((List)iDominate.get(i)).add(q); /*i支配q*/
                    var10002 = dominateMe[q]++;
                } else if (flagDominate == 1) {
                    ((List)iDominate.get(q)).add(i); /*q支配i*/
                    var10002 = dominateMe[i]++;
                }
            }
        }
        for(i = 0; i < population.size(); ++i) {
            if (dominateMe[i] == 0) {
                ((List)front.get(0)).add(i);
                ((IntegerSolution)solutionSet.get(i)).setRank(0);
            }
        }
        i = 0;

        int index;
        Iterator it1;
        while(((List)front.get(i)).size() != 0) {
            ++i;
            it1 = ((List)front.get(i - 1)).iterator();

            while(it1.hasNext()) {
                Iterator it2 = ((List)iDominate.get((Integer)it1.next())).iterator();

                while(it2.hasNext()) {
                    index = (Integer)it2.next();
                    var10002 = dominateMe[index]--;
                    if (dominateMe[index] == 0) {
                        ((List)front.get(i)).add(index);
                        ((IntegerSolution)solutionSet.get(index)).setRank(i);
                    }
                }
            }
        }

        this.rankedSubPopulations = new ArrayList();

        for(index = 0; index < i; ++index) {
            this.rankedSubPopulations.add(index, new ArrayList(((List)front.get(index)).size()));
            it1 = ((List)front.get(index)).iterator();

            while(it1.hasNext()) {
                ((ArrayList)this.rankedSubPopulations.get(index)).add(solutionSet.get((Integer)it1.next()));
            }
        }
        return this.rankedSubPopulations;

    }

    public int compare(IntegerSolution solution1, IntegerSolution solution2) {
        if (solution1 == null) {
            throw new JMetalException("Solution1 is null");
        } else if (solution2 == null) {
            throw new JMetalException("Solution2 is null");
        } else {
            int result = -1;
            result = this.dominanceTest(solution1, solution2);
            return result;
        }
    }

    private int dominanceTest(IntegerSolution solution1, IntegerSolution solution2) {
        int bestIsOne = 0;
        int bestIsTwo = 0;

        for(int i = 0; i < solution1.getF().length; ++i) {
            double value1 = solution1.getObjective()[i];
            double value2 = solution2.getObjective()[i];
            if (value1 != value2) {
                if (value1 < value2) {
                    bestIsOne = 1;
                }

                if (value2 < value1) {
                    bestIsTwo = 1;
                }
            }
        }

        byte result;
        if (bestIsOne > bestIsTwo) {
            result = -1;
        } else if (bestIsTwo > bestIsOne) {
            result = 1;
        } else {
            result = 0;
        }

        return result;
    }
    public static int getMaxIndex1(List<IntegerSolution> src,int rank){
        int index = 0;
        if (!src.isEmpty()) {
            for (int i = src.size()-1; i >=0; i--) {
                int c_rank = src.get(i).getRank();
                if (c_rank != rank) {
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * @计算拥挤度
     * @param solutionList
     */
    public void computeDensityEstimator(List<IntegerSolution> solutionList) {
        int size = solutionList.size();
        if (size != 0) {
            if (size == 1) {
                ((IntegerSolution)solutionList.get(0)).setNd(1.0D / 0.0);
            } else if (size == 2) {
                ((IntegerSolution)solutionList.get(0)).setNd(1.0D / 0.0);
                ((IntegerSolution)solutionList.get(1)).setNd(1.0D / 0.0);
            } else {
                List<IntegerSolution> front = new ArrayList(size);
                Iterator var4 = solutionList.iterator();

                while(var4.hasNext()) {
                    IntegerSolution solution = (IntegerSolution)var4.next();
                    front.add(solution);
                }

                for(int i = 0; i < size; ++i) {
                    ((IntegerSolution)front.get(i)).setNd(0.0D);
                }

                int numberOfObjectives = ((IntegerSolution)solutionList.get(0)).getNumberOfObjectives();

                for(int i = 0; i < numberOfObjectives; ++i) {
                    Collections.sort(front, new ObjectiveComparatorII(i));
                    double objetiveMinn = ((IntegerSolution)front.get(0)).getObjective(i);
                    double objetiveMaxn = ((IntegerSolution)front.get(front.size() - 1)).getObjective(i);
                    ((IntegerSolution)front.get(0)).setNd(1.0D / 0.0);
                    ((IntegerSolution)front.get(size - 1)).setNd(1.0D / 0.0);

                    for(int j = 1; j < size - 1; ++j) {
                        double distance = ((IntegerSolution)front.get(j + 1)).getObjective(i) - ((IntegerSolution)front.get(j - 1)).getObjective(i);
                        distance /= objetiveMaxn - objetiveMinn;
                        distance += (Double)((IntegerSolution)front.get(j)).getNd();
                        ((IntegerSolution)front.get(j)).setNd(distance);
                    }
                }

            }
        }
    }

    /**
     *
     * @param rank
     * @return
     */
    public List<IntegerSolution> getSubfront(int rank) {
        if (rank >= this.rankedSubPopulations.size()) {
            throw new JMetalException("Invalid rank: " + rank + ". Max rank = " + (this.rankedSubPopulations.size() - 1));
        } else {
            return (List)this.rankedSubPopulations.get(rank);
        }
    }

    public int getNumberOfSubfronts() {
        return this.rankedSubPopulations.size();
    }

    public static IntegerSolution[][] feedBackStrategy( IntegerSolution[][] chroms,List<IntegerSolution> p_f_lists){
        int N = 20;
        int size = p_f_lists.size();
        if(size>=N){
            for (int k = 0; k < N; k++) {
                int index = new Random().nextInt(size-1);
                IntegerSolution c0 = p_f_lists.get(k);

                int i = new Random().nextInt(popSize) +1;
                int j = new Random().nextInt(popSize) +1;
                String cId = i+"_"+j;

                IntegerSolution c = getChrome(chroms,cId);
                c.setX(c0.getX());
                c.setF(c0.getF());
                c.setPareto(c0.getPareto());
                c.setNd(0);
                c.setRank(0);
            }
        }


        return chroms;
    }
    public static IntegerSolution getChrome(IntegerSolution[][] chroms,String cId){
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length - 1; j++) {
                IntegerSolution c1 = chroms[i][j];/*种群个体*/
                if(c1.getId().equals(cId)){
                    return c1;
                }
            }
        }
        return null;
    }
    /**
     * 获取当前等级个体i的最大索引
     * @param src
     * @param rank
     * @return
     */
    public static int getMaxIndex(List<IntegerSolution> src,int rank){
        int index = 0;
        if (!src.isEmpty()) {
            for (int i = src.size()-1; i >=0; i--) {
                int c_rank = src.get(i).getRank();
                if (c_rank == rank) {
                    return i;
                }
            }
        }
        return -1;
    }
    public  IntegerSolution campreOperator(List<IntegerSolution> offSprs) throws Exception{
        int max_rank = offSprs.get(0).getRank();
        /*寻找当前等级i个体的最大索引*/
        int cur_index = getMaxIndex(offSprs, max_rank) + 1;
        /*等级为i的个体*/
        List<IntegerSolution> tmp_lists = copyChrome(offSprs, 0, cur_index);
        /*根据拥挤度从大到小排序*/
        popSortByNd(tmp_lists);
        /*获取较优秀个体*/
        int c_dx=-1;
        for (IntegerSolution chr : tmp_lists) {
            if(chr.isCurChr()){
                c_dx=tmp_lists.indexOf(chr);
            }
        }
        IntegerSolution elite = tmp_lists.get(0);
        if(c_dx>0){
            IntegerSolution c_chr = tmp_lists.get(c_dx);
            double nd = c_chr.getNd();
            if(nd>=elite.getNd()){
                return c_chr;
            }else{
                return elite;
            }
        }else{
            return elite;
        }
    }
    /**
     *
     * @param srcs
     * @param start
     * @param end
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<IntegerSolution> copyChrome(List<IntegerSolution> srcs,int start,int end) throws CloneNotSupportedException{
        List<IntegerSolution> n_f_lists = new ArrayList<>();
        if(!(start<0||start>=end)){
            if(!srcs.isEmpty()){
                for (int i = start; i < end; i++) {
                    n_f_lists.add((IntegerSolution) srcs.get(i));
                }
            }
        }
        return n_f_lists;
    }
    /**
     * 种群排序
     * @param chrs
     */
    public static void popSortByNd(List<IntegerSolution> chrs) {
        Collections.sort(chrs, new Comparator<IntegerSolution>() {
            @Override
            public int compare(IntegerSolution o1, IntegerSolution o2) {
                double nd1 = o1.getNd();
                double nd2 = o2.getNd();
                if(nd1<nd2){
                    return 1;
                }else if(nd1==nd2){
                    return  0;
                }else {
                    return -1;
                }

            }
        });
    }
    /**
     *
     * @param solutionsToSelect
     * @return
     * @throws Exception
     */
    public  List<IntegerSolution> crowdingDistance(int solutionsToSelect) throws Exception{
        List<IntegerSolution> population = new ArrayList(solutionsToSelect);
        int rankingIndex = 0;
        while(population.size()<solutionsToSelect){
            computeDensityEstimator(this.getSubfront(rankingIndex));
            this.addRankedSolutionsToPopulation(rankingIndex,population);
            rankingIndex++;
        }
        return population;
    }

    /**
     *
     * @param rank
     * @param population
     */
    protected void addRankedSolutionsToPopulation(int rank, List<IntegerSolution> population) {
        List<IntegerSolution> front = this.getSubfront(rank);

        for(int i = 0; i < front.size(); ++i) {
            population.add(front.get(i));
        }

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
        /*确定两个父代个体不是同一个*/
        IntegerSolution parent1 = olist.get(0);
        IntegerSolution parent2 = olist.get(size-1);


        IntegerSolution off_1 = (IntegerSolution) parent1.copy();/*子代个体*/
        IntegerSolution off_2 = (IntegerSolution) parent2.copy();

        //SBXCroosover(parent1,parent2,off_1,off_2);
        /*二进制交叉*/
        offspring = doCrossover(pc,parent1,parent2);
        /*变异*/
        doMutation(pm,offspring, offspring.get(0));
        doMutation(pm,offspring, offspring.get(1));

        //polynomialMutation(offspring,off_1);
       // polynomialMutation(offspring,off_2);

        return offspring;
    }
    /**
     * @模拟二进制交叉
     * @param c1
     * @param c2
     * @param off_1
     * @param off_2
     */
    public  void SBXCroosover(IntegerSolution c1, IntegerSolution c2, IntegerSolution off_1, IntegerSolution off_2) throws Exception {
        if(Math.random()<pc){
            for (int j = 0; j < x_num; j++) {
                double gam = getGamaOrYiTa(yta1);
                /*二进制交叉(SBX)*/
                updateX(c1, c2, off_1, j, 1 + gam, 1 - gam);
                updateX(c1, c2, off_2, j, 1 - gam, 1 + gam);
            }
        }
    }
    /**
     * @计算交叉后X的值
     * @param c1
     * @param c2
     * @param j
     * @param v
     * @param v2
     */
    private static void updateX(IntegerSolution c1, IntegerSolution c2, IntegerSolution off_1, int j, double v, double v2) {
        double v3 = v * c1.getX().get(j) + v2 * c2.getX().get(j);
        double x = mul(0.5, v3);
        int v1 = (int)((x > b) ? b : (x < a) ? a : x);
        off_1.setVariableValue(j,v1);
    }

    /**
     * @
     * @param offspring
     * @param off_1
     */
    public  void polynomialMutation(List<IntegerSolution> offspring, IntegerSolution off_1) {
        List<Integer> xx = off_1.getX();
        double v = 0;
        for (int j = 0; j < x_num; j++) {
            if (Math.random() < pm) {
                double delta = getGamaOrYiTa(yta2);
                delta = new BigDecimal(delta).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();/*保留6位有效数字*/
                /*多项式变异*/
                v = xx.get(j) + delta;
                int v1 = (int)((v > b) ? b : (v < a) ? a : v);

                off_1.setVariableValue(j,v1);
            }
        }
        /*计算子代目标函数值*/
        stmSingleCycle(off_1);
        offspring.add(off_1);
    }
    /**
     * @多项式变异算子
     * @param probability
     * @param offspring
     * @param off_1
     */
    public  void doMutation(double probability,List<IntegerSolution> offspring, IntegerSolution off_1) throws CloneNotSupportedException {
        List<Integer> x = off_1.getX();
        for (int j = 0; j < x.size(); j++) {
            if(this.randomSupplier.nextDouble() <= probability){
                double y = x.get(j);
                double yl = this.lowerBound;
                double yu = this.upperBound;
                if (yl == yu) {
                    y = yl;
                } else {
                    double delta1 = (y - yl) / (yu - yl);
                    double delta2 = (yu - y) / (yu - yl);
                    double rnd = (Double)randomSupplier.nextDouble() ;
                    double mutPow = 1.0D / (yta2 + 1.0D);
                    double deltaq;
                    double val;
                    double xy;
                    if (rnd <= 0.5D) {
                        xy = 1.0D - delta1;
                        val = 2.0D * rnd + (1.0D - 2.0D * rnd) * Math.pow(xy, this.yta2 + 1.0D);
                        deltaq = Math.pow(val, mutPow) - 1.0D;
                    } else {
                        xy = 1.0D - delta2;
                        val = 2.0D * (1.0D - rnd) + 2.0D * (rnd - 0.5D) * Math.pow(xy, this.yta2 + 1.0D);
                        deltaq = 1.0D - Math.pow(val, mutPow);
                    }

                    y += deltaq * (yu - yl);
                    y = randomSupplier.repairSolutionVariableValue(y, yl, yu);
                }
                off_1.setVariableValue(j,(int)y);
            }
        }
        /*计算子代目标函数值*/
        stmSingleCycle(off_1);

    }
    public List<IntegerSolution> doCrossover(double probability, IntegerSolution parent1, IntegerSolution parent2) throws Exception {
        List<IntegerSolution> offspring = new ArrayList(2);
        offspring.add((IntegerSolution)parent1.copy());
        offspring.add((IntegerSolution)parent2.copy());
        if ((Double)this.randomSupplier.nextDouble() <= probability) {
            for(int i = 0; i < parent1.getX().size(); ++i) {
                int valueX1 = (Integer)parent1.getX().get(i);
                int valueX2 = (Integer)parent2.getX().get(i);
                if ((Double)this.randomSupplier.nextDouble() <= 0.5D) {
                    if ((double)Math.abs(valueX1 - valueX2) > 1.0E-14D) {
                        double y1;
                        double y2;
                        if (valueX1 < valueX2) {
                            y1 = (double)valueX1;
                            y2 = (double)valueX2;
                        } else {
                            y1 = (double)valueX2;
                            y2 = (double)valueX1;
                        }

                        double yL = this.lowerBound;
                        double yu = this.upperBound;
                        double rand = (Double)this.randomSupplier.nextDouble();
                        double beta = 1.0D + 2.0D * (y1 - yL) / (y2 - y1);
                        double alpha = 2.0D - Math.pow(beta, -( this.yta2  + 1.0D));
                        double betaq;
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / ( this.yta2  + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / ( this.yta2  + 1.0D));
                        }

                        double c1 = 0.5D * (y1 + y2 - betaq * (y2 - y1));
                        beta = 1.0D + 2.0D * (yu - y2) / (y2 - y1);
                        alpha = 2.0D - Math.pow(beta, -( this.yta2  + 1.0D));
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / ( this.yta2  + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / ( this.yta2  + 1.0D));
                        }

                        double c2 = 0.5D * (y1 + y2 + betaq * (y2 - y1));
                        if (c1 < yL) {
                            c1 = yL;
                        }

                        if (c2 < yL) {
                            c2 = yL;
                        }

                        if (c1 > yu) {
                            c1 = yu;
                        }

                        if (c2 > yu) {
                            c2 = yu;
                        }

                        if ((Double)this.randomSupplier.nextDouble() <= 0.5D) {
                            ((IntegerSolution)offspring.get(0)).setVariableValue(i, (int)c2);
                            ((IntegerSolution)offspring.get(1)).setVariableValue(i, (int)c1);
                        } else {
                            ((IntegerSolution)offspring.get(0)).setVariableValue(i, (int)c1);
                            ((IntegerSolution)offspring.get(1)).setVariableValue(i, (int)c2);
                        }
                    } else {
                        ((IntegerSolution)offspring.get(0)).setVariableValue(i, valueX1);
                        ((IntegerSolution)offspring.get(1)).setVariableValue(i, valueX2);
                    }
                } else {
                    ((IntegerSolution)offspring.get(0)).setVariableValue(i, valueX2);
                    ((IntegerSolution)offspring.get(1)).setVariableValue(i, valueX1);
                }
            }
        }

        return offspring;
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

        return  b1.multiply(b2).setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * @ 计算gama或者yita
     * @param yta
     * @return
     */
    private static double getGamaOrYiTa(double yta) {
        double rand = Math.random();
        double gam = 0;
        if (rand < 0.5) {
            gam = Math.pow(2 * rand, 1 / (yta + 1));
        } else {
            gam = Math.pow(1 /(2 * (1 - rand)), 1 / (yta + 1));
        }
        return gam;
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
    public  void stmSingleCycle(IntegerSolution chr) {
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
    public  class RunThread2 implements Runnable {
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
    protected  void secondExcute(IntegerSolution chr) {
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

    protected  void thirdExcute(IntegerSolution chr) throws Exception{
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
    public   IntegerSolution[][] initPop1(){
        int size = popSize + 2;
        IntegerSolution[][] chroms = new IntegerSolution[size][size];
        /*初始化种群*/
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                IntegerSolution chr = new IntegerSolution();
                List<Integer> x = chr.getX();

                for (int h = 0; h < x_num; h++) {
                    int v = a + (int)(Math.random() * (b - a + 1));
                    chr.setVariableValue(h,v);
                }

                String idstr = i+"_"+j;
                chr.setId(idstr);
                chroms[i][j] = chr;

            }
        }
        return chroms;
    }
    @Test
    public void moc_test() throws IOException,Exception {

        long startTime = System.currentTimeMillis();    //获取开始时间

        constant.d2_chrs = initPop1();
        simDataReadyInfo1();    //测试的代码段

        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

}
