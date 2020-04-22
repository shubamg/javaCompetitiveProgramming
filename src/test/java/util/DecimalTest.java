package util;

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
}