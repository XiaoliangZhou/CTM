package algorithm;

import CGA.Model.Chromosome;
import transport.math.util.MathSupplier;

import java.util.*;

public class SA_CGA_ALG {
    static int a = 0,b = 10;
    /*种群大小*/
    static int PopSize = 400 ;
    /*最大进化代数*/
    static int MaxGen =100;
    /*精度*/
    static int Preci = 6;
    /*交叉概率*/
    static double Pc =0.7;
    /*变异概率*/
    static double Pm =0.2;

    static double sumft = 0;

    static double psum = 0;


    /*1、编码与解码*/
    /**
     * 要达到问题提出的精度，变量x所属的的域D应该被分割成(b-a)*10^n个等尺寸的空间，这里m表示使(b-a)*10^n <=2^m - 1成立的最小整数，
     * 则对域D内的变量x(i),由串长为m的二进制编码即可满足进度要求，即x(i)可以用二进制串表示为(c(i1),c(i2),c(i3)....)
     * 反过来，吧一个二进制串(c(i1),c(i2),c(i3)....)转化为区间[a,b]内对应实数，则通过下面的不走来实现：
     * 1)、将二进制串(c(i1),c(i2),c(i3)....)代表的二进制数按照下面的公式转化为十进制数x(i)'
     * 2)、将x(i)'按照下面的公式转换为对应区间[a,b]内实数
     *    x(i) = a + x(i)'*(b-a/2^m-1)
     *    以上完成了编码与解码工作
     *    见 GAUtil.java
     */

    /*2、初始化种群*/
    /**
     * 在搜索空间[a,b]中随机选定若干个体，由这些个体组成以个生物集团，
     * 搜索开始时，问题的解完全未知，个体的优秀程度完全未知。通常，初始种群用随机数产生，产生的个体总数为N,这里个体用(v(1),v(2),....N)表示，而
     * x(i)则可以视为V(i)的染色体
     * */
    public static  List<Chromosome> initPop(){
        List<Chromosome> chroms = new ArrayList<>();
        /*初始化种群*/
        int index=0;
        while(index<PopSize){
            String gstr = GAUtil.enCode(a,b,Preci);
            double x = GAUtil.deCode(a,b,gstr);
            String idstr = index+"";
            chroms.add(new Chromosome(idstr,0,0.0, 0.0, 0.0, gstr,new ArrayList<>()));
            index++;
        }
        return chroms;
    }
    /*3、计算个体适应度*/

    /**
     * 计算种群中个体V(i)对环境的适应度eval(V(i)),由于对所有的x属于D,f(x)>0,这里我们采用个体直接带入
     * f(x)所得的函数值作为标准来评价该个体的适应度
     * 则有eval(V(i))=f(x(i))
     */
    public static void updateAdaptive(List<Chromosome> chroms){
        for (Chromosome chrom : chroms) {
            String gstr = chrom.getGene(); /*解码*/
            double x = GAUtil.deCode(a,b,gstr);/*对应区间[a,b]内的实数*/
            chrom.setFitness(GAUtil.func(x));
        }
    }

    /**
     * 适应度函数
     * @param chroms
     * @return
     */
    public static void fitness(List<Chromosome> chroms){
        sumft = 0;
        /* /*1、按适应度大小对种群进行排序(降序)
        GAUtil.ChromosomeSortByfitValue(chroms);*/
        /*2、重新更新个体适应，避免适应出现负值*/
        double c_min = 0,temp = 0;
        chroms.forEach((chr)->{
            double c = chr.getFitness() + c_min;
            if(c<0) c=1;
            sumft+=c;
            chr.setFitness(c);
        });

    }
    /*4、选择操作*/

