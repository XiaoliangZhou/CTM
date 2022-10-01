package CGA.cellde;

import CGA.archive.BoundedArchive;
import CGA.comparator.CrowdingDistanceComparator;
import CGA.comparator.DominanceComparator;
import CGA.comparator.RankingAndCrowdingDistanceComparator;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.evaluator.SolutionListEvaluator;
import CGA.evaluator.impl.SequentialSolutionListEvaluator;
import CGA.neighborhood.Neighborhood;
import CGA.neighborhood.impl.C3D;
import CGA.problem.Problem;
import CGA.Model.Chrome;
import CGA.selection.TournamentSelection;
import CGA.solutionattribute.Ranking;
import CGA.solutionattribute.impl.CrowdingDistance;
import CGA.solutionattribute.impl.DominanceRanking;
import algorithm.Algorithm;

import algorithm.TDCGAUtil;

import util.JMetalRandom;

import java.util.*;

public class CellDE45 implements Algorithm<List<Chrome>> {
    private Problem<Chrome> problem;
    protected Chrome[][][] population;
    private int populationSize;
    protected int martxSize;
    protected int evaluations;
    protected int maxEvaluations;
    private Neighborhood neighborhood;
    private Chrome currentIndividual;
    private List<Chrome> currentNeighbors;
    private TournamentSelection selection;
    private DifferentialEvolutionCrossover crossover;
    protected List<Chrome> archive;
    private Comparator<Chrome> dominanceComparator;
    private SolutionListEvaluator<Chrome> evaluator;
    private double feedback;
    private CrowdingDistanceComparator<Chrome> comparator = new CrowdingDistanceComparator();
    private CrowdingDistance<Chrome> distance = new CrowdingDistance();

    public CellDE45(Problem<Chrome> problem,
                    int maxEvaluations,
                    int populationSize,
                    TournamentSelection selection,
                    DifferentialEvolutionCrossover crossover,
                    double feedback) {

        this.problem = problem;
        this.populationSize = populationSize;
        this.martxSize = 5;
        this.maxEvaluations = maxEvaluations;
        this.archive = new ArrayList(populationSize);
        this.neighborhood = new C3D( this.martxSize, this.martxSize, this.martxSize -1);;
        this.selection = selection;
        this.crossover = crossover;
        this.dominanceComparator = new DominanceComparator();
        this.feedback = feedback;
        this.evaluator = new SequentialSolutionListEvaluator();
    }

    public void run() {
        this.population = this.createInitialPopulation();
        this.population = this.inintEvaluatePopulation(this.population);
        this.initProgress();
        martxSize = population.length;
        while(!this.isStoppingConditionReached()) {
            for(int i = 0; i < martxSize; ++i) {
                for (int j = 0; j < martxSize; ++j) {
                    for (int k = 0; k < martxSize - 1; ++k) {
                        List<Chrome> matingPopulation =  selection(this.population,i,j,k);
                        List<Chrome> offspringPopulation = this.reproduction(matingPopulation);
                        offspringPopulation = this.evaluatePopulation(offspringPopulation);
                        this.population = this.replacement(this.population, offspringPopulation,i,j,k);
                        this.updateProgress();
                    }
                }
            }
            for(int i = 0; (double)i < this.feedback; ++i) {
                if (this.archive.size() > i) {
                    int random = JMetalRandom.getInstance().nextInt(0, this.archive.size() - 1);
                    if (random < this.populationSize) {
                        Chrome solution = (Chrome)this.archive.get(i);
                        int axis = new Random().nextInt(martxSize) ;
                        int yxis = new Random().nextInt(martxSize) ;
                        int hxis = new Random().nextInt(martxSize-1) ;
                        population[axis][yxis][hxis] = (solution.copy());
                    }
                }
            }
        }
    }

    public Problem<Chrome> getProblem() {
        return this.problem;
    }
    protected Chrome[][][] createInitialPopulation() {
        int martxSize = this.martxSize;
        Chrome[][][] populations = new Chrome[martxSize][martxSize][martxSize-1];
        for(int i = 0; i < martxSize; ++i) {
            for(int j = 0; j < martxSize; ++j) {
                for(int k = 0; k < martxSize-1; ++k) {
                    Chrome newIndividual = this.getProblem().createSolution(i,j,k);
                    populations[i][j][k] = newIndividual;
                }
            }

        }
        return populations;
    }


