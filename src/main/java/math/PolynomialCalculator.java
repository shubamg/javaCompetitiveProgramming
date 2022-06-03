package math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Assumes polynomial as an array of coefficients
 * Index 0 is the coefficient of the highest degree term
 * Might have overflow and underflow issues
 */
public class PolynomialCalculator {
    private final static long[] ZERO_POLYNOMIAL = new long[0];
    private final ModuloCalculator moduloCalculator;

    public PolynomialCalculator(final ModuloCalculator moduloCalculator) {
        this.moduloCalculator = moduloCalculator;
    }

    public long[] add(final long[] _p1, final long[] _p2) {
        final long[] p1 = clean(_p1);
        final long[] p2 = clean(_p2);
        final int numCoeffs;
        final long[] smallerPolynomial;
        final long[] largerPolynomial;
        if (p1.length <= p2.length) {
            numCoeffs = p2.length;
            largerPolynomial = p2;
            smallerPolynomial = p1;
        } else {
            numCoeffs = p1.length;
            smallerPolynomial = p2;
            largerPolynomial = p1;
        }
        final long[] result = new long[numCoeffs];
        final int deltaInLengths = largerPolynomial.length - smallerPolynomial.length;
        for (int smallerIndex = 0; smallerIndex < smallerPolynomial.length; smallerIndex++) {
            final int largerIndex = smallerIndex + deltaInLengths;
            result[largerIndex] = smallerPolynomial[smallerIndex] + largerPolynomial[largerIndex];
        }
        System.arraycopy(largerPolynomial, 0, result, 0, deltaInLengths);
        return clean(result);
    }

    public long[] multiply(final long[] p, long scalarMultiplier) {
        return multiply(p, new long[]{scalarMultiplier});
    }

    public long[] subtract(final long[] p1, final long[] p2) {
        return add(p1, multiply(p2, -1));
    }

    /**
     * Uses naive way of multiplying. Doesn't use FFT
     */
    public long[] multiply(long[] _p1, long[] _p2) {
        final long[] p1 = clean(_p1);
        final long[] p2 = clean(_p2);

        if (isZero(p1) || isZero(p2)) {
            return ZERO_POLYNOMIAL;
        }

        final int degree1 = p1.length - 1;
        final int degree2 = p2.length - 1;
        final int degreeOfResult = degree1 + degree2;
        final long[] result = new long[degreeOfResult + 1];
        for (int i = 0; i <= degree1; i++) {
            final int pow1 = degree1 - i;
            for (int j = 0; j <= degree2; j++) {
                final int pow2 = degree2 - j;
                final int resultingTermPower = pow1 + pow2;
                final int resultIndex = degreeOfResult - resultingTermPower;
                result[resultIndex] = moduloCalculator.normalize(
                        result[resultIndex] + moduloCalculator.normalize(p1[i] * p2[j]));
            }
        }
        return clean(result);
    }

    public boolean isZero(final long[] input) {
        return Arrays.equals(ZERO_POLYNOMIAL, clean(input));
    }

    public long evaluateAt(final long[] p, final long x) {
        final long[] evalResult = substitute(p, new long[]{x});
        return evalResult.length == 0 ? 0 : evalResult[0];
    }

    /**
     * Includes pow
     */
    public List<long[]> getAllPowersTill(final long[] base, final int pow) {
        final List<long[]> result = new ArrayList<>(pow);
        result.add(new long[]{1});
        for (int i = 1; i <= pow; i++) {
            result.add(multiply(result.get(i - 1), base));
        }
        return result;
    }

    public long[] substitute(final long[] _parent, final long[] _child) {
        final long[] parent = clean(_parent);
        final long[] child = clean(_child);
        long[] result = ZERO_POLYNOMIAL;
        for (final long coefficientInParent : parent) {
            final long[] constantPolynomial = {coefficientInParent};
            result = add(multiply(result, child), constantPolynomial);
        }
        return clean(result);
    }

    /**
     * Performs mod operation and cleans leading zeroes
     */
    private long[] clean(final long[] input) {
        int countOfLeadingZeroes = 0;
        for (int i = 0; i < input.length && moduloCalculator.normalize(input[i]) == 0; i++) {
            ++countOfLeadingZeroes;
        }
        if (countOfLeadingZeroes == input.length) {
            return ZERO_POLYNOMIAL;
        }
        final int retLength = input.length - countOfLeadingZeroes;
        final long[] ret = new long[retLength];
        for (int i = countOfLeadingZeroes; i < input.length; i++) {
            ret[i - countOfLeadingZeroes] = moduloCalculator.normalize(input[i]);
        }
        return ret;
    }

    public long[] integrate(long[] integrand, long[] lowerLimit, long[] upperLimit) {
        integrand = clean(integrand);
        lowerLimit = clean(lowerLimit);
        upperLimit = clean(upperLimit);
        if (isZero(integrand)) {
            return ZERO_POLYNOMIAL;
        }
        final long[] indefiniteIntegral = new long[integrand.length + 1];
        final int degreeOfIntegrand = integrand.length - 1;
        for (int i = 0; i < integrand.length; i++) {
            final int degreeOfTermInInResult = degreeOfIntegrand - i + 1;
            indefiniteIntegral[i] = moduloCalculator.getExactQuotient(integrand[i], degreeOfTermInInResult);
        }
        return subtract(substitute(indefiniteIntegral, upperLimit), substitute(indefiniteIntegral, lowerLimit));
    }

    public long[] doLagarangeInterpolation(final long[] x, final long[] y) {
        assert x.length == y.length;
        final int degree = x.length - 1;
        final long[] result = new long[degree + 1];
        for (int i = 0; i < x.length; i++) {
            final int finalI = i;
            extracted(x, y, finalI);
        }

    }

    private void extracted(final long[] x, final long[] y, final int excludedIndex) {
        final IntStream indexes = IntStream.range(0, x.length).filter(j -> j != excludedIndex);
        final long[] numerator = indexes.mapToObj(j -> new long[]{1, -x[j]}).reduce(this::multiply).get();
        final long denominator = indexes.mapToLong(j -> moduloCalculator.subtract(x[excludedIndex], x[j]))
                                        .reduce((a, b) -> moduloCalculator.multiply(a, b))
                                        .getAsLong();
        final long[] polynomial = multiply(multiply(numerator, y[excludedIndex]), moduloCalculator.getInverse(denominator));
    }

    public static long[] getZeroPolynomial() {
        return ZERO_POLYNOMIAL;
    }
}
