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
    public void getKeyAtIndex() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(25);
        // Based on Sample Input 1 at https://www.hackerrank.com/challenges/decibinary-numbers/problem?isFullScreen=false
        final int numDigitsIn102 = 3;
        final int decimal = DecimalToDeciBinary.getDecimal(102);
        Assert.assertEquals(new DecimalToDeciBinary.Key(decimal, numDigitsIn102), cache.getKeyAtIndex(19L));
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
    public void getStartingDigit() {
        final DecimalToDeciBinary cache = new DecimalToDeciBinary(25);
        // Based on Sample Input 1 at https://www.hackerrank.com/challenges/decibinary-numbers/problem?isFullScreen=false
        Assert.assertEquals(1, cache.getStartingDigit(19L));
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
}
