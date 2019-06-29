package subset;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        List<SubSetStreamResult<String>> actual = new SubsetStream<>(source.stream(), 3)
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
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(integerStream, 500).collect();
        System.out.println("actual = " + actual);
    }

    @Test
    public void testTrackedVariableMean() {
        List<Integer> source = Arrays.asList(5, 3, 7);
        int subsetSize = 3;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(source.stream(), subsetSize)
                .track("mean", 0,
                        (Integer runningSum, Integer newValue) -> {
                            runningSum += newValue;
                            return runningSum;
                        },
                        (Integer runningSum) -> new TrackedVariable("mean", BigDecimal.valueOf(runningSum)
                                .divide(BigDecimal.valueOf(subsetSize)))
                )
                .collect();
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(5, 3, 7), actual.get(0).getList());
        assertEquals(singletonList(new TrackedVariable("mean", BigDecimal.valueOf(5))),
                actual.get(0).getTracked());
    }

    @Test
    public void testTrackedVariableMedian() {
        List<Integer> source = Arrays.asList(5, 3, 7);
        int subsetSize = 3;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(source.stream(), subsetSize)
                .track("median", new ArrayList<>(),
                        (List<Integer> accu, Integer newValue) -> {
                            accu.add(newValue);
                            return accu;
                        },
                        (List<Integer> values) -> {
                            Collections.sort(values);
                            BigDecimal result;
                            if (values.size() % 2 == 0) {
                                result = BigDecimal.valueOf(values.get((values.size() - 1) / 2))
                                        .add(BigDecimal.valueOf(values.get((values.size() - 1) / 2)))
                                        .divide(BigDecimal.valueOf(2));
                            } else {
                                result = BigDecimal.valueOf(values.get((values.size() - 1) / 2));
                            }
                            return new TrackedVariable("median", result);
                        }
                )
                .collect();
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(5, 3, 7), actual.get(0).getList());
        assertEquals(singletonList(new TrackedVariable("median", BigDecimal.valueOf(5))),
                actual.get(0).getTracked());
    }

    @Test
    public void test2TrackedVariablesWithFilter() {
        List<Integer> source = Arrays.asList(1, 3, 94, 83, 7, 97, 83, 12, 57);
        int subsetSize = 3;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(source.stream(), subsetSize)
                .filter(it -> it < 10)
                .track("mean", 0,
                        (Integer runningSum, Integer newValue) -> {
                            runningSum += newValue;
                            return runningSum;
                        },
                        (Integer runningSum) -> new TrackedVariable("mean", BigDecimal.valueOf(runningSum)
                                .divide(BigDecimal.valueOf(subsetSize), 3, RoundingMode.HALF_EVEN))
                )
                .track("median", new ArrayList<>(),
                        (List<Integer> accu, Integer newValue) -> {
                            accu.add(newValue);
                            return accu;
                        },
                        (List<Integer> values) -> {
                            Collections.sort(values);
                            BigDecimal result;
                            if (values.size() % 2 == 0) {
                                result = BigDecimal.valueOf(values.get((values.size() - 1) / 2))
                                        .add(BigDecimal.valueOf(values.get((values.size() - 1) / 2)))
                                        .divide(BigDecimal.valueOf(2));
                            } else {
                                result = BigDecimal.valueOf(values.get((values.size() - 1) / 2));
                            }
                            return new TrackedVariable("median", result);
                        }
                )
                .collect();
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(1, 3, 7), actual.get(0).getList());
        assertEquals(Arrays.asList(
                new TrackedVariable("mean", BigDecimal.valueOf(3.667)),
                new TrackedVariable("median", BigDecimal.valueOf(3))
                ),
                actual.get(0).getTracked());
    }
}