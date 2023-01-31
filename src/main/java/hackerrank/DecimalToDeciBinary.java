package hackerrank;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
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
    private static final boolean DEBUG = false;
    private static final int MAX_NUM_DECIMALS = 300_000;

    private final long deciBinariesNeeded;
    private final List<Map<Integer, Long>> decimalToNumDigitsToCount;
    private final NavigableMap<Long, Integer> endingIndexToDecimal;
    private final List<NavigableMap<Long, Integer>> decimalToEndingRelPosToNumDigits; // might be removed
    private final List<Map<Integer, Long>> decimalToNumDigitsToEndingRelPos;
    private long totalGenerated = 0;

    DecimalToDeciBinary(final long deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        decimalToNumDigitsToCount = new ArrayList<>(MAX_NUM_DECIMALS);
        endingIndexToDecimal = new TreeMap<>();
        decimalToEndingRelPosToNumDigits = new ArrayList<>(MAX_NUM_DECIMALS);
        decimalToNumDigitsToEndingRelPos = new ArrayList<>(MAX_NUM_DECIMALS);
        generateDeciBinaries();
    }

    long getCountFor(final int decimal, final int numDigits) {
        if (decimalToNumDigitsToCount.size() < decimal) {
            return 0L;
        }
        return decimalToNumDigitsToCount.get(decimal).getOrDefault(numDigits, 0L);
    }

    long getDeciBinary(final long globalPos) {
        final int decimal = getDecimalFromGlobalPos(globalPos);
        final long relPos = getPosRelativeToDecimalStart(globalPos);
        return getDeciBinaryInternal(decimal, relPos);
    }

    private long getDeciBinaryInternal(final int decimal, final long relPos) {
        if (DEBUG) {
            System.out.printf("Begin call getDeciBinaryInternal(decimal=%d, relPos=%d%n", decimal, relPos);
        }
        if (decimal == 0) {
            return 0L;
        }
        final int numDigits = getNumDigitsSlowly(decimal, relPos);
        final long numDeciBsWithLessDigits = getNumDeciBsWithMaxDigits(decimal, numDigits - 1);
        long suffixRelPos = relPos - numDeciBsWithLessDigits;
        final int maxDigitsForSuffix = numDigits - 1;
        for (int firstDigit = 1; firstDigit <= 9; firstDigit++) {
            final int deciBinaryPlaceValOfFirstDigit = (1 << (numDigits - 1)) * firstDigit;
            final int suffixDecimal = decimal - deciBinaryPlaceValOfFirstDigit;
            final long numDeciBsWithSuffix = getNumDeciBsWithMaxDigits(suffixDecimal, maxDigitsForSuffix);
            if (numDeciBsWithSuffix < suffixRelPos) {
                suffixRelPos -= numDeciBsWithSuffix; // try a different first digit
            } else {
                final long decimalPlaceValOfFirstDigit = firstDigit * POS_TO_DECIMAL_PLACE_VALUE[numDigits];
                return decimalPlaceValOfFirstDigit + getDeciBinaryInternal(suffixDecimal, suffixRelPos);
            }
        }
        throw new IllegalStateException("Unreachable code");
    }

    long getNumDeciBsWithMaxDigits(final int decimal, final int maxDigitsAllowed) {
        return decimalToNumDigitsToEndingRelPos.get(decimal).entrySet().stream()
                                               .filter(e -> e.getKey() <= maxDigitsAllowed)
                                               .mapToLong(Map.Entry::getValue)// cumulative counts
                                               .max() // max since cumulative entries
                                               .orElse(0L);
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
        final Map<Integer, Long> numDigitsToEndingRelPos = decimalToNumDigitsToEndingRelPos.get(decimal);
        return numDigitsToEndingRelPos.entrySet().stream()
                                      .filter(e -> e.getValue() >= relPos) // filter on num digits
                                      .mapToInt(Map.Entry::getKey) // map to num Digits
                                      .min().orElse(-1);// all deciBinaries < relPos

    }

    int getDecimalFromGlobalPos(final long globalPos) {
        return endingIndexToDecimal.ceilingEntry(globalPos).getValue();
    }

    long getPosRelativeToDecimalStart(final long globalPos) {
        if (globalPos == 1) {
            return 1L;
        }
        final long endingPosForPrevDecimal = endingIndexToDecimal.lowerEntry(globalPos).getKey();
        return globalPos - endingPosForPrevDecimal;
    }

    private void initBaseCase() {
        decimalToNumDigitsToCount.add(Collections.singletonMap(0, 1L));
        final TreeMap<Long, Integer> relPosToNumDigitsForZero = new TreeMap<>();
        final TreeMap<Integer, Long> numDigitsToRelPosForZero = new TreeMap<>();
        relPosToNumDigitsForZero.put(1L, 0);
        numDigitsToRelPosForZero.put(0, 1L);
        endingIndexToDecimal.put(1L, 0);
        decimalToEndingRelPosToNumDigits.add(relPosToNumDigitsForZero);
        decimalToNumDigitsToEndingRelPos.add(numDigitsToRelPosForZero);
        totalGenerated++;
    }

    private void generateDeciBinaries() {
        initBaseCase();
        int currDeci = 1;
        while (totalGenerated < deciBinariesNeeded) {
            generateDeciBinariesFor(currDeci);
            currDeci++;
        }
    }

    private void generateDeciBinariesFor(final int decimal) {
        final int[] allowedNumDigits = getAllowedNumDigits(decimal);
//        final NavigableMap<Long, Integer> relPosToNumDigits = new TreeMap<>();
        final Map<Integer, Long> numDigitsToRelPos = new HashMap<>();
        final Map<Integer, Long> numDigitsToCount = new HashMap<>();
        long relPos = 0L;
        for (final int numDigits : allowedNumDigits) {
            final long numDeciBinaries = computeNumDeciBinaries(decimal, numDigits);
            if (numDeciBinaries != 0) {
                relPos += numDeciBinaries;
//                relPosToNumDigits.put(relPos, numDigits);
                numDigitsToRelPos.put(numDigits, relPos);
                numDigitsToCount.put(numDigits, numDeciBinaries);
                totalGenerated += numDeciBinaries;
            }
        }
        decimalToNumDigitsToCount.add(numDigitsToCount);
        endingIndexToDecimal.put(totalGenerated, decimal);
//        decimalToEndingRelPosToNumDigits.put(decimal, relPosToNumDigits);
        decimalToNumDigitsToEndingRelPos.add(numDigitsToRelPos);
    }

    private long computeNumDeciBinaries(final int decimal, final int numDigits) {
        final int numDigitsInPrefix = numDigits - 1;
        long numDeciBinaries = 0;
        final int mod2 = decimal % 2;
        for (int unitDigit = mod2; unitDigit <= Math.min(9, decimal); unitDigit += 2) {
            final int prefixDecimal = (decimal - unitDigit) / 2;
            numDeciBinaries += getCountFor(prefixDecimal, numDigitsInPrefix);
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
        ret[0] = 0L;
        long powOf10 = 1L;
        for (int i = 1; i <= MAX_NUM_DIGITS; i++) {
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
}
