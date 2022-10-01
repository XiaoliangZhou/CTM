package CGA.Model;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
public class Chromosome1 {
    public static final int GENG_LENGTH = 14;
    public static final int MAX_X = 127;
    public static final int MAX_Y = 127;
    private int x,y;
    private String gene;
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getGene() {
        return gene;
    }
    public Chromosome1(int x,int y) {
        if(x > MAX_X || y > MAX_Y || x < 0 || y < 0)
            return;
        this.x = x;
        this.y = y;
        String tem = Integer.toBinaryString(x);

        for(int i = tem.length(); i < GENG_LENGTH/2; i++) {
            tem = "0" + tem;
        }
        gene = tem;
        tem = Integer.toBinaryString(y);
        for(int i = tem.length(); i < GENG_LENGTH/2; i++) {
            tem = "0" + tem;
        }
        gene = gene + tem;
    }
    public Chromosome1(String gene) {
        if(gene.length() != Chromosome1.GENG_LENGTH)
            return;
        this.gene = gene;
        String xStr = gene.substring(0, Chromosome1.GENG_LENGTH/2);
        String yStr = gene.substring(Chromosome1.GENG_LENGTH/2);
        this.x = Integer.parseInt(xStr,2);
        this.y = Integer.parseInt(yStr,2);

    }
    public String toString() {
        return "x:" + x + "\ty:" + y + "\tgene:"+gene;
    }
    public void selfMutation(String newGene) {
        if(newGene.length() != Chromosome1.GENG_LENGTH)
            return;
        this.gene = newGene;
        String xStr = newGene.substring(0, Chromosome1.GENG_LENGTH/2);
        String yStr = newGene.substring(Chromosome1.GENG_LENGTH/2);
        this.x = Integer.parseInt(xStr,2);
        this.y = Integer.parseInt(yStr,2);
    }

    //初始化种群
    public static ArrayList<Chromosome1> initGroup(int size) {
        ArrayList<Chromosome1> list = new ArrayList<Chromosome1>();
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            int x = random.nextInt() % 128;
            int y = random.nextInt() % 128;
            x = x < 0? (-x):x;
            y = y < 0? (-y):y;
            list.add(new Chromosome1(x,y));
        }
        return list;
    }
    //计算适应度
    public int calcFitness() {
        return x*x+y*y;
    }
    //选择运算
    public static ArrayList<Chromosome1> selector(ArrayList<Chromosome1> fatherGroup,int sonGroupSize) {
        ArrayList<Chromosome1> sonGroup = new ArrayList<Chromosome1>();
        int totalFitness = 0;
        double[] fitness = new double[fatherGroup.size()];
        for(Chromosome1 chrom : fatherGroup) {
            totalFitness += chrom.calcFitness();
        }
        int index = 0;
        //计算适应度
        for(Chromosome1 chrom : fatherGroup) {
            fitness[index] = chrom.calcFitness() / ((double)totalFitness);
            index++;
        }
        //计算累加适应度
        for(int i = 1; i < fitness.length; i++) {
            fitness[i] = fitness[i-1]+fitness[i];
        }
        //轮盘赌选择
        for(int i = 0; i < sonGroupSize; i++) {
            Random random = new Random();
            double probability = random.nextDouble();
            int choose;
            for(choose = 1; choose < fitness.length - 1; choose++) {
                if(probability < fitness[choose])
                    break;
            }
            sonGroup.add(new Chromosome1(fatherGroup.get(choose).getGene()));
        }
        return sonGroup;
    }
    //交叉运算--one point
    public static ArrayList<Chromosome1> corssover(ArrayList<Chromosome1> fatherGroup,double probability) {
        ArrayList<Chromosome1> sonGroup = new ArrayList<Chromosome1>();
        sonGroup.addAll(fatherGroup);
        Random random = new Random();
        for(int k = 0; k < fatherGroup.size() / 2; k++) {
            if(probability > random.nextDouble()) {
                int i = 0,j = 0;
                do {
                    i = random.nextInt(fatherGroup.size());
                    j = random.nextInt(fatherGroup.size());
                } while(i == j);
                int position = random.nextInt(Chromosome1.GENG_LENGTH);
                String parent1 = fatherGroup.get(i).getGene();
                String parent2 = fatherGroup.get(j).getGene();
                String son1 = parent1.substring(0, position) + parent2.substring(position);
                String son2 = parent2.substring(0, position) + parent1.substring(position);
                sonGroup.add(new Chromosome1(son1));
                sonGroup.add(new Chromosome1(son2));
            }
        }
        return sonGroup;
    }
    //变异
    public static void mutation(ArrayList<Chromosome1> fatherGroup,double probability) {
        Random random = new Random();
        Chromosome1 bestOne = Chromosome1.best(fatherGroup);
        fatherGroup.add(new Chromosome1(bestOne.getGene()));
        for(Chromosome1 c : fatherGroup) {
            String newGene = c.getGene();
            for(int i = 0; i < newGene.length();i++){
                if(probability > random.nextDouble()) {
                    String newChar = newGene.charAt(i) == '0'?"1":"0";
                    newGene = newGene.substring(0, i) + newChar + newGene.substring(i+1);
                }
            }
            c.selfMutation(newGene);
        }
    }
    public static Chromosome1 best(ArrayList<Chromosome1> group) {
        Chromosome1 bestOne = group.get(0);
        for(Chromosome1 c : group) {
            if(c.calcFitness() > bestOne.calcFitness())
                bestOne = c;
        }
        return bestOne;
    }
    //测试
    public static void main(String[] args) {
        final int GROUP_SIZE = 20;
        final double CORSSOVER_P = 0.6;
        final double MUTATION_P = 0.01;
        ArrayList<Chromosome1> group = Chromosome1.initGroup(GROUP_SIZE);
        Chromosome1 theBest;
        do{
            group = Chromosome1.corssover(group, CORSSOVER_P);
            Chromosome1.mutation(group, MUTATION_P);
            group = Chromosome1.selector(group, GROUP_SIZE);
            theBest = Chromosome1.best(group);
            System.out.println(theBest.calcFitness());
        }while(theBest.calcFitness() < 32258);
    }
}