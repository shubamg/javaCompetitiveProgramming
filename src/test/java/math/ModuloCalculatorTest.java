package math;

import org.junit.Assert;
import org.junit.Test;

public class ModuloCalculatorTest {

    @Test
    public void testBezoutCases() {
        testBezout(3, 24);
        testBezout(0, 24);
        testBezout(51, 72);
        testBezout(91, 191);
        testBezout(63245986, 102334155);
    }

    private void testBezout(final long smaller, final long larger) {
        final String OUT_TMPL = "(%d)*(%d) + (%d)*(%d) = %d in %d calls";
        final BezoutRepr bezoutRepr = MathUtils.getBezoutRepr(smaller, larger);
        System.out.println(String.format(OUT_TMPL,
                                         bezoutRepr.getCoeffSmall(),
                                         smaller,
                                         bezoutRepr.getCoeffLarge(),
                                         larger,
                                         bezoutRepr.getGcd(),
                                         bezoutRepr.getNumCalls()));
        final long gcd = bezoutRepr.getGcd();
        Assert.assertEquals(gcd, bezoutRepr.getCoeffSmall() * smaller + bezoutRepr.getCoeffLarge() * larger);
        Assert.assertEquals(0, smaller % gcd);
        Assert.assertEquals(0, larger % gcd);
        Assert.assertTrue(gcd > 0);
    }

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
        System.out.println(String.format("inv(%d) (mod %d) = %d", x, base, xInverse));
    }

    /**
     * Inverse doesn't exist as x is multiple of base
     */
    @Test(expected = AssertionError.class)
    public void testGetInverse2() {
        final long base = 40;
        final ModuloCalculator calculator = new ModuloCalculator(base);
        final long x = 42;
        final long xInverse = calculator.getInverse(x);
    }

    @Test
    public void testGetEquivalenceClass() {
        final ModuloCalculator calculator = new ModuloCalculator(17);
        Assert.assertEquals(1, calculator.getEquivalenceClass(18));
        Assert.assertEquals(1, calculator.getEquivalenceClass(1));
        Assert.assertEquals(1, calculator.getEquivalenceClass(35));
        Assert.assertEquals(2, calculator.getEquivalenceClass(172));
        Assert.assertEquals(15, calculator.getEquivalenceClass(-172));
        Assert.assertEquals(16, calculator.getEquivalenceClass(-1));
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
            calculator.getQuotient(13, 0);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        try {
            calculator.getQuotient(0, 17);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        try {
            calculator.getQuotient(-1, 17);
        } catch (final AssertionError ignored) {
            ++exceptionCount;
        }
        Assert.assertEquals(3, exceptionCount);
    }

    private void testGetQuotient(final long actualQuotient, final long y, final long base) {
        final ModuloCalculator calculator = new ModuloCalculator(base);
        Assert.assertEquals(0, (actualQuotient - calculator.getQuotient(actualQuotient * y, y)) % base);
    }
}