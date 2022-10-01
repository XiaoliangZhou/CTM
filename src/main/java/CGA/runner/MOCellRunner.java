package CGA.runner;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import CGA.problem.Problem;
import util.AbstractAlgorithmRunner;
import util.AlgorithmRunner;
import util.ProblemUtils;

import java.io.FileNotFoundException;
import java.util.List;

public class MOCellRunner extends AbstractAlgorithmRunner {
    public MOCellRunner() {
    }

    /*public static void main(String[] args) throws JMetalException, FileNotFoundException {
        String referenceParetoFront = "";
        String problemName;
        if (args.length == 1) {
            problemName = args[0];
        } else if (args.length == 2) {
            problemName = args[0];
            referenceParetoFront = args[1];
        } else {
            problemName = "CGA.multiobjective.zdt.ZDT6";
            referenceParetoFront = "resources/ZDT6.pf";
        }

        Problem<DoubleSolution> problem = ProblemUtils.loadProblem(problemName);
        double crossoverProbability = 0.9D;
        double crossoverDistributionIndex = 20.0D;
        CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        double mutationProbability = 1.0D / (double)problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0D;
        MutationOperator<DoubleSolution> mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
        Algorithm<List<DoubleSolution>> algorithm = (new MOCellBuilder(problem, crossover, mutation)).setSelectionOperator(selection).setMaxEvaluations(50000).setPopulationSize(100).setArchive(new CrowdingDistanceArchive(100)).build();
        AlgorithmRunner algorithmRunner = (new AlgorithmRunner.Executor(algorithm)).execute();
        List<DoubleSolution> population = (List)algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();
        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
            printQualityIndicators(population, referenceParetoFront);
        }

    }*/
}
