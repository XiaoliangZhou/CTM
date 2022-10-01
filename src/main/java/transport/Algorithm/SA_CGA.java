package transport.Algorithm;

import CGA.Model.CellChrome;
import CGA.Model.Neighbour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author liangxiao.zhou
 * @version V1.0
 * @Title: SA_CGA.java
 * @Package CGA
 * @Description: 自适应元胞遗传算法
 * @date Sept 19, 2019 11:14:44 AM
 */

public class SA_CGA<T> {
    static int a = 0,b = 20;
    /*种群大小*/
    static int PopSize = 15;
    /*最大进化代数*/
    static int MaxGen =100;
    /*个体的二进制标识位数*/
    static int Preci =6;
    /*交叉概率*/
    static double Pc =0.5;
    /*交叉概率*/
    static double Pm =0.01;
    static int m = 2;

    /*1、种群初始化 */
    /**
     * 在 n*n的二维网格中，每个网格代表一个个体V(i,j)
     */
    public static  CellChrome[][] initPop(){
        int size = PopSize + m;
        CellChrome[][] chroms = new CellChrome[size][size];
        /*初始化种群*/
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                double x1 = a + Math.random() * b % (b - a + 1);
                double x2 = 0.0;
                String idstr = i+"_"+j;
                chroms[i][j] = new CellChrome(idstr,x1,x2,0.0,0.0, new ArrayList<>());
                chroms[i][j] = new CellChrome(idstr,0,0,0.0,0.0, new ArrayList<>());
                if(i<=4){
                    chroms[i][j].setFitness(1);
                }else{
                    chroms[i][j].setFitness(2);
                }
            }
        }
        chroms[1][1].setFitness(3);
        return chroms;
    }
    /*2、计算适应度*/
    /**
     * 计算元胞空间(种群)中个体V(i,j)的适应度eval(V(i,j))不是一般性，有eval(V(i,j)) = f(x(i,j))- min(f(i,j))
     */
    public static void updateAdaptive(CellChrome[][] chroms){
    /*    for(int i=0;i<chroms[0].length;i++){
            for(int j=0;j<chroms[i].length;j++){
                CellChrome c = chroms[i][j];
                double fc = GAUtil.func(c.getX1()) ;
                if(fc<0) fc=0; *//*避免出现负值*//*
                c.setFitness(fc);
            }
        }*/

        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j <chroms[i].length-1; j++) {
                CellChrome father = chroms[i][j];
                if(father.getOlcs().size()!=0){
                    Neighbour ngh = father.getOlcs().get(0);
                    father.setFitness(ngh.getFitness());
                }
            }
        }
    }
    /*选择操作*/
    public static CellChrome[][]  select(CellChrome[][] chroms){
        int maxLength = chroms[0].length;
        for (int i = 1; i < maxLength-1; i++) {
            for (int j = 1; j < maxLength-1; j++) {
                CellChrome father = chroms[i][j];
                double c_f = father.getFitness();/*个体V(i,j)适应度*/
                List<Neighbour> olist = father.getOlcs();
                olist.clear();
                /*获取个体V(i,j)的Moore型邻居*/
                ruleOfMooreType2(chroms, i, j, c_f, olist);
                //ruleOfVonNeumannType(chroms, i, j, c_f, olist);
            }
        }
        /*适应度评价*/
        evalAction(chroms, maxLength);
        return chroms;
    }

    /**
     *
     * @param chroms
     * @param maxLength
     */
    protected static void evalAction(CellChrome[][] chroms, int maxLength) {
        for (int i = 1; i < maxLength-1; i++) {
            for (int j = 1; j < maxLength-1; j++) {
                CellChrome father = chroms[i][j];
                List<Neighbour> childs = father.getOlcs();
                int c_size = childs.size();
                /*计算适应度*/
                //fitAction(chroms,childs, c_size);
                /*轮盘赌操作*/
                //routtleAction(chroms,maxLength,father, childs);
                TD_CGA.binaryTournamentSelection(father, childs);
            }
        }
    }

    /**
     *
     * @param neighbours
     * @param c_size
     */
    protected static void fitAction(CellChrome[][] chroms,List<Neighbour> neighbours, int c_size) {
        if(c_size>0){
            int index = 0;
            double sum = 0;
            /*适应度之和*/
            while (index < c_size) {
                Neighbour neigh = neighbours.get(index);
                sum += neigh.getFitness();
                index++;
            }
            /*个体V(i,j)的邻居V(a,b)被选择的概率*/
            double temp = 0.0;
            for (Neighbour neigh : neighbours) {
                double p = neigh.getFitness() / sum;
                neigh.setPl(p);
                temp+=p;
                neigh.setPr(temp);
            }
        }
    }

    /**
     * 轮盘赌操作
     * @param maxLength
     * @param neighs
     */
    protected static void routtleAction(CellChrome[][] chroms,int maxLength,CellChrome father, List<Neighbour> neighs) {
        int choose=0;
        while(choose<maxLength-m){
            double r = Math.random();
            Neighbour neigh = new Neighbour();
            if(neighs.size()==0){
                neighs.clear();
                neigh.setnId(father.getId());
                neigh.setPl(0.0);
                neigh.setPr(1.0);
                neigh.setX1(father.getX1());
                neigh.setFitness(father.getFitness());

                neighs.add(neigh);/*中心个体适应度最大*/
            }else{
                for (int index = 0; index < neighs.size(); index++) {
                    Neighbour neighbour = neighs.get(index);
                    if(index==0){
                        if(r<neighbour.getPr()){
                            neighs.clear();
                            neighs.add(neighbour);
                            break;
                        }
                    }else{
                        if(r<=neighbour.getPr()& r>neighs.get(index-1).getPr()){
                            neighs.clear();
                            neighs.add(neighbour);
                            break;
                        }
                    }
                }
            }
            choose++;
        }
    }

    /**
     *
     * @param chroms
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    protected static void ruleOfMooreType(CellChrome[][] chroms, int i, int j, double c_f, List<Neighbour> olist) {
        for (int k = i-1; k<= i+1; k++) {
            for (int m = j-1; m<= j+1; m++) {
                CellChrome child = chroms[k][m];
                double c_h = child.getFitness();/*个体V(i,j)邻居适应度*/
                Neighbour neigh = new Neighbour();
                if(!(k==i&m==j)){
                    if (c_h>=c_f) {
                        /*选出适应度大于等于V(i,j)的邻居元胞*/
                        neigh.setnId(child.getId());
                        neigh.setPl(0.0);
                        neigh.setPr(0.0);
                        neigh.setX1(child.getX1());
                        neigh.setFitness(child.getFitness());
                        olist.add(neigh);
                    }
                }

            }
        }
    }

    /**
     *
     * @param chroms
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    protected static void ruleOfMooreType2(CellChrome[][] chroms, int i, int j, double c_f, List<Neighbour> olist) {
        for (int k = i-1; k<= i+1; k++) {
            for (int m = j-1; m<= j+1; m++) {
                CellChrome child = chroms[k][m];
                double c_h = child.getFitness();/*个体V(i,j)邻居适应度*/
                Neighbour neigh = new Neighbour();
                /*选出适应度大于等于V(i,j)的邻居元胞*/
                neigh.setnId(child.getId());
                neigh.setPl(0.0);
                neigh.setPr(0.0);
                neigh.setX1(child.getX1());
                neigh.setFitness(child.getFitness());
                olist.add(neigh);
            }
        }
    }


    /**
     * ruleOfVonNeumannType
     * @param chroms
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    protected static void ruleOfVonNeumannType(CellChrome[][] chroms, int i, int j, double c_f, List<Neighbour> olist) {
        for (int k = i-1; k<= i+1; k++) {
            CellChrome chr1 = chroms[k][j];
            if(i!=k){
                double c_h = chr1.getFitness();/*个体V(i,j)邻居适应度*/
                Neighbour neigh = new Neighbour();
                if (c_h>=c_f) {
                    /*选出适应度大于等于V(i,j)的邻居元胞*/
                    neigh.setnId(chr1.getId());
                    neigh.setPl(0.0);
                    neigh.setPr(0.0);
                    neigh.setX1(chr1.getX1());
                    neigh.setFitness(chr1.getFitness());
                    olist.add(neigh);
                }
            }
        }
        for (int m = j-1; m<= j+1; m++) {
            if(j!=m){
                CellChrome chr2 = chroms[i][m];
                double c_h = chr2.getFitness();/*个体V(i,j)邻居适应度*/
                Neighbour neigh = new Neighbour();
                if (c_h>=c_f) {
                    /*选出适应度大于等于V(i,j)的邻居元胞*/
                    neigh.setnId(chr2.getId());
                    neigh.setPl(0.0);
                    neigh.setPr(0.0);
                    neigh.setX1(chr2.getX1());
                    neigh.setFitness(chr2.getFitness());
                    olist.add(neigh);
                }
            }
        }
    }
    /*交叉*/
    /**
     * 以Moore型元胞为例假设元胞个体V(a,b)在选择操作中被中心元胞V(i,j)选中,这里定义交叉概率为Pc,则个体V(i,j)以如下方式对染色体进行更新
     *   exp: x(i,j)' = (1-Pc)*x(i,j) + Pc*x(a,b)
     */

    /**
     ** 如果在类中定义使用泛型的静态方法，需要添加额外的泛型声明（将这个方法定义成泛型方法）
     *  即使静态方法要使用泛型类中已经声明过的泛型也不可以。
     *  如：public static void show(T t){..},此时编译器会提示错误信息："StaticGenerator cannot be refrenced from static context"
     * @param chroms
     */
    public static<T> void  crossover(CellChrome[][] chroms) {
        CellChrome maxChr = ESS_CGA.findMaxAdaptness(chroms);
        double maxfit = maxChr.getFitness(); /*当代种群最大适应度*/
        int maxLength = chroms[0].length;
        for (int i = 1; i < maxLength-1; i++) {
            for (int j = 1; j < maxLength - 1; j++) {
                CellChrome chr = chroms[i][j];
                double xi = chr.getX1();

                Neighbour bgh = chr.getOlcs().get(0);
                double xab = bgh.getX1();
                double Px=0;

                //double x = (1 - Pc) * xi +  Pc * xab ; /*固定交叉算子*/
                double x = (1 - Pc) * xi + Pc * (1 - Pc) * xab + Math.pow(Pc, 2) * maxChr.getX1(); /*固定交叉算子*/

                chr.setX1(x);
            }
        }
    }
    /*变异操作*/
    public static void mutation(CellChrome[][] chroms){
        double delt = 0.01;
        int maxLength = chroms[0].length;
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxLength; j++) {
                CellChrome chr = chroms[i][j];
                double r = Math.random();
                if (r < Pm) {
                    double x = chr.getX1();
                    x = x + (delt*(new Random().nextGaussian()));
                    chr.setX1(x);
                }

            }
        }
    }


    /**
     * 得到种群拥有最大适应度的个体
     * @param chroms
     * @return
     */
    public static CellChrome findMaxAdaptness(CellChrome[][] chroms){
        CellChrome chrome =chroms[1][1];
        for (int i = 1; i <chroms[0].length -1; i++) {
            for (int j = 1; j < chroms[i].length-1; j++) {
                CellChrome chr = chroms[i][j];
                if(chr!=null){
                    if(chr.getFitness()>chrome.getFitness()){
                        chrome=chr;
                    }
                }
            }
        }
        return chrome;
    }
    /**
     * 根据对象id 获取该对象
     * @param chroms
     * @param cstr
     * @return
     */
    public static CellChrome getChr(CellChrome[][] chroms,String cstr){
        for (int i = 0; i <chroms[0].length; i++) {
            for (int j = 0; j < chroms[0].length; j++) {
                CellChrome chr = chroms[i][j];
                if(chr!=null){
                    if(chr.getId().equals(cstr)){
                        return chr;
                    }
                }
            }
        }
        return null;
    }


}
