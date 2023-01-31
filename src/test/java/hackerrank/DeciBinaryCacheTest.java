package hackerrank;


import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DeciBinaryCacheTest {

    @Test
    public void compareToOfDeciBinaryForSameDecimal() {
        final DeciBinaryCache.DeciBinary smallDeciBinary = new DeciBinaryCache.DeciBinary(4, 4L);
        final DeciBinaryCache.DeciBinary largeDeciBinary = new DeciBinaryCache.DeciBinary(4, 12L);
        Assert.assertTrue(smallDeciBinary.compareTo(largeDeciBinary) < 0);
        Assert.assertTrue(largeDeciBinary.compareTo(smallDeciBinary) > 0);
    }

    @Test
    public void getOrderedDeciBinaries() {
        // Note that 9 is not at boundary in the sense that the decimal value of 9th and (9+1)th deciBinary is same
        final DeciBinaryCache cache = new DeciBinaryCache(9);
        final List<DeciBinaryCache.DeciBinary> expectedDeciBinaries =
                Lists.newArrayList(new DeciBinaryCache.DeciBinary(0, 0), new DeciBinaryCache.DeciBinary(1, 1),
                        new DeciBinaryCache.DeciBinary(2, 2), new DeciBinaryCache.DeciBinary(2, 10),
                        new DeciBinaryCache.DeciBinary(3, 3), new DeciBinaryCache.DeciBinary(3, 11),
                        new DeciBinaryCache.DeciBinary(4, 4), new DeciBinaryCache.DeciBinary(4, 12),
                        new DeciBinaryCache.DeciBinary(4, 20));
        Assert.assertEquals(expectedDeciBinaries, cache.getOrderedDeciBinaries());
    }

    @Test
    public void countOfDeciBinaries() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(100);
        final List<Integer> twoDigitDeciBinariesFor8 = Lists.newArrayList(16, 24, 32, 40);
        Assert.assertEquals(twoDigitDeciBinariesFor8.size(), cache.getCountFor(new DecimalToDeciBinary.Key(8, 2)));
    }

    @Test
    public void createNumDigitsToMinDecimal() {
        final int[] numDigitsToMinDecimal = DecimalToDeciBinary.createNumDigitsToMinDecimal();
        final int[] first5minDecimals = copyArray(numDigitsToMinDecimal, 1, 5);
        Assert.assertArrayEquals(Arrays.toString(first5minDecimals), new int[]{1, 2, 4, 8, 16}, first5minDecimals);
    }

    @Test
    public void createNumDigitsToMaxDecimal() {
        final int[] numDigitsToMaxDecimal = DecimalToDeciBinary.createNumDigitsToMaxDecimal();
        final int[] first2maxDecimals = copyArray(numDigitsToMaxDecimal, 1, 2);
        Assert.assertArrayEquals(Arrays.toString(first2maxDecimals), new int[]{9, 27}, first2maxDecimals);
    }

    @Test
    public void posToDecimalPlaceValue() {
        final long[] decimalPlaceValues = DecimalToDeciBinary.posToDecimalPlaceValue();
        final long[] first5decimalPlaceValues = copyLongArray(decimalPlaceValues, 1, 5);
        Assert.assertArrayEquals(Arrays.toString(first5decimalPlaceValues),
                new long[]{1L, 10L, 100L, 1_000L, 10_000L}, first5decimalPlaceValues);
    }

    @Test
    public void getAllowedNumDigits() {
        final int[] allowedNumDigitsFor7 = DecimalToDeciBinary.getAllowedNumDigits(7);
        Assert.assertArrayEquals(Arrays.toString(allowedNumDigitsFor7), new int[] {1, 2, 3}, allowedNumDigitsFor7);
    }

    @Test
    public void getDecimal() {
        final long deciBinary = 123;
        Assert.assertEquals(11, DecimalToDeciBinary.getDecimal(deciBinary));
    }

    @Test
    public void compareKeysWithDifferentDecimal() {
        final DecimalToDeciBinary.Key smallerKey = new DecimalToDeciBinary.Key(4, 1);
        final DecimalToDeciBinary.Key largerKey = new DecimalToDeciBinary.Key(5, 1);
        compareDifferentKeys(smallerKey, largerKey);
    }

    @Test
    public void compareKeysWithDifferentNumDigits() {
        final DecimalToDeciBinary.Key smallerKey = new DecimalToDeciBinary.Key(4, 1);
        final DecimalToDeciBinary.Key largerKey = new DecimalToDeciBinary.Key(4, 2);
        compareDifferentKeys(smallerKey, largerKey);
    }

    @Test
    public void compareDifferentKeys() {
        final DecimalToDeciBinary.Key smallerKey = new DecimalToDeciBinary.Key(4, 2);
        final DecimalToDeciBinary.Key largerKey = new DecimalToDeciBinary.Key(5, 1);
        compareDifferentKeys(smallerKey, largerKey);
    }

    @Test
    public void getIndexSinceKeyStart() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(18);
        /*
            DeciBinary{decimal=6, repr=14},
            DeciBinary{decimal=6, repr=22},
            DeciBinary{decimal=6, repr=30},
         */
        Assert.assertEquals(1L, cache.getIndexSinceKeyStart(16L));
        Assert.assertEquals(2L, cache.getIndexSinceKeyStart(17L));
        Assert.assertEquals(3L, cache.getIndexSinceKeyStart(18L));

    }

    @Test
    public void getDecimalFromGlobalPos() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(70);
        Assert.assertEquals(DecimalToDeciBinary.getDecimal(0L), cache.getDecimalFromGlobalPos(1L));
        Assert.assertEquals(DecimalToDeciBinary.getDecimal(14L), cache.getDecimalFromGlobalPos(16L));
        Assert.assertEquals(DecimalToDeciBinary.getDecimal(22L), cache.getDecimalFromGlobalPos(17L));
        Assert.assertEquals(DecimalToDeciBinary.getDecimal(30L), cache.getDecimalFromGlobalPos(18L));
    }

    @Test
    public void getPosRelativeToDecimalStart() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(70);
        Assert.assertEquals(1L, cache.getPosRelativeToDecimalStart(1L));
        Assert.assertEquals(1L, cache.getPosRelativeToDecimalStart(2L));


        /*
        globalPos = 15, relPos = 1: DeciBinary{decimal=6, repr=6},
        globalPos = 16, relPos = 2: DeciBinary{decimal=6, repr=14},
        globalPos = 17, relPos = 3: DeciBinary{decimal=6, repr=22},
        globalPos = 18, relPos = 4: DeciBinary{decimal=6, repr=30},
        globalPos = 19, relPos = 5: DeciBinary{decimal=6, repr=102},
        globalPos = 20, relPos = 6: DeciBinary{decimal=6, repr=110},
         */
        Assert.assertEquals(1L, cache.getPosRelativeToDecimalStart(15L));
        Assert.assertEquals(2L, cache.getPosRelativeToDecimalStart(16L));
        Assert.assertEquals(3L, cache.getPosRelativeToDecimalStart(17L));
        Assert.assertEquals(4L, cache.getPosRelativeToDecimalStart(18L));
        Assert.assertEquals(5L, cache.getPosRelativeToDecimalStart(19L));
        Assert.assertEquals(6L, cache.getPosRelativeToDecimalStart(20L));
    }

    @Test
    public void getNumDigits() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(30);
        /*
        relPos = 1: DeciBinary{decimal=6, repr=6},
        relPos = 2: DeciBinary{decimal=6, repr=14},
        relPos = 3: DeciBinary{decimal=6, repr=22},
        relPos = 4: DeciBinary{decimal=6, repr=30},
        relPos = 5: DeciBinary{decimal=6, repr=102},
        relPos = 6: DeciBinary{decimal=6, repr=110},
         */
        Assert.assertEquals(0, cache.getNumDigits(0, 1L));
        Assert.assertEquals(-1, cache.getNumDigits(0, 2L));

        Assert.assertEquals(1, cache.getNumDigits(6, 1L));
        Assert.assertEquals(2, cache.getNumDigits(6, 2L));
        Assert.assertEquals(2, cache.getNumDigits(6, 3L));
        Assert.assertEquals(2, cache.getNumDigits(6, 4L));
        Assert.assertEquals(3, cache.getNumDigits(6, 5L));
        Assert.assertEquals(3, cache.getNumDigits(6, 6L));
        Assert.assertEquals(-1, cache.getNumDigits(6, 7L));
    }

    @Test
    public void getNumDigitsSlowly() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(30);
        /*
        relPos = 1: DeciBinary{decimal=6, repr=6},
        relPos = 2: DeciBinary{decimal=6, repr=14},
        relPos = 3: DeciBinary{decimal=6, repr=22},
        relPos = 4: DeciBinary{decimal=6, repr=30},
        relPos = 5: DeciBinary{decimal=6, repr=102},
        relPos = 6: DeciBinary{decimal=6, repr=110},
         */
        Assert.assertEquals(0, cache.getNumDigitsSlowly(0, 1L));
        Assert.assertEquals(-1, cache.getNumDigitsSlowly(0, 2L));

        Assert.assertEquals(1, cache.getNumDigitsSlowly(6, 1L));
        Assert.assertEquals(2, cache.getNumDigitsSlowly(6, 2L));
        Assert.assertEquals(2, cache.getNumDigitsSlowly(6, 3L));
        Assert.assertEquals(2, cache.getNumDigitsSlowly(6, 4L));
        Assert.assertEquals(3, cache.getNumDigitsSlowly(6, 5L));
        Assert.assertEquals(3, cache.getNumDigitsSlowly(6, 6L));
        Assert.assertEquals(-1, cache.getNumDigitsSlowly(6, 7L));
    }

    @Test
    public void getNumDeciBsWithMaxDigits() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(30);

        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigits(0, 0));
        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigits(0, 2));

        /*
        relPos = 1: DeciBinary{decimal=6, repr=6},
        relPos = 2: DeciBinary{decimal=6, repr=14},
        relPos = 3: DeciBinary{decimal=6, repr=22},
        relPos = 4: DeciBinary{decimal=6, repr=30},
        relPos = 5: DeciBinary{decimal=6, repr=102},
        relPos = 6: DeciBinary{decimal=6, repr=110},
         */

        Assert.assertEquals(0, cache.getNumDeciBsWithMaxDigits(6, 0));
        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigits(6, 1));
        Assert.assertEquals(4, cache.getNumDeciBsWithMaxDigits(6, 2));
        Assert.assertEquals(6, cache.getNumDeciBsWithMaxDigits(6, 3));
        Assert.assertEquals(6, cache.getNumDeciBsWithMaxDigits(6, 5));
    }

    @Test
    public void getNumDeciBsWithMaxDigitsSlowly() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(30);

        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigitsSlowly(0, 0));
        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigitsSlowly(0, 2));

        /*
        relPos = 1: DeciBinary{decimal=6, repr=6},
        relPos = 2: DeciBinary{decimal=6, repr=14},
        relPos = 3: DeciBinary{decimal=6, repr=22},
        relPos = 4: DeciBinary{decimal=6, repr=30},
        relPos = 5: DeciBinary{decimal=6, repr=102},
        relPos = 6: DeciBinary{decimal=6, repr=110},
         */

        Assert.assertEquals(0, cache.getNumDeciBsWithMaxDigitsSlowly(6, 0));
        Assert.assertEquals(1, cache.getNumDeciBsWithMaxDigitsSlowly(6, 1));
        Assert.assertEquals(4, cache.getNumDeciBsWithMaxDigitsSlowly(6, 2));
        Assert.assertEquals(6, cache.getNumDeciBsWithMaxDigitsSlowly(6, 3));
        Assert.assertEquals(6, cache.getNumDeciBsWithMaxDigitsSlowly(6, 5));
    }

    @Test
    public void getDeciBinary() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(30);
        Assert.assertEquals(0, cache.getDeciBinary(1));
        Assert.assertEquals(1, cache.getDeciBinary(2));

        // from https://www.hackerrank.com/challenges/decibinary-numbers/problem?isFullScreen=true Sample Input 2
        Assert.assertEquals(102, cache.getDeciBinary(19));
        Assert.assertEquals(103, cache.getDeciBinary(25));
        Assert.assertEquals(11, cache.getDeciBinary(6));
        Assert.assertEquals(12, cache.getDeciBinary(8));
        Assert.assertEquals(110, cache.getDeciBinary(20));
        Assert.assertEquals(100, cache.getDeciBinary(10));
        Assert.assertEquals(8, cache.getDeciBinary(27));
        Assert.assertEquals(31, cache.getDeciBinary(24));
        Assert.assertEquals(32, cache.getDeciBinary(30));
        Assert.assertEquals(5, cache.getDeciBinary(11));
    }

    private static int firstDigit(final int x) {
        return Integer.parseInt(Integer.toString(x).substring(0, 1));
    }

    private static void compareDifferentKeys(final DecimalToDeciBinary.Key smallerKey, final DecimalToDeciBinary.Key largerKey) {
        Assert.assertTrue(smallerKey.compareTo(largerKey) < 0);
        Assert.assertTrue(largerKey.compareTo(smallerKey) > 0);
    }

    private static int[] copyArray(
            final int[] numDigitsToMaxDecimal, @SuppressWarnings("SameParameterValue") final int start,
            final int rangeLength) {
        return Arrays.copyOfRange(numDigitsToMaxDecimal, start, start + rangeLength);
    }

    private static long[] copyLongArray(
            final long[] numDigitsToMaxDecimal, @SuppressWarnings("SameParameterValue") final int start,
            final int rangeLength) {
        return Arrays.copyOfRange(numDigitsToMaxDecimal, start, start + rangeLength);
    }
}
