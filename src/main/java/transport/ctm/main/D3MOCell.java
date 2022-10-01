package transport.ctm.main;
import CGA.MocGAs.Pareto;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import transport.Algorithm.TDCGAUtil;
import transport.ctm.util.constant;
import transport.math.util.MathSupplier;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class D3MOCell {


    static int tour = 2;
    static double Inf = Double.POSITIVE_INFINITY;
    static int popSize = 5;
    static int f_num = 2;
    static int x_num = 4;
    static int c=16;
    static double a = 0, b = 1;
    static int Gmin = 3, Gmax = 6;
    static double a1 = 0, b1 = 5;
    static double yta1 = 20;
    static double yta2 = 20;
    static String fname = "ZDT1";
    static double pc=0.8;
    static double pm=0.25;
    static int maxGen=5000;

    /*1、种群初始化 */
    /**
     * 在 n*n*n的二维网格中，每个网格代表一个个体V(i,j,k)
     */
    public static  Chrome[][][] initPop1(){
        int size = popSize + 2;
        Chrome[][][] chroms = new Chrome[size][size][size];
        /*初始化种群*/
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                for(int k=0;k<size;k++){
                    Chrome chr = new Chrome();
                    List<Double> x = chr.getX();

                    for (int h = 0; h < x_num; h++) {
                        double v = Gmin + (int)(Math.random() * (Gmax - Gmin + 1));
                        chr.setVariableValue(h,v);
                    }

                    String idstr = i+"_"+j+"_"+k;
                    chr.setId(idstr);
                    chroms[i][j][k] = chr;
                }

            }
        }
        return chroms;
    }

    /**
     *
     * @param offSprs
     * @return
     * @throws Exception
     */
    public static Chrome campreOperator(List<Chrome> offSprs) throws Exception{
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
            if(chr.isCurChr()){
                c_dx=tmp_lists.indexOf(chr);
            }
        }
        Chrome elite = tmp_lists.get(0);
        if(c_dx>0){
            Chrome c_chr = tmp_lists.get(c_dx);
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
    public static Chrome getBestSolutions(List<Chrome> offsprings,Chrome f_chr) throws Exception {
        offsprings.add((Chrome) f_chr.clone());
        int size = offsprings.size();
        Map<Integer,Set<String>> rank = nonDominationSort(size,offsprings);
        offsprings = crowdingDistanceSort(rank,offsprings);

        Chrome c_chr = campreOperator(offsprings);/*选出优秀子代*/

        return c_chr;
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
                child = chroms[k][m];
                olist.add((Chrome) child.clone());
            }
        }
        return olist;
    }
    /**
     * @创建一个空的存放Pareto解集空种群
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  Chrome[][][] createNewPop(Chrome[][][] oldPops,int popSize) throws Exception{
        int length = popSize+2;
        Chrome[][][] newPops = new  Chrome[length][length][length];

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

        return newPops;
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
                    targets.add((Chrome) srcs.get(i).clone());
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
                    n_f_lists.add((Chrome) srcs.get(i).clone());
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
                Chrome chr = (Chrome) c.clone();
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
            newchrs1.add((Chrome) findChromeById(chrs,cId).clone());
        }
        return newchrs1;
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
                Chrome chrr = chrs.get(Integer.parseInt(index));/*遍历当前集合中每个个体*/
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
                            temp.add(String.valueOf(chrs.indexOf(pchr)));
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
    /**
     * @计算拥挤度进行排序
     * @param rankMaps
     * @param chrs
     * @return
     * @throws Exception
     */
    public static List<Chrome> crowdingDistanceSort(Map<Integer,Set<String>> rankMaps,List<Chrome> chrs) throws Exception{
        List<Chrome> newchrs = new ArrayList<>();
        chrs.forEach((chr)->{
            chr.setNd(0.0);
        });
        /*计算拥挤度*/
        for (Map.Entry<Integer, Set<String>> entry : rankMaps.entrySet()) {
            Set<String> sets = entry.getValue();
            if(!sets.isEmpty()){
                List<Chrome> chroms = createTempList(chrs, sets);/*临时存放chrome 对象*/
                /*对于每一个目标函数fm*/
                updateMainfuncNd(chroms);
                /*重新组装*/
                chroms.forEach((chr)->{
                    newchrs.add(chr);
                });
            }
        }
        return newchrs;
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
            chroms.add((Chrome)c.clone());
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
     * @param chrss
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<Chrome> tournamentSelection(List<Chrome> chrss,int tour) throws Exception {
        int popSize = chrss.size();
        Map<Integer,Set<String>> rankMaps = nonDominationSort(popSize,chrss);
        chrss = crowdingDistanceSort(rankMaps,chrss);

        List<Chrome> chds = new ArrayList<>();
        for (int i = 0; i < tour; i++) {
            List<Chrome> parent = binaryTournamentSelection(chrss);
            chds.addAll(parent);
        }
        return chds;
    }

    /**
     * 深度拷贝
     * @param oldPops
     * @param popSize
     * @return
     */
    public static  Chrome[][][] deepCopy(Chrome[][][] oldPops,int popSize){
        int length = popSize+2;
        Chrome[][][] newPops = new  Chrome[length][length][length];
        for(int i=0;i<oldPops[0].length;i++){
            for(int j=0;j<oldPops[i].length;j++){
                for(int k=0;k<oldPops[j].length;k++){
                    newPops[i][j][k]=oldPops[i][j][k];
                }
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
    public static List<Chrome> insertParetoFront(List<Chrome> p_f_lists,Chrome chr) throws Exception{
        int maxLimit = popSize *popSize*popSize;
        int N=20;
        if(chr!=null){
            p_f_lists.add(chr);
        }
        while (p_f_lists.size()>maxLimit){
            int popSize = p_f_lists.size();
            Map<Integer,Set<String>> rankMaps = nonDominationSort(popSize,p_f_lists);
            p_f_lists = crowdingDistanceSort(rankMaps,p_f_lists);

            List<Chrome> rk_lists = TDCGAUtil.deepCopyed(p_f_lists);
            p_f_lists.clear();
            /*(2) 求出最高的pareto等级*/
            int max_rank = rk_lists.get(rk_lists.size() - 1).getRank();
            int pre_index=0;
            for (int i = 1; i <= max_rank; i++) {
                /*寻找当前等级i个体的最大索引*/
                int cur_index = getMaxIndex(rk_lists, i) + 1;
                /*等级为i的个体*/
                List<Chrome> tmp_lists = copyChrome(rk_lists, pre_index, cur_index);
                /*根据拥挤度从大到小排序*/
                TDCGAUtil.popSortByNd(tmp_lists);

                p_f_lists.addAll(tmp_lists);

                pre_index=cur_index;
            }

            Chrome c = p_f_lists.get(p_f_lists.size()-1);
            p_f_lists.remove(c);
        }
        return p_f_lists;
    }

    public static Chrome[][][] feedBackStrategy( Chrome[][][] chroms,List<Chrome> p_f_lists){
        int N = 20;
        int size = p_f_lists.size();
        if(size>=N){
            for (int k = 0; k < N; k++) {
                Chrome c0 = p_f_lists.get(k);

                int i = new Random().nextInt(popSize) +1;
                int j = new Random().nextInt(popSize) +1;
                int h = new Random().nextInt(popSize) +1;
                String cId = i+"_"+j+"_"+h;

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

    /**
     *
     * @param chroms
     * @param cId
     * @return
     */
    public static Chrome getChrome(Chrome[][][] chroms,String cId){
        for (int i = 1; i < chroms[0].length-1; i++) {
            for (int j = 1; j < chroms[i].length - 1; j++) {
                for (int k = 1; k < chroms[i].length - 1; k++) {
                    /*种群个体*/
                    Chrome c1 = chroms[i][j][k];
                    if(c1.getId().equals(cId)){
                        return c1;
                    }
                }
            }
        }
        return null;
    }
    /**
     *
     * @param tccs
     * @param i
     * @param j
     */
    public static List<Chrome> ruleOfThreeSpaceType(Chrome[][][] tccs, int i, int j, int s) throws Exception {
        Chrome child;
        List<Chrome> olist = new ArrayList<>();
        for (int k = i-1; k<= i+1; k++) {
            if(k!=i){
                child = tccs[k][j][s];
                olist.add((Chrome) child.clone());
            }

        }
        for (int h = s-1; h<= s+1; h++) {
            if(h!=s){
                child = tccs[i][j][h];
                olist.add((Chrome) child.clone());

            }
        }
        for (int m = j-1; m<= j+1; m++) {
            child = tccs[i][m][s];
            olist.add((Chrome) child.clone());
        }
        return olist;
    }


    @Test
    public void moc_test() throws IOException,Exception {

        long startTime = System.currentTimeMillis();    //获取开始时间

        /*种群初始化*/
        constant.d3_chrs = initPop1();
        /*计算目标函数*/
        new gui().simDataReadyInfo1();    //测试的代码段

        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

}
