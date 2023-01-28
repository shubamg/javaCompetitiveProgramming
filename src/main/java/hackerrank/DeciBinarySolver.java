package hackerrank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DeciBinarySolver {

    private final int deciBinariesNeeded;
    private final List<DeciBinary> deciWithDeciBinaries;

    private DeciBinarySolver(final int deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        deciWithDeciBinaries = new ArrayList<>(deciBinariesNeeded);
    }

    private static Set<DeciBinary> getAllDeciBinaryForDeci(final int deci) {
        final Set<DeciBinary> ret = new HashSet<>();
        if (deci == 0) {
            ret.add(new DeciBinary(0, 0));
            return ret;
        }
        final int mod2 = deci % 2;
        for (int unitDigit = mod2; unitDigit <= Math.min(9, deci); unitDigit+=2) {
            ret.addAll(getDeciBinariesWithUnitDigit(deci, unitDigit));
        }
        return ret;
    }

    private static Set<DeciBinary> getDeciBinariesWithUnitDigit(final int deci, final int unitDigit) {
        final Set<DeciBinary> deciBinariesWithoutUnitDigit = getAllDeciBinaryForDeci((deci - unitDigit) / 2);
        return deciBinariesWithoutUnitDigit.stream().map(db -> db.appendUnitDigit(unitDigit)).collect(Collectors.toSet());
    }

    private void generateDeciBinaries() {
        int currDeci = 0;
        while (deciWithDeciBinaries.size() < deciBinariesNeeded) {
            deciWithDeciBinaries.addAll(getAllDeciBinaryForDeci(currDeci));
            currDeci++;
        }
        sort();
    }

    private void sort() {
        final Comparator<DeciBinary> comparator =
                Comparator.comparingInt(DeciBinary::decimal).thenComparingLong(DeciBinary::repr);
        deciWithDeciBinaries.sort(comparator);
    }

    public static void main(final String[] args) {
        final int totalNeeded = 10_000_000;
        final DeciBinarySolver solver = new DeciBinarySolver(totalNeeded);
        final long startNanos = System.nanoTime();
        solver.generateDeciBinaries();
        final long durationNanos = System.nanoTime() - startNanos;
        System.out.println(solver.deciWithDeciBinaries.subList(0, 100));
        System.out.printf("Time taken = %d nanos%n", durationNanos);
    }

    private static class DeciBinary {
        private final int decimal;
        private final long repr;

        private DeciBinary(final int decimal, final long repr) {
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
            return "DeciBinary{" +
                    "decimal=" + decimal +
                    ", repr=" + repr +
                    '}';
        }
    }
}
