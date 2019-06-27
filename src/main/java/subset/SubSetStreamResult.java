package subset;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class SubSetStreamResult<T> {

    private final ArrayList<ArrayList<T>> list;
    private final List<TrackedVariable> tracked;

    private SubSetStreamResult(ArrayList<ArrayList<T>> list, List<TrackedVariable> tracked) {
        this.list = list;
        this.tracked = tracked;
    }

    @Override
    public String toString() {
        return "SubSetStreamResult{" +
                "list=" + list +
                ", tracked=" + tracked +
                '}';
    }

    static class Builder<T> {
        private final int subsetSize;
        private final ArrayList<ArrayList<T>> resultList;
        private final Set<VariableTracker> tracked;
        private int activeArrayIndexFloor;
        private int activeArrayIndexCeiling;

        public Builder(int subsetSize, Set<VariableTracker> tracked) {
            this.subsetSize = subsetSize;
            this.activeArrayIndexFloor = 0;
            this.activeArrayIndexCeiling = activeArrayIndexFloor + subsetSize + 1;
            this.tracked = tracked;
            this.resultList = new ArrayList<>();
        }

        public void add(T newValue) {
            for (int i = activeArrayIndexFloor; i < activeArrayIndexCeiling; i++) {
                if (resultList.size() > i) {
                    ArrayList<T> activeArray = resultList.get(i);
                    activeArray.add(newValue);
                    resultList.set(i, activeArray);
                } else {
                    ArrayList<T> activeArray = new ArrayList<>();
                    resultList.add(activeArray);
                }
            }
            activeArrayIndexFloor++;
            activeArrayIndexCeiling++;
        }

        public SubSetStreamResult<T> build() {
            return new SubSetStreamResult<>(
                    new ArrayList<>(resultList.stream()
                            .filter(it -> it.size() == subsetSize)
                            .collect(Collectors.toList())),
                    tracked.stream()
                            .map(VariableTracker::finalise)
                            .collect(Collectors.toList())
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Builder<?> builder = (Builder<?>) o;
            return subsetSize == builder.subsetSize &&
                    activeArrayIndexFloor == builder.activeArrayIndexFloor &&
                    activeArrayIndexCeiling == builder.activeArrayIndexCeiling &&
                    Objects.equals(resultList, builder.resultList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subsetSize, resultList, activeArrayIndexFloor, activeArrayIndexCeiling);
        }
    }
}
