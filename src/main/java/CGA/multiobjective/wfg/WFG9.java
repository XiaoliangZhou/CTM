package CGA.multiobjective.wfg;

import CGA.Model.Chrome;
public class WFG9 extends WFG {
    public WFG9() {
        this(2, 4, 2);
    }

    public WFG9(Integer k, Integer l, Integer m) {
        super(k, l, m);
        this.setName("WFG9");
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
        y = this.t3(y, this.k, this.m);
        float[] result = new float[this.m];
        float[] x = this.calculateX(y);

        for(int m = 1; m <= this.m; ++m) {
            result[m - 1] = (float)this.d * x[this.m - 1] + (float)this.s[m - 1] * (new Shapes()).concave(x, m);
        }

        return result;
    }

    public float[] t1(float[] z, int k) {
        float[] result = new float[z.length];
        float[] w = new float[z.length];

        int i;
        for(i = 0; i < w.length; ++i) {
            w[i] = 1.0F;
        }

        for(i = 0; i < z.length - 1; ++i) {
            int head = i + 1;
            int tail = z.length - 1;
            float[] subZ = this.subVector(z, head, tail);
            float[] subW = this.subVector(w, head, tail);
            float aux = (new Transformations()).rSum(subZ, subW);
            result[i] = (new Transformations()).bParam(z[i], aux, 0.019607844F, 0.02F, 50.0F);
        }

        result[z.length - 1] = z[z.length - 1];
        return result;
    }

    public float[] t2(float[] z, int k) {
        float[] result = new float[z.length];

        int i;
        for(i = 0; i < k; ++i) {
            result[i] = (new Transformations()).sDecept(z[i], 0.35F, 0.001F, 0.05F);
        }

        for(i = k; i < z.length; ++i) {
            result[i] = (new Transformations()).sMulti(z[i], 30, 95, 0.35F);
        }

        return result;
    }

    public float[] t3(float[] z, int k, int M) {
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
        float[] variables = new float[this.getNumberOfVariables()];
        double[] x = new double[this.getNumberOfVariables()];

        int i;
        for(i = 0; i < this.getNumberOfVariables(); ++i) {
            x[i] = (Double)solution.getVariableValue(i);
        }

        for(i = 0; i < this.getNumberOfVariables(); ++i) {
            variables[i] = (float)x[i];
        }

        float[] sol2 = this.evaluate(variables);

        for(i = 0; i < sol2.length; ++i) {
            solution.setObjective(i, (double)sol2[i]);
        }

    }
}
