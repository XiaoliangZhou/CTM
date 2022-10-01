package CGA.JMetal;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

/**
 * NSGAII 算法求解
 * @author shup
 *
 */
public class NSGAIIRunner {
    public static void main(String[] args) {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

        // 定义优化问题
        problem = new BoothProblem();
        // 种群规模
        int popSize = 80;
        // SBX交叉算子
        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        // 变异算子
        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
        // 选择算子
        selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());
        // 注册
        algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation, popSize)
                .setSelectionOperator(selection).setMaxEvaluations(30000).build();
        // 运行算法
        new AlgorithmRunner.Executor((Algorithm<?>) algorithm).execute();

        // 结果集输出
        List<DoubleSolution> population = algorithm.getResult();
        for (DoubleSolution p : population) {
            System.out.println(p);
        }
    }
}