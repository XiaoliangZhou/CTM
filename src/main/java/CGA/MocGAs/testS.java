package CGA.MocGAs;

import CGA.Model.Chrome;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontNormalizer;
import CGA.front.util.FrontUtils;
import CGA.multiobjective.*;
import CGA.multiobjective.dtlz.DTLZ1;
import CGA.multiobjective.dtlz.DTLZ2;
import CGA.multiobjective.glt.GLT1;
import CGA.multiobjective.glt.GLT2;
import CGA.multiobjective.wfg.WFG7;
import CGA.multiobjective.wfg.WFG8;
import CGA.multiobjective.wfg.WFG9;
import CGA.multiobjective.zdt.ZDT1;
import CGA.multiobjective.zdt.ZDT2;
import CGA.multiobjective.zdt.ZDT4;
import CGA.mutation.PolynomialMutation;
import CGA.point.PointSolution;
import CGA.problem.Problem;
import CGA.qualityIndicator.impl.GenerationalDistance;
import CGA.qualityIndicator.impl.Spread;
import CGA.qualityIndicator.impl.hypervolume.PISAHypervolume;
import CGA.selection.TournamentSelection;
import algorithm.Algorithm;
import org.junit.Test;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import util.AlgorithmRunner;

import java.util.ArrayList;
import java.util.List;

import static util.AbstractAlgorithmRunner.printFinalSolutionSet;


public class testS<S extends Chrome> {


    @Test
    public void singal_d3_moc_test(){

        Algorithm<List<Chrome>> algorithm = null;
        Problem<Chrome> problem;
        final SolutionListEvaluator<S> evaluator;
        SBXCrossover sbxCrossover;
        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        TournamentSelection selection ;
        PolynomialMutation mutation;

        //定义优化问题
        problem =  new GLT1();
        //配置SBX交叉算子
        double crossoverProbability = 0.8;
        double crossoverDistributionIndex = 20.0;
        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

        differentialEvolutionCrossover = new DifferentialEvolutionCrossover();
        //配置变异算子
        double distributionIndex = 20.0D;;
        mutation = new PolynomialMutation(problem,distributionIndex);
        //mutation = new PolynomialMutation();
        //配置选择算子
        final int n_arity = 2;
        selection = new TournamentSelection(n_arity);
        algorithm = new D3MOCellBuilder<Chrome>(problem,sbxCrossover,differentialEvolutionCrossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();
        //用AlgorithmRunner运行算法
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //获取结果集
        List<Chrome> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //将全部种群打印到文件中
        printFinalSolutionSet(population);

        }

}
