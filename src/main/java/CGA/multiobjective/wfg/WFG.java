package CGA.multiobjective.wfg;

import CGA.Model.Chrome;
import CGA.problem.impl.AbstractDoubleProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class WFG extends AbstractDoubleProblem {
    private final float epsilon = 1.0E-7F;
    protected int k;
    protected int m;
    protected int l;
    protected int[] a;
    protected int[] s;
    protected int d = 1;
    protected Random random = new Random();

    public WFG(Integer k, Integer l, Integer M) {
        this.k = k;
        this.l = l;
        this.m = M;
        this.setNumberOfVariables(this.k + this.l);
        this.setNumberOfObjectives(this.m);
        this.setNumberOfConstraints(0);
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(0.0D);
            upperLimit.add(2.0D * (double)(i + 1));
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

   /* public Chrome createSolution() {
        return new Chrome(this);
    }*/

    public float[] calculateX(float[] t) {
        float[] x = new float[this.m];

        for(int i = 0; i < this.m - 1; ++i) {
            x[i] = Math.max(t[this.m - 1], (float)this.a[i]) * (t[i] - 0.5F) + 0.5F;
        }

        x[this.m - 1] = t[this.m - 1];
        return x;
    }

    public float[] normalise(float[] z) {
        float[] result = new float[z.length];

        for(int i = 0; i < z.length; ++i) {
            float bound = 2.0F * (float)(i + 1);
            result[i] = z[i] / bound;
            result[i] = this.correctTo01(result[i]);
        }

        return result;
    }

    public float correctTo01(float a) {
        float min = 0.0F;
        float max = 1.0F;
        float minEpsilon = min - 1.0E-7F;
        float maxEpsilon = max + 1.0E-7F;
        if ((a > min || a < minEpsilon) && (a < min || a > minEpsilon)) {
            return (a < max || a > maxEpsilon) && (a > max || a < maxEpsilon) ? a : max;
        } else {
            return min;
        }
    }

    public float[] subVector(float[] z, int head, int tail) {
        int size = tail - head + 1;
        float[] result = new float[size];
        System.arraycopy(z, head, result, head - head, tail + 1 - head);
        return result;
    }

    public abstract float[] evaluate(float[] var1);
}
