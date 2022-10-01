package algorithm;

import CGA.crosser.DifferentialEvolutionCrossover;
import CGA.crosser.SBXCrossover;
import CGA.mutation.PolynomialMutation;
import CGA.problem.Problem;
import CGA.selection.TournamentSelection;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGeneticAlgorithm<S, Result> extends AbstractEvolutionaryAlgorithm<S, Result> {
    protected int matrixSize;
    protected int maxPopulationSize;
    protected TournamentSelection selectionOperator;
    protected SBXCrossover crossoverOperator;
    protected DifferentialEvolutionCrossover differentialEvolutionOperator;
    protected PolynomialMutation mutationOperator;

    public void setMaxPopulationSize(int maxPopulationSize) {
        this.maxPopulationSize = maxPopulationSize;
    }

    public int getMaxPopulationSize() {
        return this.maxPopulationSize;
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public void setMatrixSize(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    public TournamentSelection getSelectionOperator() {
        return this.selectionOperator;
    }

    public SBXCrossover getCrossoverOperator() {
        return this.crossoverOperator;
    }

    public DifferentialEvolutionCrossover getDEOperator() { return differentialEvolutionOperator; }

    public void setDifferentialEvolutionOperator(DifferentialEvolutionCrossover differentialEvolutionOperator) {
        this.differentialEvolutionOperator = differentialEvolutionOperator;
    }

    public void setSelectionOperator(TournamentSelection selectionOperator) { this.selectionOperator = selectionOperator; }

    public PolynomialMutation getMutationOperator() {
        return this.mutationOperator;
    }

    public AbstractGeneticAlgorithm(Problem<S> problem) {
        this.setProblem(problem);
    }

}
