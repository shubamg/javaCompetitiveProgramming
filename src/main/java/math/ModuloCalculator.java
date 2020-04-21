package math;

public class ModuloCalculator {
    private final long base;

    public ModuloCalculator(final long base) {
        assert base > 0;
        this.base = base;
    }

    public long getInverse(final long y) {
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

    public long getQuotient(final long x, final long y) {
        assert  y != 0;
        assert x % y == 0;
        return multiply(x, getInverse(y));
    }

    /**
     * @param y
     * @return a value b/w 0 <= ret < b
     */
    public long getEquivalenceClass(final long y) {
        long x = y % base;
        if (x < 0) {
            x = base + x;
        }
        return x;
    }
}
