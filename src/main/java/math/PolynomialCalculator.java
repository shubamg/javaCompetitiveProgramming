package math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * Assumes polynomial as an array of coefficients
 * Index 0 is the coefficient of the highest degree term
 */
public class PolynomialCalculator {
    private final LongUnaryOperator modularOp;
    private final UnaryOperator<long[]> polyCleanOp;
    private final static long[] ZERO_POLYNOMIAL = new long[0];

    public PolynomialCalculator(final long base) {
        final ModuloCalculator moduloCalculator = new ModuloCalculator(base);
        this.modularOp = moduloCalculator::getEquivalenceClass;
        this.polyCleanOp = UnaryOperator.identity();
    }

    public PolynomialCalculator() {
        this.modularOp = LongUnaryOperator.identity();
        this.polyCleanOp = PolynomialCalculator::cleanLeadingZeroes;
    }

    public long[] add(final long[] _p1, final long[] _p2) {
        final long[] p1 = polyCleanOp.apply(_p1);
        final long[] p2 = polyCleanOp.apply(_p2);
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
            result[largerIndex] = modularOp.applyAsLong(modularOp.applyAsLong(smallerPolynomial[smallerIndex])
                                                                + modularOp.applyAsLong(largerPolynomial[largerIndex]));
        }
        for (int i = 0; i < deltaInLengths; i++) {
            result[i] = modularOp.applyAsLong(largerPolynomial[i]);
        }
        return polyCleanOp.apply(result);
    }

    public long[] multiply(final long[] p, long scalarMultiplier) {
        return multiply(p, new long[]{scalarMultiplier});
    }

    public long[] subtract(final long[] p1, final long[] p2) {
        return add(p1, multiply(p2, -1));
    }

    /**
     * Uses naive way of multiplying. Doesn't use FFT
     *
     * @param _p1
     * @param _p2
     * @return
     */
    public long[] multiply(long[] _p1, long[] _p2) {
        final long[] p1 = polyCleanOp.apply(_p1);
        final long[] p2 = polyCleanOp.apply(_p2);
        for (int i = 0; i < p1.length; i++) {
            p1[i] = modularOp.applyAsLong(p1[i]);
        }
        for (int j = 0; j < p2.length; j++) {
            p2[j] = modularOp.applyAsLong(p2[j]);
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
                result[resultIndex] = modularOp.applyAsLong(result[resultIndex] + modularOp.applyAsLong(p1[i] * p2[j]));
            }
        }
        return polyCleanOp.apply(result);
    }

    public long evaluateAt(final long[] p, final long x) {
        final long[] evalResult = substitute(p, new long[]{x});
        return evalResult.length == 0 ? 0 : evalResult[0];
    }

    /**
     * Includes pow
     *
     * @param base
     * @param pow
     * @return
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
        final long[] parent = polyCleanOp.apply(_parent);
        final long[] child = polyCleanOp.apply(_child);
        long[] result = ZERO_POLYNOMIAL;
        for (int parentIndex = 0; parentIndex < parent.length; parentIndex++) {
            final long[] constantPolynomial = {parent[parentIndex]};
            result = add(multiply(result, child), constantPolynomial);
        }
        return polyCleanOp.apply(result);
    }

    private static long[] cleanLeadingZeroes(final long[] input) {
        int countOfLeadingZeroes = 0;
        for (int i = 0; i < input.length && input[i] == 0; i++) {
            ++countOfLeadingZeroes;
        }
        return countOfLeadingZeroes > 0 ? Arrays.copyOfRange(input, countOfLeadingZeroes, input.length) : input;
    }
}
