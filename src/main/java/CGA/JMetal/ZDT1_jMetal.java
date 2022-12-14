package CGA.JMetal;


import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.cellde.CellDE45;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.SimpleRandomMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.*;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.glt.GLT1;
import org.uma.jmetal.problem.multiobjective.glt.GLT2;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.problem.multiobjective.wfg.WFG6;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT2;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT4;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT6;
import org.uma.jmetal.qualityindicator.impl.*;
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
import static org.uma.jmetal.util.AbstractAlgorithmRunner.printQualityIndicators;


/**
 * Solve ZDT1 with NSGA-II on jMetal
 */
public class ZDT1_jMetal {
    public static void main(String[] args) throws FileNotFoundException {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm = null;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;


        int populationSize =100;
        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        SolutionListEvaluator evaluator = new SequentialSolutionListEvaluator();;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        SelectionOperator<List<DoubleSolution>, List<DoubleSolution>> differentialEvolutionSelection;

        Neighborhood neighborhood = new C9((int)Math.sqrt((double)populationSize), (int)Math.sqrt((double)populationSize));;
        String referenceParetoFront = "src/main/pareto_fronts/DTLZ1.3D.pf";
        //??????????????????


         problem = new DTLZ1();
        //??????SBX????????????
        double crossoverProbability = 0.8;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        differentialEvolutionCrossover = new DifferentialEvolutionCrossover();
        //??????????????????
        double distributionIndex = 20.0D;
        mutation = new PolynomialMutation((DoubleProblem) problem,distributionIndex);
        //mutation = new PolynomialMutation();
        //??????????????????
        selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());
        differentialEvolutionSelection = new DifferentialEvolutionSelection();
        //??????????????????algorithm
     /*   algorithm = new MOCellBuilder<DoubleSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();*/



      /*  algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation,100)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();*/
/*       ??????????????????????????????????????????
          evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
          algorithm = new NSGAII<DoubleSolution>(problem, 25000, 100, crossover,
          mutation, selection, evaluator);*/

        AlgorithmRunner algorithmRunner = null;
        int runCycle = 100;
        List<Double> hvs = new ArrayList<>(runCycle);
        List<Double> gds = new ArrayList<>(runCycle);
        List<Double> sps = new ArrayList<>(runCycle);
        int count =0;
        while (count<runCycle){
             algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation,100)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();
         /*   algorithm = new MOCellBuilder<DoubleSolution>(problem, crossover, mutation)
                    .setSelectionOperator(selection)
                    .setMaxEvaluations(25000)
                    .build();*/
            algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<DoubleSolution> population = algorithm.getResult();
            if (!referenceParetoFront.equals("")) {
                try {

                    Front referenceFront = new ArrayFront(referenceParetoFront);
                    FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
                    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
                    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
                    List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

                    double hv = (new PISAHypervolume(normalizedReferenceFront)).evaluate(normalizedPopulation);
                    double gd = (new GenerationalDistance(normalizedReferenceFront)).evaluate(normalizedPopulation);
                    double spread =  (new Spread(normalizedReferenceFront)).evaluate(normalizedPopulation);

                    hvs.add(hv);
                    gds.add(gd);
                    sps.add(spread);

                    if(count==0){
                        printFinalSolutionSet(population);
                    }else{
                        double result = Double.compare(spread,sps.get(count-1));
                        if(result==-1){
                            printFinalSolutionSet(population);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++count;
        }
        hvs.forEach((hv)->{
            System.out.println(hv);
        });
        System.out.println("-----------------------------------");
        gds.forEach((hv)->{
            System.out.println(hv);
        });
        System.out.println("-----------------------------------");
        sps.forEach((hv)->{
            System.out.println(hv);
        });
        //???AlgorithmRunner????????????
        //AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //???????????????
        List<DoubleSolution> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //?????????????????????????????????
       printFinalSolutionSet(population);
        /* if (!referenceParetoFront.equals("")) printQualityIndicators(population, referenceParetoFront);*/
    }
}