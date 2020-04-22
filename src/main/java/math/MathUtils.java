package math;

import util.Decimal;

public class MathUtils {
    /**
     * Assumes x <= y
     * @param smaller - >= 0
     * @param larger  - >= x
     */
    public static BezoutRepr getBezoutRepr(final long smaller, final long larger) {
        assert smaller >= 0;
        assert smaller <= larger;
        if (smaller == 0) {
            return new BezoutRepr(1, 1, larger, 1);
        }
        final long r = larger % smaller;
        final long q = larger / smaller;
        // gcd(x, y): y = q1*x + r1 => r1 = y - q1*x
        // a * smaller(r1) + b * larger(x) = gcd
        // => a * (y - q1*x) + b * x = gcd
        // => (b - a * q1) * x + a * y = gcd
        // => a' = b - a * q1
        // => b' = a
        // gcd(r, x): x = q2*r + r2 => gcd = r2 = x - q2*r1 (1, -q2)
        final BezoutRepr child = getBezoutRepr(r, smaller);
        long smallCoeff = child.getCoeffLarge() - child.getCoeffSmall() * q;
        final long gcd = child.getGcd();
        return new BezoutRepr(smallCoeff, child.getCoeffSmall(), gcd, child.getNumCalls() + 1);
    }

    // Assumes first character is not decimal or - (for -ve number)
    public static Decimal getDecimal(final String decimalNum, final ModuloCalculator calculator) {
        assert decimalNum.length() > 0 && decimalNum.charAt(0) != '.' && decimalNum.charAt(0) != '-';
        final String[] split = decimalNum.split("\\.");
        if (split.length == 1) {
            return Decimal.getInteger(Long.parseLong(split[0]), true);
        }
        return new Decimal(Long.parseLong(split[0]), Long.parseLong(split[1]), split[1].length(), true);
    }
}
