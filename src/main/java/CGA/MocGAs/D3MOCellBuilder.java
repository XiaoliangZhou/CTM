package CGA.MocGAs;
import CGA.Model.Chrome;
import CGA.comparator.CrowdingDistanceComparator;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.evaluator.SolutionListEvaluator;
import CGA.evaluator.impl.SequentialSolutionListEvaluator;
import CGA.mutation.PolynomialMutation;
import CGA.neighborhood.Neighborhood;
import CGA.selection.TournamentSelection;
import CGA.neighborhood.impl.C3D;
import CGA.problem.Problem;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.uma.jmetal.util.JMetalException;
import transport.math.util.MathSupplier;
import util.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class D3MOCellBuilder<S extends Chrome> implements AlgorithmBuilder<D3MOCell<S>> {
    static int tour = 2;
    static double Inf = Double.POSITIVE_INFINITY;
    static int popSize = 5;
    static int f_num = 2;
    static int x_num = 10;
    static double a1 = 0, b1 = 5;
    private double yta1 = 20;
    private double yta2 = 20;
    static String fname = "ZDT6";
    static double probability=1.0;
    static double mutationProbability=0.01;
    static int maxGen=200;

    private Chrome[][][] chroms;
    private List<Chrome> archives ;

    protected int martix;
    protected int evaluations;
    protected int populationSize;
    protected int maxEvaluations;
    protected Problem<S> problem;
    protected SBXCrossover crosserOperator;
    protected DifferentialEvolutionCrossover differentialEvolutionCrossover;
    protected PolynomialMutation mutationOperator;
    protected TournamentSelection selectionOperator;
    protected SolutionListEvaluator<S> evaluator;
    protected Neighborhood neighborhood;
    private Comparator<Chrome> crowdingDistanceComparator = new CrowdingDistanceComparator();

    public D3MOCellBuilder() {
    }

    public D3MOCellBuilder(Problem<S> problem, SBXCrossover sbxCrosser,DifferentialEvolutionCrossover differentialEvolutionCrossover, PolynomialMutation mutationOperator) {
        this.problem = problem;
        this.martix = 5;
        this.maxEvaluations = 25000;
        this.populationSize = 100;
        this.crosserOperator = sbxCrosser;
        this.differentialEvolutionCrossover = differentialEvolutionCrossover;
        this.mutationOperator = mutationOperator;
        this.neighborhood = new C3D( this.martix, this.martix, this.martix -1);
        this.evaluator = new SequentialSolutionListEvaluator();
        this.archives =  new ArrayList(populationSize);
        this.chroms = new Chrome[popSize][popSize][popSize-1];
    }

    public D3MOCellBuilder<S> setSelectionOperator(TournamentSelection selectionOperator) {
        if (selectionOperator == null) {
            throw new JMetalException("selectionOperator is null");
        } else {
            this.selectionOperator = selectionOperator;
            return this;
        }
    }
    public D3MOCellBuilder<S> setMaxEvaluations(int maxEvaluations) {
        if (maxEvaluations < 0) {
            throw new JMetalException("maxEvaluations is negative: " + maxEvaluations);
        } else {
            this.maxEvaluations = maxEvaluations;
            return this;
        }
    }

    public D3MOCell<S> build() {
        D3MOCell<S> algorithm = new D3MOCell(this.problem,
                this.martix,
                this.evaluations,
                this.maxEvaluations,
                this.evaluator,
                this.neighborhood,
                this.archives,
                this.selectionOperator,
                this.crosserOperator,
                this.differentialEvolutionCrossover,
                this.mutationOperator,this.crowdingDistanceComparator);
        return algorithm;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public Problem<S> getProblem() {
        return problem;
    }

    public int getMartix() {
        return martix;
    }

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
            Chrome c2 = chrs.get(cIndex[1]);


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
        List<Chrome> offspring = this.crosserOperator.execute(doubleSolutions);
        /*变异*/
        this.mutationOperator.execute(offspring.get(0));
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

        parents.add(this.selectionOperator.execute(currentNeighbors));
        if (this.archives.size() > 0) {
            parents.add(this.selectionOperator.execute(this.archives));
        } else {
            parents.add(this.selectionOperator.execute(currentNeighbors));
        }
        return parents;
    }

    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    public  List<Chrome> selection(List<Chrome> chrss,int n_arity) throws Exception {

        List<Chrome> chds = new ArrayList<>();
        int start = 0;
        while (start<n_arity){
            List<Chrome> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
            ++start;
        }
        //Chrome result = this.tournamentSelection.getBestSolution(chds.get(0), chds.get(1), new RankingAndCrowdingDistanceComparator());
        //chds.clear();
        //chds.add(result);
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
     *
     * @throws Exception
     */
    protected  void MOcellBuilder() throws Exception {
        /*1、随机产生初始种群*/
        //chroms = this.createInitialPopulation();
        int step=0;
        while(step<maxGen){
            for (int i = 0; i < chroms[0].length; i++) {
                for (int j = 0; j < chroms[i].length ; j++) {
                    for (int k = 0; k < chroms[j].length-1 ; k++) {

                        Chrome currentIndividual = chroms[i][j][k];/*种群个体*/
                        currentIndividual.setCurChr(true);
                        List<Chrome> cur_neigh_lists = this.neighborhood.getNeighbors(chroms,i,j,k);

                        /*二进制选择，从周围邻居中选出1个个体作为父本*/
                        //cur_neigh_lists.add(currentIndividual);
                        //List<Chrome> t_f_lists = selection(cur_neigh_lists);
                        //List<Chrome> t_f_lists = selection(cur_neigh_lists,tour);
                        //List<Chrome> t_f_lists = selection(cur_neigh_lists,currentIndividual,0.5);
                        //t_f_lists.add(currentIndividual);

                        //List<Chrome> offspring = reproduction(t_f_lists);
                      //  offspring = evaluatePopulation(offspring);

                        //this.chroms = replacement(this.chroms,currentIndividual,offspring.get(0),cur_neigh_lists,i,j,k);
                    }
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
        //this.evaluator.evaluate(population,problem);
        while(var2.hasNext()) {
            Chrome solution = (Chrome) var2.next();
            evolfx(solution);
            //evaluate(solution);
            //add(solution.copy());
        }

        return population;
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
