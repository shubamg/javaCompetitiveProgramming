package projectEuler;

import math.BinomialCoefficientsForN;
import math.ModuloCalculator;

public class Prob657 {
    private final BinomialCoefficientsForN binomialCoeffs;
    private final ModuloCalculator calculator;
    private final int m;
    private final long N;

    private Prob657(final BinomialCoefficientsForN binomialCoeffs,
                    final ModuloCalculator calculator,
                    final int m,
                    final long n) {
        this.binomialCoeffs = binomialCoeffs;
        this.calculator = calculator;
        this.m = m;
        N = n;
    }

    public static void main(final String[] args) {
        final int m = 10_000_000;
        final ModuloCalculator calculator = new ModuloCalculator(1_000_000_007L);
        final BinomialCoefficientsForN binomialCalculator = new BinomialCoefficientsForN(m, calculator);
        final Prob657 solver = new Prob657(binomialCalculator, calculator, m, 1_000_000_000_000L);
        System.out.println(solver.solve());
    }

    private long solve() {
        long sum = 0L;
        for (int r = 1; r <= m - 2; r++) {
            final long denominator = m - r - 1L;
            final long numerator = calculator.subtract(calculator.power(m - r, N + 1), 1L);
            final long fraction = calculator.getExactQuotient(numerator, denominator);
            final long term = calculator.multiply(binomialCoeffs.getCoefficient(r), fraction);
            if ((r & 1) == 1) {
                sum = calculator.add(sum, term);
            } else {
                sum = calculator.subtract(sum, term);
            }
        }
        final long correction = calculator.subtract(calculator.multiply(N + 1, m), 1L);
        if ((m & 1) == 0) {
            sum = calculator.add(sum, correction);
        } else {
            sum = calculator.subtract(sum, correction);
        }
        return sum;
    }
}
