package CGA.multiobjective.zdt;


import CGA.Model.Chrome;

import java.util.ArrayList;
import java.util.List;

public class ZDT4 extends ZDT1 {
    public ZDT4() {
        this(10);
    }

    public ZDT4(Integer numberOfVariables) {
        super(numberOfVariables);
        this.setName("ZDT4");
        List<Double> lowerLimit = new ArrayList(this.getNumberOfVariables());
        List<Double> upperLimit = new ArrayList(this.getNumberOfVariables());
        lowerLimit.add(0.0D);
        upperLimit.add(1.0D);

        for(int i = 0; i < this.getNumberOfVariables(); ++i) {
            lowerLimit.add(-5.0D);
            upperLimit.add(5.0D);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    public double evalG(Chrome solution) {
        double g = 0.0D;

        for(int var = 1; var < solution.getNumberOfVariables(); ++var) {
            g += Math.pow((Double)solution.getVariableValue(var), 2.0D) + -10.0D * Math.cos(12.566370614359172D * (Double)solution.getVariableValue(var));
        }

        double constant = 1.0D + 10.0D * (double)(solution.getNumberOfVariables() - 1);
        return g + constant;
    }

    public double evalH(double f, double g) {
        return 1.0D - Math.sqrt(f / g);
    }
}