    protected Chrome[][][] inintEvaluatePopulation(Chrome[][][] populations) {
        this.evaluator.evaluate(populations,this.martxSize, this.getProblem());
        int martxSize = this.martxSize;
        for(int i = 0; i < martxSize; ++i) {
            for(int j = 0; j < martxSize; ++j) {
                for(int k = 0; k < martxSize-1; ++k) {
                    Chrome solution = populations[i][j][k];;
                    add(solution.copy());
                }
            }

        }
        return populations;
    }
    public boolean add(Chrome solution) {
        boolean success = addArchive(solution);
        if (success) {
            this.prune();
        }
        return success;
    }

    public void prune() {
        if (this.archive.size() > 100) {
            CrowdingDistance<Chrome> crowdingDistance = new CrowdingDistance();
            crowdingDistance.computeDensityEstimator(this.archive);
            Chrome worst = findWorstSolution(this.archive,this.comparator);
            this.archive.remove(worst);
        }

    }
    public  Chrome findWorstSolution(Collection<Chrome> solutionList, Comparator<Chrome> comparator) {
        if (solutionList != null && !solutionList.isEmpty()) {
            Chrome worstKnown = solutionList.iterator().next();
            Iterator var4 = solutionList.iterator();

            while(var4.hasNext()) {
                Chrome candidateSolution = (Chrome) var4.next();
                if (comparator.compare(worstKnown, candidateSolution) < 0) {
                    worstKnown = candidateSolution;
                }
            }
            return worstKnown;
        } else {
            throw new IllegalArgumentException("No solution provided: " + solutionList);
        }
    }
    public  List<Chrome> selection(Chrome[][][] populations,int axis,int yaxis,int haxis)  {
        currentIndividual = populations[axis][yaxis][haxis];
        currentNeighbors = this.neighborhood.getNeighbors(populations,axis,yaxis,haxis);
        currentNeighbors.add(currentIndividual);
        List<Chrome> parents = new ArrayList(2);
        parents.add(this.selection.execute(this.currentNeighbors));
        parents.add(this.selection.execute(this.currentNeighbors));
        /*if (this.archive.size() > 0) {
            parents.add(this.selection.execute(this.archive));
        } else {
            parents.add(this.selection.execute(this.currentNeighbors));
        }*/
        this.crossover.setCurrentSolution(currentIndividual);
        parents.add(currentIndividual);
        return parents;
    }
    public boolean addArchive(Chrome solution) {
        boolean solutionInserted = false;
        if (this.archive.size() == 0) {
            this.archive.add(solution);
            solutionInserted = true;
            return solutionInserted;
        } else {
            Iterator<Chrome> iterator = this.archive.iterator();
            boolean isDominated = false;
            boolean isContained = false;

            while(!isDominated && !isContained && iterator.hasNext()) {
                Chrome listIndividual = (Chrome)iterator.next();
                int flag = this.dominanceComparator.compare(solution, listIndividual);
                if (flag == -1) {
                    iterator.remove();
                } else if (flag == 1) {
                    isDominated = true;
                } else if (flag == 0) {
                    int equalflag = this.equalcompare(solution, listIndividual);
                    if (equalflag == 0) {
                        isContained = true;
                    }
                }
            }

            if (!isDominated && !isContained) {
                this.archive.add(solution);
                solutionInserted = true;
            }

            return solutionInserted;
        }
    }

