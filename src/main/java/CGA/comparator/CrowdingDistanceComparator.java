package CGA.comparator;

import CGA.Model.Chrome;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;

import java.io.Serializable;
import java.util.Comparator;

public class CrowdingDistanceComparator<S extends Chrome> implements Comparator<S>, Serializable {

    public CrowdingDistanceComparator() {
    }

    public int compare(Chrome solution1, Chrome solution2) {
        byte result;
        if (solution1 == null) {
            if (solution2 == null) {
                result = 0;
            } else {
                result = 1;
            }
        } else if (solution2 == null) {
            result = -1;
        } else {
            double distance1 = 4.9E-324D;
            double distance2 = 4.9E-324D;
            if (solution1 != null) {
                distance1 = solution1.getNd();
            }

            if (solution2 != null) {
                distance2 = solution2.getNd();
            }

            if (distance1 > distance2) {
                result = -1;
            } else if (distance1 < distance2) {
                result = 1;
            } else {
                result = 0;
            }
        }

        return result;
    }
}
