package transport.Algorithm;

import CGA.Model.CellChrome;
import CGA.Model.Neighbour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TD_CGA {
    static double a = -5,b = 5;
    /*种群大小*/
    static int PopSize = 9;
    /*交叉概率*/
    static double Pc =0.7;
    static double Ps =0.5;
    /*交叉概率*/
    static double Pm =0.01;
    static int m = 2;


    /*1、种群初始化 */
    /**
     * 在 n*n*n的二维网格中，每个网格代表一个个体V(i,j,k)
     */
    public static  CellChrome[][][] initPop(){
        int size = PopSize + m;
        CellChrome[][][] chroms = new CellChrome[size][size][size];
        /*初始化种群*/
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                for(int k=0;k<size;k++){
                    double x1 = a + Math.random() * (b - a + 1);
                    double x2 = a + Math.random() * (b - a + 1);
                    double fx = GAUtil.func(x1,x2);
                    String idstr = i + "_" + j + "_" + k;
                    chroms[i][j][k] = new CellChrome(idstr,x1,x2,0.0,fx, new ArrayList<>());
                   /* chroms[i][j][k] = new CellChrome(idstr,0,0,0.0,0.0, new ArrayList<>());
                    if(i<=4){
                        chroms[i][j][k].setFitness(1);
                    }else{
                        chroms[i][j][k].setFitness(2);
                    }*/
                }

            }
        }
        //chroms[4][5][1].setFitness(3);
        return chroms;
    }
    /*2、计算适应度*/
    /**
     * 计算元胞空间(种群)中个体V(i,j)的适应度eval(V(i,j))不是一般性，有eval(V(i,j)) = f(x(i,j))- min(f(i,j))
     */
    public static void updateAdaptive(CellChrome[][][] chroms){
        double minfx = TD_CGA.getMinFitness(chroms);
        for(int i=0;i<chroms[0].length;i++){
            for(int j=0;j<chroms[i].length;j++){
                for(int k=0;k<chroms[j].length;k++){
                    CellChrome c = chroms[i][j][k];
                    double fit = GAUtil.func(c.getX1(),c.getX2());
                    c.setFitness(fit - minfx);
                }
            }
        }
       /* for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j <chroms[i].length-1; j++) {
                for (int k = 1; k < chroms[j].length-1; k++) {
                    CellChrome father = chroms[i][j][k];
                    if(father.getOlcs().size()!=0){
                        Neighbour ngh = father.getOlcs().get(0);
                        if(father.getFitness()<ngh.getFitness()){
                            father.setFitness(ngh.getFitness());
                        }
                    }
                }
            }
        }*/
    }
    /*选择操作*/
    public static CellChrome[][][]  select(CellChrome[][][] chroms){
        CellChrome father;
        for (int i = 1; i < chroms[0].length - 1; i++) {
            for (int j = 1; j < chroms[i].length - 1; j++) {
                for (int k = 1; k < chroms[j].length-1; k++) {
                    father = chroms[i][j][k];
                    double c_f = father.getFitness();/*个体V(i,j)适应度*/
                    ruleOfThreeSpaceType(chroms, i, j, k, c_f, father.getOlcs());
                }
            }
        }
        evalAction(chroms); /*适应度评价*/
        return chroms;
    }

    /**
     *
     * @param chroms
     */
    protected static void evalAction(CellChrome[][][] chroms) {
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length-1; j++) {
                for (int k = 1; k < chroms[j].length-1; k++) {
                    CellChrome father = chroms[i][j][k];
                    setMaxNeighFitness(father);
                    List<Neighbour> childs = father.getOlcs();
                    /*计算适应度*/
                    fitAction(childs);
                    double r = Math.random();
                  /*  if(father.getFitness()>0.9903){
                        System.out.println(father.getId()+" "+father.getFitness());
                    }*/
                    stochastiTournamentSelection(childs,Ps);

                    /*if(r<Ps){
                          *//*轮盘赌操作*//*
                         routtleAction(chroms,father,childs);
                         //binaryTournamentSelection(father,childs);
                    }else{
                        *//*以1-ps直接选择邻居及自身中最优个体*//*
                        Neighbour ngh = findMaxAdaptness(childs);
                        childs.clear();
                        childs.add(ngh);
                    }*/
                }
            }
        }
    }

    /**
     * 二元锦标赛选择 binaryTournamentSelection
     * @param father
     * @param childs
     */
    public static void  binaryTournamentSelection(CellChrome father,List<Neighbour> childs){
        List<Neighbour> cchilds = new ArrayList<>();

        Collections.shuffle(childs);
        for (int i = 0; i < 2; i++) {
            cchilds.add(childs.get(i));

        }
        Neighbour ngb = findMaxAdaptness(cchilds);
        childs.clear();
        childs.add(ngb);
    }

    /**
     * 随机锦标赛选择
     * @param childs
     */
    public static void stochastiTournamentSelection(List<Neighbour> childs,double Ps){
        List<Neighbour> cchilds = new ArrayList<>();

        Collections.shuffle(childs);
        for (int i = 0; i < 2; i++) {
            cchilds.add(childs.get(i));

        }
        double rand = Math.random();
        Neighbour ngb;
        if(rand<Ps){
            ngb = findMinAdaptness(cchilds);
        }else{
            ngb = findMaxAdaptness(cchilds);
        }
        childs.clear();
        childs.add(ngb);
    }

    public static void setMaxNeighFitness(CellChrome father){
        List<Neighbour> nghs = father.getOlcs();
        if(father!=null){
            boolean fag = !nghs.isEmpty();
            if(fag){
                Neighbour bgh = nghs.get(0);
                for (Neighbour ngh : nghs) {
                    if(ngh.getFitness()>bgh.getFitness()){
                        bgh=ngh;
                    }
                }
                father.setMaxNghFitness(bgh.getFitness());
            }
            if(!fag){
                father.setMaxNghFitness(father.getFitness());
            }
        }

    }

    public static double getMinFitness(CellChrome[][][] chroms){
        CellChrome c = (CellChrome) chroms[1][1][1].clone();
        for (int i = 0; i < chroms[0].length; i++) {
            for (int j = 0; j < chroms[i].length; j++) {
                for (int k = 0; k < chroms[j].length; k++) {
                    CellChrome cc = chroms[i][j][k];
                    if(cc.getFitness()<c.getFitness()){
                        c=(CellChrome) cc.clone();
                    }
                }
            }
        }
       /* for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length-1; j++) {
                for (int k = 1; k < chroms[j].length-1; k++) {
                    CellChrome cc = chroms[i][j][k];
                    if(cc.getFitness()<c.getFitness()){
                        c=(CellChrome) cc.clone();
                    }
                }
            }
        }*/
        return c.getFitness();
    }

    public static CellChrome getMinFitness1(CellChrome[][][] chroms){
        CellChrome c = (CellChrome) chroms[1][1][1].clone();
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length-1; j++) {
                for (int k = 1; k < chroms[j].length-1; k++) {
                    CellChrome cc = chroms[i][j][k];
                    if(cc.getFitness()<c.getFitness()){
                        c=(CellChrome) cc.clone();
                    }
                }
            }
        }
        return c;
    }

    /**
     *
     * @param neighbours
     */
    protected static void fitAction(List<Neighbour> neighbours) {
        int c_size = neighbours.size();
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
     * @param neighs
     */
    protected static void routtleAction(CellChrome[][][] chroms, List<Neighbour> neighs) {
        int choose=0;
        int maxLength =  chroms[0].length;
        while(choose<maxLength-m){
            double r = Math.random();
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
            choose++;
        }
    }
    /**
     *
     * @param tccs
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    public static List<Neighbour> ruleOfThreeSpaceType(CellChrome[][][] tccs, int i, int j,int s, double c_f, List<Neighbour> olist) {
        olist.clear();
        for (int k = i-1; k<= i+1; k++) {
            if(k!=i){
                CellChrome child = tccs[k][j][s];
                Neighbour neigh = new Neighbour();
                neigh.setnId(child.getId());
                neigh.setPl(0.0);
                neigh.setPr(0.0);
                neigh.setX1(child.getX1());
                neigh.setX2(child.getX2());
                neigh.setFitness(child.getFitness());
                olist.add(neigh);
            }

        }
        for (int h = s-1; h<= s+1; h++) {
            if(h!=s){
                CellChrome child = tccs[i][j][h];
                Neighbour neigh = new Neighbour();
                neigh.setnId(child.getId());
                neigh.setPl(0.0);
                neigh.setPr(0.0);
                neigh.setX1(child.getX1());
                neigh.setX2(child.getX2());
                neigh.setFitness(child.getFitness());
                olist.add(neigh);

            }
        }
        for (int m = j-1; m<= j+1; m++) {
            CellChrome child = tccs[i][m][s];
            Neighbour neigh = new Neighbour();
            neigh.setnId(child.getId());
            neigh.setPl(0.0);
            neigh.setPr(0.0);
            neigh.setX1(child.getX1());
            neigh.setX2(child.getX2());
            neigh.setFitness(child.getFitness());
            olist.add(neigh);
        }
        return olist;
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
    public static<T> void  crossover(CellChrome[][][] chroms) {
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length - 1; j++) {
                for (int k = 1; k < chroms[j].length - 1; k++) {
                    CellChrome chr = chroms[i][j][k];

                    double xi1 = chr.getX1();
                    double xi2 = chr.getX2();

                    Neighbour bgh = chr.getOlcs().get(0);
                    double xab1 = bgh.getX1();
                    double xab2 = bgh.getX2();

                    double x1 = (1 - Pc) * xi1 +  Pc * xab1 ; /*普通交叉算子*/
                    double x2 = (1 - Pc) * xi2 +  Pc * xab2 ; /*普通交叉算子*/

                    chr.setX1(x1);
                    chr.setX2(x2);
                }
            }
        }
    }
    /*变异操作*/
    public static void mutation(CellChrome[][][] chroms){
        double delt = 0.001;
        for (int i = 0; i < chroms[0].length; i++) {
            for (int j = 0; j < chroms[i].length; j++) {
                for (int k = 0; k < chroms[j].length; k++) {
                    CellChrome chr = chroms[i][j][k];
                    double r = Math.random();
                    if (r < Pm) {
                        double x1 = chr.getX1();
                        double x2 = chr.getX2();
                        double dl = new Random().nextGaussian();
                        x1 = x1 + (delt * dl);
                        x2 = x2 + (delt * dl);

                        chr.setX1(x1);
                        chr.setX2(x2);
                    }
                }
            }
        }
    }
    /**
     * 得到种群拥有最大适应度的个体
     * @param nghs
     * @return
     */
    public static Neighbour findMaxAdaptness(List<Neighbour> nghs){
        if(!nghs.isEmpty()){
            Neighbour ngb = nghs.get(0);
            for (Neighbour ngh : nghs) {
                if(ngh.getFitness()>ngb.getFitness()){
                    ngb=ngh;
                }
            }
            return ngb;
        }
        return null;
    }
    /**
     * 得到种群拥有最大适应度的个体
     * @param nghs
     * @return
     */
    public static Neighbour findMinAdaptness(List<Neighbour> nghs){
        if(!nghs.isEmpty()){
            Neighbour ngb = nghs.get(0);
            for (Neighbour ngh : nghs) {
                if(ngh.getFitness()<ngb.getFitness()){
                    ngb=ngh;
                }
            }
            return ngb;
        }
        return null;
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
