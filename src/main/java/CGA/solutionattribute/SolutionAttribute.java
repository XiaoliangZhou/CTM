package CGA.solutionattribute;

import java.io.Serializable;

public interface SolutionAttribute<S, V> extends Serializable {
    //void setAttribute(S var1, V var2);

    //V getAttribute(S var1);

    Object getAttributeIdentifier();
}
