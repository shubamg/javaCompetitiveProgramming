package util;

import math.ModuloCalculator;

import java.util.Objects;

public class Decimal {
    private final long integerPart;
    private final long fractionalPart;
    private final int fractionalLength;
    private final short sign;
    private final static ModuloCalculator noModCalculator = ModuloCalculator.getWithoutMod();

    public Decimal(final long integerPart,
                   final long _fractionalPart,
                   final int fractionalLength,
                   final boolean isNonNegative) {
        checkInvariant(integerPart, _fractionalPart, fractionalLength);
        // make intPart +ve
        int fracLenDecrement = 0;
        long fracPart = _fractionalPart;
        while (fracPart % 10 == 0 && fracLenDecrement < fractionalLength) {
            fracPart /= 10;
            fracLenDecrement++;
        }
        this.sign = (short) (isNonNegative ? 1 : -1);
        this.integerPart = integerPart;
        this.fractionalPart = fracPart;
        this.fractionalLength = fractionalLength - fracLenDecrement;
    }

    public static Decimal getInteger(final long integerPart, final boolean isNonNegative) {
        return new Decimal(integerPart, 0, 0, isNonNegative);
    }

    private void checkInvariant(final long integerPart, final long fractionalPart, final int fractionalLength) {
        assert integerPart >= 0;
        assert fractionalPart >= 0;
        assert fractionalLength >= 0;
        assert fractionalPart == 0 || String.valueOf(fractionalPart).length() <= fractionalLength;
    }

    public long getIntegerPart() {
        return integerPart;
    }

    public long getFractionalPart() {
        return fractionalPart;
    }

    public int getFractionalLength() {
        return fractionalLength;
    }

    public short getSign() {
        return sign;
    }

    private long signedAddition(final long primary, final long secondary) {
        return primary + (primary >= 0 ? secondary : -secondary);
    }

    //VisibleForTesting
    Decimal invert() {
        return new Decimal(getIntegerPart(), getFractionalPart(), getFractionalLength(), getSign() < 0);
    }

    public Decimal add(final Decimal that) {
        // 20.035 - 25.0009 = -4.9659
//        35 * 10^(-3),  9 * 10^(-4)
        // 350 * 10^(-4), 9 * 10^(-4)
        // 341 * 10 ^ -4
        final int resFracLen = Math.max(this.getFractionalLength(), that.getFractionalLength());
        final long powOf10 = noModCalculator.power(10, resFracLen);
        final long powThis = noModCalculator.power(10, resFracLen - this.getFractionalLength());
        final long powThat = noModCalculator.power(10, resFracLen - that.getFractionalLength());
        final long frac1 = this.getSign() * powThis * this.getFractionalPart();
        final long frac2 = that.getSign() * powThat * that.getFractionalPart();
        long resFrac = frac1 + frac2;
        long intPart = this.getSign() * this.getIntegerPart() + that.getSign() * that.getIntegerPart();

        // check for carry
        if (Math.abs(resFrac) >= powOf10) {
            // both frac part must have same sign => both nums have same sign
            intPart += ((resFrac >= 0) ? 1 : -1);
            resFrac -= ((resFrac >= 0) ? powOf10 : -powOf10);
        }

        // make fractional part have same sign as to int part
        if (intPart * resFrac < 0) {
            resFrac += ((resFrac < 0) ? powOf10 : -powOf10);
            intPart += ((resFrac < 0) ? 1 : -1);
        }

        return new Decimal(Math.abs(intPart), Math.abs(resFrac), resFracLen, intPart >= 0 && resFrac >= 0);
    }

    public Decimal subtract(final Decimal that) {
        return add(that.invert());
    }

    @Override
    public String toString() {
        return "Decimal{" + "integerPart=" + integerPart + ", fractionalPart=" + fractionalPart + ", fractionalLength="
                + fractionalLength + ", sign=" + sign + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Decimal decimal = (Decimal) o;
        return integerPart == decimal.integerPart && fractionalPart == decimal.fractionalPart
                && fractionalLength == decimal.fractionalLength && sign == decimal.sign;
    }

    @Override
    public int hashCode() {
        return Objects.hash(integerPart, fractionalPart, fractionalLength, sign);
    }
}
