package util;

import math.ModuloCalculator;
import org.junit.Assert;
import org.junit.Test;

public class DecimalTest {

    @Test
    public void testZero() {
        final Decimal positiveZero = Decimal.getInteger(0, true);
        final Decimal negativeZero = Decimal.getInteger(0, false);
        Assert.assertEquals(positiveZero, positiveZero.add(negativeZero));
        Assert.assertEquals(positiveZero, negativeZero.add(negativeZero));
        Assert.assertEquals(positiveZero, negativeZero.add(positiveZero));
        Assert.assertEquals(positiveZero, positiveZero.add(positiveZero));
    }

    @Test
    public void testSubtraction() {
        // 20.035 - 25.0009 = -4.9659
        final Decimal d1 = new Decimal(20, 35, 3, true);
        final Decimal d2 = new Decimal(25, 9, 4, true);
        Assert.assertEquals(new Decimal(4, 9659, 4, false), d1.subtract(d2));
    }

    @Test
    public void testCarry() {
        // -20.5 - 25.637 = -46.137
        final Decimal d1 = new Decimal(20, 5, 1, false);
        final Decimal d2 = new Decimal(25, 637, 3, false);
        Assert.assertEquals(new Decimal(46, 137, 3, false), d1.add(d2));
    }

    @Test
    public void testFractions() {
        // 0.004 - 0.09 = - 0.086
        final Decimal d1 = new Decimal(0, 4, 3, true);
        final Decimal d2 = new Decimal(0, 9, 2, true);
        Assert.assertEquals(new Decimal(0, 86, 3, false), d1.subtract(d2));
    }

    @Test
    public void testZeroSum() {
        // 0.004 - 0.004 = 0
        final Decimal d1 = new Decimal(0, 4, 3, false);
        Assert.assertEquals(Decimal.getInteger(0, true), d1.subtract(d1));
    }

    @Test
    public void testDifferentSignIntAndFrac() {
        // 1.004 - 0.9
        final Decimal d1 = new Decimal(1, 4, 3, true);
        final Decimal d2 = new Decimal(0, 9, 1, true);
        Assert.assertEquals(new Decimal(0, 104, 3, true), d1.subtract(d2));
    }

    @Test
    public void testDecimalToLong() {
        final ModuloCalculator moduloCalculator = new ModuloCalculator(17);
        final Decimal d = new Decimal(105, 678, 4, false);
        Assert.assertEquals(6, d.getLong(moduloCalculator));
    }

    @Test
    public void testComparisonOnSign() {
        // compare on Sign
        testInequality(1, 1, 3, 10, 10, 2, false);

        // compare on int part
        testInequality(10, 1, 3, 1, 10, 2, true);

        // compare on fraction part of different frac len
        testInequality(10, 1, 2, 10, 99, 4, true);

        // compare on fraction part of equal frac len
        testInequality(10, 99, 2, 10, 1, 2, true);
    }

    @Test
    public void testEquality() {
        final Decimal d1 = new Decimal(10, 99, 2, true);
        final Decimal d2 = new Decimal(10, 99, 2, true);
        Assert.assertEquals(0, d1.compareTo(d2));
        Assert.assertEquals(d1, d2);
    }

    @Test
    public void testEqualityOfZeroes() {
        final Decimal d1 = new Decimal(0, 0, 2, true);
        final Decimal d2 = new Decimal(0, 0, 3, false);
        Assert.assertEquals(0, d1.compareTo(d2));
        Assert.assertEquals(d1, d2);
    }


    private void testInequality(final int int1,
                                final int frac1,
                                final int fracLen1,
                                final int int2,
                                final int frac2,
                                final int fracLen2,
                                final boolean sign2) {
        final Decimal d1 = new Decimal(int1, frac1, fracLen1, true);
        final Decimal d2 = new Decimal(int2, frac2, fracLen2, sign2);
        Assert.assertTrue(d1.compareTo(d2) > 0);
        Assert.assertTrue(d2.compareTo(d1) < 0);
        Assert.assertTrue(d1.invert().compareTo(d2.invert()) < 0);
        Assert.assertTrue(d2.invert().compareTo(d1.invert()) > 0);
        Assert.assertNotEquals(d1, d2);
    }

}