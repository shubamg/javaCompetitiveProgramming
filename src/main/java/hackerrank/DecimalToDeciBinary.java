package hackerrank;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DecimalToDeciBinary {

    private static final int MAX_NUM_DIGITS = 20;
    private static final int[] NUM_DIGITS_TO_MIN_DECIMAL = createNumDigitsToMinDecimal();
    private static final int[] NUM_DIGITS_TO_MAX_DECIMAL = createNumDigitsToMaxDecimal();
    private final long deciBinariesNeeded;
    private final Map<Key, Long> decimal2CountOfDeciB;
    private long totalGenerated = 0;

    DecimalToDeciBinary(final long deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        decimal2CountOfDeciB = new HashMap<>();
        generateDeciBinaries();
    }

    Key getKeyAtIndex(final long index) {
        return null;
    }

    long getCountFor(final Key key) {
        return decimal2CountOfDeciB.get(key);
    }

    private void initBaseCase() {
        decimal2CountOfDeciB.put(new Key(0, 0), 1L);
        totalGenerated++;
    }

    private void generateDeciBinaries() {
        initBaseCase();
        int currDeci = 1;
        while (totalGenerated < deciBinariesNeeded) {
            processAllKeysFor(currDeci);
            currDeci++;
        }
    }

    private void processAllKeysFor(final int decimal) {
        final int[] allowedNumDigits = getAllowedNumDigits(decimal);
        for (final int numDigits : allowedNumDigits) {
            processKey(new Key(decimal, numDigits));
        }
    }

    private void processKey(final Key key) {
        final long numDeciBinaries = computeNumDeciBinaries(key);
        if (numDeciBinaries != 0) {
            decimal2CountOfDeciB.put(key, numDeciBinaries);
            totalGenerated += numDeciBinaries;
        }
    }

    private long computeNumDeciBinaries(final Key key) {
        final int decimal = key.getDecimal();
        final int numDigits = key.getNumDigits();
        final int numDigitsInPrefix = numDigits - 1;
        long numDeciBinaries = 0;
        final int mod2 = decimal % 2;
        for (int unitDigit = mod2; unitDigit <= Math.min(9, decimal); unitDigit += 2) {
            final int prefixDecimal = (decimal - unitDigit) / 2;
            final Key prefixKey = new Key(prefixDecimal, numDigitsInPrefix);
            numDeciBinaries += decimal2CountOfDeciB.getOrDefault(prefixKey, 0L);
        }
        return numDeciBinaries;
    }

    static int[] createNumDigitsToMinDecimal() {
        final int[] ret = new int[MAX_NUM_DIGITS + 1];
        ret[0] = 0;
        int powOf2 = 1;
        for (int i = 1; i <= MAX_NUM_DIGITS; i++) {
            ret[i] = powOf2;
            powOf2 *= 2;
        }
        return ret;
    }

    static int[] createNumDigitsToMaxDecimal() {
        final int[] ret = new int[MAX_NUM_DIGITS + 1];
        int powOf2 = 1;
        for (int i = 0; i <= MAX_NUM_DIGITS; i++) {
            ret[i] = 9 * (powOf2 - 1);
            powOf2 *= 2;
        }
        return ret;
    }

    static int[] getAllowedNumDigits(final int currDeci) {
        return IntStream.range(1, MAX_NUM_DIGITS + 1)
                        .filter(numDigit -> NUM_DIGITS_TO_MIN_DECIMAL[numDigit] <= currDeci)
                        .filter(numDigit -> currDeci <= NUM_DIGITS_TO_MAX_DECIMAL[numDigit]).toArray();
    }

    static int getDecimal(final long deciBinary) {
        long remainingDeciBinary = deciBinary;
        int powOf2 = 1;
        int decimal = 0;
        while (remainingDeciBinary > 0) {
            final int digit = (int) (remainingDeciBinary % 10);
            decimal += (powOf2 * digit);
            powOf2 *= 2;
            remainingDeciBinary /= 10;
        }
        return decimal;
    }

    static class Key implements Comparable<Key> {
        private final int decimal;
        private final int numDigits;

        Key(final int decimal, final int numDigits) {
            this.decimal = decimal;
            this.numDigits = numDigits;
        }

        int getDecimal() {
            return decimal;
        }

        int getNumDigits() {
            return numDigits;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Key key = (Key) o;

            if (decimal != key.decimal) {
                return false;
            }
            return numDigits == key.numDigits;
        }

        @Override
        public int hashCode() {
            int result = decimal;
            result = 31 * result + numDigits;
            return result;
        }

        @Override
        public String toString() {
            return "Key{" + "decimal=" + decimal + ", numDigits=" + numDigits + '}';
        }

        @Override
        public int compareTo(final Key other) {
            return Comparator.comparingInt(Key::getDecimal).thenComparingInt(Key::getNumDigits)
                             .compare(this, other);
        }
    }
}

