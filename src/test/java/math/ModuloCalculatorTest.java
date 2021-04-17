package math;

import org.junit.Assert;
import org.junit.Test;
import util.Decimal;

public class ModuloCalculatorTest {

    @Test
    public void testGetInverse() {
        testGetInverse(71, 172);
        testGetInverse(71, 40);
    }

    private void testGetInverse(final long base, final long x) {
        final ModuloCalculator calculator = new ModuloCalculator(base);
        final long xInverse = calculator.getInverse(x);
        Assert.assertTrue(xInverse > 0);
        Assert.assertTrue(xInverse < base);
        Assert.assertEquals(1, (xInverse * x) % base);
        System.out.printf("inv(%d) (mod %d) = %d%n", x, base, xInverse);
    }

    /**
     * Inverse doesn't exist as x is not co-prime with base
     */
    @Test(expected = AssertionError.class)
    public void testNoInverse() {
        final long base = 40;
        final ModuloCalculator calculator = new ModuloCalculator(base);
        final long x = 42;
        calculator.getInverse(x);
    }

    @Test
    public void testGetEquivalenceClass() {
        final ModuloCalculator calculator = new ModuloCalculator(17);
        Assert.assertEquals(1, calculator.normalize(18));
        Assert.assertEquals(1, calculator.normalize(1));
        Assert.assertEquals(1, calculator.normalize(35));
        Assert.assertEquals(2, calculator.normalize(172));
        Assert.assertEquals(15, calculator.normalize(-172));
        Assert.assertEquals(16, calculator.normalize(-1));
    }

    @Test
    public void testAdd() {
        testAdd(13, 19, 17);
        testAdd(170, 29, 29);
        testAdd(-29, 29, 29);
        testAdd(-57, 29, 28);
        testAdd(57, -57, 5);
        testAdd(1, -57, 5);
        testAdd(0, -57, 5);
    }

    private void testAdd(final long x, final long y, final long base) {
        final ModuloCalculator calculator = new ModuloCalculator(base);
        final long actualSum = x + y;
        Assert.assertEquals(0, (actualSum - calculator.add(x, y)) % base);
    }

    @Test
    public void testMultiply() {
        testMultiply(13, 19, 17);
        testMultiply(170, 29, 29);
        testMultiply(-29, 29, 29);
        testMultiply(-57, 29, 28);
        testMultiply(57, -57, 5);
        testMultiply(1, -57, 5);
        testMultiply(0, -57, 5);
    }

    private void testMultiply(final long x, final long y, final long base) {
        final ModuloCalculator calculator = new ModuloCalculator(base);
        final long actualProduct = x * y;
        Assert.assertEquals(0, (actualProduct - calculator.multiply(x, y)) % base);
    }

    @Test
    public void testGetQuotient() {
        testGetQuotient(2, 13, 17);
        testGetQuotient(-2, 29, 28);
        testGetQuotient(0, -57, 5);
        testGetQuotient(0, 57, 5);
    }

    @Test
    public void testDivideByZero() {
        final ModuloCalculator calculator = new ModuloCalculator(17);
        int exceptionCount = 0;
        try {
            calculator.getExactQuotient(13, 0);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        try {
            calculator.getExactQuotient(0, 17);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        try {
            calculator.getExactQuotient(-1, 17);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        Assert.assertEquals(3, exceptionCount);
    }

    @Test
    public void testPower() {
        final ModuloCalculator calculator = new ModuloCalculator(17);
        Assert.assertEquals(14, calculator.power(-7, 3));
    }

    @Test
    public void testDecimalToLong() {
        final ModuloCalculator calculator = new ModuloCalculator(17);
        Assert.assertEquals(16, new Decimal(12345, 678, 6, true).getLong(calculator));
    }

    private void testGetQuotient(final long actualQuotient, final long y, final long base) {
        final ModuloCalculator calculator = new ModuloCalculator(base);
        Assert.assertEquals(0, (actualQuotient - calculator.getExactQuotient(actualQuotient * y, y)) % base);
    }
}