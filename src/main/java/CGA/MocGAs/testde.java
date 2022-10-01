package CGA.MocGAs;

import CGA.Model.Chrome;
import CGA.cellde.CellDE45;
import CGA.comparator.RankingAndCrowdingDistanceComparator;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.evaluator.SolutionListEvaluator;
import CGA.evaluator.impl.SequentialSolutionListEvaluator;
import CGA.multiobjective.Osyczka2;
import CGA.multiobjective.dtlz.DTLZ1;
import CGA.multiobjective.glt.GLT1;
import CGA.multiobjective.wfg.WFG1;
import CGA.multiobjective.wfg.WFG6;
import CGA.multiobjective.zdt.ZDT4;
import CGA.multiobjective.zdt.ZDT6;
import CGA.mutation.PolynomialMutation;
import CGA.neighborhood.Neighborhood;
import CGA.neighborhood.impl.C3D;
import CGA.neighborhood.impl.C9;
import CGA.problem.Problem;
import CGA.selection.TournamentSelection;
import algorithm.Algorithm;
import org.junit.Test;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;

import util.AlgorithmRunner;

import java.util.List;

import static util.AbstractAlgorithmRunner.printFinalSolutionSet;


public class testde<S extends Chrome> {


    @Test
    public void d3de_moc_test(){


        final int n_arity = 2;
        int populationSize =100;
        Algorithm<List<Chrome>> algorithm;
        Problem<Chrome> problem;
        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        String  referenceParetoFront = "src/main/pareto_fronts/ZDT4.pf";

        //定义优化问题
        problem =  new DTLZ1();
        //配置DE交叉算子
        differentialEvolutionCrossover = new DifferentialEvolutionCrossover();
        //配置选择算子
        TournamentSelection deSelection;
        deSelection =  new TournamentSelection<Chrome>(n_arity);
        algorithm = new CellDE45(problem, 25000, populationSize, deSelection, differentialEvolutionCrossover, 100);
        //用AlgorithmRunner运行算法
      /*  double[] attrbutes = new double[]{0.0,0.0,0.0};
        int runCycle = 200;
        int count =0;
        AlgorithmRunner algorithmRunner = null;
        while (count<runCycle){
            algorithm = new D3MOCellBuilder<Chrome>(problem,sbxCrossover,differentialEvolutionCrossover, mutation)
                    .setSelectionOperator(selection)
                    .setMaxEvaluations(25000)
                    .build();
            algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<Chrome> population = algorithm.getResult();
            if (!referenceParetoFront.equals("")) {
                try {
                     attrbutes = printQualityIndicators(population, referenceParetoFront,attrbutes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++count;
        }

        System.out.print("Hypervolume     : " + (attrbutes[0]/runCycle + "\n"));
        System.out.print("GD              : " + (attrbutes[1]/runCycle + "\n"));
        System.out.print("Spread          : " + (attrbutes[2]/runCycle + "\n"));*/
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //获取结果集
        List<Chrome> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //将全部种群打印到文件中
        printFinalSolutionSet(population);

        }

}
