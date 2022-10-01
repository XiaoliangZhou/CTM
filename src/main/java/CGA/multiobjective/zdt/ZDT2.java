package CGA.multiobjective.zdt;

public class ZDT2 extends ZDT1 {
    public ZDT2() {
        this(30);
    }

    public ZDT2(Integer numberOfVariables) {
        super(numberOfVariables);
        this.setName("ZDT2");
    }

    public double evalH(double f, double g) {
        return 1.0D - Math.pow(f / g, 2.0D);
    }
}
