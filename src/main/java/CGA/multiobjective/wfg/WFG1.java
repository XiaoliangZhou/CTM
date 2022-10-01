package CGA.multiobjective.wfg;

import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

import java.util.logging.Level;

public class WFG1 extends WFG {
    public WFG1() {
        this(2, 4, 2);
    }

    public WFG1(Integer k, Integer l, Integer m) {
        super(k, l, m);
        this.setName("WFG1");
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
        y = this.t2(y, this.k);

        try {
            y = this.t3(y);
        } catch (JMetalException var6) {
            JMetalLogger.logger.log(Level.SEVERE, "Error", var6);
        }

        y = this.t4(y, this.k, this.m);
        float[] result = new float[this.m];
        float[] x = this.calculateX(y);

        for(int m = 1; m <= this.m - 1; ++m) {
            result[m - 1] = (float)this.d * x[this.m - 1] + (float)this.s[m - 1] * (new Shapes()).convex(x, m);
        }

        result[this.m - 1] = (float)this.d * x[this.m - 1] + (float)this.s[this.m - 1] * (new Shapes()).mixed(x, 5, 1.0F);
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

    public float[] t2(float[] z, int k) {
        float[] result = new float[z.length];
        System.arraycopy(z, 0, result, 0, k);

        for(int i = k; i < z.length; ++i) {
            result[i] = (new Transformations()).bFlat(z[i], 0.8F, 0.75F, 0.85F);
        }

        return result;
    }

    public float[] t3(float[] z) throws JMetalException {
        float[] result = new float[z.length];

        for(int i = 0; i < z.length; ++i) {
            result[i] = (new Transformations()).bPoly(z[i], 0.02F);
        }

        return result;
    }


  /*  public float[] t4(float[] z, int k, int M) {
        float[] result = new float[M];
        float[] w = new float[z.length];

        int head;
        for(head = 0; head < z.length; ++head) {
            w[head] = 2.0F * (float)(head + 1);
        }

        int tail;
        float[] subW;
        for(head = 1; head <= M - 1; ++head) {
            tail = (head - 1) * k / (M - 1) + 1;
            int tail = head * k / (M - 1);
            subW = this.subVector(z, tail - 1, tail - 1);
            float[] subW = this.subVector(w, tail - 1, tail - 1);
            result[head - 1] = (new Transformations()).rSum(subW, subW);
        }

        head = k + 1 - 1;
        tail = z.length - 1;
        float[] subZ = this.subVector(z, head, tail);
        subW = this.subVector(w, head, tail);
        result[M - 1] = (new Transformations()).rSum(subZ, subW);
        return result;
    }
*/

    public float[] t4(float[] z, int k, int M) {
        float[] result = new float[M];
        float[] w = new float[z.length];

        int head;
        for(head = 0; head < z.length; ++head) {
            w[head] = 2.0F * (float)(head + 1);
        }

        int tail;
        float[] subW;
        for(head = 1; head <= M - 1; ++head) {
            tail = (head - 1) * k / (M - 1) + 1;
            int tail1 = head * k / (M - 1);
            subW = this.subVector(z, tail - 1, tail1 - 1);
            float[] subW1 = this.subVector(w, tail - 1, tail1 - 1);
            result[head - 1] = (new Transformations()).rSum(subW, subW1);
        }

        head = k + 1 - 1;
        tail = z.length - 1;
        float[] subZ = this.subVector(z, head, tail);
        subW = this.subVector(w, head, tail);
        result[M - 1] = (new Transformations()).rSum(subZ, subW);
        return result;
    }


    public void evaluate(Chrome solution) {
        float[] variables = new float[this.getNumberOfVariables()];
        double[] x = new double[this.getNumberOfVariables()];

        int i;
        for(i = 0; i < this.getNumberOfVariables(); ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        for(i = 0; i < this.getNumberOfVariables(); ++i) {
            variables[i] = (float)x[i];
        }

        float[] f = this.evaluate(variables);

        for(int i1 = 0; i1 < f.length; ++i1) {
            solution.setObjective(i1, (double)f[i1]);
        }

    }
}
