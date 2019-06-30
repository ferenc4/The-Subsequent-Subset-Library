package stream.trackables;

import stream.Measurement;
import stream.Trackable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class Median<T extends Number> implements Trackable<List<BigDecimal>, T> {

    public static final String MEDIAN = "median";

    public Median() {
    }

    @Override
    public String getName() {
        return MEDIAN;
    }

    @Override
    public List<BigDecimal> getRunningValue() {
        return null;
    }

    @Override
    public void add(T newValue) {
        // do nothing
    }

    @Override
    public Measurement evaluate(List<T> dataset) {
        List<BigDecimal> runningValue = dataset.stream()
                .map(it -> new BigDecimal(it.doubleValue())).sorted()
                .collect(Collectors.toList());
        BigDecimal result;

        if (runningValue.size() % 2 == 0) {
            result = runningValue.get((runningValue.size()) / 2)
                    .add(runningValue.get((runningValue.size() - 2) / 2))
                    .divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_EVEN);
        } else {
            result = runningValue.get((runningValue.size() - 1) / 2);
        }
        return new Measurement(getName(), result);
    }

    @Override
    public Median<T> deepCopy() {
        return new Median<>();
    }

    @Override
    public String toString() {
        return "Median{}";
    }
}
