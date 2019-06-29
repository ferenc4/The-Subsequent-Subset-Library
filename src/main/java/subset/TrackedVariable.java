package subset;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class TrackedVariable {

    private final String name;
    private final BigDecimal value;

    public TrackedVariable(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TrackedVariable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedVariable that = (TrackedVariable) o;
        return getName().equals(that.getName()) &&
                getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }
}
