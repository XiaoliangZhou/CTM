package transport.ctm.main;

import org.junit.Test;

import java.io.IOException;

public class MOCellTest {
    static int tour = 2;
    static double Inf = Double.POSITIVE_INFINITY;
    static int popSize =5;
    static int f_num = 2;
    static int x_num = 3;
    static double a = 2, b = 5;
    static int Gmin = 2, Gmax = 6;
    static double a1 = 0, b1 = 5;
    static double yta1 = 20;
    static double yta2 = 20;
    static String fname = "Kursawe";
    static double pc=1;
    static double pm=1/125;
    static int maxGen=100;

    @Test()
    public void MOCTest() throws IOException,Exception {
     /*   *//*1、随机产生初始种群*//*
        Chrome[][][] chroms = D3MOCell.initPop();
        *//*2、产生一个存放pareto解集空种群*//*
        List<Chrome> p_f_lists = new ArrayList<>(popSize*popSize);
        Chrome[][][] aux_pop = D3MOCell.createNewPop(chroms,popSize);
        int step=0;
        double[] f = new double[maxGen];
        while(step<maxGen){
            for (int i = 1; i < chroms[0].length-1; i++) {
                for (int j = 1; j < chroms[i].length - 1; j++) {
                    for (int k = 1; k < chroms[j].length - 1; k++) {
                        Chrome chr = chroms[i][j][k];*//*种群个体*//*
                        chr.setCurChr(true);
                         List<Chrome> cur_neigh_lists = D3MOCell.ruleOfThreeSpaceType(chroms,i,j,k);
                        *//*二进制选择，从周围邻居中选出两个个体作为父本*//*
                        List<Chrome> t_f_lists = D3MOCell.tournamentSelection(cur_neigh_lists,tour);

                        Chrome c = D3MOCell.getBestSolutions(t_f_lists,chr);
                        if(!c.getId().equals(chr.getId())){
                            Chrome c0 = (Chrome) chr.clone();
                            *//*子代占优，子代替换当前个体*//*
                            c0.setF(c.getF());
                            c0.setX(c.getX());
                            c0.setCurChr(false);

                            p_f_lists = D3MOCell.insertParetoFront(p_f_lists,c);
                            aux_pop[i][j][k]=c0;
                        }else{
                            chr.setCurChr(false);
                            aux_pop[i][j][k]=chr;
                        }
                    }
                }
            }
            int sum=0;
            for (int i = 1; i < chroms[0].length-1; i++) {
                for (int j = 1; j < chroms[i].length - 1; j++) {
                    for (int k = 1; k < chroms[j].length - 1; k++) {
                        Chrome chr = chroms[i][j][k];*//*种群个体*//*
                        double[] fx = chroms[i][j][k].getF();
                        if(fx[0]==-20&fx[1]==0){
                            sum+=1;
                        }
                    }
                }
            }
            f[step] += sum;
            chroms= D3MOCell.deepCopy(aux_pop,popSize);*//*更新种群*//*
            //D3MOCell.feedBackStrategy(chroms,p_f_lists);
            step++;
        }
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]/(popSize*popSize*popSize));
        }*/
    }

    @Test()
    public void MOCTest1() throws IOException,Exception {
       /* *//*1、随机产生初始种群*//*
        Chrome[][] chroms = MOCell.initPop();
        *//*2、产生一个存放pareto解集空种群*//*
        List<Chrome> p_f_lists = new ArrayList<>(popSize*popSize);
        Chrome[][] aux_pop = MOCell.createNewPop(chroms,popSize);
        int step=0;
        double[] f = new double[maxGen];
        while(step<maxGen){
            for (int i = 1; i < chroms[0].length-1; i++) {
                for (int j = 1; j < chroms[i].length - 1; j++) {
                    Chrome chr = chroms[i][j];*//*种群个体*//*
                    chr.setCurChr(true);
                    List<Chrome> cur_neigh_lists = MOCell.ruleOfMooreType(chroms,i,j);
                    *//*二进制选择，从周围邻居中选出两个个体作为父本*//*
                    List<Chrome> t_f_lists = MOCell.tournamentSelection(cur_neigh_lists,tour);
                    Chrome c = MOCell.getBestSolutions(t_f_lists,chr);
                    if(!c.getId().equals(chr.getId())){
                        Chrome c0 = (Chrome) chr.clone();
                        *//*子代占优，子代提花当前个体*//*
                        c0.setF(c.getF());
                        c0.setX(c.getX());
                        c0.setCurChr(false);

                        p_f_lists = MOCell.insertParetoFront(p_f_lists,c);
                        aux_pop[i][j]=c0;
                    }else{
                        chr.setCurChr(false);
                        aux_pop[i][j]=chr;
                    }
                }
            }

            int sum=0;
            for (int i = 1; i < chroms[0].length-1; i++) {
                for (int j = 1; j < chroms[i].length - 1; j++) {
                    Chrome chr = chroms[i][j];*//*种群个体*//*
                    double[] fx = chroms[i][j].getF();
                    if(fx[0]==-20&fx[1]==0){
                        sum+=1;
                    }
                }
            }
            f[step] += sum;

            chroms= MOCell.deepCopy(aux_pop,popSize);*//*更新种群*//*
            *//*反馈机制*//*
            if(step%100==0){
                System.out.println(step + "  gen is completed!");
            }
            //MOCell.feedBackStrategy(chroms,p_f_lists);
            step++;
        }
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]/(popSize*popSize));
        }
*/
    }

    @Test()
    public void test1() throws IOException,Exception {
        int c=16;
        int[] x = new int[4];
        for (int i1 = Gmin; i1 < Gmax+1; i1++) {
            for (int j1 = Gmin; j1 <  Gmax+1; j1++) {
                for (int k1 = Gmin; k1 <  Gmax+1; k1++) {
                    for (int h1 = Gmin; h1 <  Gmax+1; h1++) {
                        if(i1+j1+k1+h1==c){
                            x[0] = i1;
                            x[1] = j1;
                            x[2] = k1;
                            x[3] = h1;

                            System.out.println(i1*5+"   "+j1*5+"   "+k1*5+"   "+h1*5);
                        }
                    }
                }
            }
        }

    }
}
