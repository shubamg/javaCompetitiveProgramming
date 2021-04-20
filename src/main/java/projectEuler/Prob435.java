package projectEuler;

import math.MathUtils;
import math.MatrixCalculator;
import math.ModuloCalculator;

public class Prob435 {
    private static final long[][] FIBONACCI_MATRIX = {{1, 1}, {1, 0}};
    private final ModuloCalculator moduloCalculator;
    private final long modBase;

    private Prob435(final ModuloCalculator moduloCalculator, final long modBase) {
        this.moduloCalculator = moduloCalculator;
        this.modBase = modBase;
    }

    public static void main(final String[] args) {
        final long modBase = 1_307_674_368_000L;
        final ModuloCalculator moduloCalculator = new ModuloCalculator(modBase);
        final MatrixCalculator matrixCalculator = new MatrixCalculator(moduloCalculator);
        final Prob435 solver = new Prob435(moduloCalculator, modBase);
        System.out.println(solver.solve(1_000_000_000_000_000L, 100));
    }

    private long solve(final long n, final long xMax) {
        long sum = 0L;
        for (int x = 1; x <= xMax; x++) {
            final long D = (long) x * x + x - 1;
            final long commonFactor = commonFactor(D);
            final long D_ = D / commonFactor;
            final long N_ = getN(n, x, commonFactor) / commonFactor;
            System.out.printf("x=%d, commonFactor=%d, D=%d, N'=%d, D'=%d\n", x, commonFactor, D, N_, D_);
            sum = moduloCalculator.add(sum, moduloCalculator.getExactQuotient(N_, D_));
        }
        return sum;
    }

    private long commonFactor(final long D) {
        long D_ = D;
        long factor = 1;
        long gcd = MathUtils.getBezoutRepr(D_, modBase).getGcd();
        while (gcd > 1) {
            factor *= gcd;
            D_ /= gcd;
            gcd = MathUtils.getBezoutRepr(D_, modBase).getGcd();
        }
        return factor;
    }

    private long getN(final long n, final int x, final long multiplier) {
        // There might be a cache for optimization as multiplier are very few in number
        final ModuloCalculator moduloCalculator = new ModuloCalculator(modBase * multiplier);
        final MatrixCalculator matrixCalculator = new MatrixCalculator(moduloCalculator);
        final long[][] fibonacciNums = matrixCalculator.power(FIBONACCI_MATRIX, n);
        final long f_nPlus1 = fibonacciNums[0][0];
        final long f_n = fibonacciNums[0][1];
        final long powOfX = moduloCalculator.power(x, n);
        final long A = moduloCalculator.add(moduloCalculator.multiply(x, f_n), f_nPlus1);
        final long B = moduloCalculator.subtract(moduloCalculator.multiply(powOfX, A), 1L);
        return moduloCalculator.multiply(x, B);
    }
}
