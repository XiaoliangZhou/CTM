package CGA.JMetal;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Booth 优化问题
 * @author shup
 *
 */
public class BoothProblem extends AbstractDoubleProblem{

    public BoothProblem() {
        // 变量个数
        setNumberOfVariables(2);
        // 目标函数个数
        setNumberOfObjectives(1);
        setName("BoothProblem");

        // 定义域范围
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());
        for (int i=0; i<getNumberOfVariables(); i++){
            lowerLimit.add(-10.0);
            upperLimit.add(10.0);
        }
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        // 变量值
        Double[] X = solution.getVariables().toArray(new Double[getNumberOfVariables()]);

        //
        double f = Math.pow(X[0]+2*X[1]-7, 2)+Math.pow(2*X[0]+X[1]-5, 2);

        solution.setObjective(0, f);
    }

}