package math;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongUnaryOperator;

/**
 * Assumes polynomial as an array of coefficients
 * Left most is the coefficient of the highest degree term
 */
public class PolynomialCalculator {
    private final LongUnaryOperator modularOp;

    public PolynomialCalculator(final long base) {
        final ModuloCalculator moduloCalculator = new ModuloCalculator(base);
        modularOp = moduloCalculator::getEquivalenceClass;
    }

    public PolynomialCalculator() {
        this.modularOp = LongUnaryOperator.identity();
    }

    public long[] add(final long[] p1, final long[] p2) {
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
        // to iterate from right side
        for (int smallerIndex = 0; smallerIndex < smallerPolynomial.length; smallerIndex++) {
            final int largerIndex = smallerIndex + deltaInLengths;
            result[largerIndex] = modularOp.applyAsLong(modularOp.applyAsLong(smallerPolynomial[smallerIndex])
                                                                + modularOp.applyAsLong(largerPolynomial[largerIndex]));
        }
        for (int i = 0; i < deltaInLengths; i++) {
            result[i] = modularOp.applyAsLong(largerPolynomial[i]);
        }
        return result;
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
    public long[] multiply(final long[] _p1, final long[] _p2) {
        final int degree1 = _p1.length - 1;
        final int degree2 = _p2.length - 1;
        final int degreeOfResult = degree1 + degree2;
        final long[] result = new long[degreeOfResult + 1];
        final long[] p1 = new long[_p1.length];
        final long[] p2 = new long[_p2.length];
        for (int i = 0; i < p1.length; i++) {
            p1[i] = modularOp.applyAsLong(_p1[i]);
        }
        for (int j = 0; j < p2.length; j++) {
            p2[j] = modularOp.applyAsLong(_p2[j]);
        }

        for (int i = 0; i <= degree1; i++) {
            final int pow1 = degree1 - i;
            for (int j = 0; j <= degree2; j++) {
                final int pow2 = degree2 - j;
                final int resultingTermPower = pow1 + pow2;
                final int resultIndex = degreeOfResult - resultingTermPower;
                result[resultIndex] = modularOp.applyAsLong(result[resultIndex] + modularOp.applyAsLong(p1[i] * p2[j]));
            }
        }
        return result;
    }

    public long evaluateAt(final long[] p, final long x) {
        return substitute(p, new long[]{1})[0];
    }

    /**
     * Includes pow
     *
     * @param base
     * @param pow
     * @return
     */
    private List<long[]> getAllPowersTill(final long[] base, final int pow) {
        final List<long[]> result = new ArrayList<>(pow);
        result.add(new long[]{1});
        for (int i = 1; i <= pow; i++) {
            result.add(multiply(result.get(i - 1), base));
        }
        return result;
    }

    public long[] substitute(final long[] parent, final long[] child) {
        final int degreeOfParent = parent.length - 1;
        final int degreeOfChild = child.length - 1;
        final List<long[]> powersOfChild = getAllPowersTill(child, degreeOfParent);
        long[] result = new long[degreeOfChild * degreeOfParent + 1];
        for (int parentIndex = 0; parentIndex < parent.length; parentIndex++) {
            final int powerOfTerm = degreeOfParent - parentIndex;
            final long[] toAdd = multiply(powersOfChild.get(powerOfTerm), parent[parentIndex]);
            result = add(result, toAdd);
        }
        return result;
    }
}
