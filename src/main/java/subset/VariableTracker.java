package subset;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class VariableTracker<V, N> {
    private final String name;
    private V runningValue;
    private final BiFunction<V, N, V> accumulator;
    private final Function<V, TrackedVariable> combiner;

    VariableTracker(String name, V initValue, BiFunction<V, N, V> accumulator, Function<V, TrackedVariable> combiner) {
        this.runningValue = initValue;
        this.accumulator = accumulator;
        this.combiner = combiner;
        this.name = name;
    }

    public void add(N newValue) {
        this.runningValue = accumulator.apply(runningValue, newValue);
    }

    public TrackedVariable finalise() {
        return combiner.apply(runningValue);
    }
}
