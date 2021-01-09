package projectEuler;

import it.unimi.dsi.fastutil.ints.IntList;
import math.ModuloCalculator;
import math.PrimeSieve;

public class Prob603 {
    private final static char ZERO = '0';
    private final static long TEN = 10L;
    private final ModuloCalculator calculator;
    private final long k;
    private final String number;

    public Prob603(final ModuloCalculator calculator, final long k, final String number) {
        this.calculator = calculator;
        this.k = k;
        this.number = number;
    }

    public static void main(String[] args) {
        final int upperBoundOnPrime = 16441302;  //Based on Approximations for the nth prime number in
        // https://en.wikipedia.org/wiki/Prime_number_theorem
        final int numPrimes = 1_000_000;
        final long k = 1_000_000_000_000L;
        final String number = getNumber(upperBoundOnPrime, numPrimes);
        System.out.printf("Calculated prime concatenation with %d digits.\n", number.length());
        final long MOD_BASE = 1_000_000_007L;
        final ModuloCalculator moduloCalculator = new ModuloCalculator(MOD_BASE);
        final Prob603 solver = new Prob603(moduloCalculator, k, number);
        System.out.println(solver.solve());
    }

    private static String getNumber(final int upperBoundOnPrime, final int numPrimes) {
        final PrimeSieve primeSieve = new PrimeSieve(upperBoundOnPrime);
        final IntList allPrimes = primeSieve.getAllPrimes().subList(0, numPrimes);
        final StringBuilder sb = new StringBuilder();
        for (final int prime : allPrimes) {
            sb.append(prime);
        }
        return sb.toString();
    }

    public long solve() {
        final int l = number.length();
        final long powLOf10 = calculator.power(TEN, l);
        final long powLKOf10 = calculator.power(powLOf10, k);
        final long D = calculator.subtract(powLOf10, 1);
        final long D2 = calculator.power(D, 2);
        final long S4 = getS4(D2);
        final long S2 = getS2(l, D2);
        final long S3 = getS3(D, powLKOf10);
        final long S6 = getS6(l, powLKOf10, D);
        final long S5 = getS5(l, powLOf10, powLKOf10);
        final long S1 = calculator.add(S5, S6);
        long powOf10 = 1;
        long T1 = 0L;
        long T2 = 0L;
        for (int i = 0; i < l; i++) {
            final int digit = number.charAt(l - i - 1) - ZERO;
            powOf10 = calculator.multiply(powOf10, TEN);
            if (digit == 0) {
                continue;
            }
            final long T1Contribution = calculator.add(calculator.multiply(powOf10, S1), calculator.multiply(i, S4));
            T1 = calculator.add(T1, calculator.multiply(digit, T1Contribution));
            final long T2Contribution = calculator.add(S2, calculator.multiply(i, calculator.multiply(powOf10, S3)));
            T2 = calculator.add(T2, calculator.multiply(digit, T2Contribution));
        }
        final long denominator = calculator.multiply(9, D2);
        return calculator.getExactQuotient(calculator.subtract(T1, T2), denominator);
    }

    private long getS5(final int l, final long powLOf10, final long powLKOf10) {
        return calculator.multiply(l, calculator.subtract(powLKOf10, powLOf10));
    }

    private long getS6(final int l, final long powLKOf10, final long D) {
        return calculator.multiply(l, calculator.multiply(calculator.subtract(powLKOf10, k), D));
    }

    private long getS3(final long D, final long powLKOf10) {
        return calculator.multiply(calculator.subtract(powLKOf10, 1), D);
    }

    private long getS2(final int l, final long D2) {
        final long A = calculator.multiply(k, calculator.multiply(l, calculator.add(k, 1)));
        return calculator.multiply(D2, calculator.getExactQuotient(A, 2));
    }

    private long getS4(final long D2) {
        return calculator.multiply(k, D2);
    }
}
