package CGA.JMetal;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

public class ZDT3 extends AbstractDoubleProblem {

    /** Constructor. Creates default instance of problem ZDT1 (30 decision variables) */
    public ZDT3() {
        this(3);
    }

    /**
     * Creates a new instance of problem ZDT1.
     *
     * @param numberOfVariables Number of variables.
     */
    public ZDT3(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);//设定决策变量个数，30
        setNumberOfObjectives(2);//设定优化目标函数个数,2
        setName("ZDT3");//给这个问题起名,ZDT1.

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        //设置定义域
        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-5.0);
            upperLimit.add(5.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    //这里就是优化目标函数的实现过程，Algorithm.evlataionPopulation()会调用这个方法
    /** Evaluate() method */
    public void evaluate(DoubleSolution solution) {
        double[] f = new double[getNumberOfObjectives()];

      /*  f[0] = solution.getVariableValue(0);
        double g = this.evalG(solution);
        double h = this.evalH(f[0], g);
        f[1] = h;*/

        f[0] = this.evalG(solution);
        double h = this.evalH(solution);
        f[1] = h;

        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    }

    /**
     * Returns the value of the ZDT1 function G.
     *
     * @param solution Solution
     */
    private double evalG(DoubleSolution solution) {
        double g = 0.0;
        for (int i = 0; i < solution.getNumberOfVariables()-1; i++) {
            double p = solution.getVariableValue(i);
            double q = solution.getVariableValue(i+1);
            double s = Math.pow(p, 2) + Math.pow(q, 2);
            g += Math.sqrt(s);
        }
        double f1 = -10 * Math.pow(Math.E, -g / 5);
        return f1;
    }

   /* private double evalG(DoubleSolution solution) {
        double g = 0.0;
        for (int i = 1; i < solution.getNumberOfVariables(); i++) {
            g += solution.getVariableValue(i);
        }
        double constant = 9.0 / (solution.getNumberOfVariables() - 1);
        g = constant * g;
        g = g + 1.0;
        return g;
    }*/

  /*  public double evalH(double f, double g) {
        double h ;
        h = 1.0 - Math.sqrt(f / g);
        //h = g * (1.0 - Math.sqrt(f / g) - (f / g) * Math.sin(10 * Math.PI * f));
        return h;
    }*/
    public double evalH(DoubleSolution solution) {
        double h =0;
        double afpha = 0.8, belta = 3;
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            double p = solution.getVariableValue(i);
            double q = Math.pow(Math.abs(p), afpha) + 5 * Math.sin(Math.pow(p, belta));
            h += q;
        }
        return h;
    }
}
