package math;

import org.junit.Assert;
import org.junit.Test;

public class BinomialCoefficientsForNTest {

    @Test
    public void test() {
        final BinomialCoefficientsForN biCalculator = new BinomialCoefficientsForN(5, ModuloCalculator.getWithoutMod());
        final long[] expectedResult = {1, 5, 10, 10, 5, 1};
        for (int i = 0; i <= 5; i++) {
            Assert.assertEquals(expectedResult[i], biCalculator.getCoefficient(i));
        }
    }

    @Test
    public void testWithMod() {
        final BinomialCoefficientsForN biCalculator = new BinomialCoefficientsForN(5, new ModuloCalculator(7));
        final long[] expectedResult = {1, 5, 3, 3, 5, 1};
        for (int i = 0; i <= 5; i++) {
            Assert.assertEquals(expectedResult[i], biCalculator.getCoefficient(i));
        }
    }

}