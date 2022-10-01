package CGA.problem;


import CGA.Model.Chrome;

import java.io.Serializable;

public interface Problem<S> extends Serializable {
    int getNumberOfVariables();

    int getNumberOfObjectives();

    int getNumberOfConstraints();

    String getName();

    void evaluate(Chrome var1);

    Chrome createSolution(int axis,int yasix,int haxis);
}
