package math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PolynomialCalculatorTest {

    @Test
    public void substitute() {
        final long[] parentPolynomial = new long[] {4, 1, 10}; // 4x^2 + 1x + 10
        final long[] childPolynomial = new long[] {3, -1}; //3x - 1
        //   4*(3x-1)^2 + (3x-1) + 10
        // = 4*(9x^2 - 6x + 1) + 3x + 9
        // = 36x^2 - 21x + 13
        final PolynomialCalculator polynomialCalculator = new PolynomialCalculator();
        final long[] actualResult = polynomialCalculator.substitute(parentPolynomial, childPolynomial);
        final long[] expectedResult = new long[] {36, -21, 13};
        Assert.assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void multiply() {
        final long[] parentPolynomial = new long[] {3, -4, 1, 10}; // 3x^3 -4x^2 + 1x + 10
        final long[] childPolynomial = new long[] {2, -3, 1}; //2x^2 - 3x + 1
        /**
         * 6x^5  - 9x^4 + 3x^3 -8x^4 + 12x^3 -4x^2 + 2x^3 -3x^2 + 1x + 20x^2 - 30x + 10
         * = 6x^5 - 17x^4 + 17x^3 + 13x^2 - 29x + 10
         */
        {
            final PolynomialCalculator polynomialCalculator = new PolynomialCalculator();
            final long[] actualResult = polynomialCalculator.multiply(parentPolynomial, childPolynomial);
            final long[] expectedResult = new long[]{6, -17, 17, 13, -29, 10};
            Assert.assertArrayEquals(expectedResult, actualResult);
        }
        // test with mod
        {
            final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(7);
            final long[] actualResult = polynomialCalculator.multiply(parentPolynomial, childPolynomial);
            final long[] expectedResult = new long[]{6, 4, 3, 6, 6, 3};
            Assert.assertArrayEquals(expectedResult, actualResult);
        }
    }
}