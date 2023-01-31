package hackerrank;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DecimalToDeciBinary {

    private static final int MAX_NUM_DIGITS = 20;
    private static final int[] NUM_DIGITS_TO_MIN_DECIMAL = createNumDigitsToMinDecimal();
    private static final int[] NUM_DIGITS_TO_MAX_DECIMAL = createNumDigitsToMaxDecimal();
    private static final long[] POS_TO_DECIMAL_PLACE_VALUE = posToDecimalPlaceValue();

    private final long deciBinariesNeeded;
    private final SortedMap<Key, Long> keyToDeciBCount; // may be removed
    private final NavigableMap<Long, Key> endingIndexToKeys;
    private final NavigableMap<Long, Integer> endingIndexToDecimal;
    private final NavigableMap<Key, Long> keys2CumulativeIndex; // may be removed
    private final Map<Integer, NavigableMap<Long, Integer>> decimalToEndingRelPosToNumDigits; // might be removed
    private final Map<Integer, NavigableMap<Integer, Long>> decimalToNumDigitsToEndingRelPos;
    private long totalGenerated = 0;

    DecimalToDeciBinary(final long deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        keyToDeciBCount = new TreeMap<>();
        endingIndexToKeys = new TreeMap<>();
        endingIndexToDecimal = new TreeMap<>();
        decimalToEndingRelPosToNumDigits = new HashMap<>();
        decimalToNumDigitsToEndingRelPos = new HashMap<>();
        keys2CumulativeIndex = new TreeMap<>();
        generateDeciBinaries();
    }

    long getCountFor(final Key key) {
        return keyToDeciBCount.getOrDefault(key, 0L);
    }

    Key getKeyAtIndex(final long index) {
        return endingIndexToKeys.ceilingEntry(index).getValue();
    }

    long getDeciBinary(final long globalPos) {
        final int decimal = getDecimalFromGlobalPos(globalPos);
        final long relPos = getPosRelativeToDecimalStart(globalPos);
        final int numDigits = getNumDigits(decimal, relPos);
        return getDeciBinaryInternal(decimal, relPos, numDigits);
    }

    private long getDeciBinaryInternal(final int decimal, final long relPos, final int numDigits) {
        System.out.printf("Begin call getDeciBinaryInternal(decimal=%d, relPos=%d, numDigits=%d%n", decimal,
                relPos, numDigits);
        if (decimal == 0) {
            return 0L;
        }
        final long numDeciBsWithLessDigits = getNumDeciBsWithMaxDigits(decimal, numDigits - 1);
        long suffixRelPos = relPos - numDeciBsWithLessDigits;
        final int maxDigitsForSuffix = numDigits - 1;
        for (int firstDigit = 1; firstDigit <= 9; firstDigit++) {
            final int contributionOfFirstDigit = (1 << (numDigits - 1)) * firstDigit;
            final int suffixDecimal = decimal - contributionOfFirstDigit;
            final long numDeciBsWithSuffix = getNumDeciBsWithMaxDigits(suffixDecimal, maxDigitsForSuffix);
            if (numDeciBsWithSuffix < suffixRelPos) {
                suffixRelPos -= numDeciBsWithSuffix; // try a different first digit
            } else {
                final int suffixNumDigits = getNumDigits(suffixDecimal, suffixRelPos);
                return contributionOfFirstDigit + getDeciBinaryInternal(
                        suffixDecimal, suffixRelPos, suffixNumDigits);
            }
        }
        throw new IllegalStateException("Unreachable code");
    }

    long getNumDeciBsWithMaxDigits(final int decimal, final int maxDigitsAllowed) {
        final Map.Entry<Integer, Long> floorEntry =
                decimalToNumDigitsToEndingRelPos.get(decimal).floorEntry(maxDigitsAllowed);
        if (floorEntry == null) {
            return 0L;
        }
        return floorEntry.getValue();

    }

    long getNumDeciBsWithMaxDigitsSlowly(final int decimal, final int maxDigitsAllowed) {
        final NavigableMap<Long, Integer> endingRelPosToNumDigits = decimalToEndingRelPosToNumDigits.get(decimal);
        final int minDigits = endingRelPosToNumDigits.firstEntry().getValue();
        if (minDigits > maxDigitsAllowed) {
            return 0L; // return 0 if all deciBinary have greater than maxDigitsAllowed
        }
        //noinspection OptionalGetWithoutIsPresent, explicitly checked up
        return endingRelPosToNumDigits.entrySet().stream()
                                      .filter(e -> e.getValue() <= maxDigitsAllowed) // filter on num digits
                                      .mapToLong(Map.Entry::getKey) // map to Rel Pos
                                      .max()
                                      .getAsLong();
    }


    int getNumDigits(final int decimal, final long relPos) {
        assert relPos > 0;
        final NavigableMap<Long, Integer> endingRelPosToNumDigits = decimalToEndingRelPosToNumDigits.get(decimal);
        final Map.Entry<Long, Integer> ceilingEntry = endingRelPosToNumDigits.ceilingEntry(relPos);
        if (ceilingEntry == null) {
            return -1;
        }
        return ceilingEntry.getValue();
    }

    int getNumDigitsSlowly(final int decimal, final long relPos) {
        assert relPos > 0;
        final NavigableMap<Integer, Long> numDigitsToEndingRelPos = decimalToNumDigitsToEndingRelPos.get(decimal);
        final long size = numDigitsToEndingRelPos.lastEntry().getValue();
        if (size < relPos) {
            return -1; // return -1 if less than relPos decimals are there
        }
        //noinspection OptionalGetWithoutIsPresent, explicitly checked up
        return numDigitsToEndingRelPos.entrySet().stream()
                                      .filter(e -> e.getValue() >= relPos) // filter on num digits
                                      .mapToInt(Map.Entry::getKey) // map to num Digits
                                      .findFirst() // equivalent to min since this is sorted stream
                                      .getAsInt();
    }

    int getDecimalFromGlobalPos(final long globalPos) {
        return getKeyAtIndex(globalPos).getDecimal();
    }

    long getPosRelativeToDecimalStart(final long globalPos) {
        if (globalPos == 1) {
            return 1L;
        }
        final long endingPosForPrevDecimal = endingIndexToDecimal.lowerEntry(globalPos).getKey();
        return globalPos - endingPosForPrevDecimal;
    }

    int getStartingDigit(final long index) {
        assert index > 1;
        final Key key = getKeyAtIndex(index);
        final long indexSinceKeyStart = getIndexSinceKeyStart(index);
        final int decimal = key.getDecimal();
        final int numDigits = key.getNumDigits();
        final int powOf2 = 1 << (numDigits - 1);
        final int suffixNumDigits = numDigits - 1;
        long cumulativeDeciBs = 0L;
        for(int startDigit = 1; startDigit <= 9; startDigit++) {
            final int suffixDecimal = decimal - powOf2 * startDigit;
            if (suffixDecimal < 0) {
                break;
            }
            final Key suffixKey = new Key(suffixDecimal, suffixNumDigits);
            final long suffixDeciBs = getFlooredDeciBsWithSameDecimal(suffixKey);
            cumulativeDeciBs += suffixDeciBs;
            if (indexSinceKeyStart <= cumulativeDeciBs) {
                return startDigit;
            }
        }
        throw new IllegalStateException("Unreachable code");
    }

    private long getFlooredDeciBsWithSameDecimal(final Key key) {
        final int decimal = key.getDecimal();
        if (decimal == 0) {
            return 1;
        }
        final Map.Entry<Key, Long> floorEntry = keys2CumulativeIndex.floorEntry(key);
        final Map.Entry<Key, Long> entryFlooredOnDecimal = keys2CumulativeIndex.floorEntry(new Key(decimal, 0));
        assert floorEntry.getKey().getDecimal() == decimal;
        return floorEntry.getValue() - entryFlooredOnDecimal.getValue();
    }

    long getIndexSinceKeyStart(final long index) {
        final long lowerIndex = endingIndexToKeys.lowerEntry(index).getKey();
        return index - lowerIndex;
    }

    private void populateCumulativeMaps() {
        long cumulativeIndex = 0L;
        for (final Map.Entry<Key, Long> keyDeciBCountEntry : keyToDeciBCount.entrySet()) {
            final Key key = keyDeciBCountEntry.getKey();
            final long count = keyDeciBCountEntry.getValue();
            cumulativeIndex += count;
            endingIndexToKeys.put(cumulativeIndex, key);
            keys2CumulativeIndex.put(key, cumulativeIndex);
        }
    }

    private void initBaseCase() {
        keyToDeciBCount.put(new Key(0, 0), 1L);
        final TreeMap<Long, Integer> relPosToNumDigitsForZero = new TreeMap<>();
        final TreeMap<Integer, Long> numDigitsToRelPosForZero = new TreeMap<>();
        relPosToNumDigitsForZero.put(1L, 0);
        numDigitsToRelPosForZero.put(0, 1L);
        endingIndexToDecimal.put(1L, 0);
        decimalToEndingRelPosToNumDigits.put(0, relPosToNumDigitsForZero);
        decimalToNumDigitsToEndingRelPos.put(0, numDigitsToRelPosForZero);
        totalGenerated++;
    }

    private void generateDeciBinaries() {
        initBaseCase();
        int currDeci = 1;
        while (totalGenerated < deciBinariesNeeded) {
            processAllKeysFor(currDeci);
            currDeci++;
        }
        populateCumulativeMaps();
    }

    private void processAllKeysFor(final int decimal) {
        final int[] allowedNumDigits = getAllowedNumDigits(decimal);
        final NavigableMap<Long, Integer> relPosToNumDigits = new TreeMap<>();
        final NavigableMap<Integer, Long> numDigitsToRelPos = new TreeMap<>();
        long relPos = 0L;
        for (final int numDigits : allowedNumDigits) {
            final Key key = new Key(decimal, numDigits);
            final long numDeciBinaries = computeNumDeciBinaries(key);
            if (numDeciBinaries != 0) {
                relPos += numDeciBinaries;
                relPosToNumDigits.put(relPos, numDigits);
                numDigitsToRelPos.put(numDigits, relPos);
                keyToDeciBCount.put(key, numDeciBinaries);
                totalGenerated += numDeciBinaries;
            }
        }
        endingIndexToDecimal.put(totalGenerated, decimal);
        decimalToEndingRelPosToNumDigits.put(decimal, relPosToNumDigits);
        decimalToNumDigitsToEndingRelPos.put(decimal, numDigitsToRelPos);
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
            numDeciBinaries += keyToDeciBCount.getOrDefault(prefixKey, 0L);
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


    static long[] posToDecimalPlaceValue() {
        final long[] ret = new long[MAX_NUM_DIGITS + 1];
        long powOf10 = 1L;
        for (int i = 0; i <= MAX_NUM_DIGITS; i++) {
            ret[i] = powOf10;
            powOf10 *= 10L;
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

    int getRelativePos(final int decimal, final long relativeIndex) {
        assert relativeIndex > 0;
        final Map.Entry<Long, Integer> ceilingEntry =
                decimalToEndingRelPosToNumDigits.get(decimal).ceilingEntry(relativeIndex);
        if (ceilingEntry == null) {
            return -1; // default value
        }
        return ceilingEntry.getValue();
    }

    static class Key implements Comparable<Key> { // might be removed
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
