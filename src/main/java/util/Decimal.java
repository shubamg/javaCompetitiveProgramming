package util;

import math.ModuloCalculator;

import java.util.Objects;

public class Decimal implements Comparable<Decimal> {
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
        this.integerPart = integerPart;

        // trim fractional Part
        int fracLenDecrement = 0;
        long fracPart = _fractionalPart;
        while (fracPart % 10 == 0 && fracLenDecrement < fractionalLength) {
            fracPart /= 10;
            fracLenDecrement++;
        }
        this.fractionalPart = fracPart;
        this.fractionalLength = fractionalLength - fracLenDecrement;

        this.sign = (short) ((isNonNegative || (integerPart == 0 && this.fractionalLength == 0)) ? 1 : -1);
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

        // make fractional part have same sign as the int part
        if (intPart * resFrac < 0) {
            resFrac += ((resFrac < 0) ? powOf10 : -powOf10);
            intPart += ((resFrac < 0) ? 1 : -1);
        }

        return new Decimal(Math.abs(intPart), Math.abs(resFrac), resFracLen, intPart >= 0 && resFrac >= 0);
    }

    public long getLong(final ModuloCalculator moduloCalculator) {
        return moduloCalculator.normalize(this.getSign() * this.integerPart + moduloCalculator.normalize(
                this.getSign() * this.fractionalPart * moduloCalculator.getInverse(moduloCalculator.power(10,
                                                                                                          this.fractionalLength))));
    }

    public Decimal subtract(final Decimal that) {
        return add(that.invert());
    }

    @Override
    public String toString() {
        String str = "Decimal{" + (sign > 0 ? "" : "-") + integerPart;
        if (fractionalLength > 0) {
            str += ".";
            str += String.format("%0" + fractionalLength + "d", fractionalPart);
        }
        return str + '}';
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

    @Override
    public int compareTo(final Decimal that) {
        if (this.getSign() < 0 && that.getSign() > 0) {
            return -1;
        }
        if (this.getSign() > 0 && that.getSign() < 0) {
            return 1;
        }
        // both have same signs
        final int sign = this.getSign();
        if (this.getIntegerPart() > that.getIntegerPart()) {
            return sign;
        }
        if (this.getIntegerPart() < that.getIntegerPart()) {
            return -sign;
        }
        final int resFracLen = Math.max(this.getFractionalLength(), that.getFractionalLength());
        final long powThis = noModCalculator.power(10, resFracLen - this.getFractionalLength());
        final long powThat = noModCalculator.power(10, resFracLen - that.getFractionalLength());
        final long frac1 = powThis * this.getFractionalPart();
        final long frac2 = powThat * that.getFractionalPart();
        if (frac1 > frac2) {
            return sign;
        }
        if (frac1 < frac2) {
            return -sign;
        }
        return 0;
    }
}
