package CGA.archive;

import java.io.Serializable;
import java.util.List;

public interface Archive<S> extends Serializable {
    boolean add(S var1);

    S get(int var1);

    List<S> getSolutionList();

    int size();
}
