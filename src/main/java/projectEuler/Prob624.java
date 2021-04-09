package projectEuler;

import math.MatrixCalculator;
import math.ModuloCalculator;

public class Prob624 {
    private static final long[][] FIBONACCI_MATRIX = {{1, 1}, {1, 0}};
    private final ModuloCalculator moduloCalculator;
    private final MatrixCalculator matrixCalculator;

    private Prob624(final ModuloCalculator moduloCalculator, final MatrixCalculator matrixCalculator) {
        this.moduloCalculator = moduloCalculator;
        this.matrixCalculator = matrixCalculator;
    }

    public static void main(final String[] args) {
        final long modBase = 1_000_000_009L;
        final ModuloCalculator moduloCalculator = new ModuloCalculator(modBase);
        final MatrixCalculator matrixCalculator = new MatrixCalculator(moduloCalculator);
        final Prob624 solver = new Prob624(moduloCalculator, matrixCalculator);
        System.out.println(solver.solve(1_000_000_000_000_000_000L));
    }

    private long solve(final long n) {
        final long[][] fibonacciNums = matrixCalculator.power(FIBONACCI_MATRIX, n);
        final long f_n = fibonacciNums[0][1];
        final long f_nPlus1 = fibonacciNums[0][0];
        final long f_nMinus1 = fibonacciNums[1][1];
        final long powOf2 = moduloCalculator.power(2L, n);
        final long powOf4 = moduloCalculator.power(powOf2, 2);
        final int minus1PowN = (n & 1) == 1 ? -1 : 1;
        final long X = moduloCalculator.subtract(moduloCalculator.multiply(f_nMinus1, powOf2), minus1PowN);
        final long Z = moduloCalculator.multiply(powOf2, moduloCalculator.add(f_nMinus1, f_nPlus1));
        final long Y = moduloCalculator.subtract(moduloCalculator.add(powOf4, minus1PowN), Z);
        return moduloCalculator.getExactQuotient(X, Y);
    }
}
