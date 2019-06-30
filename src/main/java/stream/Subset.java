package stream;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class Subset<T extends Number> {

    private final List<T> set;
    private final List<Trackable<?, T>> tracked;

    public Subset(List<T> set, List<Trackable<?, T>> tracked) {
        this.set = set;
        this.tracked = tracked;
    }

    public List<T> getSet() {
        return set;
    }

    public List<Trackable<?, T>> getTracked() {
        return tracked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subset<?> subset = (Subset<?>) o;
        return getSet().equals(subset.getSet()) &&
                getTracked().equals(subset.getTracked());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSet(), getTracked());
    }

    @Override
    public String toString() {
        return "Subset{" +
                "set=" + set +
                ", tracked=" + tracked +
                '}';
    }
}
