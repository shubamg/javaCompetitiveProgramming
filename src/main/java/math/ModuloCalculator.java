package math;

import java.math.BigInteger;

public class ModuloCalculator implements Calculator {
    private final long base;
    private static final long NO_BASE = 0;

    public ModuloCalculator(final long base) {
        assert base >= 0;
        this.base = base;
    }

    public static ModuloCalculator getWithoutMod() {
        return new ModuloCalculator(NO_BASE);
    }

    public long getInverse(final long y) {
        assert base != NO_BASE;
        final long x = normalize(y);
        assert x > 0;
        final BezoutRepr bezoutRepr = MathUtils.getBezoutRepr(x, base);
        assert bezoutRepr.getGcd() == 1;
        return normalize(bezoutRepr.getCoeffSmall());
    }

    @Override
    public long add(final long x, final long y) {
        final long normalizedX = normalize(x);
        final long normalizedY = normalize(y);
        try {
            final long result = Math.addExact(normalizedX, normalizedY);
            return normalize(result);
        } catch (final ArithmeticException e) {
            return addWithBigInteger(normalizedX, normalizedY);
        }
    }

    public long subtract(final long x, final long y) {
        return add(x, -y);
    }

    @Override
    public long multiply(final long x, final long y) {
        final long normalizedX = normalize(x);
        final long normalizedY = normalize(y);
        try {
            final long result = Math.multiplyExact(normalizedX, normalizedY);
            return normalize(result);
        } catch (final ArithmeticException e) {
            return multiplyWithBigInteger(normalizedX, normalizedY);
        }
    }

    public long getExactQuotient(final long x, final long y) {
        if (base != NO_BASE) {
            return multiply(x, getInverse(y));
        }
        assert x % y == 0;
        return x / y;
    }

    public long power(final long _base, final long pow) {
        return _power(normalize(_base), pow);
    }

    private long _power(final long _base, final long pow) {
        if (pow == 0) {
            return 1;
        }
        assert pow > 0;
        long partialResult = _power(_base, pow / 2);
        partialResult = multiply(partialResult, partialResult);
        if (pow % 2 == 1) {
            partialResult = multiply(partialResult, _base);
        }
        return partialResult;
    }

    private long addWithBigInteger(final long normalizedX, final long normalizedY) {
        final BigInteger bigX = BigInteger.valueOf(normalizedX);
        final BigInteger bigY = BigInteger.valueOf(normalizedY);
        return normalize(bigX.add(bigY).remainder(BigInteger.valueOf(base)).longValueExact());
    }

    private long multiplyWithBigInteger(final long normalizedX, final long normalizedY) {
        final BigInteger bigX = BigInteger.valueOf(normalizedX);
        final BigInteger bigY = BigInteger.valueOf(normalizedY);
        return normalize(bigX.multiply(bigY).remainder(BigInteger.valueOf(base)).longValueExact());
    }

    /**
     * @return a value b/w 0 <= ret < b
     */
    public long normalize(final long y) {
        if (base == NO_BASE) {
            return y;
        }
        long x = y % base;
        if (x < 0) {
            x = base + x;
        }
        return x;
    }
}