    public int equalcompare(Chrome solution1, Chrome solution2) {
        if (solution1 == null) {
            return 1;
        } else if (solution2 == null) {
            return -1;
        } else {
            boolean dominate1 = false;
            boolean dominate2 = false;

            for (int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
                double value1 = solution1.getObjective(i);
                double value2 = solution2.getObjective(i);
                byte flag;
                if (value1 < value2) {
                    flag = -1;
                } else if (value1 > value2) {
                    flag = 1;
                } else {
                    flag = 0;
                }

                if (flag == -1) {
                    dominate1 = true;
                }

                if (flag == 1) {
                    dominate2 = true;
                }
            }

            if (!dominate1 && !dominate2) {
                return 0;
            } else if (dominate1) {
                return -1;
            } else if (dominate2) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    protected List<Chrome> reproduction(List<Chrome> doubleSolutions) {
        List<Chrome> result = new ArrayList<>();
        /*二进制交叉*/
        //List<Chrome> offspring = this.crossover.execute(doubleSolutions);
        this.crossover.setCurrentSolution(currentIndividual);
        List<Chrome> offspring = this.crossover.execute((List<Chrome>) doubleSolutions);
        /*变异*/
        result.add(offspring.get(0));
        return result;
    }

    protected Chrome[][][] replacement(Chrome[][][] population, List<Chrome> list1,int axis,int yaxis,int haxis) {
        Chrome newIndivdual = list1.get(0);

        newIndivdual.setAxis(-1);
        newIndivdual.setYaxis(-1);
        newIndivdual.setHaxis(-1);
        int result = this.dominanceComparator.compare(currentIndividual,newIndivdual);
        if(result==1){
            /*新产生的个体支配当前个体*/
            population = insertNewIndividualWhenDominates(population,newIndivdual,axis,yaxis,haxis);
        }else if(result==0){
            population = insertNewIndividualWhenNonDominates(population,newIndivdual,axis,yaxis,haxis);
        }
        return  population;
    }

    private  Chrome[][][] insertNewIndividualWhenDominates(Chrome[][][] populations,
                                                           Chrome offspringPopulation,int axis,int yaxis,int haxis)  {
        int currentIndivdualAxis = currentIndividual.getAxis();
        int currentIndivdualYaxis = currentIndividual.getYaxis();
        int currentIndivdualHaxis = currentIndividual.getHaxis();

        updateAxisAndYaxisAndId(offspringPopulation,
                currentIndivdualAxis,currentIndivdualYaxis,currentIndivdualHaxis,currentIndividual.getId());

        Chrome[][][] results = TDCGAUtil.deepCopy(populations);

        results[axis][yaxis][haxis] = offspringPopulation;
        add(offspringPopulation);
        return results;
    }

    private  Chrome[][][] insertNewIndividualWhenNonDominates(Chrome[][][] populations,
                                                              Chrome offspringPopulation,int axis,int yaxis,int haxis)  {

        Ranking<Chrome> ranking = this.computeRanking(this.currentNeighbors);
        this.distance.computeDensityEstimator(ranking.getSubfront(0));
        Chrome[][][] results = TDCGAUtil.deepCopy(populations);
        boolean deleteMutant = true;
        int compareResult = this.comparator.compare(currentIndividual, offspringPopulation);
        if (compareResult == 1) {
            deleteMutant = false;
        }
        int wostAxis = currentIndividual.getAxis();
        int worstYaxis = currentIndividual.getYaxis();
        int worstHaxis = currentIndividual.getHaxis();
        updateAxisAndYaxisAndId(offspringPopulation,wostAxis,worstYaxis,worstHaxis,currentIndividual.getId());

        if (!deleteMutant) {
            results[wostAxis][worstYaxis][worstHaxis] = offspringPopulation;
            add(offspringPopulation.copy());
        } else {
            add(offspringPopulation.copy());
        }

        return results;
    }

    public void updateAxisAndYaxisAndId(Chrome offspringPopulation,int axis,int yaxis,int haxis,String id){
        if(offspringPopulation!=null){
            offspringPopulation.setId(id);
            offspringPopulation.setAxis(axis);
            offspringPopulation.setYaxis(yaxis);
            offspringPopulation.setHaxis(haxis);
        }
    }
    protected List<Chrome> evaluatePopulation(List<Chrome> population) {
        return this.evaluator.evaluate(population, this.problem);
    }

    protected void initProgress() {
        this.evaluations = this.populationSize;
    }

    protected void updateProgress() {
        ++this.evaluations;
    }

    protected boolean isStoppingConditionReached() {
        return this.evaluations == this.maxEvaluations;
    }

    public String getName() {
        return "CellDE";
    }

    public String getDescription() {
        return "Multi-Objective Differential Evolution Cellular evolutionary algorithm";
    }

    public List<Chrome> getResult() {
        return this.archive;
    }

    protected Ranking<Chrome> computeRanking(List<Chrome> solutionList) {
        Ranking<Chrome> ranking = new DominanceRanking();
        ranking.computeRanking(solutionList);
        return ranking;
    }
}
