package stream;

import org.junit.Test;
import stream.trackables.Mean;
import stream.trackables.Median;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class SubsetStreamTest {
    @Test
    public void testSmallSubset() {
        List<Integer> source = Arrays.asList(1, 2, 3);
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(source.stream(), 2)
                .collect();
        List<SubSetStreamResult<Integer>> expected = Arrays.asList(
                new SubSetStreamResult<>(Arrays.asList(1, 2), Collections.emptyList()),
                new SubSetStreamResult<>(Arrays.asList(2, 3), Collections.emptyList())
        );
        assertEquals(expected, actual);
    }

    @Test
    public void testTooLargeSubset() {
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(Stream.of(1, 2, 3), 5).collect();
        assertEquals(Collections.EMPTY_LIST, actual);
    }

    @Test
    public void testTrackedVariableMean() {
        int subsetSize = 3;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(Stream.of(5, 3, 7), subsetSize)
                .track(new Mean<>())
                .collect();
        List<Measurement> expected = singletonList(new Measurement("mean", BigDecimal.valueOf(5)
                .setScale(3, HALF_EVEN)));
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(5, 3, 7), actual.get(0).getList());
        assertEquals(expected, actual.get(0).getTracked());
    }

    @Test
    public void testTrackedVariableMedian() {
        int subsetSize = 3;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(Stream.of(5, 3, 7), subsetSize)
                .track(new Median<>())
                .collect();
        List<Measurement> expected = singletonList(new Measurement("median", BigDecimal.valueOf(5)));
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(5, 3, 7), actual.get(0).getList());
        assertEquals(expected, actual.get(0).getTracked());
    }

    @Test
    public void testTrackedVariableMedianForEvenCount() {
        int subsetSize = 4;
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(Stream.of(5, 3, 7, 1), subsetSize)
                .track(new Median<>())
                .collect();
        List<Measurement> expected = singletonList(new Measurement("median", BigDecimal.valueOf(4)
                .setScale(3, HALF_EVEN)));
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(5, 3, 7, 1), actual.get(0).getList());
        assertEquals(expected, actual.get(0).getTracked());
    }

    @Test
    public void test2TrackedVariablesWithFilter() {
        int subsetSize = 3;
        Stream<Integer> source = Stream.of(1, 3, 94, 83, 7, 97, 83, 12, 57);
        List<SubSetStreamResult<Integer>> actual = new SubsetStream<>(source, subsetSize)
                .filter(it -> it < 10)
                .track(new Mean<>())
                .track(new Median<>())
                .collect();
        assertEquals(1, actual.size());
        assertEquals(Arrays.asList(1, 3, 7), actual.get(0).getList());
        assertEquals(Arrays.asList(
                new Measurement("mean", BigDecimal.valueOf(3.667)),
                new Measurement("median", BigDecimal.valueOf(3))
                ),
                actual.get(0).getTracked());
    }
}