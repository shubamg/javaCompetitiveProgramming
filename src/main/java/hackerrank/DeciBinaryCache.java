package hackerrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DeciBinaryCache {

    private final int deciBinariesNeeded;
    private final Map<Integer, List<DeciBinary>> decimal2DeciBinaries;
    private final List<DeciBinary> orderedDeciBinaries;

    DeciBinaryCache(final int deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        decimal2DeciBinaries = new HashMap<>();
        orderedDeciBinaries = new ArrayList<>(deciBinariesNeeded);
        initBaseCase();
        generateDeciBinaries();
    }

    private void initBaseCase() {
        decimal2DeciBinaries.put(0, Collections.singletonList(new DeciBinary(0, 0L)));
    }

    private List<DeciBinary> computeDeciBinariesIfAbsent(final int deci) {
        return decimal2DeciBinaries.computeIfAbsent(deci, this::computeDeciBinariesFor);
    }

    private List<DeciBinary> computeDeciBinariesFor(final int deci) {
        final List<DeciBinary> ret = new ArrayList<>();
        final int mod2 = deci % 2;
        for (int unitDigit = mod2; unitDigit <= Math.min(9, deci); unitDigit += 2) {
            ret.addAll(getDeciBinariesWithUnitDigit(deci, unitDigit));
        }
        Collections.sort(ret);
        return ret;
    }

    private List<DeciBinary> getDeciBinariesWithUnitDigit(final int deci, final int unitDigit) {
        final List<DeciBinary> deciBinariesWithoutUnitDigit = computeDeciBinariesIfAbsent((deci - unitDigit) / 2);
        return deciBinariesWithoutUnitDigit.stream().map(db -> db.appendUnitDigit(unitDigit))
                                           .collect(Collectors.toList());
    }

    private void generateDeciBinaries() {
        int currDeci = 0;
        while (orderedDeciBinaries.size() < deciBinariesNeeded) {
            final List<DeciBinary> currDeciBinaries = computeDeciBinariesIfAbsent(currDeci);
            orderedDeciBinaries.addAll(currDeciBinaries);
            currDeci++;
        }
    }

    public static void main(final String[] args) {
        final int totalNeeded = 10_000_000;
        new DeciBinaryCache(totalNeeded);
        final long startNanos = System.nanoTime();
        final long durationNanos = System.nanoTime() - startNanos;
        System.out.printf("Time taken = %d nanos%n", durationNanos);
    }

    List<DeciBinary> getOrderedDeciBinaries() {
        return orderedDeciBinaries.subList(0, deciBinariesNeeded);
    }

    static class DeciBinary implements Comparable<DeciBinary> {
        private final int decimal;
        private final long repr;

        DeciBinary(final int decimal, final long repr) {
            this.decimal = decimal;
            this.repr = repr;
        }

        private DeciBinary appendUnitDigit(final int unitDigit) {
            assert 0 <= unitDigit && unitDigit < 10;
            final long newDeciBinaryRepr = repr() * 10 + unitDigit;
            final int newDecimal = decimal() * 2 + unitDigit;
            return new DeciBinary(newDecimal, newDeciBinaryRepr);
        }

        private int decimal() {
            return decimal;
        }

        private long repr() {
            return repr;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final DeciBinary that = (DeciBinary) o;

            if (decimal != that.decimal) {
                return false;
            }
            return repr == that.repr;
        }

        @Override
        public int hashCode() {
            int result = decimal;
            result = 31 * result + (int) (repr ^ (repr >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "DeciBinary{" + "decimal=" + decimal + ", repr=" + repr + '}';
        }

        @Override
        public int compareTo(final DeciBinary other) {
            final int compareDecimal = Integer.compare(this.decimal(), other.decimal());
            return compareDecimal != 0 ? compareDecimal : Long.compare(this.repr(), other.repr());
        }
    }
}
