package subset.trackables;

import subset.Measurement;
import subset.Trackable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class Mean<T extends Number> implements Trackable<BigDecimal, T> {

    private static final String MEAN = "mean";
    private BigDecimal runningValue;

    public Mean() {
        this(BigDecimal.ZERO);
    }

    private Mean(BigDecimal runningValue) {
        this.runningValue = runningValue;
    }

    @Override
    public String getName() {
        return MEAN;
    }

    @Override
    public BigDecimal getRunningValue() {
        return runningValue;
    }

    @Override
    public void add(T newValue) {
        runningValue = getRunningValue().add(new BigDecimal(newValue.doubleValue()));
    }

    @Override
    public Measurement evaluate(List<T> dataset) {
        BigDecimal mean = getRunningValue().divide(new BigDecimal(dataset.size()), 3, RoundingMode.HALF_EVEN);
        return new Measurement(getName(), mean);
    }

    @Override
    public Mean<T> deepCopy() {
        return new Mean<>(getRunningValue());
    }

    @Override
    public String toString() {
        return "Mean{" +
                "runningValue=" + runningValue +
                '}';
    }
}
