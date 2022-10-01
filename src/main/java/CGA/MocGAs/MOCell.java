package CGA.MocGAs;

import CGA.Model.Chrome;
import CGA.comparator.CrowdingDistanceComparator;
import CGA.comparator.ObjectiveComparator;
import CGA.comparator.RankingAndCrowdingDistanceComparator;
import CGA.crosser.SBXCrossover;
import CGA.neighborhood.Neighborhood;
import CGA.neighborhood.impl.C9;
import algorithm.TDCGAUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.uma.jmetal.util.JMetalException;
import transport.math.util.MathSupplier;
import util.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


public class MOCell  {
    static int tour = 2;
    static double Inf = Double.POSITIVE_INFINITY;
    static int popSize = 10;
    static int f_num = 2;
    static int x_num = 10;
    static double a1 = 0, b1 = 5;
    private double yta1 = 20;
    private double yta2 = 20;
    static String fname = "ZDT6";
    static double probability=1.0;
    static double mutationProbability=0.01;
    static int maxGen=200;


    private  double lowerBound = 0;
    private  double upperBound = 1;
    private SBXCrossover sbxCrossover = new SBXCrossover(yta1,probability) ;
    private List<ArrayList<Chrome>> rankedSubPopulations;
    private Chrome[][] chroms =  new Chrome[popSize][popSize];
    private randomSupplier randomSupplier = new randomSupplier();
    private List<Chrome> archives = new ArrayList(popSize*popSize);
    protected Neighborhood neighborhood = new C9(this.popSize, this.popSize);/*2、产生一个存放pareto解集空种群*/
    private Comparator<Chrome> crowdingDistanceComparator = new CrowdingDistanceComparator();
    protected TournamentSelection tournamentSelection = new TournamentSelection(tour);


    public  void evaluate(Chrome chr){


        double[] f = new double[chr.getNumberOfObjectives()];
        f[0] = (Double)chr.getVariableValue(0);
        double g = evalG(chr);
        double h = evalH(f[0], g);
        f[1] = h * g;
        chr.setObjective(0, f[0]);
        chr.setObjective(1, f[1]);
    }

    protected double evalG(Chrome solution) {
      /*  double g = 0.0D;

        for(int i = 1; i < solution.getNumberOfVariables(); ++i) {
            g += (Double)solution.getVariableValue(i);
        }

        double constant = 9.0D / (double)(solution.getNumberOfVariables() - 1);
        return constant * g + 1.0D;*/


        double g = 0.0D;

        for(int var = 1; var < solution.getNumberOfVariables(); ++var) {
            g += (Double)solution.getVariableValue(var);
        }

        g /= (double)(solution.getNumberOfVariables() - 1);
        g = Math.pow(g, 0.25D);
        g = 9.0D * g;
        ++g;
        return g;
    }

