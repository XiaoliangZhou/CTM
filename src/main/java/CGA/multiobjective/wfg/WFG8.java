package CGA.multiobjective.wfg;


import CGA.Model.Chrome;

public class WFG8 extends WFG {
    public WFG8() {
        this(2, 4, 2);
    }

    public WFG8(Integer k, Integer l, Integer m) {
        super(k, l, m);
        this.setName("WFG8");
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

        System.arraycopy(z, 0, result, 0, k);

        for(i = k; i < z.length; ++i) {
            int head = 0;
            int tail = i - 1;
            float[] subZ = this.subVector(z, head, tail);
            float[] subW = this.subVector(w, head, tail);
            float aux = (new Transformations()).rSum(subZ, subW);
            result[i] = (new Transformations()).bParam(z[i], aux, 0.019607844F, 0.02F, 50.0F);
        }

        return result;
    }

    public float[] t2(float[] z, int k) {
        float[] result = new float[z.length];
        System.arraycopy(z, 0, result, 0, k);

        for(int i = k; i < z.length; ++i) {
            result[i] = (new Transformations()).sLinear(z[i], 0.35F);
        }

        return result;
    }

    public float[] t3(float[] z, int k, int M) {
        float[] result = new float[M];
        float[] w = new float[z.length];

        int head;
        for(head = 0; head < z.length; ++head) {
            w[head] = 1.0F;
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

        head = k + 1;
        tail = z.length;
        float[] subZ = this.subVector(z, head - 1, tail - 1);
        subW = this.subVector(w, head - 1, tail - 1);
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

        float[] sol2 = this.evaluate(variables);

        for(i = 0; i < sol2.length; ++i) {
            solution.setObjective(i, (double)sol2[i]);
        }

    }
}
