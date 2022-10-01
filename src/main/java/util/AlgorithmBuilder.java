package util;

import algorithm.Algorithm;

public interface AlgorithmBuilder<A extends Algorithm<?>> {
    A build();
}
