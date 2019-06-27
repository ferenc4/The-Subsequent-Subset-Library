package subset;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class SubsetStreamTest {
    @Test
    public void testGreenPath() {
        List<String> source = Arrays.asList("apple", "grapes", "milk", "vinegar", "pepper", "salt", "paprika");
        SubSetStreamResult<String> actual = new SubsetStream<>(source.stream(), 3)
                .filter(it -> !it.startsWith("p"))
                .map(String::toUpperCase)
                .collect();
        System.out.println("actual = " + actual);
    }
}