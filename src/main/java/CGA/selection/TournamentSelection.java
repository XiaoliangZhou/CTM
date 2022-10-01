package CGA.selection;


import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;
import CGA.comparator.RankingAndCrowdingDistanceComparator;

import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class TournamentSelection<S extends Chrome>  {
    private Comparator<Chrome> comparator;
    private final int n_arity;

    public TournamentSelection(int n_arity) {
        this(new RankingAndCrowdingDistanceComparator(), n_arity);
    }

    public TournamentSelection(Comparator<Chrome> comparator, int n_arity) {
        this.n_arity = n_arity;
        this.comparator = comparator;
    }

    public Chrome execute(List<Chrome> solutionList) {
        if (null == solutionList) {
            throw new JMetalException("The solution list is null");
        } else if (solutionList.isEmpty()) {
            throw new JMetalException("The solution list is empty");
        } else {
            Chrome result;
            if (solutionList.size() == 1) {
                result = (Chrome)solutionList.get(0);
            } else {
                result = (Chrome) SolutionListUtils.selectNRandomDifferentSolutions(1, solutionList).get(0);
                int count = 1;

                do {
                    Chrome candidate = (Chrome)SolutionListUtils.selectNRandomDifferentSolutions(1, solutionList).get(0);
                    result = this.getBestSolution(result, candidate, this.comparator);
                    ++count;
                } while(count < this.n_arity);
            }

            return result;
        }
    }
    public Chrome getRandomIndeividual(List<Chrome> solutionList) {
        Chrome result = getBestSolution(solutionList.get(0),solutionList.get(1),this.comparator);
        return result;
    }

    public static <S extends Chrome> Chrome getRandomIndeividual(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator) {
        return getRandomIndeividual(solution1, solution2, comparator, () -> {
            return JMetalRandom.getInstance().nextDouble();
        });
    }
    public static <S extends Chrome> Chrome getBestSolution(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator) {
        return getBestSolution(solution1, solution2, comparator, () -> {
            return JMetalRandom.getInstance().nextDouble();
        });
    }
    public static <S extends Chrome> Chrome getRandomIndeividual(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator, RandomGenerator<Double> randomGenerator) {
        return getRandomIndeividual(solution1, solution2, comparator, (a, b) -> {
            return (Double)randomGenerator.getRandomValue() < 0.5D ? a : b;
        });
    }

    public static <S extends Chrome> Chrome getBestSolution(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator, RandomGenerator<Double> randomGenerator) {
        return getBestSolution(solution1, solution2, comparator, (a, b) -> {
            return (Double)randomGenerator.getRandomValue() < 0.5D ? a : b;
        });
    }
    public static <S extends Chrome> Chrome getBestSolution(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator, BinaryOperator<Chrome> equalityPolicy) {
        int flag = comparator.compare(solution1, solution2);
        Chrome result;
        if (flag == -1) {
            result = solution1;
        } else if (flag == 1) {
            result = solution2;
        } else {
            result = (Chrome)equalityPolicy.apply(solution1, solution2);
        }

        return result;
    }
    public static <S extends Chrome> Chrome getRandomIndeividual(Chrome solution1, Chrome solution2, Comparator<Chrome> comparator, BinaryOperator<Chrome> equalityPolicy) {
        Chrome result;
        result = (Chrome)equalityPolicy.apply(solution1, solution2);
        return result;
    }
}

