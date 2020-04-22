package math;

public class ModuloCalculator {
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
        assert base > 0;
        final long x = normalize(y);
        assert x > 0;
        final BezoutRepr bezoutRepr = MathUtils.getBezoutRepr(x, base);
        assert bezoutRepr.getGcd() == 1;
        return normalize(bezoutRepr.getCoeffSmall());
    }

    public long add(final long x, final long y) {
        return normalize(normalize(x) + normalize(y));
    }

    public long subtract(final long x, final long y) {
        return add(x, -y);
    }

    public long multiply(final long x, final long y) {
        return normalize(normalize(x) * normalize(y));
    }

    public long getExactQuotient(final long x, final long y) {
        if (base != NO_BASE) {
            return multiply(x, getInverse(y));
        }
        assert x % y == 0;
        return x / y;
    }

    public long power(final long _base, final int pow) {
        return _power(normalize(_base), pow);
    }

    private long _power(final long _base, final long pow) {
        if (pow == 0) {
            return 1;
        }
        assert pow > 0;
        long partialResult = _power(_base, pow / 2);
        partialResult = normalize(partialResult * partialResult);
        if (pow % 2 == 1) {
            partialResult = normalize(partialResult * _base);
        }
        return partialResult;
    }

    public long decimalToLong(final long preDecimal, final long postDecimal, final int powerOfTen) {
        return normalize(preDecimal + normalize(postDecimal * getInverse(power(10, powerOfTen))));
    }

    /**
     * @return a value b/w 0 <= ret < b
     */
    public long normalize(final long y) {
        if (base == 0) {
            return y;
        }
        long x = y % base;
        if (x < 0) {
            x = base + x;
        }
        return x;
    }
}
