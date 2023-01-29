package hackerrank;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DecimalToDeciBinary {

    private static final int MAX_NUM_DIGITS = 20;
    private final long deciBinariesNeeded;
    private final Map<Key, Long> decimal2CountOfDeciB;
    private final long[] numDigitsToMinDecimal;
    private final long[] numDigitsToMaxDecimal;
    private long totalGenerated = 0;

    DecimalToDeciBinary(final long deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        decimal2CountOfDeciB = new HashMap<>();
        this.numDigitsToMinDecimal = createNumDigitsToMinDecimal();
        this.numDigitsToMaxDecimal = createNumDigitsToMaxDecimal();
        generateDeciBinaries();
    }

    private void initBaseCase() {
        decimal2CountOfDeciB.put(new Key(0, 0), 0L);
        totalGenerated++;
    }

    static long[] createNumDigitsToMinDecimal() {
        final long[] ret = new long[MAX_NUM_DIGITS + 1];
        ret[0] = 0;
        long powOf2 = 1;
        for (int i = 1; i <= MAX_NUM_DIGITS; i++) {
            ret[i] = powOf2;
            powOf2 *= 2;
        }
        return ret;
    }

    static long[] createNumDigitsToMaxDecimal() {
        final long[] ret = new long[MAX_NUM_DIGITS + 1];
        long powOf2 = 1;
        for (int i = 0; i <= MAX_NUM_DIGITS; i++) {
            ret[i] = 9 * (powOf2 - 1);
            powOf2 *= 2;
        }
        return ret;
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
            process(new Key(decimal, numDigits));
        }
    }

    private void process(final Key key) {
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

    private int[] getAllowedNumDigits(final int currDeci) {
        return IntStream.range(1, MAX_NUM_DIGITS + 1)
                        .filter(numDigit -> numDigitsToMinDecimal[numDigit] <= currDeci)
                        .filter(numDigit -> currDeci <= numDigitsToMaxDecimal[numDigit]).toArray();
    }

    public static void main(final String[] args) {
        final long totalNeeded = 10_000_000_000_000_000L;
        final long startNanos = System.nanoTime();
        final DecimalToDeciBinary counter = new DecimalToDeciBinary(totalNeeded);
        final long durationNanos = System.nanoTime() - startNanos;
        for (int i = 0; counter.decimal2CountOfDeciB.containsKey(i); i++) {
            System.out.printf("%d:%d%n", i, counter.decimal2CountOfDeciB.get(i));
        }
        System.out.printf("Time taken = %d nanos%n", durationNanos);
    }

    public long getCountFor(final Key key) {
        return -1;
    }

    static class Key {
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
    }
}

