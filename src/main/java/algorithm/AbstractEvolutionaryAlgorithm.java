package algorithm;

import CGA.Model.Chrome;
import CGA.problem.Problem;
import util.JMetalRandom;

import java.util.List;
import java.util.Random;

public abstract class AbstractEvolutionaryAlgorithm<S,R> implements Algorithm<R>   {
    protected Chrome[][][] population;
    protected Problem<S> problem;
    protected int  martxSize;


    public void setProblem(Problem<S> problem) {
        this.problem = problem;
    }

    public Problem<S> getProblem() {
        return this.problem;
    }

    public int getMartxSize() {
        return this.population.length;
    }

    protected abstract void initProgress();

    protected abstract void updateProgress();

    protected abstract boolean isStoppingConditionReached();

    protected abstract Chrome[][][] createInitialPopulation();

    protected abstract Chrome[][][] inintEvaluatePopulation(Chrome[][][] population);

    protected abstract List<S> getArchives();

    protected abstract List<S> evaluatePopulation(List<S> population);

    protected abstract List<S> selection(Chrome[][][] var1,int axis,int yaxis,int haxis);

    protected abstract List<S> selection(Chrome[][][] populations,int axis,int yaxis,int haxis,double p0);

    protected abstract List<S> reproduction(List<S> var1);

    protected abstract Chrome[][][] replacement(Chrome[][][] var1, List<S> var2,int axis,int yaxis,int haxis);

    public void run() {
        this.population = this.createInitialPopulation();
        this.population = this.inintEvaluatePopulation(this.population);
        this.initProgress();
        while(!this.isStoppingConditionReached()) {
            martxSize = population.length;
            for(int i = 0; i < martxSize; ++i) {
                for(int j = 0; j < martxSize; ++j) {
                    for(int k = 0; k < martxSize-1; ++k) {
                        List<S> matingPopulation =  this.selection(this.population,i,j,k);
                        List<S> offspringPopulation = this.reproduction(matingPopulation);
                        offspringPopulation = this.evaluatePopulation(offspringPopulation);
                        this.population = this.replacement(this.population, offspringPopulation,i,j,k);
                        this.updateProgress();
                    }
                }

            }
            /*for(int i = 0; (double)i < 100; ++i) {
                if (this.getArchives().size() > i) {
                    int random = JMetalRandom.getInstance().nextInt(0, this.getArchives().size() - 1);
                    if (random < 100) {
                        Chrome solution = (Chrome)this.getArchives().get(i);
                        int axis = new Random().nextInt(martxSize) ;
                        int yxis = new Random().nextInt(martxSize) ;
                        int hxis = new Random().nextInt(martxSize) ;
                        population[axis][yxis][hxis] = (solution.copy());
                    }
                }
            }*/
        }
    }

}