    protected double evalH(double f, double g) {
      /*  double h = 1.0D - Math.sqrt(f / g);
        return h;*/
       /*ZDT2*/
        //return 1.0D - Math.pow(f / g, 2.0D);
        /*ZDT3*/
        /*double h = 1.0D - Math.sqrt(f / g) - f / g * Math.sin(31.41592653589793D * f);
        return h;*/
        /*ZDT6*/
        return 1.0D - Math.pow(f / g, 2.0D);
    }
    public void evolfx(Chrome chr){
        List<Double> x = chr.getX();
        double[] f = chr.getF();
        double PI = Math.PI;
        double[] fx = new double[f_num];
        if(fname.equals("ZDT1")){
            double f1=0,f2=0;
            double sum=0;
            f1=x.get(0);
            for (int i = 1; i < x_num; i++) {
                sum=sum+x.get(i);
            }
            double gx = 1 + 9 * (sum / (x_num - 1));
            f2 = mul(gx,(1 - Math.sqrt(f1 / gx)));
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("ZDT2")){
            double f1=0,f2=0;
            double sum=0;
            f1=x.get(0);
            for (int i = 1; i < x_num; i++) {
                sum=sum+x.get(i);
            }
            double gx = 1 + 9 * (sum / (x_num - 1));
            f2 = mul(gx,(1 - Math.pow(f1 / gx,2)));
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("ZDT3")){
            double f1 = 0, f2 = 0;
            double sum = 0;
            f1 = x.get(0);
            for (int i = 1; i < x_num; i++) {
                sum = sum + x.get(i);
            }
            double gx = 1 + 9 * (sum / (x_num - 1));
            f2 = mul(gx, (1 - Math.sqrt(f1 / gx))) - mul((f1 / gx), Math.sin(10 * PI * f1));
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("ZDT4")){
            double f1 = 0, f2 = 0;
            double sum = 0;
            f1 = x.get(0);
            for (int i = 1; i < x_num; i++) {
                sum = sum + (Math.pow(x.get(i), 2) - 10 * Math.cos(4 * PI * x.get(i)));
            }
            double gx = 1 + 9 *10 + sum;
            f2 = mul(gx, (1 - Math.sqrt(f1 / gx)));
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("ZDT6")){
            double f1 = 0, f2 = 0;
            double sum = 0;
            f1 = 1 - Math.exp(-4 * x.get(0)) * Math.pow(Math.sin(6 * PI * x.get(0)), 6);
            for (int i = 1; i < x_num; i++) {
                sum = sum + x.get(i);
            }
            double gx = 1 + 9 * Math.pow(sum / (x_num - 1), 0.25);
            f2 = mul(gx, (1 - Math.pow(f1 / gx, 2)));
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("ConstrEx")){
            double f1 = 0, f2 = 0;
            double sum = 0;
            f1=x.get(0);
            f2=(1+x.get(1))/f1;
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
        if(fname.equals("Kursawe")){
            double f1 = 0, f2 = 0;
            double sum = 0;
            for (int i = 0; i < x_num-1; i++) {
                double q = Math.pow(x.get(i), 2) + Math.pow(x.get(i + 1), 2);
                double p = -10*Math.pow(Math.E, -0.2 * Math.sqrt(q));
                sum += p;
            }
            f1 = sum;
            sum = 0;
            double afpha = 0.8, belta = 3;
            for (int i = 0; i < x_num; i++) {
                double q = Math.pow(Math.abs(x.get(i)), afpha) + 5 * Math.sin(Math.pow(x.get(i), belta));
                sum += q;
            }
            f2=sum;
            chr.setObjective(0,f1);
            chr.setObjective(1,f2);
        }
    }
    /**
     * 在 n*n的二维网格中，每个网格代表一个个体V(i,j)
     */
    public  Chrome[][] createInitialPopulation() throws CloneNotSupportedException{
        int size = popSize ;
        Chrome[][] chroms = new Chrome[size][size];
        /*初始化种群*/
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                Chrome chr = new Chrome();
                for (int k = 0; k < x_num; k++) {
                    double value = this.randomSupplier.nextDouble(this.lowerBound,this.upperBound);
                    chr.setVariableValue(k,value);
                }
                String ids = i+"_"+j;
                chr.setAxis(i);
                chr.setYaxis(j);
                chr.setId(ids);
                chr.setRank(-1);
                evolfx(chr);
                //evaluate(chr);
                add((Chrome) chr.copy());

                chroms[i][j] = chr;
            }
        }
        return chroms;
    }

    public  Chrome campreOperator(List<Chrome> offSprs) throws Exception{
        int max_rank = offSprs.get(0).getRank();
        /*寻找当前等级i个体的最大索引*/
        int cur_index = getMaxIndex(offSprs, max_rank) + 1;
        /*等级为i的个体*/
        List<Chrome> tmp_lists = copyChrome(offSprs, 0, cur_index);
        /*根据拥挤度从大到小排序*/
        popSortByNd(tmp_lists);
        /*获取较优秀个体*/
        int c_dx=-1;
        for (Chrome chr : tmp_lists) {
            if(chr.isCurChr){
                c_dx=tmp_lists.indexOf(chr);
            }
        }
        Chrome elite = tmp_lists.get(0);
        if(c_dx>0){
            Chrome c_chr = tmp_lists.get(c_dx);
            double nd = c_chr.getNd();
            if(nd>elite.getNd()){
                return c_chr;
            }else{
                return elite;
            }
        }else{
            return elite;
        }
    }

    /**
     * 种群排序
     * @param chrs
     */
    public static void popSortByNd(List<Chrome> chrs) {
        Collections.sort(chrs, new Comparator<Chrome>() {
            @Override
            public int compare(Chrome o1, Chrome o2) {
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
     * @param offsprings
     * @param f_chr
     * @return
     * @throws Exception
     */
    public  Chrome getBestSolutions(List<Chrome> offsprings,Chrome f_chr) throws Exception {
        offsprings.add((Chrome) f_chr.copy());
        int size = offsprings.size();

        this.computeRanking(offsprings);
        offsprings = this.crowdingDistance(size);

        Chrome c_chr = campreOperator(offsprings);/*选出优秀子代*/
        return c_chr;
    }

    /**
     *
     * @param offSprs
     * @return
     */
    public static Chrome getBestChrome(List<Chrome> offSprs){
        int size = offSprs.size();
        if(!offSprs.isEmpty()){
            Chrome c1 = offSprs.get(0);
            Chrome c2 = offSprs.get(size-1);

            int rk1 = c1.getRank();
            int rk2 = c2.getRank();

            if (rk1!=rk2) {
                return (rk1 < rk2) ? c1 : c2;
            }
            /*如果等级相同，则按拥挤度排序，拥挤度大的被选中*/
            if(rk1==rk2){
                double nd1 = c1.getNd();
                double nd2 = c2.getNd();
                if (nd1 != nd2){
                    return (nd1 > nd2) ? c1 : c2;
                }
                if (nd1 == nd2){
                    return c1 ;/*等级相同、拥挤度相同 则随机选择其中一个*/
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
    protected static Integer[] findDiffChromIndex(List<Chrome> olist) {
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
                    Chrome cc = olist.get(index);
                    if(cc!=null){
                        c[m] =olist.get(index).getId();
                    }else{
                        System.out.println();
                    }

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
     * @ 计算gama或者yita
     * @param yta
     * @return
     */
    private  double getGamaOrYiTa(double yta) {
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
     * 拥挤度
     * @param nd
     * @return
     */
    public  static double getSum(double[] nd){
        double sum=0;
        for (double v : nd) {
            sum+=v;
        }
        return sum;
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
     *
     * @param chroms
     * @param i
     * @param j
     */
    public static List<Chrome> ruleOfMooreType(Chrome[][] chroms, int i, int j) throws Exception {
        Chrome child;
        List<Chrome> olist = new ArrayList<>();
        for (int k = i - 1; k <= i + 1; k++) {
            for (int m = j - 1; m <= j + 1; m++) {
                if(!(k==i&m==j)){
                    child = chroms[k][m];
                    olist.add(child);
                }
            }
        }
        return olist;
    }

    /**
     *
     * @param srcs
     * @param start
     * @param end
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> deepCopy(List<Chrome> srcs,List<Chrome> targets,int start,int end) throws CloneNotSupportedException{
        if(!(start<0||start>=end)){
            if(!srcs.isEmpty()){
                for (int i = start; i < end; i++) {
                    targets.add((Chrome) srcs.get(i).copy());
                }
            }
        }
        return targets;
    }

    /**
     *
     * @param srcs
     * @param start
     * @param end
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> copyChrome1(List<Chrome> srcs,int start,int end) throws CloneNotSupportedException{
        List<Chrome> n_f_lists = new ArrayList<>();
        if(!(start<0||start>=end)){
            if(!srcs.isEmpty()){
                for (int i = start; i < end; i++) {
                    n_f_lists.add((Chrome) srcs.get(i));
                }
            }
        }
        return n_f_lists;
    }

    /**
     *
     * @param srcs
     * @param start
     * @param end
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> copyChrome(List<Chrome> srcs,int start,int end) throws CloneNotSupportedException{
        List<Chrome> n_f_lists = new ArrayList<>();
        if(!(start<0||start>=end)){
            if(!srcs.isEmpty()){
                for (int i = start; i < end; i++) {
                    n_f_lists.add(srcs.get(i));
                }
            }
        }
        return n_f_lists;
    }

    /**
     * 获取当前等级个体i的最大索引
     * @param src
     * @param rank
     * @return
     */
    public static int getMaxIndex(List<Chrome> src,int rank){
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
    public static int getMaxIndex1(List<Chrome> src,int rank){
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
     * @ 合并集合
     * @param chrs
     * @param off_sprs
     * @return
     */
    public static List<Chrome> addAll(List<Chrome> chrs,List<Chrome> off_sprs) throws CloneNotSupportedException{
        List<Chrome> com_list = new ArrayList<>();
        addList(chrs, com_list);
        addList(off_sprs, com_list);
        return com_list;

    }
    /**
     *
     * @param chrs
     * @param com_list
     * @throws CloneNotSupportedException
     */
    protected static void addList(List<Chrome> chrs, List<Chrome> com_list) throws CloneNotSupportedException {
        if (!chrs.isEmpty()) {
            for (Chrome c : chrs) {
                Chrome chr = (Chrome) c.copy();
                chr.setRank(0);
                chr.setNd(0);
                chr.setId(String.valueOf(com_list.size()));
                com_list.add(chr);
            }
        }
    }

    /**
     *
     *@ 二进制竞标赛
     * @param chrs
     * @return
     */
    public static List<Chrome> binaryTournamentSelection(List<Chrome> chrs) throws Exception{
        int length = chrs.size();
        int n = MathSupplier.divide(length, 2);
        List<Chrome> newchrs1 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            String cId="";
            Integer[] cIndex = findDiffChromIndex(chrs);/*随机选取两个父代个体、保证选出的是不同个体*/

            Chrome c1 = chrs.get(cIndex[0]);
            Chrome c2 = chrs.get(cIndex[cIndex.length - 1]);


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
            newchrs1.add((Chrome) findChromeById(chrs,cId));
        }
        return newchrs1;
    }

    /**
     * @非支配排序
     * @param solutionSet
     * @return
     */
    public List<ArrayList<Chrome>> computeRanking(List<Chrome> solutionSet){

        List<Chrome> population = solutionSet;
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
                ((Chrome)solutionSet.get(i)).setRank(0);
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
                        ((Chrome)solutionSet.get(index)).setRank(i);
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

    public int compare(Chrome solution1, Chrome solution2) {
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

    private int dominanceTest(Chrome solution1, Chrome solution2) {
        int bestIsOne = 0;
        int bestIsTwo = 0;


        for(int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
            double value1 = solution1.getObjective(i);
            double value2 = solution2.getObjective(i);
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

    /**
     * @计算拥挤度
     * @param solutionList
     */
    public void computeDensityEstimator(List<Chrome> solutionList) {
        int size = solutionList.size();
        if (size != 0) {
            if (size == 1) {
                ((Chrome)solutionList.get(0)).setNd(1.0D / 0.0);
            } else if (size == 2) {
                ((Chrome)solutionList.get(0)).setNd(1.0D / 0.0);
                ((Chrome)solutionList.get(1)).setNd(1.0D / 0.0);
            } else {
                List<Chrome> front = new ArrayList(size);
                Iterator var4 = solutionList.iterator();

                while(var4.hasNext()) {
                    Chrome solution = (Chrome)var4.next();
                    front.add(solution);
                }

                for(int i = 0; i < size; ++i) {
                    ((Chrome)front.get(i)).setNd(0.0D);
                }

                int numberOfObjectives = ((Chrome)solutionList.get(0)).getNumberOfObjectives();

                for(int i = 0; i < numberOfObjectives; ++i) {
                    Collections.sort(front, new ObjectiveComparator(i));
                    double objetiveMinn = ((Chrome)front.get(0)).getObjective(i);
                    double objetiveMaxn = ((Chrome)front.get(front.size() - 1)).getObjective(i);
                    ((Chrome)front.get(0)).setNd(1.0D / 0.0);
                    ((Chrome)front.get(size - 1)).setNd(1.0D / 0.0);

                    for(int j = 1; j < size - 1; ++j) {
                        double distance = ((Chrome)front.get(j + 1)).getObjective(i) - ((Chrome)front.get(j - 1)).getObjective(i);
                        distance /= objetiveMaxn - objetiveMinn;
                        distance += (Double)((Chrome)front.get(j)).getNd();
                        ((Chrome)front.get(j)).setNd(distance);
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
    public List<Chrome> getSubfront(int rank) {
        if (rank >= this.rankedSubPopulations.size()) {
            throw new JMetalException("Invalid rank: " + rank + ". Max rank = " + (this.rankedSubPopulations.size() - 1));
        } else {
            return (List)this.rankedSubPopulations.get(rank);
        }
    }

    public int getNumberOfSubfronts() {
        return this.rankedSubPopulations.size();
    }


    /**
     *
     * @param solutionsToSelect
     * @return
     * @throws Exception
     */
    public  List<Chrome> crowdingDistance(int solutionsToSelect) throws Exception{
        List<Chrome> population = new ArrayList(solutionsToSelect);
        int rankingIndex = 0;
        while(population.size()<solutionsToSelect){
            this.computeDensityEstimator(this.getSubfront(rankingIndex));
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
    protected void addRankedSolutionsToPopulation(int rank, List<Chrome> population) {
        List<Chrome> front = this.getSubfront(rank);

        for(int i = 0; i < front.size(); ++i) {
            population.add(front.get(i));
        }

    }


    /**
     * @临时存放chrome 对象
     * @param chrs
     * @param sets
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> createTempList(List<Chrome> chrs, Set<String> sets) throws CloneNotSupportedException {
        List<Chrome> chroms = new ArrayList<>();/*储存当前处理的等级的个体*/
        for (String index : sets) {
            Chrome c =  chrs.get(Integer.parseInt(index));
            c.setNd(0);
            chroms.add(c);
        }
        return chroms;
    }


    public List<Chrome> doCrossover(double probability, Chrome parent1, Chrome parent2) {
        List<Chrome> offspring = new ArrayList(2);
        offspring.add((Chrome)parent1.copy());
        offspring.add((Chrome)parent2.copy());
        if ((Double)this.randomSupplier.getRandomValue() <= probability) {
            for(int i = 0; i < parent1.getNumberOfVariables(); ++i) {
                double valueX1 = (Double)parent1.getVariableValue(i);
                double valueX2 = (Double)parent2.getVariableValue(i);
                if ((Double)this.randomSupplier.getRandomValue() <= 0.5D) {
                    if (Math.abs(valueX1 - valueX2) > 1.0E-14D) {
                        double y1;
                        double y2;
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        double lowerBound = this.lowerBound;
                        double upperBound = this.upperBound;
                        double rand = (Double)this.randomSupplier.getRandomValue();
                        double beta = 1.0D + 2.0D * (y1 - lowerBound) / (y2 - y1);
                        double alpha = 2.0D - Math.pow(beta, -(this.yta1 + 1.0D));
                        double betaq;
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.yta1 + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.yta1 + 1.0D));
                        }

                        double c1 = 0.5D * (y1 + y2 - betaq * (y2 - y1));
                        beta = 1.0D + 2.0D * (upperBound - y2) / (y2 - y1);
                        alpha = 2.0D - Math.pow(beta, -(this.yta1 + 1.0D));
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.yta1 + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.yta1 + 1.0D));
                        }

                        double c2 = 0.5D * (y1 + y2 + betaq * (y2 - y1));
                        c1 = this.randomSupplier.repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = this.randomSupplier.repairSolutionVariableValue(c2, lowerBound, upperBound);
                        if ((Double)this.randomSupplier.getRandomValue() <= 0.5D) {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c2);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c1);
                        } else {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c1);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c2);
                        }
                    } else {
                        ((Chrome)offspring.get(0)).setVariableValue(i, valueX1);
                        ((Chrome)offspring.get(1)).setVariableValue(i, valueX2);
                    }
                } else {
                    ((Chrome)offspring.get(0)).setVariableValue(i, valueX2);
                    ((Chrome)offspring.get(1)).setVariableValue(i, valueX1);
                }
            }
        }

        return offspring;
    }

    /**
     *
     * @param probability
     * @param parent1
     * @param parent2
     * @return
     */
    public List<Chrome> doCrossover1(double probability, Chrome parent1, Chrome parent2) throws Exception{
        List<Chrome> offspring = new ArrayList(2);
        offspring.add((Chrome)parent1.copy());
        offspring.add((Chrome)parent2.copy());
        if ((Double)this.randomSupplier.getRandomValue() <= probability) {
            for(int i = 0; i < parent1.getNumberOfVariables(); ++i) {
                double valueX1 = (Double)parent1.getVariableValue(i);
                double valueX2 = (Double)parent2.getVariableValue(i);
                if ((Double)this.randomSupplier.nextDouble() <= 0.5D) {
                    if (Math.abs(valueX1 - valueX2) > 1.0E-14D) {
                        double y1;
                        double y2;
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        double lowerBound = this.lowerBound;
                        double upperBound = this.upperBound;
                        double rand = (Double)this.randomSupplier.getRandomValue();
                        double beta = 1.0D + 2.0D * (y1 - lowerBound) / (y2 - y1);
                        double alpha = 2.0D - Math.pow(beta, -(this.yta1 + 1.0D));
                        double betaq;
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.yta1 + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.yta1 + 1.0D));
                        }

                        double c1 = 0.5D * (y1 + y2 - betaq * (y2 - y1));
                        beta = 1.0D + 2.0D * (upperBound - y2) / (y2 - y1);
                        alpha = 2.0D - Math.pow(beta, -(this.yta1 + 1.0D));
                        if (rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.yta1 + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.yta1 + 1.0D));
                        }

                        double c2 = 0.5D * (y1 + y2 + betaq * (y2 - y1));
                        c1 = this.randomSupplier.repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = this.randomSupplier.repairSolutionVariableValue(c2, lowerBound, upperBound);
                        if ((Double)this.randomSupplier.getRandomValue() <= 0.5D) {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c2);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c1);
                        } else {
                            ((Chrome)offspring.get(0)).setVariableValue(i, c1);
                            ((Chrome)offspring.get(1)).setVariableValue(i, c2);
                        }
                    } else {
                        ((Chrome)offspring.get(0)).setVariableValue(i, valueX1);
                        ((Chrome)offspring.get(1)).setVariableValue(i, valueX2);
                    }
                } else {
                    ((Chrome)offspring.get(0)).setVariableValue(i, valueX2);
                    ((Chrome)offspring.get(1)).setVariableValue(i, valueX1);
                }
            }
        }

        return offspring;
    }


    /**
     * @对于每一个目标函数拥挤度
     * @param chroms
     * @throws Exception
     */
    public static void updateMainfuncNd(List<Chrome> chroms) throws Exception {
        for (int i = 0; i < f_num; i++) {
            /*根据该目标函数值对该等级的个体进行排序*/
            List<ChromeBase> objective_sort = popSortByRank(chroms,i);

            ChromeBase c1 = objective_sort.get(0);
            ChromeBase c2 = objective_sort.get(chroms.size()-1);

            /*边界拥挤度计算*/
            updateBorderNd(chroms, c1, c2);

            /*记fmax为最大值，fmin为最小值*/
            double fmin = c1.getF()[i];
            double fmax = c2.getF()[i];

             /*其余拥挤度计算*/
            updateOtherNd(chroms, i, objective_sort, fmin, fmax);
        }
    }

    /**
     * @对排序后的两个边界拥挤度设为1d和nd设为无穷
     * @param chroms
     * @param c1
     * @param c2
     */
    private static void updateBorderNd(List<Chrome> chroms, ChromeBase c1, ChromeBase c2) {

        Chrome cc1 = chroms.get(c1.getIndex());
        Chrome cc2 = chroms.get(c2.getIndex());

        double distance1=  cc1.getNd();
        double distance2 = cc2.getNd();

        distance1+=Inf;
        distance2+=Inf;

        cc1.setNd(distance1);
        cc2.setNd(distance2);
    }

    /**
     * @更新剩余拥挤度
     * @param chroms
     * @param i
     * @param objective_sort
     * @param fmin
     * @param fmax
     */
    protected static void updateOtherNd(List<Chrome> chroms, int i, List<ChromeBase> objective_sort, double fmin, double fmax) {
        for (int j = 1; j < chroms.size() - 1; j++) {
            double before_f = objective_sort.get(j - 1).getF()[i];
            double next_f = objective_sort.get(j + 1).getF()[i];

            Chrome cc = chroms.get(objective_sort.get(j).getIndex());
            double distance =cc.getNd();
            if (fmax - fmin == 0) {
                distance+=Inf;
            } else {
                double nd = MathSupplier.divScale(next_f - before_f, fmax - fmin, 6);
                distance+=nd;
                distance = addScale(distance,6);
            }
            cc.setNd(distance);
        }
    }

    /**
     * @
     * @param v
     * @param scale
     * @return
     */
    public static double addScale(double v,int scale){
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if(v==Double.POSITIVE_INFINITY){
            return v;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v));
        return b1.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     *通多目标函数值对个体排序
     */
    public static List<ChromeBase> popSortByRank(List<Chrome> chrs,int index) throws Exception {
        List<ChromeBase> sorts = new ArrayList<>();
        for (Chrome chr : chrs) {
            ChromeBase c = new ChromeBase();
            c.setId(chr.getId());
            c.setF(chr.getF());
            c.setIndex(chrs.indexOf(chr));
            sorts.add(c);
        }
        Collections.sort(sorts, new Comparator<ChromeBase>() {
            @Override
            public int compare(ChromeBase o1, ChromeBase o2) {
                double f1 = o1.getF()[index];
                double f2 = o2.getF()[index];
                if(f1>f2){
                    return 1;
                }else if(f1<f2){
                    return -1;
                }else {
                    return 0;
                }

            }
        });
        return sorts;
    }
    /**
     *
     * @param chrs
     * @param Id
     * @return
     */
    public static Chrome getInstance(List<Chrome> chrs,String Id){
        if(!chrs.isEmpty()){
            for (Chrome c : chrs) {
                if(StringUtils.isNotEmpty(Id)){
                    if(c.getId().equals(Id)){
                        return c;
                    }
                }
            }
        }
        return null;
    }
    /**
     *
     * @return
     * @throws Exception
     */
    public  List<Chrome> reproduction(List<Chrome> doubleSolutions) throws Exception{
        List<Chrome> result = new ArrayList<>();
        /*二进制交叉*/
        List<Chrome> offspring = this.sbxCrossover.doCrossover(probability,doubleSolutions.get(0),doubleSolutions.get(1));
        /*变异*/
        //this..doMutation(mutationProbability,offspring.get(0));
        result.add(offspring.get(0));
        return result;
    }

    /**
     *
     * @param chrs
     * @param Id
     * @return
     */
    public static Chrome findChromeById(List<Chrome> chrs,String Id){
        if(!chrs.isEmpty()){
            for (Chrome c : chrs) {
                if(c.getId().equals(Id)){
                    return c;
                }
            }
        }
        return null;
    }
    /**
     * 二进制交叉
     * @param currentNeighbors
     * @return
     * @throws CloneNotSupportedException
     */
    public  List<Chrome> selection(List<Chrome> currentNeighbors) throws Exception {
        List<Chrome> parents = new ArrayList(2);

        parents.add(this.tournamentSelection.execute(currentNeighbors));
        if (this.archives.size() > 0) {
            parents.add(this.tournamentSelection.execute(this.archives));
        } else {
            parents.add(this.tournamentSelection.execute(currentNeighbors));
        }
        return parents;
    }

    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> selection(List<Chrome> chrss,int n_arity) throws Exception {

        List<Chrome> chds = new ArrayList<>();
        int start = 0;
        while (start<n_arity){
            List<Chrome> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
            ++start;
        }
        return chds;
    }

    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    public  List<Chrome> selection(List<Chrome> chrss,double p0) throws Exception {
        List<Chrome> chds = new ArrayList<>();
        if(Math.random()<p0){
            int start = 0;
            while (start<1){
                List<Chrome> parent = binaryTournamentSelection(chrss);
                chds.addAll(parent);
                ++start;
            }
            return chds;
        }else{
            this.computeRanking(chrss);
            this.crowdingDistance(chrss.size());
            Chrome bestSolution = chrss.get(0);
            chds.add(bestSolution);
        }
      return chds;
    }

    /**
     * 深度拷贝
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  Chrome[][] deepCopy(Chrome[][] oldPops,int popSize){
        int length = popSize+2;
        Chrome[][] newPops = new  Chrome[length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                newPops[i][j]=oldPops[i][j];
            }
        }
        return newPops;
    }

    /**
     *
     * @param p_f_lists
     * @param chr
     * @throws Exception
     */
    public  List<Chrome> insertParetoFront(List<Chrome> p_f_lists,Chrome chr) throws Exception{

        int maxLimit = 100;
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
                /*寻找当前等级i个体的最大索引*/
                int cur_index = getMaxIndex1(p_f_lists, worstRank)+1;
                /*等级为i的个体*/
                List<Chrome> tmp_lists = copyChrome(p_f_lists, cur_index, p_f_lists.size());
                /*根据拥挤度从大到小排序*/
                TDCGAUtil.popSortByNd(tmp_lists);

                Chrome c = tmp_lists.get(tmp_lists.size()-1);
                p_f_lists.remove(c);
            }
            return p_f_lists;
        }

    }

    public  Chrome[][] feedBackStrategy( Chrome[][] chroms){
        int N = 20;
        int size = this.archives.size();
        if(size>=N){
            for (int k = 0; k < N; k++) {
                int index = new Random().nextInt(size-1);
                Chrome c0 = this.archives.get(index);

                int i = new Random().nextInt(popSize);
                int j = new Random().nextInt(popSize);
                String cId = i+"_"+j;

                Chrome c = getChrome(chroms,cId);
                c.setX(c0.getX());
                c.setF(c0.getF());
                c.setPareto(c0.getPareto());
                c.setNd(0);
                c.setRank(0);
            }
        }


        return chroms;
    }

    public static Chrome getChrome(Chrome[][] chroms,String cId){
        for (int i = 0; i < chroms[0].length; i++) {
            for (int j = 0; j < chroms[i].length; j++) {
                Chrome c1 = chroms[i][j];/*种群个体*/
                if(c1.getId().equals(cId)){
                    return c1;
                }
            }
        }
        return null;
    }
    /**
     * 随机锦标赛选择
     * @param childs
     */
    public  List<Chrome> stochastiTournamentSelection(List<Chrome> childs,double Ps) throws Exception{
        List<Chrome> cchilds = new ArrayList<>();

       /* List<Chrome> parent = binaryTournamentSelection(childs);
        cchilds.addAll(parent);*/
        List<Chrome> nghos = findNeghours(childs);
        double rand = Math.random();
        Chrome ngb;
        if(rand<Ps){
            ngb = nghos.get(0);
        }else{
            ngb = nghos.get(1);
        }
        childs.clear();
        childs.add(ngb);
        return childs;
    }
    public List<Chrome> findNeghours(List<Chrome> neghours) throws Exception{
        int size = neghours.size();
        List<Chrome> nweNehgs = new LinkedList<>();
        this.computeRanking(neghours);
        List<Chrome> nghs = this.crowdingDistance(size);
        nweNehgs.add(nghs.get(0));
        nweNehgs.add(nghs.get(size-1));
        return nweNehgs;
    }

    public Chrome[][] replacement(Chrome[][] populations,Chrome currentIndivdual,Chrome newIndivdual,List<Chrome> currentNeighbors,int axis,int yaxis) throws Exception{
        newIndivdual.setAxis(-1);
        newIndivdual.setYaxis(-1);
        int result = this.compare(currentIndivdual,newIndivdual);
        if(result==1){
            /*新产生的个体支配当前个体*/
           populations = insertNewIndividualWhenDominates(populations,newIndivdual,currentIndivdual,axis,yaxis);
        }else{
           populations = insertNewIndividualWhenNonDominates(populations,currentIndivdual,newIndivdual,currentNeighbors,axis,yaxis);
        }
        return  populations;
    }

    private  Chrome[][] insertNewIndividualWhenDominates(Chrome[][] populations,Chrome offspringPopulation,Chrome currentIndivdual,int axis,int yaxis) throws Exception {
        int currentIndivdualAxis = currentIndivdual.getAxis();
        int currentIndivdualYaxis = currentIndivdual.getYaxis();
        updateAxisAndYaxisAndId(offspringPopulation,currentIndivdualAxis,currentIndivdualYaxis,currentIndivdual.getId());

        Chrome[][] results = TDCGAUtil.deepCopy(populations);

        results[axis][yaxis] = offspringPopulation;
        add(offspringPopulation);
        return results;
    }
    private  Chrome[][] insertNewIndividualWhenNonDominates(Chrome[][] populations,Chrome currentIndivdual,Chrome offspringPopulation,List<Chrome> currentNeighbors,int axis,int yaxis) throws Exception {

        //this.populations[axis][yaxis] = (Chrome) currentIndivdual.copy();
        currentNeighbors.add(offspringPopulation);

        this.computeRanking(currentNeighbors);
        for(int j = 0; j < this.getNumberOfSubfronts(); ++j) {
            this.computeDensityEstimator(this.getSubfront(j));
        }

        Collections.sort(currentNeighbors, new RankingAndCrowdingDistanceComparator());
        Chrome worst = currentNeighbors.get(currentNeighbors.size() - 1);

        Chrome[][] results = TDCGAUtil.deepCopy(populations);

        add(offspringPopulation);
        if(worst.getAxis()==-1){
            add(offspringPopulation);
        }else{
            int wostAxis = worst.getAxis();
            int worstYaxis = worst.getYaxis();
            updateAxisAndYaxisAndId(offspringPopulation,wostAxis,worstYaxis,worst.getId());
            results[wostAxis][worstYaxis] = offspringPopulation;
            add(offspringPopulation);

        }
        return results;
    }

    public void updateAxisAndYaxisAndId(Chrome offspringPopulation,int axis,int yaxis,String id){
        if(offspringPopulation!=null){
            offspringPopulation.setId(id);
            offspringPopulation.setAxis(axis);
            offspringPopulation.setYaxis(yaxis);
        }
    }

    public boolean addArchive(Chrome solution) {
        boolean solutionInserted = false;
        if (this.archives.size() == 0) {
            this.archives.add(solution);
            solutionInserted = true;
            return solutionInserted;
        } else {
            Iterator<Chrome> iterator = this.archives.iterator();
            boolean isDominated = false;
            boolean isContained = false;

            while(!isDominated && !isContained && iterator.hasNext()) {
                Chrome listIndividual = (Chrome)iterator.next();
                int flag = this.dominanceTest(solution, listIndividual);
                if (flag == -1) {
                    iterator.remove();
                } else if (flag == 1) {
                    isDominated = true;
                } else if (flag == 0) {
                    int equalflag = this.equalcompare(solution, listIndividual);
                    if (equalflag == 0) {
                        isContained = true;
                    }
                }
            }

            if (!isDominated && !isContained) {
                this.archives.add(solution);
                solutionInserted = true;
            }

            return solutionInserted;
        }
    }
    public boolean add(Chrome solution) throws CloneNotSupportedException{
        boolean success = addArchive(solution);
        if (success) {
            this.prune();
        }
        return success;
    }

    public void prune() {
        if (this.archives.size() > 100) {
            this.computeDensityEstimator(this.archives);
            Chrome worst = findWorstSolution(this.archives,this.crowdingDistanceComparator);
            this.archives.remove(worst);
        }

    }
    public  Chrome findWorstSolution(Collection<Chrome> solutionList, Comparator<Chrome> comparator) {
        if (solutionList != null && !solutionList.isEmpty()) {
            Chrome worstKnown = solutionList.iterator().next();
            Iterator var4 = solutionList.iterator();

            while(var4.hasNext()) {
                Chrome candidateSolution = (Chrome) var4.next();
                if (comparator.compare(worstKnown, candidateSolution) < 0) {
                    worstKnown = candidateSolution;
                }
            }
            return worstKnown;
        } else {
            throw new IllegalArgumentException("No solution provided: " + solutionList);
        }
    }


    public int equalcompare(Chrome solution1, Chrome solution2) {
        if (solution1 == null) {
            return 1;
        } else if (solution2 == null) {
            return -1;
        } else {
            boolean dominate1 = false;
            boolean dominate2 = false;

            for(int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
                double value1 = solution1.getObjective(i);
                double value2 = solution2.getObjective(i);
                byte flag;
                if (value1 < value2) {
                    flag = -1;
                } else if (value1 > value2) {
                    flag = 1;
                } else {
                    flag = 0;
                }

                if (flag == -1) {
                    dominate1 = true;
                }

                if (flag == 1) {
                    dominate2 = true;
                }
            }

            if (!dominate1 && !dominate2) {
                return 0;
            } else if (dominate1) {
                return -1;
            } else if (dominate2) {
                return 1;
            } else {
                return 2;
            }
        }
    }


    /**
     *
     * @throws Exception
     */
    protected  void MOcellBuilder() throws Exception {
        /*1、随机产生初始种群*/
        chroms = this.createInitialPopulation();
        int step=0;
        while(step<maxGen){
            for (int i = 0; i < chroms[0].length; i++) {
                for (int j = 0; j < chroms[i].length ; j++) {
                    Chrome currentIndividual = chroms[i][j];/*种群个体*/
                    currentIndividual.setCurChr(true);
                    List<Chrome> cur_neigh_lists = this.neighborhood.getNeighbors(chroms,i,j);

                    /*二进制选择，从周围邻居中选出1个个体作为父本*/
                    cur_neigh_lists.add(currentIndividual);
                    //List<Chrome> t_f_lists = selection(cur_neigh_lists);
                    //List<Chrome> t_f_lists = selection(cur_neigh_lists,tour);
                    List<Chrome> t_f_lists = selection(cur_neigh_lists,0.2);
                    //t_f_lists.add(currentIndividual);

                    List<Chrome> offspring = reproduction(t_f_lists);
                    offspring = evaluatePopulation(offspring);

                    this.chroms = replacement(this.chroms,currentIndividual,offspring.get(0),cur_neigh_lists,i,j);

                  /*  Chrome c = getBestSolutions(offspring,currentIndividual);
                    if(!c.getId().equals(currentIndividual.getId())){
                        Chrome c0 = (Chrome) currentIndividual.copy();
                        *//*子代占优，子代提花当前个体*//*
                        c0.setF(c.getF());
                        c0.setX(c.getX());
                        c0.setCurChr(false);
                        this.archives = insertParetoFront(this.archives,c);
                        this.populations[i][j]=c0;
                    }else{
                        currentIndividual.setCurChr(false);
                        this.populations[i][j]=currentIndividual;
                    }
*/
                }
            }
            //chroms=this.populations;
            /*反馈机制*/
            //System.out.println(step + "  gen is completed!");
            /*if(step%100==0){
                System.out.println(step + "  gen is completed!");
            }*/
            //feedBackStrategy(chroms);
            step++;

        }
        this.archives.forEach((c)->{
            double[] fx = c.getF();
            System.out.println(fx[0]+"  "+fx[1]);
        });
    }
    protected List<Chrome> evaluatePopulation(List<Chrome> population) throws CloneNotSupportedException {
        Iterator var2 = population.iterator();

        while(var2.hasNext()) {
            Chrome solution = (Chrome) var2.next();
            evolfx(solution);
            //evaluate(solution);
            add(solution.copy());
        }

        return population;
    }


    public Chrome findWorstNeghour(List<Chrome> neghours){
        if(!neghours.isEmpty()){
            this.computeRanking(neghours);
            List<Chrome> chromes = null;
            try {
                chromes = this.crowdingDistance(neghours.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chromes.get(chromes.size()-1);
        }
        return null;
    }


    @Test
    public void moc_test() throws IOException,Exception {
        //Algorithm<List<Chrome>> algorithm;

       // long startTime = System.currentTimeMillis();    //获取开始时间

        MOcellBuilder();    //测试的代码段
       // long endTime = System.currentTimeMillis();    //获取结束时间

       // System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

}
