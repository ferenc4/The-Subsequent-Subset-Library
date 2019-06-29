package subset;

import java.util.Objects;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class Tuple<A, B> {
    private final A a;
    private final B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return getA().equals(tuple.getA()) &&
                getB().equals(tuple.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB());
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
