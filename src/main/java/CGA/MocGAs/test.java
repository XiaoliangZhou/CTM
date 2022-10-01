package CGA.MocGAs;

import CGA.Model.Chrome;
import CGA.cellde.CellDE45;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontNormalizer;
import CGA.front.util.FrontUtils;
import CGA.multiobjective.ConstrEx;
import CGA.multiobjective.Fonseca;
import CGA.multiobjective.Golinski;
import CGA.multiobjective.Osyczka2;
import CGA.multiobjective.dtlz.DTLZ1;
import CGA.multiobjective.dtlz.DTLZ2;
import CGA.multiobjective.glt.GLT1;
import CGA.multiobjective.glt.GLT2;
import CGA.multiobjective.wfg.*;
import CGA.multiobjective.zdt.ZDT1;
import CGA.multiobjective.zdt.ZDT2;
import CGA.multiobjective.zdt.ZDT4;
import CGA.multiobjective.zdt.ZDT6;
import CGA.mutation.PolynomialMutation;
import CGA.point.PointSolution;
import CGA.qualityIndicator.impl.GenerationalDistance;
import CGA.qualityIndicator.impl.Spread;
import CGA.qualityIndicator.impl.hypervolume.PISAHypervolume;
import CGA.selection.TournamentSelection;
import CGA.problem.Problem;
import algorithm.Algorithm;
import org.junit.Test;

import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.neighborhood.Neighborhood;
import org.uma.jmetal.util.neighborhood.impl.C9;
import util.AlgorithmRunner;

import java.util.ArrayList;
import java.util.List;

import static util.AbstractAlgorithmRunner.printFinalSolutionSet;
import static util.AbstractAlgorithmRunner.printQualityIndicators;


public class test<S extends Chrome> {


    @Test
    public void d3_moc_test(){
        int populationSize =100;
        Algorithm<List<Chrome>> algorithm = null;
        Problem<Chrome> problem;
        final SolutionListEvaluator<S> evaluator;
        SBXCrossover sbxCrossover;
        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        TournamentSelection selection ;
        PolynomialMutation mutation;
        String  referenceParetoFront = "src/main/pareto_fronts/GLT2.pf";

        //定义优化问题
        problem =  new GLT2();
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
     /*   algorithm = new D3MOCellBuilder<Chrome>(problem,sbxCrossover,differentialEvolutionCrossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(25000)
                .build();*/
        //用AlgorithmRunner运行算法

        int runCycle = 1;
        int count =0;
        List<Double> hvs = new ArrayList<>(runCycle);
        List<Double> gds = new ArrayList<>(runCycle);
        List<Double> sps = new ArrayList<>(runCycle);
        AlgorithmRunner algorithmRunner = null;
        while (count<runCycle){
           /* algorithm = new CellDE45(problem, 25000, populationSize, selection, differentialEvolutionCrossover, 100);*/
            algorithm = new D3MOCellBuilder<Chrome>(problem,sbxCrossover,differentialEvolutionCrossover, mutation)
                    .setSelectionOperator(selection)
                    .setMaxEvaluations(25000)
                    .build();
            algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<Chrome> population = algorithm.getResult();
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

       // AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //获取结果集
        List<Chrome> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        //JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //将全部种群打印到文件中
        //printFinalSolutionSet(population);

        }

}
