package CGA.JMetal;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.cellde.CellDE45;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.*;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.glt.GLT1;
import org.uma.jmetal.problem.multiobjective.glt.GLT2;
import org.uma.jmetal.problem.multiobjective.wfg.WFG7;
import org.uma.jmetal.problem.multiobjective.wfg.WFG8;
import org.uma.jmetal.problem.multiobjective.wfg.WFG9;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT2;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT4;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.neighborhood.Neighborhood;
import org.uma.jmetal.util.neighborhood.impl.C9;
import org.uma.jmetal.util.point.PointSolution;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.uma.jmetal.util.AbstractAlgorithmRunner.printFinalSolutionSet;


/**
 * Solve ZDT1 with NSGA-II on jMetal
 */
public class singal_jmetal_test {
    public static void main(String[] args) throws FileNotFoundException {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;


        int populationSize =100;
        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        SolutionListEvaluator evaluator = new SequentialSolutionListEvaluator();;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        SelectionOperator<List<DoubleSolution>, List<DoubleSolution>> differentialEvolutionSelection;

        Neighborhood neighborhood = new C9((int)Math.sqrt((double)populationSize), (int)Math.sqrt((double)populationSize));;
        String referenceParetoFront = "src/main/pareto_fronts/Fonseca.pf";
        //定义优化问题


         problem = new GLT2();
        //配置SBX交叉算子
        double crossoverProbability = 0.8;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        differentialEvolutionCrossover = new DifferentialEvolutionCrossover();
        //配置变异算子
        double distributionIndex = 20.0D;
        mutation = new PolynomialMutation((DoubleProblem) problem,distributionIndex);
        //配置选择算子
        selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());
        differentialEvolutionSelection = new DifferentialEvolutionSelection();
        //将组件注册到algorithm
        algorithm = new MOCellBuilder<DoubleSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();

       /* algorithm = new CellDE45(problem,
                                    25000,
                                    populationSize,
                                    new CrowdingDistanceArchive(populationSize),
                                    neighborhood,
                                    selection,
                                    differentialEvolutionCrossover,
                                    100,
                                    evaluator);*/
      /*  algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation,100)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();*/
/*       或者用这样的方法注册一个算法
          evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
          algorithm = new NSGAII<DoubleSolution>(problem, 25000, 100, crossover,
          mutation, selection, evaluator);*/

        //用AlgorithmRunner运行算法
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //获取结果集
        List<DoubleSolution> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //将全部种群打印到文件中
       printFinalSolutionSet(population);
        /* if (!referenceParetoFront.equals("")) printQualityIndicators(population, referenceParetoFront);*/
    }
}