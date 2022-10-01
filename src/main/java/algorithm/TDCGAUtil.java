package algorithm;

import CGA.Model.CellChrome;
import CGA.MocGAs.ChromeBase;
import CGA.Model.Chrome;
import CGA.Model.Neighbour;

import java.io.*;
import java.util.*;

public class TDCGAUtil {

    public static  CellChrome[][][] createNewPop(CellChrome[][][] oldPops,int popSize){
        int length = popSize+2;
        CellChrome[][][] newPops = new  CellChrome[length][length][length];

        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                for(int k=0;k<oldPops[j].length;k++){
                    newPops[i][j][k]=oldPops[i][j][k];
                }
            }
        }
        for (int i = 1; i < newPops[0].length-1; i++) {
            for (int j = 1; j <newPops[i].length-1; j++) {
                for (int k = 1; k < newPops[j].length-1; k++) {
                    newPops[i][j][k]=null;
                }
            }
        }

       /* CellChrome[][][] newPops  = new CellChrome[length][length][length];
       int i = 0;
        int size = oldPops[0].length;
        while (i < size) {
            int j=0;
            while(j<size){
                if(i==0||i==size-1){
                    for (int k = 0; k < oldPops[j].length; k++) {
                        newPops[i][j][k] = oldPops[i][j][k];
                    }
                    j++;
                }else{
                    for (int k = 0; k < oldPops[j].length; k++) {
                        newPops[i][j][k] = oldPops[i][j][k];
                    }
                    j+=size-1;
                }
            }
            i++;
        }*/
        return newPops;
    }

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
    /**
     * 深度拷贝
     * @param oldPops
     * @return
     */
    public static  Chrome[][] deepCopy(Chrome[][] oldPops){
        int length = oldPops.length;
        Chrome[][] newPops = new  Chrome[length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                newPops[i][j]=oldPops[i][j];

            }
        }
        return newPops;
    }
    /**
     * 深度拷贝
     * @param oldPops
     * @return
     */
    public static  Chrome[][][] deepCopy(Chrome[][][] oldPops){
        int length = oldPops.length;
        Chrome[][][] newPops = new  Chrome[length][length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                for(int k=0;k<oldPops[j].length-1;k++){
                    newPops[i][j][k]=oldPops[i][j][k];
                }
            }
        }
        return newPops;
    }
    /**
     *
     * @param tccs
     * @param i
     * @param j
     * @param c_f
     * @param olist
     */
    public static List<Neighbour> ruleOfThreeSpaceType(CellChrome[][][] tccs, int i, int j, int s, double c_f, List<Neighbour> olist) {
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
       /* for (int m = j-1; m<= j+1; m++) {
            if(m!=j){
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
        }*/
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

    /**
     * 二元锦标赛选择 binaryTournamentSelection
     * @param father
     * @param childs
     */
    public static void binaryTournamentSelection(CellChrome father,List<Neighbour> childs){
        List<Neighbour> cchilds = new ArrayList<>();

        Collections.shuffle(childs);
        for (int i = 0; i < 2; i++) {
            cchilds.add(childs.get(i));

        }
        Neighbour ngb = TD_CGA.findMaxAdaptness(cchilds);
        childs.clear();
        childs.add(ngb);
    }

    public static void tournamentSelection(CellChrome father,List<Neighbour> childs,double Ps){
        double r = Math.random();
        if(r<Ps){
            binaryTournamentSelection(father,childs);
        }else{
            Neighbour ngh = TD_CGA.findMaxAdaptness(childs);
            childs.clear();
            childs.add(ngh);
        }
    }
    public static void tournamentSelection1(CellChrome father,List<Neighbour> childs,double Ps,int popSize){
        double r = Math.random();
        if(r<Ps){
            TD_CGA.fitAction(childs);
            routtleAction(popSize,childs);
            //binaryTournamentSelection(father,childs);
        }else{
            Neighbour ngh = TD_CGA.findMaxAdaptness(childs);
            childs.clear();
            childs.add(ngh);
        }
    }
    /**
     * 轮盘赌操作
     * @param neighs
     */
    protected static void routtleAction(int PopSize, List<Neighbour> neighs) {
        int choose=0;
        int m=2;
        int maxLength =  PopSize;
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
     * 交叉
     * @param Pc
     * @param chr
     */
    public static CellChrome AUX(double Pc, CellChrome chr,CellChrome elite) {
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
      /*  if (max_pop_fit == max_ngh_fit) {
            Px = Pc;
        } else {
            Px = Pc + (1 - Pc) * K1 * Math.tanh((max_ngh_fit - max_pop_fit * Pc) / (K2 * (max_pop_fit - max_ngh_fit)));
        }*/

        /*自适应交叉算子*/
      /*  double x1 = (1 - Px) * xi1 +  Px * xab1 ;
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
    public static void Mutation(double Pm,CellChrome chr){
        double delt = 0.01;
        double r = Math.random();
        if (r < Pm) {
            double x1 = chr.getX1();
            double x2 = chr.getX2();
            double dl = new Random().nextGaussian();
            x1 = x1 + (delt * dl);
            x2 = x2 + (delt * dl);

            chr.setX1(x1);
            chr.setX2(x2);
            chr.setFitness(GAUtil.func(x1,x2));
        }
    }

    /**
     * 替换策略
     * @param aux_pop
     * @param i
     * @param j
     * @param k
     * @param c1 父代个体
     * @param cc 新个体
     */
    public static void replaceIfBetter(CellChrome[][][] aux_pop, int i, int j, int k,CellChrome c1,CellChrome cc,CellChrome elite){
        if(c1!=null){
            if(cc!=null){
                CellChrome chr = new CellChrome();
                chr.setId(c1.getId());
                if(c1.getFitness()<cc.getFitness()){
                    chr.setX1(cc.getX1());
                    chr.setX2(cc.getX2());
                    chr.setFitness(cc.getFitness());
                }else{
                    if(c1.getId().equals(elite.getId())){
                        chr.setX1(c1.getX1());
                        chr.setX2(c1.getX2());
                        chr.setFitness(c1.getFitness());
                    }else{
                        Neighbour ngh = c1.getOlcs().get(0);
                        if(ngh.getnId().equals(elite.getId())){
                            chr.setX1(cc.getX1());
                            chr.setX2(cc.getX2());
                            chr.setFitness(cc.getFitness());
                        }else{
                            chr.setX1(c1.getX1());
                            chr.setX2(c1.getX2());
                            chr.setFitness(c1.getFitness());
                        }
                    }

                }
                aux_pop[i][j][k]=chr;
            }
        }
    }

    public static void replaceIfBetter1(CellChrome[][][] aux_pop, int i, int j, int k,CellChrome c1,CellChrome cc,CellChrome elite){
        if(c1!=null){
            if(cc!=null){
                CellChrome chr = new CellChrome();
                chr.setId(c1.getId());
                if(c1.getFitness()<cc.getFitness()){
                    chr.setX1(cc.getX1());
                    chr.setX2(cc.getX2());
                    chr.setFitness(cc.getFitness());
                }else{
                  /*  chr.setX1(cc.getX1());
                    chr.setX2(cc.getX2());
                    chr.setFitness(cc.getFitness());*/
                    chr.setX1(c1.getX1());
                    chr.setX2(c1.getX2());
                    chr.setFitness(c1.getFitness());
                }
                aux_pop[i][j][k]=chr;
            }
        }
    }

    /**
     * 初始种群适应度并获得最大适应度个体(精英个体)
     * @param chroms
     */
    public static CellChrome getMaxFitObject(CellChrome[][][] chroms){
        CellChrome c = chroms[0][0][0];
        for(int i=0;i<chroms[0].length;i++){
            for(int j=0;j<chroms[i].length;j++){
                for(int k=0;k<chroms[j].length;k++){
                    CellChrome c1 = chroms[i][j][k];
                    if(c1.getFitness()>c.getFitness()){
                        c=c1;
                    }
                }
            }
        }
        return c;
    }

    /**
     *替换策略
     * @param i
     * @param j
     * @param k
     * @param c1
     */
    public static void replaceIfBetterTest(CellChrome[][][] aux_pop,int i, int j, int k,CellChrome c1){
        if(c1!=null){
            Neighbour cc = c1.getOlcs().get(0);
            if(cc!=null){
                CellChrome chr = new CellChrome();
                chr.setId(c1.getId());
                chr.setX1(c1.getX1());
                chr.setX2(c1.getX2());
                if(c1.getFitness()<cc.getFitness()){
                    chr.setFitness(cc.getFitness());
                }else{
                    chr.setFitness(c1.getFitness());
                }
                aux_pop[i][j][k]=chr;
            }
        }
    }
    /**
     * 种群排序
     * @param chrs
     */
    public static void popSort(List<Chrome> chrs) {
        Collections.sort(chrs, new Comparator<Chrome>() {
            @Override
            public int compare(Chrome o1, Chrome o2) {
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
     * 种群排序
     */
    public static void popSortByNd() {
        popSortByNd();
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
     *通多目标函数值对个体排序
     */
    public static List<ChromeBase> popSortByRank(List<Chrome> chrs,int index) throws Exception {
        List<ChromeBase> sorts = new ArrayList<>();
        for (Chrome chr : chrs) {
            ChromeBase c = new ChromeBase();
            c.setId(chr.getId());
            c.setF(chr.getF());
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
     * 深度拷贝
     * @param srcs
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Chrome> deepCopyed(List<Chrome> srcs) throws CloneNotSupportedException{
        List<Chrome> n_c_lists = new ArrayList<>();
        for (Chrome src : srcs) {
            n_c_lists.add((Chrome) src);
        }
       return n_c_lists;
    }


    /**
     * 深度拷贝
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
