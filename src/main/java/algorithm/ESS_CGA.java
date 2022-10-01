package algorithm;

import CGA.Model.CellChrome;
import CGA.Model.Neighbour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ESS_CGA {
    static double a = -5,b = 5;
    /*种群大小*/
    static int PopSize = 27;
    /*最大进化代数*/
    static int MaxGen =100;
    /*个体个数*/
    static int Nvar =20;
    /*个体的二进制标识位数*/
    static int Preci =6;
    /*交叉概率*/
    static double Pc =0.7;
    /*交叉概率*/
    static double Pm =0.01;
    static int m = 2;

    final static int mh = GAUtil.getMinInteger(a,b,Preci);

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
                double x2 = a + Math.random() * b % (b - a + 1);
                String idstr = i+"_"+j;
                chroms[i][j] = new CellChrome(idstr,x1,x2,0.0,0.0, new ArrayList<>());
               /* chroms[i][j] = new CellChrome(idstr,0,0,0.0,0.0, new ArrayList<>());
                if(i<=4){
                    chroms[i][j].setFitness(1);
                }else{
                    chroms[i][j].setFitness(2);
                }*/
            }
        }
        //chroms[1][1].setFitness(3);
        return chroms;
    }
    /*2、计算适应度*/
    /**
     * 计算元胞空间(种群)中个体V(i,j)的适应度eval(V(i,j))不是一般性，有eval(V(i,j)) = f(x(i,j))- min(f(i,j))
     */
    public static void updateAdaptive(CellChrome[][] chroms){
        for(int i=0;i<chroms[0].length;i++){
            for(int j=0;j<chroms[i].length;j++){
                CellChrome c = chroms[i][j];
                double fc = GAUtil.func(c.getX1(),c.getX2()) ;
                c.setFitness(fc);
            }
        }
      /*  double minf = findMinAdaptness(chroms);
        for(int i=0;i<chroms[0].length;i++){
            for(int j=0;j<chroms[i].length;j++){
                CellChrome c = chroms[i][j];
                c.setFitness(c.getFitness() - minf);
            }
        }*/
       /* for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j <chroms[i].length-1; j++) {
                CellChrome father = chroms[i][j];
                if(father.getOlcs().size()!=0){
                    Neighbour ngh = father.getOlcs().get(0);
                    father.setFitness(ngh.getFitness());
                }
            }
        }*/
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
                //ruleOfMooreType(chroms, i, j, c_f, olist);
                ruleOfVonNeumannType(chroms, i, j, c_f, olist);
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
                setMaxNeighFitness(father);
                List<Neighbour> childs = father.getOlcs();
                routtleSelect(father,childs);

               /* if(childs.size()==0){
                    Neighbour neigh = new Neighbour();
                    childs.clear();
                    neigh.setnId(father.getId());
                    neigh.setPl(0.0);
                    neigh.setPr(1.0);
                    neigh.setX1(father.getX1());
                    neigh.setX2(father.getX2());
                    neigh.setFitness(father.getFitness());
                    childs.add(neigh);*//*中心个体适应度最大*//*
                }else{
                    Neighbour ngh = TD_CGA.findMaxAdaptness(childs);
                    childs.clear();
                    childs.add(ngh);
                }*/

            }
        }
    }

    /**
     * 轮盘赌
     * @param father
     */
    public static void routtleSelect(CellChrome father,List<Neighbour> childs) {
        setMaxNeighFitness(father);
        /*计算适应度*/
        int c_size = childs.size();
        if(c_size>0){
            int index = 0;
            double sum = 0;
            /*适应度之和*/
            while (index < c_size) {
                Neighbour neigh = childs.get(index);
                sum += neigh.getFitness();
                index++;
            }
            /*个体V(i,j)的邻居V(a,b)被选择的概率*/
            double temp = 0.0;
            for (Neighbour neigh : childs) {
                double p = neigh.getFitness() / sum;
                neigh.setPl(p);
                temp+=p;
                neigh.setPr(temp);
            }
        }
        /*轮盘赌操作*/
        double r = Math.random();
        for (int index = 0; index < childs.size(); index++) {
            Neighbour neighbour = childs.get(index);
            if(index==0){
                if(r<neighbour.getPr()){
                    childs.clear();
                    childs.add(neighbour);
                    break;
                }
            }else{
                if(r<=neighbour.getPr()& r>childs.get(index-1).getPr()){
                    childs.clear();
                    childs.add(neighbour);
                    break;
                }
            }
        }
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
    protected static void routtleAction(CellChrome father, List<Neighbour> neighs) {
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
    }

    /**
     *
     * @param chroms
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    public static List<Neighbour> ruleOfMooreType(CellChrome[][] chroms, int i, int j, double c_f, List<Neighbour> olist) {
        for (int k = i-1; k<= i+1; k++) {
            for (int m = j-1; m<= j+1; m++) {
                CellChrome child = chroms[k][m];
                double c_h = child.getFitness();/*个体V(i,j)邻居适应度*/
                Neighbour neigh = new Neighbour();
                    if (c_h>=c_f) {
                        /*选出适应度大于等于V(i,j)的邻居元胞*/
                        neigh.setnId(child.getId());
                        neigh.setPl(0.0);
                        neigh.setPr(0.0);
                        neigh.setX1(child.getX1());
                        neigh.setX2(child.getX2());
                        neigh.setFitness(child.getFitness());
                        olist.add(neigh);
                    }
            }
        }
        return olist;
    }

    /**
     * ruleOfVonNeumannType
     * @param chroms
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    public static List<Neighbour> ruleOfVonNeumannType(CellChrome[][] chroms, int i, int j, double c_f, List<Neighbour> olist) {
        for (int k = i-1; k<= i+1; k++) {
            CellChrome chr1 = chroms[k][j];
            double c_h = chr1.getFitness();/*个体V(i,j)邻居适应度*/
            Neighbour neigh = new Neighbour();
            if (c_h>=c_f) {
                /*选出适应度大于等于V(i,j)的邻居元胞*/
                neigh.setnId(chr1.getId());
                neigh.setPl(0.0);
                neigh.setPr(0.0);
                neigh.setX1(chr1.getX1());
                neigh.setX2(chr1.getX2());
                neigh.setFitness(chr1.getFitness());
                olist.add(neigh);
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
                    neigh.setX2(chr2.getX2());
                    neigh.setFitness(chr2.getFitness());
                    olist.add(neigh);
                }
            }
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
     */
    public static<T> CellChrome  crossover(double Pc,CellChrome chr,CellChrome elite) {
        CellChrome nchr =  new CellChrome();

        double xi1 = chr.getX1();
        double xi2 = chr.getX2();

        Neighbour bgh = chr.getOlcs().get(0);
        double xab1 = bgh.getX1();
        double xab2 = bgh.getX2();

        double maxx1 = elite.getX1();
        double maxx2 = elite.getX2();

        double Px = 0,K1 = 0.9,K2 = 2;
        double max_pop_fit = elite.getFitness();
        double max_ngh_fit = chr.getMaxNghFitness();

        /*引入自适应交叉算子*/
        if (max_pop_fit == max_ngh_fit) {
            Px = Pc;
        } else {
            Px = Pc + (1 - Pc) * K1 * Math.tanh((max_ngh_fit - max_pop_fit * Pc) / (K2 * (max_pop_fit - max_ngh_fit)));
        }

        /*自适应交叉算子*/
        /*double x1 = (1 - Px) * xi1 +  Px * xab1 ;
        double x2 = (1 - Px) * xi2 +  Px * xab2 ;*/

        /*精英交叉算子*/
        double x1 = (1 - Pc) * xi1 + Pc * (1 - Pc) * xab1 + Pc * Pc * maxx1;
        double x2 = (1 - Pc) * xi2 + Pc * (1 - Pc) * xab2 + Pc * Pc * maxx2;

        /*普通交叉算子*/
      /*  double x1 = (1 - Pc) * xi1 + Pc * xab1;
        double x2 = (1 - Pc) * xi2 + Pc * xab2;*/

        nchr.setId(chr.getId());
        nchr.setX1(x1);
        nchr.setX2(x2);
        nchr.setFitness(GAUtil.func(x1,x2));
        //nchr.setOlcs(chr.getOlcs());
        return nchr;
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
     * 得到种群拥有最大适应度的个体
     * @param chroms
     * @return
     */
    public static double findMinAdaptness(CellChrome[][] chroms){
        CellChrome c = chroms[1][1];
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length-1; j++) {
                CellChrome cc = chroms[i][j];
                if(cc.getFitness()<c.getFitness()){
                    c=cc;
                }

            }
        }
        return c.getFitness();
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
    public static void replaceIfBetter1(CellChrome[][] aux_pop, int i, int j,CellChrome c1,CellChrome cc,CellChrome elite){
        if(c1!=null){
            if(cc!=null){
                CellChrome chr = new CellChrome();
                chr.setId(c1.getId());
                if(c1.getFitness()<cc.getFitness()){
                    chr.setX1(cc.getX1());
                    chr.setX2(cc.getX2());
                    chr.setFitness(cc.getFitness());
                }else{
                    chr.setX1(cc.getX1());
                    chr.setX2(cc.getX2());
                    chr.setFitness(cc.getFitness());
                /*    chr.setX1(c1.getX1());
                    chr.setX2(c1.getX2());
                    chr.setFitness(c1.getFitness());*/
                }
                aux_pop[i][j]=chr;
            }
        }
    }
    /**
     * 深度拷贝
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  CellChrome[][] deepCopy(CellChrome[][] oldPops,int popSize){
        int length = popSize+2;
        CellChrome[][] newPops = new  CellChrome[length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                newPops[i][j]=oldPops[i][j];
            }
        }
        return newPops;
    }

    /**
     *
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  CellChrome[][] createNewPop(CellChrome[][] oldPops,int popSize){
        int length = popSize+2;
        CellChrome[][] newPops = new  CellChrome[length][length];

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


}
