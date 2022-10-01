package algorithm;

import org.uma.jmetal.util.naming.DescribedEntity;

import java.io.Serializable;

public interface Algorithm<Result> extends Runnable, Serializable, DescribedEntity {
    void run();

    Result getResult();
}
