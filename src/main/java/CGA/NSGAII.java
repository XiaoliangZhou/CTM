package CGA;

import CGA.MocGAs.MOCell;
import CGA.MocGAs.Pareto;
import CGA.Model.Chrome;
import algorithm.TDCGAUtil;
import org.junit.Test;
import transport.math.util.MathSupplier;
import transport.math.util.randomSupplier;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class NSGAII {

    static int tour = 2;
    static double Inf = Double.POSITIVE_INFINITY;
    static int popSize = 100;
    static int f_num = 2;
    static int x_num = 10;
    static int c = 16;
    static double a = 0, b = 1;
    static double yta1 = 20;
    static double yta2 = 20;
    static String fname = "ZDT1";
    static double pc=0.9;
    static double pm=1/100;
    static int maxGen=2500;

    private MOCell moc = new MOCell();


    private randomSupplier randomSupplier = new randomSupplier();
    private  double lowerBound = 0;
    private  double upperBound = 1;

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
     * 初始化种群
     * @return
     */
    public  List<Chrome> initPop(){
        List<Chrome> chromes = new ArrayList<>();
        int index=0;
        while (index<popSize){
            Chrome chr = new Chrome();
            List<Double> x = chr.getX();
            for (int j = 0; j < x_num; j++) {
                double value = a + new Random().nextDouble()*(b-a);
                chr.setVariableValue(j,value);
            }
            evolfx(chr);
            chr.setId(String.valueOf(index));
            chr.setX(x);
            chromes.add(chr);
            index++;
        }
        return chromes;
    }

    /**
     * 初始种群的非支配排序
     * @return
     */
    public static Map<Integer,Set<String>> nonDominationSort(int popSize, List<Chrome> chrs){
        Map<Integer,Set<String>> rankMps = new TreeMap<Integer, Set<String>>();
        Set<String> kset = new TreeSet<>();
        int rank=1;
        for (int i = 0; i < popSize; i++) {
            Chrome chr = chrs.get(i);
            Pareto ptu = new Pareto();
            int np = 0;/*被支配个体数目*/
            Set<Integer> pset = new HashSet<>();/*存放该个体支配解的集合s*/
            double[] f0 = chr.getF();/*目标函数集合*/
            for (int j = 0; j < popSize; j++) {
                if(i!=j){
                    int less=0,equal=0,greater=0;
                    double[] f1 = chrs.get(j).getF();/*目标函数集合*/
                    for (int k = 0; k < f_num; k++) {
                        if (f0[k] < f1[k]) {
                            less += 1;
                        } else if (f0[k] == f1[k]) {
                            equal += 1;
                        } else {
                            greater += 1;
                        }
                    }
                    if(equal!=f_num){
                        if (less == 0) {
                            np += 1;/*被支配个体数*/
                            ptu.setN(np);
                        }else if (greater == 0) {
                            pset.add(j);/*该个体支配的解的集合s(个体索引)*/
                        }
                    }
                }
            }
            ptu.setPset(pset);
            chr.setPareto(ptu);
            /*等级pareto==1的非支配个体*/
            if(np==0){
                chr.setRank(1);
                kset.add(String.valueOf(i));
            }
        }
        rankMps.put(rank,kset);
        while(!rankMps.get(rank).isEmpty()){
            Set<String> temp = new TreeSet<>();
            for (String index : rankMps.get(rank)) {
                Chrome chrr = getChromeById(chrs,index);/*遍历当前集合中每个个体*/
                Set<Integer> paset = chrr.getPareto().getPset();
                if(!paset.isEmpty()){
                    for (Integer idx : paset) {
                        Chrome pchr = chrs.get(idx);/*遍历当前个体支配的解集合*/
                        Pareto pto = pchr.getPareto();
                        int nl = pto.getN();
                        nl -= 1;
                        pto.setN(nl);
                        if (nl == 0) {
                            pchr.setRank(rank + 1);
                            temp.add(pchr.getId());
                        }
                    }
                    paset.clear();
                }

            }
            rank+=1;
            rankMps.put(rank,temp);
        }
        return rankMps;
    }


   /* *//**
     * 初始种群的非支配排序
     * @return
     *//*
    public static Map<Integer,Set<String>> nonDominationSort(int popSize, List<Chrome> chrs){
        Map<Integer,Set<String>> rankMps = new TreeMap<Integer, Set<String>>();
        Set<String> kset = new TreeSet<>();
        int rank=1;
        for (int i = 0; i < popSize; i++) {
            Chrome chr = chrs.get(i);
            Pareto ptu = new Pareto();
            int np = 0;*//*被支配个体数目*//*
            Set<Integer> pset = new HashSet<>();*//*存放该个体支配解的集合s*//*
            double[] f0 = chr.getF();*//*目标函数集合*//*
            for (int j = 0; j < popSize; j++) {
                if(i!=j){
                    int less=0,equal=0,greater=0;
                    double[] f1 = chrs.get(j).getF();*//*目标函数集合*//*
                    for (int k = 0; k < f_num; k++) {
                        if (f0[k] < f1[k]) {
                            less += 1;
                        } else if (f0[k] == f1[k]) {
                            equal += 1;
                        } else {
                            greater += 1;
                        }
                    }
                    if(equal!=f_num){
                        if (less == 0) {
                            np += 1;*//*被支配个体数*//*
                            ptu.setN(np);
                        }else if (greater == 0) {
                            pset.add(j);*//*该个体支配的解的集合s(个体索引)*//*
                        }
                    }
                }
            }
            ptu.setPset(pset);
            chr.setPareto(ptu);
            *//*等级pareto==1的非支配个体*//*
            if(np==0){
               chr.setRank(1);
               kset.add(chr.getId());
            }
        }
        rankMps.put(rank,kset);
        while(!rankMps.get(rank).isEmpty()){
            Set<String> temp = new TreeSet<>();
            for (String index : rankMps.get(rank)) {
                Chrome chrr = getChromeById(chrs,index);*//*遍历当前集合中每个个体*//*
                Set<Integer> paset = chrr.getPareto().getPset();
                if(!paset.isEmpty()){
                    for (Integer idx : paset) {
                        Chrome pchr = chrs.get(idx);*//*遍历当前个体支配的解集合*//*
                        Pareto pto = pchr.getPareto();
                        int nl = pto.getN();
                        nl -= 1;
                        pto.setN(nl);
                        if (nl == 0) {
                            pchr.setRank(rank + 1);
                            temp.add(pchr.getId());
                        }
                    }
                    paset.clear();
                }

            }
            rank+=1;
            rankMps.put(rank,temp);
        }
        return rankMps;
    }*/
    /**
     * @计算拥挤度进行排序
     * @param rankMaps
     * @param chrs
     * @return
     * @throws Exception
     */
    public static List<Chrome> crowdingDistanceSort(Map<Integer,Set<String>> rankMaps, List<Chrome> chrs) throws Exception{
        List<Chrome> newchrs = new ArrayList<>();
        /*1、种群排序*/
        //TDCGAUtil.popSort(chrs);
        /*计算拥挤度*/
        for (Map.Entry<Integer, Set<String>> entry : rankMaps.entrySet()) {
            Set<String> sets = entry.getValue();
            if(!sets.isEmpty()){
                List<Chrome> chroms = MOCell.createTempList(chrs, sets);/*临时存放chrome 对象*/
                /*对于每一个目标函数fm*/
                MOCell.updateMainfuncNd(chroms);
                /*重新组装*/
                chroms.forEach((chr)->{
                    newchrs.add(chr);
                });
            }
        }
        return newchrs;
    }
    /**
     * @计算拥挤度进行排序
     * @param rankMaps
     * @param chrs
     * @return
     * @throws Exception
     */
   /* public static List<Chrome> crowdingDistanceSort1(Map<Integer,Set<String>> rankMaps,List<Chrome> chrs) throws Exception{
        List<Chrome> newchrs = new ArrayList<>();
        *//*1、种群排序*//*
        TDCGAUtil.popSort(chrs);
        *//*计算拥挤度*//*
        for (Map.Entry<Integer, Set<String>> entry : rankMaps.entrySet()) {
            Set<String> sets = entry.getValue();
            if(!sets.isEmpty()){
                List<Chrome> chroms = new ArrayList<>();*//*储存当前处理的等级的个体*//*
                for (String Id : sets) {
                    chroms.add((Chrome) getChromeById(chrs,Id).clone());
                }
                *//*对于每一个目标函数fm*//*
                for (int i = 0; i < f_num; i++) {
                    *//*根据该目标函数值对该等级的个体进行排序*//*
                    List<ChromeBase> objective_sort = TDCGAUtil.popSortByRank(chroms,i);
                    *//*记fmax为最大值，fmin为最小值*//*
                    ChromeBase c1 = objective_sort.get(0);
                    ChromeBase c2 = objective_sort.get(chroms.size()-1);

                    double fmin = c1.getF()[i];
                    double fmax = c2.getF()[i];

                    *//*对排序后的两个边界拥挤度设为1d和nd设为无穷*//*
                    Chrome cc1 = getChromeById(chroms,c1.getId());
                    Chrome cc2 = getChromeById(chroms,c2.getId());
                    cc1.getNd()[i]=Inf;
                    cc2.getNd()[i]=Inf;

                    *//*拥挤度计算*//*
                    getNd(chroms, i, objective_sort, fmin, fmax); *//*计算nd=nd+(fm(i+1)-fm(i-1))/(fmax-fmin)*//*
                }
               chroms.forEach((chr)->{
                   newchrs.add(chr);
               });
            }
        }
        return newchrs;
    }

    *//**
     * @计算拥挤度
     * @param chroms
     * @param i
     * @param objective_sort
     * @param fmin
     * @param fmax
     *//*
    protected static void getNd(List<Chrome> chroms, int i, List<ChromeBase> objective_sort, double fmin, double fmax) {
        for (int j = 1; j < chroms.size() - 1; j++) {
            double before_f = objective_sort.get(j - 1).getF()[i];
            double next_f = objective_sort.get(j + 1).getF()[i];

            Chrome cc = getChromeById(chroms, objective_sort.get(j).getId());
            if (fmax - fmin == 0) {
                cc.getNd()[i] = Inf;
            } else {
                double nd = MathSupplier.divScale(next_f - before_f, fmax - fmin, 6);
                cc.getNd()[i] = nd;
            }
        }
    }*/

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
        for (int i = 0; i < n; i++) {
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
                if (nd1 == nd2) cId = (Math.random() > 0.5) ? c1.getId() : c2.getId();/*等级相同、拥挤度相同 则随机选择其中一个*/
            }
            newchrs1.add((Chrome) getChromeById(chrs,cId).copy());
        }
        return newchrs1;
    }
    /**
     *
     * @param olist
     * @return
     * @throws Exception
     */
    public  List<Chrome> SBXAndMutation(List<Chrome> olist) throws Exception{
        List<Chrome> offspring = new ArrayList<>();
        int size = olist.size();
        for (int i = 0; i < size; i++) {
            Integer[] cdx = findDiffChromIndex(olist); /*随机选取两个父代个体、保证选出的是不同个体*/

            /*确定两个父代个体不是同一个*/
            Chrome c1 = olist.get(cdx[0]);
            Chrome c2 = olist.get(cdx[cdx.length - 1]);

            Chrome off_1 = (Chrome) c1.copy();/*子代个体*/
            Chrome off_2 = (Chrome) c2.copy();
            /*二进制交叉*/
            //SBX(c1,c2,off_1,off_2);
            offspring = doCrossover(pc,offspring,c1,c2);
            /*变异*/
            //polynomialMutation(offspring, off_1);
            //polynomialMutation(offspring, off_2);
            doMutation(pm,offspring,offspring.get(0));
            doMutation(pm,offspring,offspring.get(1));
        }
        return offspring;
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
     *
     * @param probability
     * @param parent1
     * @param parent2
     * @return
     */
    public List<Chrome> doCrossover(double probability,List<Chrome> offspring, Chrome parent1, Chrome parent2) throws Exception{
        offspring.add((Chrome)parent1.copy());
        offspring.add((Chrome)parent2.copy());
        if ((Double)this.randomSupplier.nextDouble() <= probability) {
            for(int i = 0; i < parent1.getX().size(); ++i) {
                double valueX1 = (Double)parent1.getX().get(i);
                double valueX2 = (Double)parent2.getX().get(i);
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
                        double rand = (Double)this.randomSupplier.nextDouble();
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
                        c1 = this. randomSupplier.repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = this. randomSupplier.repairSolutionVariableValue(c2, lowerBound, upperBound);
                        if ((Double)this.randomSupplier.nextDouble() <= 0.5D) {
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
     * @多项式变异算子
     * @param probability
     * @param offspring
     * @param off_1
     */
    public  void doMutation(double probability,List<Chrome> offspring, Chrome off_1){
        List<Double> x = off_1.getX();
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
                off_1.setVariableValue(j,y);
            }
        }
        /*计算子代目标函数值*/
       evolfx(off_1);

    }
    /**
     * @模拟二进制交叉
     * @param c1
     * @param c2
     * @param off_1
     * @param off_2
     */
    protected  void SBX(Chrome c1, Chrome c2, Chrome off_1, Chrome off_2) throws Exception {
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
    private static void updateX(Chrome c1, Chrome c2,Chrome off_1, int j, double v, double v2) {
        double v3 = v * c1.getX().get(j) + v2 * c2.getX().get(j);
        double x = mul(0.5, v3);
        x = (x > b) ? b : (x < a) ? a : x;
        off_1.setVariableValue(j,x);
    }

    /**
     * @ 计算gama或者yita
     * @param yta
     * @return
     */
    private static double getGamaOrYiTa(double yta) {
        double xx = Math.random();
        double gam = 0;
        if (xx < 0.5) {
            gam = Math.pow(2 * xx, 1 / (yta + 1));
        } else {
            gam = Math.pow(1 /(2 * (1 - xx)), 1 / (yta + 1));
        }
        return gam;
    }

    /**
     * @
     * @param offspring
     * @param off_1
     */
    protected  void polynomialMutation(List<Chrome> offspring, Chrome off_1) {
        List<Double> xx = off_1.getX();
        double v = 0;
        for (int j = 0; j < x_num; j++) {
            if (Math.random() < pm) {
                double delta = getGamaOrYiTa(yta2);
                delta = new BigDecimal(delta).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();/*保留6位有效数字*/
                /*多项式变异*/
                v = xx.get(j) + delta;
                v = (v > b) ? b : (v < a) ? a : v;
                off_1.setVariableValue(j,v);
            }
        }
        /*计算子代目标函数值*/
        evolfx(off_1);
        offspring.add(off_1);
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
     *
     * @param chrs
     * @param Id
     * @return
     */
    public static Chrome getChromeById(List<Chrome> chrs,String Id){
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
     * @产生新父代个体(精英保留策略)
     * @param popSize 种群大小
     * @param fchrs 父代个体
     * @param cchrs 子代个体
     * @return
     * @throws Exception
     */
    public  List<Chrome> saveStrategyOfElite(int popSize,List<Chrome> fchrs,List<Chrome> cchrs) throws Exception{

        List<Chrome> chroms = new ArrayList<>();
        /*1、父子代合并*/
        List<Chrome> fc_lists = addAll(fchrs,cchrs);
        /*2、快速非支配排序*/
        int pop = fc_lists.size();

        moc.computeRanking(fc_lists);
        List<Chrome> n_fc_lists = moc.crowdingDistance(pop);

        /********************************精英保留策略***************************************/
        /*(1) 根据pareto等级从高到低进行排序*/
        TDCGAUtil.popSort(n_fc_lists);
        List<Chrome> rk_lists = TDCGAUtil.deepCopyed(n_fc_lists);
        /*(2) 求出最高的pareto等级*/
        int max_rank = rk_lists.get(rk_lists.size() - 1).getRank();
        /*(3) 根据排序后的顺序，将等级相同的种群整个放入父代种群中，直到某一层不能全部放下为止*/
        int pre_index=0;
        for (int i = 1; i <= max_rank; i++) {
            /*寻找当前等级i个体的最大索引*/
            int cur_index = getMaxIndex(rk_lists, i) + 1;
            if (cur_index > popSize) {
                /*剩余群体数*/
                int rem_index = popSize - pre_index;
                /*等级为i的个体*/
                List<Chrome> tmp_lists = copyChrome(rk_lists, pre_index, cur_index);
                /*根据拥挤度从大到小排序*/
                TDCGAUtil.popSortByNd(tmp_lists);
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
    public static List<Chrome> copyChrome(List<Chrome> srcs,int start,int end) throws CloneNotSupportedException{
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
               /* chr.setNd(new double[]{0, 0});*/
                chr.setId(String.valueOf(com_list.size()));
                com_list.add(chr);
            }
        }
    }
    /**
     * 二进制交叉
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    protected static List<Chrome> tournamentSelection(List<Chrome> chrss,int tour) throws Exception {
        List<Chrome> chds = new ArrayList<>();
        for (int i = 0; i < tour; i++) {
            List<Chrome> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
        }
        return chds;
    }




    protected  void NSGA_II_Builder() throws Exception {
        List<Chrome> chroms = new ArrayList<>();
        /*种群初始化*/
        chroms = initPop();

      /*  *//*非支配排序*//*
        Map<Integer, Set<String>> rankMaps = nonDominationSort(popSize,chroms);
        *//*拥挤度计算*//*
        chroms = crowdingDistanceSort(rankMaps,chroms);*/
        moc.computeRanking(chroms);
        chroms = moc.crowdingDistance(chroms.size());
        int step=0;
        while (step<maxGen){
            /*二进制竞赛选择(k取了pop/2，所以选两次)*/
            List<Chrome> chds = tournamentSelection(chroms,tour);
            /*模拟二进制交叉与多项式变异*/
            List<Chrome> off_sprs =  SBXAndMutation(chds);
            /*精英保留*/
            if(step%100==0){
                System.out.println(step + "gen is completed!");
            }
            chroms = saveStrategyOfElite(popSize,chroms,off_sprs);
            step++;
        }
        for (Chrome chr : chroms) {
            double[] fx = chr.getF();
            System.out.println(fx[0]+"  "+fx[1]);
        }
    }

    @Test
    public void NsGAII_test() throws IOException,Exception {

        long startTime = System.currentTimeMillis();    //获取开始时间

        NSGA_II_Builder();    //测试的代码段

        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }
}
