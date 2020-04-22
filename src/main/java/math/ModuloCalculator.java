package math;

public class ModuloCalculator {
    private final long base;
    static final long NO_BASE = 0;

    public ModuloCalculator(final long base) {
        assert base >= 0;
        this.base = base;
    }

    public long getInverse(final long y) {
        assert base > 0;
        final long x = getEquivalenceClass(y);
        assert x > 0;
        final BezoutRepr bezoutRepr = MathUtils.getBezoutRepr(x, base);
        assert bezoutRepr.getGcd() == 1;
        return getEquivalenceClass(bezoutRepr.getCoeffSmall());
    }

    public long add(final long x, final long y) {
        return getEquivalenceClass(getEquivalenceClass(x) + getEquivalenceClass(y));
    }

    public long subtract(final long x, final long y) {
        return add(x, -y);
    }

    public long multiply(final long x, final long y) {
        return getEquivalenceClass(getEquivalenceClass(x) * getEquivalenceClass(y));
    }

    public long getExactQuotient(final long x, final long y) {
        if (base != NO_BASE) {
            return multiply(x, getInverse(y));
        }
        assert x % y == 0;
        return x / y;
    }

    /**
     * @return a value b/w 0 <= ret < b
     */
    public long getEquivalenceClass(final long y) {
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
