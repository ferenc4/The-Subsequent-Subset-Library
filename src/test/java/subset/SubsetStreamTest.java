package subset;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    public void testData1() {
        InputStream resourceAsStream = SubsetStreamTest.class.getClassLoader().getResourceAsStream("data1.txt");
        Stream<Integer> integerStream = new BufferedReader(new InputStreamReader(resourceAsStream)).lines()
                .map(Integer::valueOf);
        SubSetStreamResult<Integer> actual = new SubsetStream<>(integerStream, 500).collect();
        System.out.println("actual = " + actual);
    }
}