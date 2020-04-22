package math;

import org.junit.Assert;
import org.junit.Test;

public class PolynomialCalculatorTest {

    @Test
    public void substitute() {
        final long[] parentPolynomial = new long[]{4, 1, 10}; // 4x^2 + 1x + 10
        final long[] childPolynomial = new long[]{3, -1}; //3x - 1
        //   4*(3x-1)^2 + (3x-1) + 10
        // = 4*(9x^2 - 6x + 1) + 3x + 9
        // = 36x^2 - 21x + 13
        final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(new ModuloCalculator(0));
        final long[] actualResult = polynomialCalculator.substitute(parentPolynomial, childPolynomial);
        final long[] expectedResult = new long[]{36, -21, 13};
        Assert.assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void multiply() {
        final long[] parentPolynomial = new long[]{3, -4, 1, 10}; // 3x^3 -4x^2 + 1x + 10
        final long[] childPolynomial = new long[]{2, -3, 1}; //2x^2 - 3x + 1
        /*
          6x^5  - 9x^4 + 3x^3 -8x^4 + 12x^3 -4x^2 + 2x^3 -3x^2 + 1x + 20x^2 - 30x + 10
          = 6x^5 - 17x^4 + 17x^3 + 13x^2 - 29x + 10
         */
        {
            final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(new ModuloCalculator(0));
            final long[] actualResult = polynomialCalculator.multiply(parentPolynomial, childPolynomial);
            final long[] expectedResult = new long[]{6, -17, 17, 13, -29, 10};
            Assert.assertArrayEquals(expectedResult, actualResult);
        }
        // test with mod
        {
            final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(new ModuloCalculator(6));
            final long[] actualResult = polynomialCalculator.multiply(parentPolynomial, childPolynomial);
            final long[] expectedResult = new long[]{1, 5, 1, 1, 4}; // leading zero not removed because of mod
            Assert.assertArrayEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void integrate() {
        final long[] integrand = {3, 0, 2};
        final long base = 7;
        final long[] lowerLimit = {1, -1};
        final long[] upperLimit = {1, 0};
        /*
          3x^2 + 2
          indefiniteIntegral: f = x^3 + 2x
          upperLimit = x
          lower limit = x - 1
          f(upperLimit) =                                                  x^3        + 2x
          f(lowerLimit) = (x-1)^3 + 2(x-1) = x^3- 3x^2 + 3x - 1 + 2x - 2 = x^3 - 3x^2 + 5x - 3
          definite Integral =                                              0   + 3x^2 - 3x + 3
          In mod 7: 3x^2 + 4x + 3
         */

        final PolynomialCalculator calculator = new PolynomialCalculator(new ModuloCalculator(base));
        final long[] actualResult = calculator.integrate(integrand, lowerLimit, upperLimit);
        Assert.assertArrayEquals(new long[]{3, 4, 3}, actualResult);
    }
}