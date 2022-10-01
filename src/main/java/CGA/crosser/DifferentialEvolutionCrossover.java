package CGA.crosser;

import java.util.ArrayList;
import java.util.List;

import CGA.Model.Chrome;
import org.uma.jmetal.operator.CrossoverOperator;
import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

public class DifferentialEvolutionCrossover  {
    private static final double DEFAULT_CR = 0.5D;
    private static final double DEFAULT_F = 0.5D;
    private static final double DEFAULT_K = 0.5D;
    private static final String DEFAULT_DE_VARIANT = "rand/1/bin";
    private double cr;
    private double f;
    private double k;
    private String variant;
    private Chrome currentSolution;
    private BoundedRandomGenerator<Integer> jRandomGenerator;
    private BoundedRandomGenerator<Double> crRandomGenerator;

    public DifferentialEvolutionCrossover() {
        this(0.5D, 0.5D, 0.5D, "rand/1/bin");
    }

    public DifferentialEvolutionCrossover(double cr, double f, String variant) {
        this(cr, f, variant, (a, b) -> {
            return JMetalRandom.getInstance().nextInt(a, b);
        }, (a, b) -> {
            return JMetalRandom.getInstance().nextDouble(a, b);
        });
    }

    public DifferentialEvolutionCrossover(double cr, double f, String variant, RandomGenerator<Double> randomGenerator) {
        this(cr, f, variant, BoundedRandomGenerator.fromDoubleToInteger(randomGenerator), BoundedRandomGenerator.bound(randomGenerator));
    }

    public DifferentialEvolutionCrossover(double cr, double f, String variant, BoundedRandomGenerator<Integer> jRandomGenerator, BoundedRandomGenerator<Double> crRandomGenerator) {
        this.cr = cr;
        this.f = f;
        this.k = 0.5D;
        this.variant = variant;
        this.jRandomGenerator = jRandomGenerator;
        this.crRandomGenerator = crRandomGenerator;
    }

    public DifferentialEvolutionCrossover(double cr, double f, double k, String variant) {
        this(cr, f, variant);
        this.k = k;
    }

    public double getCr() {
        return this.cr;
    }

    public double getF() {
        return this.f;
    }

    public double getK() {
        return this.k;
    }

    public String getVariant() {
        return this.variant;
    }

    public void setCurrentSolution(Chrome current) {
        this.currentSolution = current;
    }

    public void setCr(double cr) {
        this.cr = cr;
    }

    public void setF(double f) {
        this.f = f;
    }

    public void setK(double k) {
        this.k = k;
    }

