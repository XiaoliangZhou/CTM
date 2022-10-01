package CGA.comparator.impl;


import CGA.Model.Chrome;
import CGA.comparator.ConstraintViolationComparator;
import util.OverallConstraintViolation;


public class OverallConstraintViolationComparator<S extends Chrome> implements ConstraintViolationComparator<S> {
    private OverallConstraintViolation<S> overallConstraintViolation = new OverallConstraintViolation();

    public OverallConstraintViolationComparator() {
    }

    public int compare(S solution1, S solution2) {
        if (this.overallConstraintViolation.getAttribute(solution1) == null) {
            return 0;
        } else {
            double violationDegreeSolution1 = (Double)this.overallConstraintViolation.getAttribute(solution1);
            double violationDegreeSolution2 = (Double)this.overallConstraintViolation.getAttribute(solution2);
            if (violationDegreeSolution1 < 0.0D && violationDegreeSolution2 < 0.0D) {
                if (violationDegreeSolution1 > violationDegreeSolution2) {
                    return -1;
                } else {
                    return violationDegreeSolution2 > violationDegreeSolution1 ? 1 : 0;
                }
            } else if (violationDegreeSolution1 == 0.0D && violationDegreeSolution2 < 0.0D) {
                return -1;
            } else {
                return violationDegreeSolution1 < 0.0D && violationDegreeSolution2 == 0.0D ? 1 : 0;
            }
        }
    }
}

