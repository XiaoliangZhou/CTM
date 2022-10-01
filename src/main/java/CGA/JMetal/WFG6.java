package CGA.JMetal;

import CGA.Model.Chrome;
import org.uma.jmetal.problem.multiobjective.wfg.Shapes;
import org.uma.jmetal.problem.multiobjective.wfg.Transformations;
import org.uma.jmetal.solution.DoubleSolution;

public class WFG6 extends WFG {

    public WFG6() {
        this(2, 4, 2);
    }

    public WFG6(Integer k, Integer l, Integer m) {

        this.s = new int[m];
        int i;
        for(i = 0; i < m; ++i) {
            this.s[i] = 2 * (i + 1);
        }

        this.a = new int[m - 1];

        for(i = 0; i < m - 1; ++i) {
            this.a[i] = 1;
        }

    }

    public float[] evaluate(float[] z) {
        float[] y = this.normalise(z);
        y = this.t1(y, this.k);
        y = this.t2(y, this.k, this.m);
        float[] result = new float[this.m];
        float[] x = this.calculateX(y);

        for(int m = 1; m <= this.m; ++m) {
            result[m - 1] = (float)this.d * x[this.m - 1] + (float)this.s[m - 1] * (new Shapes()).concave(x, m);
        }

        return result;
    }

    public float[] t1(float[] z, int k) {
        float[] result = new float[z.length];
        System.arraycopy(z, 0, result, 0, k);

        for(int i = k; i < z.length; ++i) {
            result[i] = (new Transformations()).sLinear(z[i], 0.35F);
        }

        return result;
    }

    public float[] t2(float[] z, int k, int M) {
        float[] result = new float[M];

        int head;
        int tail;
        int l;
        float[] subZ;
        for(head = 1; head <= M - 1; ++head) {
            tail = (head - 1) * k / (M - 1) + 1;
            l = head * k / (M - 1);
            subZ = this.subVector(z, tail - 1, l - 1);
            result[head - 1] = (new Transformations()).rNonsep(subZ, k / (M - 1));
        }

        head = k + 1;
        tail = z.length;
        l = z.length - k;
        subZ = this.subVector(z, head - 1, tail - 1);
        result[M - 1] = (new Transformations()).rNonsep(subZ, l);
        return result;
    }

    public void evaluate(Chrome solution) {
        float[] variables = new float[solution.getNumberOfVariables()];
        double[] x = new double[solution.getNumberOfVariables()];

        int i;
        for(i = 0; i < solution.getNumberOfVariables(); ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        for(i = 0; i < solution.getNumberOfVariables(); ++i) {
            variables[i] = (float)x[i];
        }

        float[] sol2 = this.evaluate(variables);

        for(int j = 0; j < sol2.length; ++j) {
            solution.setObjective(j, (double)sol2[j]);
        }
    }
}
