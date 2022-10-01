package CGA.point.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

import java.util.Iterator;
import java.util.List;

public class NadirPoint extends ArrayPoint {
    public NadirPoint(int dimension) {
        super(dimension);

        for(int i = 0; i < dimension; ++i) {
            this.point[i] = -1.0D / 0.0;
        }

    }

    public void update(double[] point) {
        if (point.length != this.point.length) {
            throw new JMetalException("The point to be update have a dimension of " + point.length + " while the parameter point has a dimension of " + point.length);
        } else {
            for(int i = 0; i < point.length; ++i) {
                if (this.point[i] < point[i]) {
                    this.point[i] = point[i];
                }
            }

        }
    }

    public void update(List<? extends Solution<?>> solutionList) {
        Iterator var2 = solutionList.iterator();

        while(var2.hasNext()) {
            Solution<?> solution = (Solution)var2.next();
            this.update(solution.getObjectives());
        }

    }
}
