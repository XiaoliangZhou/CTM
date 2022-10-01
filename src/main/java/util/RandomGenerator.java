package util;

import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;


@FunctionalInterface
public interface RandomGenerator<Value> {
    Value getRandomValue();

    static <T> RandomGenerator<T> forCollection(BoundedRandomGenerator<Integer> indexSelector, Collection<T> values) {
        ArrayList<T> list = new ArrayList(values);
        return () -> {
            return list.get((Integer)indexSelector.getRandomValue(0, values.size() - 1));
        };
    }

    @SafeVarargs
    static <T> RandomGenerator<T> forArray(BoundedRandomGenerator<Integer> indexSelector, T... values) {
        return forCollection(indexSelector, Arrays.asList(values));
    }

    static <T extends Enum<T>> RandomGenerator<T> forEnum(BoundedRandomGenerator<Integer> indexSelector, Class<T> enumClass) {
        return forArray(indexSelector, enumClass.getEnumConstants());
    }

    static <T> RandomGenerator<T> filter(final RandomGenerator<T> generator, final Predicate<T> filter) {
        return new RandomGenerator<T>() {
            public T getRandomValue() {
                T value;
                do {
                    value = generator.getRandomValue();
                } while(!filter.test(value));

                return value;
            }
        };
    }
}
