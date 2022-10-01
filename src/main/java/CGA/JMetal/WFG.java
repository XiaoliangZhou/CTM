package CGA.JMetal;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WFG extends AbstractDoubleProblem{
    private final float epsilon = 1.0E-7F;
    protected int k;
    protected int m;
    protected int l;
    protected int[] a;
    protected int[] s;
    protected int d = 1;
    protected Random random = new Random();

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

}
