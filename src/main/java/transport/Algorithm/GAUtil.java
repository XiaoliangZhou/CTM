package transport.Algorithm;

import CGA.Model.Chromosome;
import CGA.Model.Neighbour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GAUtil {
    /**
     * 获取二进制标识位数
     * @param a 区间下限
     * @param b 区间上限
     * @param preci 精度
     * @return
     */
    public static int getMinInteger(double a,double b,int preci){
        if (a > b) {
            System.out.println("区间设置错误！");
        }
        int m = 1;
        double mx = (b - a) * Math.pow(10, preci);

        int index = 0;
        while (true) {
            double nx = Math.pow(2, m) - 1;
            if (nx > mx) {
                break;
            }
            m++;
        }
        return m;

    }

    /**
     * @编码
     * @return
     */
    public static String enCode(int a,int b,int preci){

        /*获取二进制标识位数*/
        int m = getMinInteger(a, b, preci);
        /*存放m位二进制数*/
        StringBuffer sb = new StringBuffer();
        int c = 0;
        while (c < m) {
            sb.append((int) (Math.random() * 10) % 2);
            c++;
        }
        return sb.toString();
    }

    /**
     * @解码
     * @二进制转换为十进制
     * @param gstr
     * @return
     */
    public static double deCode(int a,int b,String gstr){

        int d = Integer.valueOf(gstr, 2);/*二进制转换成十进制*/
        /*将十进制转换为对应区间[a,b]内的实数*/
        double x = a + d * ((b - a) / (Math.pow(2, gstr.length()) - 1));

        return x;
    }

    /**
     * 函数
     * @param x
     * @return
     */
    public static double func(double x){
        return 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x);
    }

    public static double func(double x1,double x2){
        double PI = Math.PI;
        // return 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x);
        //return 10 * Math.sin(5 * x1) + 7 * Math.cos(4 * x1);
        double a = Math.pow(x1, 2) + Math.pow(x2, 2);
        double b = Math.pow(Math.sin(Math.sqrt(a)), 2) - 0.5;
        double c = Math.pow(1 + 0.001 * a, 2);

        double q = x1 * Math.sin(2 * x1 + 5);
        double p = x2 * Math.sin(2 * x2 + 10);

        //return q-p;
        return 0.5 - b/c;
        //return x1 * Math.sin(4 * PI * x1) - x2 * Math.sin(4 * PI * x2 + PI + 1);


    }

    /**
     * 获取适应度最小
     * @return
     */
    public static double minAdaptLimit(List<Chromosome> chroms){
        double ft = chroms.get(0).getFitness();
        if(!chroms.isEmpty()){
            for (Chromosome c : chroms) {
                double fd = c.getFitness();
                if (fd < ft) {
                    ft=fd;
                }
            }
        }
        return ft;
    }


    /**
     * 按适应度大小对种群排序
     * @param chroms
     */
    public static void ChromosomeSortByfitValue(List<Chromosome> chroms){
        if(!chroms.isEmpty()){
            Collections.sort(chroms, new Comparator<Chromosome>() {
                @Override
                public int compare(Chromosome o1, Chromosome o2) {
                    return o1.compareTo(o2);
                }
            });
        }

    }
    /*
     * 轮盘赌
     */
    public static List<Chromosome> roulette(List<Chromosome> chroms,int PopSize) {
        int choose = 0;
        List<Chromosome> newchroms = new ArrayList<>();
        while (choose < PopSize) {
            double r = Math.random();
            int index=0;
            while(index<PopSize){
                Chromosome chrom = chroms.get(index);
                if(index==0){
                    if(r<=chrom.getPr()){
                        newchroms.add((Chromosome) chrom.clone());
                        break;
                    }
                }
                if(index>0){
                    if (r <= chrom.getPr() & r > chroms.get(index - 1).getPr()) {
                        newchroms.add((Chromosome)chrom.clone());
                        break;
                    }
                }
                index++;
            }
            choose++;
        }
        return newchroms;
    }



    /**
     * 计算累计概率
     */
    public static void getChromAddProb(List<Chromosome> chroms,double sumft,double psum) {
        psum = 0.0;
        double start = 0.0;
        for (int i = 0; i < chroms.size(); i++) {
            Chromosome chrom = chroms.get(i);
            double p = chrom.getFitness()/sumft;
            chrom.setPl(p);
            psum+=p;
            chrom.setPr(psum);
        }
    }
    /**
     * @选择适应性最强的个体
     * @param Pop
     * @return
     */
    public static Chromosome adaptivest(List<Chromosome> Pop){
        Chromosome chromosome = (Chromosome) Pop.get(0).clone();
        double fit=  chromosome.getFitness();
        for (Chromosome chrom : Pop) {
            if(chrom.getFitness()>fit){
                fit = chrom.getFitness();
                chromosome = (Chromosome) chrom.clone();
            }
        }
        return chromosome;
    }
    /**
     *
     * @param t
     * @return
     */
    public static <T> double getX(T t,int a,int b){
        if(t!=null){
            if(t instanceof Chromosome){
                Chromosome chr = (Chromosome) t;
                return GAUtil.deCode(a,b,chr.getGene());/*对应区间[a,b]内的实数*/
            }
            if(t instanceof Neighbour){
                Neighbour neigh = (Neighbour)t;
                return GAUtil.deCode(a,b,"");/*对应区间[a,b]内的实数*/
            }
        }
        return -1;
    }

    public static void main(String[] args) {

//        String d2 = "0110010010000101010101000";
//        String d3 = "11111111111101111000010010001111";
//        int d = Integer.valueOf(d2.toString(), 2);/*二进制转换成十进制*/
//        /*将十进制转换为对应区间[a,b]内的实数*/
//        double x = 0 + d * ((20 - 0) / (Math.pow(2, d2.length()) - 1));
//        System.out.println(x);
//        System.out.println(GAUtil.func(x));
      /*  for (int i = 0; i < 100; i++) {
            double f =   0 + Math.random() * 10 % (10 - 0 + 1);
            System.out.println(f);
        }*/
        System.out.println(func(0,0));

    }
}
