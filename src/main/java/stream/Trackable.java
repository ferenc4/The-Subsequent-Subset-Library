package stream;

import java.util.List;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public interface Trackable<V, N extends Number> {
    String getName();

    V getRunningValue();

    void add(N newValue);

    Measurement evaluate(List<N> dataset);

    Trackable<V, N> deepCopy();
}
