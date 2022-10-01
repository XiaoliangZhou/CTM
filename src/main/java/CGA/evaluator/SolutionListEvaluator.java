package CGA.evaluator;

import CGA.Model.Chrome;
import CGA.problem.Problem;

import java.io.Serializable;
import java.util.List;

public interface SolutionListEvaluator<S> extends Serializable {

    List<S> evaluate(List<S> var1, Problem<S> var2);

    Chrome[][][] evaluate(Chrome[][][] populations,int matrix, Problem<S> var2);

    void shutdown();
}
