package CGA.evaluator.impl;

import CGA.Model.Chrome;
import CGA.evaluator.SolutionListEvaluator;
import CGA.problem.Problem;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

public class SequentialSolutionListEvaluator<S> implements SolutionListEvaluator<S> {
    public SequentialSolutionListEvaluator() {
    }

    public List<S> evaluate(List<S> solutionList, Problem<S> problem) throws JMetalException {
        solutionList.stream().forEach((s) -> {
            problem.evaluate((Chrome) s);
        });
        return solutionList;
    }

    @Override
    public Chrome[][][] evaluate(Chrome[][][] populations,int matrixSize, Problem<S> problem) {
        for(int i = 0; i < matrixSize; ++i) {
            for(int j = 0; j < matrixSize; ++j) {
                for(int k = 0; k < matrixSize-1; ++k) {
                    problem.evaluate(populations[i][j][k]);
                }
            }
        }
        return populations;
    }
    public void shutdown() {
    }
}
