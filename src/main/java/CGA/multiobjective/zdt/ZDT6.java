package CGA.multiobjective.zdt;

import CGA.Model.Chrome;

public class ZDT6 extends ZDT1 {
    public ZDT6() {
        this(10);
    }

    public ZDT6(Integer numberOfVariables) {
        super(numberOfVariables);
        this.setName("ZDT6");
    }

    protected double evalG(Chrome solution) {
        double g = 0.0D;

        for(int var = 1; var < solution.getNumberOfVariables(); ++var) {
            g += (Double)solution.getVariableValue(var);
        }

        g /= (double)(solution.getNumberOfVariables() - 1);
        g = Math.pow(g, 0.25D);
        g = 9.0D * g;
        ++g;
        return g;
    }

    protected double evalH(double f, double g) {
        return 1.0D - Math.pow(f / g, 2.0D);
    }
}
