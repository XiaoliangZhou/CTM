package transport.ctm.main;

import CGA.Model.CellChrome;
import CGA.Model.Neighbour;
import algorithm.GAUtil;
import algorithm.TDCGAUtil;
import algorithm.TD_CGA;
import org.junit.Test;
import transport.file.util.fileutils;

import java.io.IOException;
import java.util.List;

public class D3_cGAs {
    static double a = -5,b = 5;
    /*种群大小*/
    static int PopSize =9;
    /*最大进化代数*/
    static int MaxGen =100;
    /*个体的二进制标识位数*/
    static int Preci =6;
    /*交叉概率*/
    static double Pc =0.7;
    static double Ps =0.5;
    /*交叉概率*/
    static double Pm =0.01;
    static int m = 2;




                                   /*##############################################*/
    /*########################################### 同步3D-cGA 算法测试 ########################################################*/
    @Test()
    public void Synch_3D_cGAs() throws IOException,Exception {
        int sample = 1;
        int cx = 1;
        int cnt=0;
        double[] f = new double[MaxGen];
        double[][] x = new double[PopSize*PopSize*PopSize][6];
        while (cx<sample+1){
            int step = 0;
            CellChrome[][][] chroms = TD_CGA.initPop();
            CellChrome[][][] aux_pop = TDCGAUtil.createNewPop(chroms,PopSize);
            int t=0,t1=0,t2=0,t3=0;
            while(step<MaxGen){
                /*2、计算适应度*/
                //TD_CGA.updateAdaptive(chroms);
                /*获取种群当代精英个体*/
                CellChrome elite = TDCGAUtil.getMaxFitObject(chroms);
                //System.out.println(step +" "+elite.getId()+" "+elite.getFitness());
                double sum = 0.0;
                boolean flag = false;
                for (int i = 1; i < chroms[0].length-1; i++) {
                    for (int j = 1; j < chroms[i].length - 1; j++) {
                        for (int k = 1; k < chroms[j].length - 1; k++) {
                            /*中心个体*/
                            CellChrome c1 = chroms[i][j][k];
                            double c_f = c1.getFitness();/*中心个体V(i,j)适应度*/
                            List<Neighbour> neighbours = TDCGAUtil.ruleOfThreeSpaceType(chroms, i, j, k, c_f, c1.getOlcs());/*获取邻居*/
                            TD_CGA.setMaxNeighFitness(c1);
                            /*通过二元锦标赛选出邻居个体*/
                            //ESS_CGA.routtleSelect(c1,neighbours);
                            //TD_CGA.binaryTournamentSelection(c1,neighbours);
                            TDCGAUtil.tournamentSelection1(c1,neighbours,Ps,PopSize);
                            TD_CGA.stochastiTournamentSelection(neighbours,Ps);
                            /*交叉、变异(算术交叉)*/
                            CellChrome cc = TDCGAUtil.AUX(Pc,c1,elite);
                            TDCGAUtil.Mutation(Pm,cc);
                            /*替换策略*/
                            TDCGAUtil.replaceIfBetter1(aux_pop,i, j, k,c1,cc,elite);

                            double fx = GAUtil.func(c1.getX1(),c1.getX2());
                            String[] strs =  c1.getId().split("_");
                           /* if(fx>0.9903){
                                //System.out.println(cx+" "+step+" "+c1.getId() + " "+fx);
                            }*/
                            if(step==10){
                                x[t][0] = Double.parseDouble(strs[0]);
                                x[t][1] = Double.parseDouble(strs[1]);
                                x[t][2] = Double.parseDouble(strs[2]);
                                x[t][3] = c1.getX1();
                                x[t][4] = c1.getX2();
                                x[t][5] = fx;
                                if(fx>=0.9903){
                                   // System.out.println(cx+" "+step+" "+ x[t][0] +" "+ x[t][1]+" "+ x[t][2] + " "+fx);
                                    flag=true;
                                }
                                t++;
                            }
                            sum+=fx;
                        }
                    }
                }
                if(flag){
                    flag=false;
                    sample=cx;
                }
                //chroms=TDCGAUtil.deepCopy(aux_pop);/*更新种群*/
                step++;
                f[step-1] += sum;
            }
            fileutils.writeToTxtBypath("D:\\ga.txt",x);
            cx++;
        }

        /*计算种群函数平均值*/
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]/(sample*PopSize*PopSize*PopSize));
            //System.out.println(f[i]/(sample));
        }
    }
                                       /*##############################################*/
    /*########################################### 同步3D-cGA 选择压力测试 ########################################################*/
    @Test()
    public void Asyn_3D_cGAs_Gc() throws IOException,Exception {
        int sample = 10;
        int cx = 0;
        double[] f = new double[MaxGen];
        while (cx<sample){
            int step = 0;
            CellChrome[][][] chroms = TD_CGA.initPop();
            CellChrome[][][] aux_pop = TDCGAUtil.createNewPop(chroms,PopSize);
            int t=0,t1=0,t2=0,t3=0;
            while(step<MaxGen){
                /*2、计算适应度*/
                TD_CGA.updateAdaptive(chroms);
                double sum = 0.0;
                for (int i = 1; i < chroms[0].length-1; i++) {
                    for (int j = 1; j < chroms[i].length - 1; j++) {
                        for (int k = 1; k < chroms[j].length - 1; k++) {
                            /*中心个体*/
                            CellChrome c1 = chroms[i][j][k];
                            double c_f = c1.getFitness();/*中心个体V(i,j)适应度*/
                            List<Neighbour> neighbours = TDCGAUtil.ruleOfThreeSpaceType(chroms, i, j, k, c_f, c1.getOlcs());/*获取邻居*/
                            /*通过二元锦标赛选出邻居个体*/
                            //TD_CGA.binaryTournamentSelection(c1,neighbours);
                            //TDCGAUtil.tournamentSelection(c1,neighbours,Ps);
                            TD_CGA.stochastiTournamentSelection(neighbours,Ps);
                            /*替换策略*/
                            TDCGAUtil.replaceIfBetterTest(aux_pop, i, j, k, c1);
                            if(c1.getFitness()==3){
                                sum+=1;
                            }
                        }
                    }
                }
               // chroms=TDCGAUtil.deepCopy(aux_pop,PopSize);/*更新种群*/
                f[step] += sum;
                step++;

            }
            cx++;
        }

        /*计算种群函数平均值*/
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]/(sample*PopSize*PopSize*PopSize));
            //System.out.println(f[i]/(sample));
        }

    }
    /*##############################################*/
    /*########################################### 异步3D-cGA 选择压力测试 ########################################################*/
    @Test()
    public void ssyn_3D_cGAs_Gc() throws IOException,Exception {
        int sample = 20;
        int cx = 0;
        double[] f = new double[MaxGen];
        while (cx<sample){
            int step = 0;
            CellChrome[][][] chroms = TD_CGA.initPop();
            CellChrome[][][] aux_pop = TDCGAUtil.createNewPop(chroms,PopSize);
            /*2、计算适应度*/
            TD_CGA.updateAdaptive(chroms);
            int t=0,t1=0,t2=0,t3=0;
            while(step<MaxGen){
                double sum = 0.0;
                for (int i = 1; i < chroms[0].length-1; i++) {
                    for (int j = 1; j < chroms[i].length - 1; j++) {
                        for (int k = 1; k < chroms[j].length - 1; k++) {
                            /*中心个体*/
                            CellChrome c1 = chroms[i][j][k];
                            double c_f = c1.getFitness();/*中心个体V(i,j)适应度*/
                            List<Neighbour> neighbours = TDCGAUtil.ruleOfThreeSpaceType(chroms, i, j, k, c_f, c1.getOlcs());/*获取邻居*/
                            /*通过二元锦标赛选出邻居个体*/
                            TD_CGA.stochastiTournamentSelection(neighbours,Ps);
                            /*替换策略*/
                            TDCGAUtil.replaceIfBetterTest(chroms, i, j, k, c1);
                            if(c1.getFitness()==3){
                                sum+=1;
                            }
                        }
                    }
                }
               // chroms=TDCGAUtil.deepCopy(aux_pop,PopSize);/*更新种群*/
                TD_CGA.updateAdaptive(chroms);/*2、更新适应度*/
                f[step] += sum;
                step++;

            }
            cx++;
        }

        /*计算种群函数平均值*/
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]/(sample*PopSize*PopSize*PopSize));
            //System.out.println(f[i]/(sample));
        }

    }
}
