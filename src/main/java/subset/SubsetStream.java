package subset;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class SubsetStream<N extends Number> {
    private final Stream<N> stream;
    private final int subsetSize;
    private final List<Trackable<?, N>> tracked;

    public SubsetStream(Stream<N> stream, int subsetSize) {
        this.stream = stream;
        this.subsetSize = subsetSize;
        this.tracked = new ArrayList<>();
    }

    public SubsetStream<N> filter(Predicate<? super N> predicate) {
        return new SubsetStream<>(stream.filter(predicate), subsetSize);
    }

    public SubsetStream<N> track(Trackable<?, N> trackable) {
        tracked.add(trackable);
        return this;
    }

    public <R extends Number> SubsetStream<R> map(Function<? super N, ? extends R> mapper) {
        return new SubsetStream<>(stream.map(mapper), subsetSize);
    }

    public List<SubSetStreamResult<N>> collect() {
        SubSetStreamResult.Builder<N> builder = new SubSetStreamResult.Builder<>(subsetSize, tracked);
        stream.forEach(builder::add);
        return builder.build();
    }

    @Override
    public String toString() {
        return "SubsetStream{" +
                "stream=" + stream +
                ", subsetSize=" + subsetSize +
                ", tracked=" + tracked +
                '}';
    }
}
