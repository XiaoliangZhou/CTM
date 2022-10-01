package CGA.qualityIndicator;

import org.uma.jmetal.util.naming.DescribedEntity;

import java.io.Serializable;

public interface QualityIndicator<Evaluate, Result> extends DescribedEntity, Serializable {
    Result evaluate(Evaluate var1);
}
