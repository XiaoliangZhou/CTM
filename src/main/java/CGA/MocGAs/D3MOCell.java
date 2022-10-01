package CGA.MocGAs;
import CGA.comparator.DominanceComparator;
import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.evaluator.SolutionListEvaluator;
import CGA.mutation.PolynomialMutation;
import CGA.neighborhood.Neighborhood;
import CGA.problem.Problem;
import CGA.selection.TournamentSelection;
import CGA.solutionattribute.Ranking;
import CGA.solutionattribute.impl.CrowdingDistance;
import CGA.solutionattribute.impl.DominanceRanking;
import algorithm.AbstractGeneticAlgorithm;
import algorithm.TDCGAUtil;
import CGA.Model.Chrome;

import org.uma.jmetal.util.JMetalException;
import CGA.comparator.CrowdingDistanceComparator;
import CGA.comparator.ObjectiveComparator;
import CGA.comparator.RankingAndCrowdingDistanceComparator;
import util.JMetalRandom;
import util.RandomGenerator;

import java.util.*;

public class D3MOCell<S extends Chrome>  extends AbstractGeneticAlgorithm<S, List<S>> {

    protected int matrix;
    protected int evaluations;
    protected int maxEvaluations;
    protected SolutionListEvaluator<S> evaluator;
    protected Neighborhood neighborhood;
    protected Chrome currentIndividual;
    protected List<Chrome> currentNeighbors;
    protected List<Chrome> archive;
    protected Comparator<S> dominanceComparator;
    private List<ArrayList<Chrome>> rankedSubPopulations;
    private  RandomGenerator<Double> randomGenerator;
    private Comparator<Chrome> crowdingDistanceComparator = new CrowdingDistanceComparator();

    public D3MOCell(Problem<S> problem,
                    int matrix,
                    int evaluations,
                    int maxEvaluations,
                    SolutionListEvaluator<S> evaluator,
                    Neighborhood neighborhood,
                    List<Chrome> archive,
                    TournamentSelection selectionOperator,
                    SBXCrossover crossoverOperator,
                    DifferentialEvolutionCrossover differentEvolutionOperator,
                    PolynomialMutation mutationOperator, Comparator<S> dominanceComparator) {

        super(problem);
        this.matrix = matrix;
        this.evaluations = evaluations;
        this.maxEvaluations = maxEvaluations;
        this.evaluator = evaluator;
        this.neighborhood = neighborhood;
        this.currentIndividual = currentIndividual;
        this.archive = archive;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.differentialEvolutionOperator = differentEvolutionOperator;
        this.mutationOperator = mutationOperator;
        this.randomGenerator = ()->{return JMetalRandom.getInstance().nextDouble();};
        this.dominanceComparator = new DominanceComparator();
    }
    protected void initProgress() {
        this.evaluations = 0;
    }

    protected void updateProgress() {
        ++this.evaluations;
    }
    protected boolean isStoppingConditionReached() {
        return this.evaluations == this.maxEvaluations;
    }

