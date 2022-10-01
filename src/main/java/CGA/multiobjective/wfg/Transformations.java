package CGA.multiobjective.wfg;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

public class Transformations {
    private static final float EPSILON = 1.0E-10F;

    public Transformations() {
    }

    public float bPoly(float y, float alpha) throws JMetalException {
        if (alpha <= 0.0F) {
            JMetalLogger.logger.severe("wfg.Transformations.bPoly: Param alpha must be > 0");
            Class<String> cls = String.class;
            String name = cls.getName();
            throw new JMetalException("Exception in " + name + ".bPoly()");
        } else {
            return this.correctTo01((float)StrictMath.pow((double)y, (double)alpha));
        }
    }

    public float bFlat(float y, float A, float B, float C) {
        float tmp1 = Math.min(0.0F, (float)Math.floor((double)(y - B))) * A * (B - y) / B;
        float tmp2 = Math.min(0.0F, (float)Math.floor((double)(C - y))) * (1.0F - A) * (y - C) / (1.0F - C);
        return this.correctTo01(A + tmp1 - tmp2);
    }

    public float sLinear(float y, float A) {
        return this.correctTo01(Math.abs(y - A) / (float)Math.abs(Math.floor((double)(A - y)) + (double)A));
    }

    public float sDecept(float y, float A, float B, float C) {
        float tmp1 = (float)Math.floor((double)(y - A + B)) * (1.0F - C + (A - B) / B) / (A - B);
        float tmp2 = (float)Math.floor((double)(A + B - y)) * (1.0F - C + (1.0F - A - B) / B) / (1.0F - A - B);
        float tmp = Math.abs(y - A) - B;
        return this.correctTo01(1.0F + tmp * (tmp1 + tmp2 + 1.0F / B));
    }

    public float sMulti(float y, int A, int B, float C) {
        float tmp1 = (4.0F * (float)A + 2.0F) * 3.1415927F * (0.5F - Math.abs(y - C) / (2.0F * ((float)Math.floor((double)(C - y)) + C)));
        float tmp2 = 4.0F * (float)B * (float)StrictMath.pow((double)(Math.abs(y - C) / (2.0F * ((float)Math.floor((double)(C - y)) + C))), 2.0D);
        return this.correctTo01((1.0F + (float)Math.cos((double)tmp1) + tmp2) / ((float)B + 2.0F));
    }

    public float rSum(float[] y, float[] w) {
        float tmp1 = 0.0F;
        float tmp2 = 0.0F;

        for(int i = 0; i < y.length; ++i) {
            tmp1 += y[i] * w[i];
            tmp2 += w[i];
        }

        return this.correctTo01(tmp1 / tmp2);
    }

    public float rNonsep(float[] y, int A) {
        float tmp = (float)Math.ceil((double)((float)A / 2.0F));
        float denominator = (float)y.length * tmp * (1.0F + 2.0F * (float)A - 2.0F * tmp) / (float)A;
        float numerator = 0.0F;

        for(int j = 0; j < y.length; ++j) {
            numerator += y[j];

            for(int k = 0; k <= A - 2; ++k) {
                numerator += Math.abs(y[j] - y[(j + k + 1) % y.length]);
            }
        }

        return this.correctTo01(numerator / denominator);
    }

    public float bParam(float y, float u, float A, float B, float C) {
        float v = A - (1.0F - 2.0F * u) * Math.abs((float)Math.floor((double)(0.5F - u)) + A);
        float exp = B + (C - B) * v;
        float result = (float)StrictMath.pow((double)y, (double)exp);
        return this.correctTo01(result);
    }

    float correctTo01(float a) {
        float min = 0.0F;
        float max = 1.0F;
        float min_epsilon = min - 1.0E-10F;
        float max_epsilon = max + 1.0E-10F;
        if ((a > min || a < min_epsilon) && (a < min || a > min_epsilon)) {
            return (a < max || a > max_epsilon) && (a > max || a < max_epsilon) ? a : max;
        } else {
            return min;
        }
    }
}
