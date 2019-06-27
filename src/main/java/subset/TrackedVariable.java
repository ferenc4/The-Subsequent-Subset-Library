package subset;

import java.math.BigDecimal;

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
}
