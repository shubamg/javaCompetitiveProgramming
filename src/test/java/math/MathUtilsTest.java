package math;

import org.junit.Assert;
import org.junit.Test;
import util.Decimal;

public class MathUtilsTest {

    @Test
    public void testBezoutCases() {
        testBezout(3, 24);
        testBezout(0, 24);
        testBezout(51, 72);
        testBezout(91, 191);
        testBezout(63245986, 102334155);
    }

    private void testBezout(long smaller, final long larger) {
        final String OUT_TMPL = "(%d)*(%d) + (%d)*(%d) = %d in %d calls";
        final BezoutRepr bezoutRepr = MathUtils.getBezoutRepr(smaller, larger);
        System.out.printf((OUT_TMPL) + "%n",
                          bezoutRepr.getCoeffSmall(),
                          smaller,
                          bezoutRepr.getCoeffLarge(),
                          larger,
                          bezoutRepr.getGcd(),
                          bezoutRepr.getNumCalls());
        final long gcd = bezoutRepr.getGcd();
        Assert.assertEquals(gcd, bezoutRepr.getCoeffSmall() * smaller + bezoutRepr.getCoeffLarge() * larger);
        Assert.assertEquals(0, smaller % gcd);
        Assert.assertEquals(0, larger % gcd);
        Assert.assertTrue(gcd > 0);
    }


    @Test
    public void testGetDecimal() {
        final String input = "12345.000678";
        final ModuloCalculator calculator = new ModuloCalculator(17);
        Assert.assertEquals(new Decimal(12345, 678, 6, true), MathUtils.getDecimal(input));
    }
}