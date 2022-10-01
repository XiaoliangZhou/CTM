package CGA.multiobjective.wfg;

public class Shapes {
    public Shapes() {
    }

    public float linear(float[] x, int m) {
        float result = 1.0F;
        int M = x.length;

        for(int i = 1; i <= M - m; ++i) {
            result *= x[i - 1];
        }

        if (m != 1) {
            result *= 1.0F - x[M - m];
        }

        return result;
    }

    public float convex(float[] x, int m) {
        float result = 1.0F;
        int M = x.length;

        for(int i = 1; i <= M - m; ++i) {
            result = (float)((double)result * (1.0D - Math.cos((double)x[i - 1] * 3.141592653589793D * 0.5D)));
        }

        if (m != 1) {
            result = (float)((double)result * (1.0D - Math.sin((double)x[M - m] * 3.141592653589793D * 0.5D)));
        }

        return result;
    }

    public float concave(float[] x, int m) {
        float result = 1.0F;
        int M = x.length;

        for(int i = 1; i <= M - m; ++i) {
            result = (float)((double)result * Math.sin((double)x[i - 1] * 3.141592653589793D * 0.5D));
        }

        if (m != 1) {
            result = (float)((double)result * Math.cos((double)x[M - m] * 3.141592653589793D * 0.5D));
        }

        return result;
    }

    public float mixed(float[] x, int A, float alpha) {
        float tmp = (float)Math.cos((double)(2.0F * (float)A * 3.1415927F * x[0] + 1.5707964F));
        tmp = (float)((double)tmp / (2.0D * (double)((float)A) * 3.141592653589793D));
        return (float)Math.pow((double)(1.0F - x[0] - tmp), (double)alpha);
    }

    public float disc(float[] x, int A, float alpha, float beta) {
        float tmp = (float)Math.cos((double)((float)A) * Math.pow((double)x[0], (double)beta) * 3.141592653589793D);
        return 1.0F - (float)Math.pow((double)x[0], (double)alpha) * (float)Math.pow((double)tmp, 2.0D);
    }
}
