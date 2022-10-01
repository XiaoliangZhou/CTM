package util;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import transport.graph.loadVehMap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Title: randomSupplier.java
 * @Package transport.math.util
 * @Description: 提供随机数的生成
 * @author liangxiao.zhou
 * @date Apr 23 2019 17:11:14 PM
 * @version V3.0
 */
public class randomSupplier implements Serializable {
    private static randomSupplier instance;
    private  Random rnd;
    private  long seed;

    public static randomSupplier getInstance() {
        if (instance == null) {
            instance = new randomSupplier();
        }
        return instance;
    }

    public double  getRandomValue(){
        return JMetalRandom.getInstance().nextDouble();
    }
    public randomSupplier() {
        this(System.currentTimeMillis());
    }

    public randomSupplier(long seed) {
        this.seed = seed;
        this.rnd = new Random(seed);
    }

    public  long getSeed() {
        return seed;
    }

    public  int nextInt(int lowerBound, int upperBound) {
        return lowerBound + rnd.nextInt(upperBound - lowerBound + 1);
    }

    public double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + this.rnd.nextDouble() * (upperBound - lowerBound);
    }
    public  double nextDouble() {
        return nextDouble(0.0D, 1.0D);
    }

    public void setSeed(long seed) {
        this.seed = seed;
        this.rnd.setSeed(seed);
    }

    public double repairSolutionVariableValue(double value, double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new JMetalException("The lower bound (" + lowerBound + ") is greater than the upper bound (" + upperBound + ")");
        } else {
            double result = value;
            if (value < lowerBound) {
                result = lowerBound;
            }

            if (value > upperBound) {
                result = upperBound;
            }

            return result;
        }
    }
    /**
     * 泊松分布随机数的生成
     * @Title: getPoissionDistribution
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param lmd
     * @return int   返回类型
     * @throws
     */

    public static int getPoissionDistribution(double lmd){
        double m = Math.exp(-lmd);
        double p = 1.0;
        int k = 0 ;
        do{
            k++;
            p*= Math.random();
        }while (p>m);
        return k-1;
    }

    /**
     * 二项分布随机数的产生
     * @Title: getBinomialDistribution
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param n
     * @param p
     * @return int   返回类型
     * @throws
     */
    public static int getBinomialDistribution(int n ,double p){
        double r ;
        int x = 0;
        for (int i = 0; i < n; i++) {
            r = Math.random();
            if(r<p){
                x++;
            }
        }
        return x;
    }
    /**
     * 产生指定区间N个不重复的随机数
     * @Title: getBinomialDistribution
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param max
     * @param n
     * @return int   返回类型
     * @throws
     */
    public static int[] randomSet(int max, int n) {
        int min = 1;
        Set<Integer> set = new HashSet<Integer>();
        int[] rd = new int[n];
        for (;true;) {
            int num = new Random().nextInt(max - min) + min;
            set.add(num);
            /*如果存入的数小于指定生成的个数，
            则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小*/
            if (set.size() >= n) {
                break;
            }
        }
        int i = 0;
        if(rd.length>0){
            for (int a : set) {
                rd[i] = a;
                i++;
            }
        }
        Arrays.sort(rd);
        return rd;
    }
    public  static loadVehMap randmsetCar(int loadCar){
        loadVehMap vMap = new loadVehMap();
        if(loadCar==1){
            int[] c = randomSupplier.randomSet(100,1);
            vMap.put(c[0],1);
            return vMap;
        }
        if(loadCar>1){
            int cc = new Random().nextInt(loadCar - (loadCar/2)) + (loadCar/2);
            vMap = ggf1(loadCar,cc);
            return vMap;
        }
        return null;
    }

    public static loadVehMap ggf1(int N, int N0){
        int sum = 0;
        int sp =100;
        List o = new ArrayList<>();
        loadVehMap vMap = new loadVehMap();
        int lmd= MathSupplier.divide(N,N0);
        int k=0,k0=0;
        while (k < N0){
            int r = randomSupplier.getPoissionDistribution(lmd);
            sum+=r;
            if(r==0){
                k=k0;
                continue;
            }
            if(sum>N){
                k=k0;
                sum=sum-r;
                continue;
            }
            if(sum<N){
                o.add(r);
                k=k0;
                continue;
            }
            if(sum==N){
                o.add(r);
                break;
            }
        }
        int[] tv = randomSupplier.randomSet(sp,o.size());
        int s =0;
        for (int i = 0; i < tv.length; i++) {
            vMap.put(tv[i],(int)o.get(i));
            s+=(int)o.get(i);
        }
        //System.out.println("s："+s +":"+N+":"+vMap.size());
        return vMap;
    }


    public static loadVehMap ggf(int N, int N0){
        int sum = 0;
        int sp =100;
        List o = new ArrayList<>();
        loadVehMap vMap = new loadVehMap();
        if(N == 0){
           return vMap;
        }
        int lmd= MathSupplier.divide(N,N0);
        int k0=0;
        for (int k = 0; k < N0; k++) {
            int r = randomSupplier.getPoissionDistribution(lmd);
            sum+=r;
            if(r==0){
                N0=N0+1;
               continue;
            }else{
                if(sum>N){
                    if(sum-N>=N){
                        o.add(N);
                        break;
                    }else{
                        o.add(sum-N);
                        sum=sum-N;
                        N0=N0+1;
                       continue;
                    }
                }
                if(sum==N){
                    o.add(r);
                    break;
                }
                o.add(r);
            }
            if(k==N0-1 && sum<N){
                o.add(N-sum);
            }
        }
        int[] tv = randomSupplier.randomSet(sp,o.size());
        int s =0;
        for (int i = 0; i < tv.length; i++) {
            vMap.put(tv[i],(int)o.get(i));
            s+=(int)o.get(i);
        }
        System.out.println("s："+s +":"+N+":"+vMap.size());
        return vMap;
    }

    public static int sumd(List o){
        int sum =0;
        for (int i = 0; i < o.size(); i++) {
            sum+=(int)o.get(i);
        }
        return sum;
    }

    public static void dds(){
        int num = 10;//产生随机数的个数
        double lamda = 4;// lamda值
        int sum = 0;
        for(int i = 0; i < num; i++){
            BigDecimal p0 = new BigDecimal(Math.exp(-lamda));
            int k = 0;
            while(true){
                double randValue = Math.random();
                if (p0.doubleValue()>randValue) {
                    break;
                }else{
                    p0 = p0.multiply(new BigDecimal(1.0 * lamda / (k + 1)));
                    k++;
                    //System.out.println(randValue);
                }
                //防止找不到的情况
                if( k >= 3 * lamda ){
                    k = 0;p0 = new BigDecimal(Math.exp(-lamda));
                }
                //System.out.println("--------------");
               /* if(k==0){
                    p0 = p0.multiply(new BigDecimal(1.0 * lamda / (k + 1)));
                    k++;
                }*/
            }
            sum+=k;
            if(sum<num){
            }
            System.out.println(i+" : "+k);
        }
        System.out.println("随机数的和："+sum);
    }

    private static int getPossionVariable(double lamda) {
        int x = 0;
        double y = Math.random(), cdf = getPossionProbability(x, lamda);
        while (cdf < y) {
            x++;
            cdf += getPossionProbability(x, lamda);
        }
        return x;
    }

    private static double getPossionProbability(int k, double lamda) {
        double c = Math.exp(-lamda), sum = 1;
        for (int i = 1; i <= k; i++) {
            sum *= lamda / i;
        }
        return sum * c;
    }
    /**
     * 方法一：最简单最易理解的两重循环去重
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static List<Integer> randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        List<Integer> res = new ArrayList<Integer>();
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        Arrays.sort(result);
        for (int s : result) {
            res.add(s);
        }
        return res;
    }

    public static void main(String[] args) {
        /*for (int i = 1; i < 51; i++) {
            randmsetCar(i);
        }*/
        List d = randomCommon(1,100,40);
        for (Object o : d) {
            System.out.println(o);
        }
       /* int c = 0;
        for (int i = 1; i < 201; i++) {
            double d = Math.random();
            double m =  MathSupplier.mul(i,MathSupplier.div(100,200));
            double r = getPossionProbability(1,m);
            if(r>d){
                System.out.println("第"+i+ "产生一辆车");
                c+=1;
            }
        }
        System.out.println("c= :"+c);*/
        //dds();
        //System.out.println(getPoissionDistribution(2));
    }



}
