package subset;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void testTrackedVariable() {
        List<Integer> source = Arrays.asList(5, 3, 94, 83, 7, 97, 83, 12, 57);
        int subsetSize = 3;
        SubSetStreamResult<Integer> actual = new SubsetStream<>(source.stream(), subsetSize)
                .filter(it -> it < 10)
                .track("mean", 0,
                        (Integer runningSum, Integer newValue) -> {
                            runningSum += newValue;
                            return runningSum;
                        },
                        (Integer runningSum) -> new TrackedVariable("mean", BigDecimal.valueOf(runningSum)
                                .divide(BigDecimal.valueOf(subsetSize)))
                )
                .collect();
        assertEquals(actual.getList(), singletonList(Arrays.asList(5, 3, 7)));
        assertEquals(actual.getTracked(), singletonList(new TrackedVariable("mean", BigDecimal.valueOf(5))));
    }
}