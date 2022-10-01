package CGA.comparator;

import CGA.Model.Chrome;
import CGA.comparator.impl.OverallConstraintViolationComparator;
import org.uma.jmetal.util.JMetalException;

import java.io.Serializable;
import java.util.Comparator;

public class DominanceComparator<S extends Chrome> implements Comparator<S>, Serializable {
    private ConstraintViolationComparator<S> constraintViolationComparator;

    public DominanceComparator() {
        this(new OverallConstraintViolationComparator(), 0.0D);
    }

    public DominanceComparator(double epsilon) {
        this(new OverallConstraintViolationComparator(), epsilon);
    }

    public DominanceComparator(ConstraintViolationComparator<S> constraintComparator) {
        this(constraintComparator, 0.0D);
    }

    public DominanceComparator(ConstraintViolationComparator<S> constraintComparator, double epsilon) {
        this.constraintViolationComparator = constraintComparator;
    }

    public int compare(S solution1, S solution2) {
        if (solution1 == null) {
            throw new JMetalException("Solution1 is null");
        } else if (solution2 == null) {
            throw new JMetalException("Solution2 is null");
        } else if (solution1.getNumberOfObjectives() != solution2.getNumberOfObjectives()) {
            throw new JMetalException("Cannot compare because solution1 has " + solution1.getNumberOfObjectives() + " objectives and solution2 has " + solution2.getNumberOfObjectives());
        } else {
            int result = this.constraintViolationComparator.compare(solution1, solution2);
            if (result == 0) {
                result = this.dominanceTest(solution1, solution2);
            }

            return result;
        }
    }

    private int dominanceTest(S solution1, S solution2) {
        int bestIsOne = 0;
        int bestIsTwo = 0;

        for(int i = 0; i < solution1.getNumberOfObjectives(); ++i) {
            double value1 = solution1.getObjective(i);
            double value2 = solution2.getObjective(i);
            if (value1 != value2) {
                if (value1 < value2) {
                    bestIsOne = 1;
                }

                if (value2 < value1) {
                    bestIsTwo = 1;
                }
            }
        }

        byte result;
        if (bestIsOne > bestIsTwo) {
            result = -1;
        } else if (bestIsTwo > bestIsOne) {
            result = 1;
        } else {
            result = 0;
        }

        return result;
    }
}
