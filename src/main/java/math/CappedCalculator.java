package math;

/**
 * Created by Shubham Gupta
 * on 16 Apr 2022.
 */
public class CappedCalculator implements Calculator {

    @Override
    public long add(final long x, final long y) {
        assert x >= 0;
        assert y >= 0;
        try {
            return Math.addExact(x, y);
        } catch (final ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

    @Override
    public long multiply(final long x, final long y) {
        assert x >= 0;
        assert y >= 0;
        try {
            return Math.multiplyExact(x, y);
        } catch (final ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }
}