    public List<Chrome> execute(List<Chrome> parentSolutions) {
        Chrome child = (Chrome) this.currentSolution.copy();
        int numberOfVariables = ((Chrome)parentSolutions.get(0)).getNumberOfVariables();
        int jrand = (Integer)this.jRandomGenerator.getRandomValue(0, numberOfVariables - 1);
        int j;
        double value;
        if (!"rand/1/bin".equals(this.variant) && !"best/1/bin".equals(this.variant)) {
            if ("rand/1/exp".equals(this.variant) || "best/1/exp".equals(this.variant)) {
                for(j = 0; j < numberOfVariables; ++j) {
                    if ((Double)this.crRandomGenerator.getRandomValue(0.0D, 1.0D) >= this.cr && j != jrand) {
                        this.cr = 0.0D;
                        value = (Double)this.currentSolution.getVariableValue(j);
                        child.setVariableValue(j, value);
                    } else {
                        value = (Double)((Chrome)parentSolutions.get(2)).getVariableValue(j) + this.f * ((Double)((Chrome)parentSolutions.get(0)).getVariableValue(j) - (Double)((Chrome)parentSolutions.get(1)).getVariableValue(j));
                        if (value < child.getLowerBound(j)) {
                            value = child.getLowerBound(j);
                        }

                        if (value > child.getUpperBound(j)) {
                            value = child.getUpperBound(j);
                        }

                        child.setVariableValue(j, value);
                    }
                }
            } else if ("current-to-rand/1".equals(this.variant) || "current-to-best/1".equals(this.variant)) {
                for(j = 0; j < numberOfVariables; ++j) {
                    value = (Double)this.currentSolution.getVariableValue(j) + this.k * ((Double)((Chrome)parentSolutions.get(2)).getVariableValue(j) - (Double)this.currentSolution.getVariableValue(j)) + this.f * ((Double)((Chrome)parentSolutions.get(0)).getVariableValue(j) - (Double)((Chrome)parentSolutions.get(1)).getVariableValue(j));
                    if (value < child.getLowerBound(j)) {
                        value = child.getLowerBound(j);
                    }

                    if (value > child.getUpperBound(j)) {
                        value = child.getUpperBound(j);
                    }

                    child.setVariableValue(j, value);
                }
            } else if ("current-to-rand/1/bin".equals(this.variant) || "current-to-best/1/bin".equals(this.variant)) {
                for(j = 0; j < numberOfVariables; ++j) {
                    if ((Double)this.crRandomGenerator.getRandomValue(0.0D, 1.0D) >= this.cr && j != jrand) {
                        value = (Double)this.currentSolution.getVariableValue(j);
                        child.setVariableValue(j, value);
                    } else {
                        value = (Double)this.currentSolution.getVariableValue(j) + this.k * ((Double)((Chrome)parentSolutions.get(2)).getVariableValue(j) - (Double)this.currentSolution.getVariableValue(j)) + this.f * ((Double)((Chrome)parentSolutions.get(0)).getVariableValue(j) - (Double)((Chrome)parentSolutions.get(1)).getVariableValue(j));
                        if (value < child.getLowerBound(j)) {
                            value = child.getLowerBound(j);
                        }

                        if (value > child.getUpperBound(j)) {
                            value = child.getUpperBound(j);
                        }

                        child.setVariableValue(j, value);
                    }
                }
            } else {
                if (!"current-to-rand/1/exp".equals(this.variant) && !"current-to-best/1/exp".equals(this.variant)) {
                    JMetalLogger.logger.severe("DifferentialEvolutionCrossover.execute:  unknown DE variant (" + this.variant + ")");
                    Class<String> cls = String.class;
                    String name = cls.getName();
                    throw new JMetalException("Exception in " + name + ".execute()");
                }

                for(j = 0; j < numberOfVariables; ++j) {
                    if ((Double)this.crRandomGenerator.getRandomValue(0.0D, 1.0D) >= this.cr && j != jrand) {
                        this.cr = 0.0D;
                        value = (Double)this.currentSolution.getVariableValue(j);
                        child.setVariableValue(j, value);
                    } else {
                        value = (Double)this.currentSolution.getVariableValue(j) + this.k * ((Double)((Chrome)parentSolutions.get(2)).getVariableValue(j) - (Double)this.currentSolution.getVariableValue(j)) + this.f * ((Double)((Chrome)parentSolutions.get(0)).getVariableValue(j) - (Double)((Chrome)parentSolutions.get(1)).getVariableValue(j));
                        if (value < child.getLowerBound(j)) {
                            value = child.getLowerBound(j);
                        }

                        if (value > child.getUpperBound(j)) {
                            value = child.getUpperBound(j);
                        }

                        child.setVariableValue(j, value);
                    }
                }
            }
        } else {
            for(j = 0; j < numberOfVariables; ++j) {
                if ((Double)this.crRandomGenerator.getRandomValue(0.0D, 1.0D) >= this.cr && j != jrand) {
                    value = (Double)this.currentSolution.getVariableValue(j);
                    child.setVariableValue(j, value);
                } else {
                    value = (Double)((Chrome)parentSolutions.get(2)).getVariableValue(j) + this.f * ((Double)((Chrome)parentSolutions.get(0)).getVariableValue(j) - (Double)((Chrome )parentSolutions.get(1)).getVariableValue(j));
                    if (value < child.getLowerBound(j)) {
                        value = child.getLowerBound(j);
                    }

                    if (value > child.getUpperBound(j)) {
                        value = child.getUpperBound(j);
                    }

                    child.setVariableValue(j, value);
                }
            }
        }

        List<Chrome> result = new ArrayList(1);
        result.add(child);
        return result;
    }

    public int getNumberOfRequiredParents() {
        return 3;
    }

    public int getNumberOfGeneratedChildren() {
        return 1;
    }
}
