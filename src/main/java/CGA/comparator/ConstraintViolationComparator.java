package CGA.comparator;

import java.io.Serializable;
import java.util.Comparator;

public interface ConstraintViolationComparator<S> extends Comparator<S>, Serializable {
    int compare(S var1, S var2);
}
