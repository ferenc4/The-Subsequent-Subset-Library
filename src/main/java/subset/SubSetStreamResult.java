package subset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/ferenc4">Ferenc Fazekas</a>
 */
public class SubSetStreamResult<T> {

    private final List<T> list;
    private final List<TrackedVariable> tracked;

    private SubSetStreamResult(List<T> list, List<TrackedVariable> tracked) {
        this.list = list;
        this.tracked = tracked;
    }

    public List<T> getList() {
        return list;
    }

    public List<TrackedVariable> getTracked() {
        return tracked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSetStreamResult<?> that = (SubSetStreamResult<?>) o;
        return getList().equals(that.getList()) &&
                getTracked().equals(that.getTracked());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getList(), getTracked());
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
        private final List<Tuple<List<T>, List<VariableTracker>>> resultList;
        private final List<VariableTracker> toTrack;
        private int activeArrayIndexFloor;
        private int activeArrayIndexCeiling;

        public Builder(int subsetSize, List<VariableTracker> toTrack) {
            this.subsetSize = subsetSize;
            this.activeArrayIndexFloor = 0;
            this.activeArrayIndexCeiling = activeArrayIndexFloor + subsetSize;
            this.toTrack = toTrack;
            this.resultList = new ArrayList<>();
        }

        public void add(T newValue) {
            for (int i = activeArrayIndexFloor; i < activeArrayIndexCeiling; i++) {
                if (i < resultList.size()) {
                    Tuple<List<T>, List<VariableTracker>> tuple = resultList.get(i);
                    List<T> activeArray = tuple.getA();
                    activeArray.add(newValue);
                    resultList.set(i, new Tuple<>(activeArray, tuple.getB().stream()
                            .map(variableTracker -> {
                                VariableTracker newInstance = variableTracker.newInstance();
                                newInstance.add(newValue);
                                return newInstance;
                            })
                            .collect(Collectors.toList())));
                } else {
                    List<T> activeArray = new ArrayList<>();
                    activeArray.add(newValue);
                    List<VariableTracker> variableTrackers = toTrack.stream()
                            .map(variableTracker -> {
                                VariableTracker newInstance = variableTracker.newInstance();
                                newInstance.add(newValue);
                                return newInstance;
                            })
                            .collect(Collectors.toList());
                    resultList.add(new Tuple<>(activeArray, variableTrackers));
                }
            }
            activeArrayIndexFloor++;
            activeArrayIndexCeiling++;
        }

        public List<SubSetStreamResult<T>> build() {
            return resultList.stream()
                    .filter(it -> it.getA().size() == subsetSize)
                    .map(it -> new SubSetStreamResult<>(it.getA(), it.getB().stream()
                            .map(VariableTracker::finalise)
                            .collect(Collectors.toList())))
                    .collect(Collectors.toList());
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