    @Override
    protected Chrome[][][] createInitialPopulation() {
        int martxSize = this.matrix;
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

    @Override
    protected Chrome[][][] inintEvaluatePopulation(Chrome[][][] populations) {
        int martxSize = this.matrix;
        this.evaluator.evaluate(populations,martxSize, this.getProblem());
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

    @Override
    protected List<S> getArchives() {
        return (List<S>)this.archive;
    }

    protected List<S> evaluatePopulation(List<S> population) {
        population = this.evaluator.evaluate(population, this.getProblem());
        Iterator var2 = population.iterator();

        while(var2.hasNext()) {
            Chrome solution = (Chrome)var2.next();
            add(solution.copy());
        }

        return population;
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
            CrowdingDistance<S> crowdingDistance = new CrowdingDistance();
            crowdingDistance.computeDensityEstimator((List<S>) this.archive);
            Chrome worst = findWorstSolution(this.archive,this.crowdingDistanceComparator);
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
                int flag = this.dominanceComparator.compare((S)solution, (S)listIndividual);
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
    /**
     *
     * @param rank
     * @return
     */
    public List<Chrome> getSubfront(int rank) {
        if (rank >= this.rankedSubPopulations.size()) {
            throw new JMetalException("Invalid rank: " + rank + ". Max rank = " + (this.rankedSubPopulations.size() - 1));
        } else {
            return (List)this.rankedSubPopulations.get(rank);
        }
    }
    public int getNumberOfSubfronts() {
        return this.rankedSubPopulations.size();
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

        currentNeighbors.add(offspringPopulation);

        Ranking<S> rank = new DominanceRanking();
        rank.computeRanking((List<S>)this.currentNeighbors);
        CrowdingDistance<S> crowdingDistance = new CrowdingDistance();

        for(int j = 0; j < rank.getNumberOfSubfronts(); ++j) {
            crowdingDistance.computeDensityEstimator(rank.getSubfront(j));
        }


        Collections.sort(currentNeighbors, new RankingAndCrowdingDistanceComparator());
        Chrome worst = currentNeighbors.get(currentNeighbors.size() - 1);

        Chrome[][][] results = TDCGAUtil.deepCopy(populations);

        if(worst.getAxis()==-1){
            add(offspringPopulation);
        }else{
            int wostAxis = worst.getAxis();
            int worstYaxis = worst.getYaxis();
            int worstHaxis = worst.getHaxis();
            updateAxisAndYaxisAndId(offspringPopulation,wostAxis,worstYaxis,worstHaxis,worst.getId());
            results[wostAxis][worstYaxis][worstHaxis] = offspringPopulation;
            add(offspringPopulation);

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
    /**
     * @非支配排序
     * @param solutionSet
     * @return
     */
    public List<ArrayList<Chrome>> computeRanking(List<Chrome> solutionSet){

        List<Chrome> population = solutionSet;
        int[] dominateMe = new int[solutionSet.size()];
        List<List<Integer>> iDominate = new ArrayList(solutionSet.size());
        ArrayList<List<Integer>> front = new ArrayList(solutionSet.size() + 1);

        int flagDominate;
        for(flagDominate = 0; flagDominate < population.size() + 1; ++flagDominate) {
            front.add(new LinkedList());
        }

        for(flagDominate = 0; flagDominate < population.size(); ++flagDominate) {
            iDominate.add(new LinkedList());
            dominateMe[flagDominate] = 0;
        }

        int i;
        int var10002;
        for(i = 0; i < population.size() - 1; ++i) {
            for(int q = i + 1; q < population.size(); ++q) {
                flagDominate = this.dominanceComparator.compare((S)solutionSet.get(i), (S)solutionSet.get(q));
                if (flagDominate == -1) {
                    ((List)iDominate.get(i)).add(q); /*i支配q*/
                    var10002 = dominateMe[q]++;
                } else if (flagDominate == 1) {
                    ((List)iDominate.get(q)).add(i); /*q支配i*/
                    var10002 = dominateMe[i]++;
                }
            }
        }
        for(i = 0; i < population.size(); ++i) {
            if (dominateMe[i] == 0) {
                ((List)front.get(0)).add(i);
                ((Chrome)solutionSet.get(i)).setRank(0);
            }
        }
        i = 0;

        int index;
        Iterator it1;
        while(((List)front.get(i)).size() != 0) {
            ++i;
            it1 = ((List)front.get(i - 1)).iterator();

            while(it1.hasNext()) {
                Iterator it2 = ((List)iDominate.get((Integer)it1.next())).iterator();

                while(it2.hasNext()) {
                    index = (Integer)it2.next();
                    var10002 = dominateMe[index]--;
                    if (dominateMe[index] == 0) {
                        ((List)front.get(i)).add(index);
                        ((Chrome)solutionSet.get(index)).setRank(i);
                    }
                }
            }
        }

        this.rankedSubPopulations = new ArrayList();

        for(index = 0; index < i; ++index) {
            this.rankedSubPopulations.add(index, new ArrayList(((List)front.get(index)).size()));
            it1 = ((List)front.get(index)).iterator();

            while(it1.hasNext()) {
                ((ArrayList)this.rankedSubPopulations.get(index)).add(solutionSet.get((Integer)it1.next()));
            }
        }
        return this.rankedSubPopulations;

    }

    /**
     * 二进制交叉
     * @return
     * @throws CloneNotSupportedException
     */
    public  List<S> selection(Chrome[][][] populations,int axis,int yaxis,int haxis)  {
        currentIndividual = populations[axis][yaxis][haxis];
        currentNeighbors = this.neighborhood.getNeighbors(populations,axis,yaxis,haxis);
        currentNeighbors.add(currentIndividual);
        List<Chrome> parents = new ArrayList(2);
        parents.add(this.selectionOperator.execute(currentNeighbors));
        if (this.archive.size() > 0) {
            parents.add(this.selectionOperator.execute(this.archive));
        } else {
            parents.add(this.selectionOperator.execute(currentNeighbors));
        }
        return (List<S>)parents;
    }
    /**
     * 二进制交叉
     * @return
     * @throws CloneNotSupportedException
     */
    public  List<S> selection(Chrome[][][] populations,int axis,int yaxis,int haxis,double p0){
        currentIndividual = populations[axis][yaxis][haxis];
        currentNeighbors = this.neighborhood.getNeighbors(populations,axis,yaxis,haxis);
        currentNeighbors.add(currentIndividual);
        List<Chrome> parents = new ArrayList(2);

        parents.add(this.selectionOperator.execute(currentNeighbors));
      /*  if(this.randomGenerator.getRandomValue()<1-p0){
            parents.add(this.selectionOperator.execute(this.archive));
        }else{
            parents.add(this.selectionOperator.execute(currentNeighbors));
        }*/

        List<Chrome> dsd = new ArrayList<>(2);
        int start = 0;
        while (start<2){
           dsd.add(this.selectionOperator.execute(currentNeighbors));
           ++start;
        }
        Ranking<S> rank = new DominanceRanking();
        rank.computeRanking((List<S>)currentNeighbors);
        CrowdingDistance<S> crowdingDistance = new CrowdingDistance();

        for(int j = 0; j < rank.getNumberOfSubfronts(); ++j) {
            crowdingDistance.computeDensityEstimator(rank.getSubfront(j));
        }
        Collections.sort(currentNeighbors, new RankingAndCrowdingDistanceComparator());
        Chrome bestSolution = currentNeighbors.get(0);
        parents.add(bestSolution);
      /*  if(this.randomGenerator.getRandomValue()<p0){
            Chrome bestSolution = currentNeighbors.get(0);
            parents.add(bestSolution);
         }else{
            Chrome bestSolution = currentNeighbors.get(currentNeighbors.size()-1);
            parents.add(bestSolution);
        }*/

        /*if(this.randomGenerator.getRandomValue()<p0){
            int start = 0;
            while (start<2){
                parents.add(this.selectionOperator.execute(currentNeighbors));
                ++start;
            }
            return (List<S>)parents;
        }else{
            parents.add(this.selectionOperator.execute(currentNeighbors));
            Ranking<S> rank = new DominanceRanking();
            rank.computeRanking((List<S>)this.currentNeighbors);
            CrowdingDistance<S> crowdingDistance = new CrowdingDistance();

            for(int j = 0; j < rank.getNumberOfSubfronts(); ++j) {
                crowdingDistance.computeDensityEstimator(rank.getSubfront(j));
            }
            Collections.sort(currentNeighbors, new RankingAndCrowdingDistanceComparator());
            Chrome bestSolution = currentNeighbors.get(0);
            parents.add(bestSolution);



        }*/
        return (List<S>)parents;
    }
    @Override
    protected List<S> reproduction(List<S> doubleSolutions) {
        List<Chrome> result = new ArrayList<>();
        /*二进制交叉*/
        List<Chrome> offspring = this.crossoverOperator.execute((List<Chrome>) doubleSolutions);
        //this.differentialEvolutionOperator.setCurrentSolution(currentIndividual);
        //doubleSolutions.add((S)currentIndividual);
        //List<Chrome> offspring = this.differentialEvolutionOperator.execute((List<Chrome>) doubleSolutions);
        /*变异*/
        this.mutationOperator.execute(offspring.get(0));
        this.mutationOperator.execute(offspring.get(1));

        this.evaluatePopulation((List<S>)offspring);
        Ranking<S> rank = new DominanceRanking();
        rank.computeRanking((List<S>)offspring);

        Chrome result1 = this.selectionOperator.getRandomIndeividual(offspring);
        this.mutationOperator.execute(result1);
        result.add(result1);
        return (List<S>)result;
    }

    @Override
    protected Chrome[][][] replacement(Chrome[][][] population, List<S> list1,int axis,int yaxis,int haxis) {
        Chrome newIndivdual = list1.get(0);

        newIndivdual.setAxis(-1);
        newIndivdual.setYaxis(-1);
        newIndivdual.setHaxis(-1);
        int result = this.dominanceComparator.compare((S)currentIndividual,(S)newIndivdual);
        if(result==1){
            /*新产生的个体支配当前个体*/
            population = insertNewIndividualWhenDominates(population,newIndivdual,axis,yaxis,haxis);
        }else if(result==0){
            population = insertNewIndividualWhenNonDominates(population,newIndivdual,axis,yaxis,haxis);
        }
        return  population;
    }




    public String getName() {
        return "MOCell";
    }

    public String getDescription() {
        return "Multi-Objective Cellular evolutionry algorithm";
    }

    @Override
    public List<S> getResult() {
        return (List<S>) this.archive;
    }
}
