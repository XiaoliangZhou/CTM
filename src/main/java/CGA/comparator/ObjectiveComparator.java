package CGA.comparator;

import CGA.MocGAs.ChromeBase;
import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;

import java.io.Serializable;
import java.util.Comparator;

public class ObjectiveComparator<C extends Chrome> implements Comparator<C>, Serializable {
    private int objectiveId;
    private ObjectiveComparator.Ordering order;

    public ObjectiveComparator(int objectiveId) {
        this.objectiveId = objectiveId;
        this.order = ObjectiveComparator.Ordering.ASCENDING;
    }

    public ObjectiveComparator(int objectiveId, ObjectiveComparator.Ordering order) {
        this.objectiveId = objectiveId;
        this.order = order;
    }

    public int compare(Chrome solution1, Chrome solution2) {
        int result;
        if (solution1 == null) {
            if (solution2 == null) {
                result = 0;
            } else {
                result = 1;
            }
        } else if (solution2 == null) {
            result = -1;
        } else {
            if (solution1.getNumberOfObjectives() <= this.objectiveId) {
                throw new JMetalException("The solution1 has " + solution1.getNumberOfObjectives() + " objectives and the objective to sort is " + this.objectiveId);
            }

            if (solution2.getNumberOfObjectives() <= this.objectiveId) {
                throw new JMetalException("The solution2 has " + solution2.getNumberOfObjectives() + " objectives and the objective to sort is " + this.objectiveId);
            }

            Double objective1 = solution1.getObjective(this.objectiveId);
            Double objective2 = solution2.getObjective(this.objectiveId);
            if (this.order == ObjectiveComparator.Ordering.ASCENDING) {
                result = Double.compare(objective1, objective2);
            } else {
                result = Double.compare(objective2, objective1);
            }
        }

        return result;
    }

    public static enum Ordering {
        ASCENDING,
        DESCENDING;

        private Ordering() {
        }
    }
}