    /**
     * 1)、计算种群的适应度之和FIT = eval(V(1)) +  eval(V(2)) +  eval(V(2)) +  eval(V(2)) + .....
     * 2)、计算每个个体被选择的概率，即个体V(i)的适应度占种群总适应度的百分比
     * 3)、计算每个个体的累计概率，所谓“累计概率” ，就是体V(i)的被选择概率与个体V(1)~V(i-1)的被选择概率之和
     *     q(i) = 1/FIT*(eval(V(1)) +  eval(V(2)) +  eval(V(2)) +  eval(V(2)) + .....+eval(j))
     *          = p1+p2+p3+....+p(i)
     * 4)、产生一个在区间[0,1]上的随机数r,如果r<q1,则选择第一个染色体，否则选择使(r>=q(i-1)&r<=q(i))成立的第i个染色体V(i)(i>=2&i<=N)
     *     每一执行一次轮盘赌操作，轮盘转动N次 ，每一次箭头所指向区域代表的个体被选择出来，这样种群通过选择操作得到N个新的个体。
     */
    public static List<Chromosome> operateOfSelect(List<Chromosome> chroms){
        /*1、计算适应度之和*/
        fitness(chroms);
        /*2、计算累计概率*/
        GAUtil.getChromAddProb(chroms, sumft, psum);
        /*3、轮盘赌选择*/
        List<Chromosome> newchroms = GAUtil.roulette(chroms, PopSize);

        return newchroms;
    }
    /*5、交叉操作*/
    /**
     * s设置交叉概率pc ,这个概率我们可以理解为进行交叉操作的个体个数为N*pc个，可以通过以下方式选择要进行的交叉操作的个体：对于由步骤（4）选择出每一个体
     * 产生一个在区间[0,1]上的随机数r1, 若r1<pc,则对该个体进行交叉操作
     * 其中交叉操作包括 单点交叉，两点交叉，多点交叉，分裂交叉，均匀交叉，混合交叉等...
     */
    public static void operateOfCrossover(List<Chromosome> chroms){
        int gLength = GAUtil.getMinInteger(a, b, Preci);
        for (int i = 0; i < PopSize; i++) {
            double P = Math.random();
            if (P < Pc) {
                /*随机对个体进行排队*/
                int pf = 0, pm = 0;
                do {
                    pf = (int) (Math.random() * (gLength));
                    pm = (int) (Math.random() * (gLength));
                } while (pf == pm);
                /*产生交叉点*/
                int position = new Random().nextInt(gLength);
                /*交叉操作(单点交叉)*/
                String temp ;
                for (int s = position; s < gLength; s++) {
                    String dfd = chroms.get(pf).getGene().substring(position);
                    temp = (chroms.get(pf).getGene()).split("")[s];
                    (chroms.get(pf).getGene()).split("")[s] = (chroms.get(pm).getGene()).split("")[s];
                    (chroms.get(pm).getGene()).split("")[s] = temp;
                }
            }
        }
    }
    /*6、变异操作*/

    /**
     * 设变异概率为pm ,对于经历了交叉操作都产生的每一个新个体，都与之对应的产生一个在区间[0,1]里的随机数r1,若r1<pm,则在该个体（染色体）中任意选择
     * 以基因位进行变异操作 exp:
     *   变异前：1001 1001 0101 0
     *   变异后：1001 1101 0101 0
     *   一般来说，变异概率不易过大，否则使得变异后的个体失去上一代的遗传特征。
     */
    public static void mutation(List<Chromosome> Pops){
        int gLength = GAUtil.getMinInteger(a, b, Preci);
        for (int i = 0; i < PopSize; i++) {
            double r = Math.random();
            if (r < Pm) {
                int temp = (int) (Math.random()*gLength);
                while(temp>0){
                    int index =  (int) (Math.random()*(gLength - 1));/*变异位置*/
                    int gene = Integer.parseInt(Pops.get(i).getGene().split("")[index]);
                    gene = 1 - gene;
                    temp--;
                }
            }
        }
    }
    /*7、生物集团评价*/
    /**
     * 完成上述操作后,就要评价已生成的新种群是否满足求解问题的标准，及生物集团评价，一般来讲反复执行上面的3~6步骤，遗传算法典型的结束评价基准有以下几条：
     * 1)、种群中的最大适应度比某一设定值大
     * 2)、种群中的平均适应度比某一设定值大
     * 3)、进化达到一定次数
     * 4)、种群适应度增加率随着进化次数增加不再增加
     */
    public static void main(String[] args) {
        int sample = 1;
        int cx = 0;
        double[] f = new double[MaxGen];
        while (cx<sample){
            int step=0;
            /*1、初始化种群*/
            List<Chromosome> chroms = initPop();
            List<Chromosome> bestList = new ArrayList<>();
            while(step<MaxGen){
                /*2、计算适应度*/
                updateAdaptive(chroms);
                /*3、选择*/
                List<Chromosome> newPopulation =  operateOfSelect(chroms);
                /*4、交叉*/
                operateOfCrossover(newPopulation);
                /*5、变异*/
                mutation(newPopulation);
                chroms = newPopulation;
                /*适应度最好个体*/
                bestList.add(GAUtil.adaptivest(newPopulation));
                step++;
                double sum = 0.0;
                for(int i=0; i<PopSize; i++){
                    Chromosome c = chroms.get(i);
                    double x = GAUtil.deCode(a,b,c.getGene());/*对应区间[a,b]内的实数*/
                    /*避免出现负值*/
                    double fx = GAUtil.func(x);

                    sum+=fx;
                }
                f[step-1] += sum;
            }
            cx++;
        }
        /*计算种群函数平均值*/
        for (double fs : f) {
            System.out.println(fs/(sample*PopSize));
        }

    }

}
