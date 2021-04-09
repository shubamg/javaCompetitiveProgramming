package projectEuler;

import math.ModuloCalculator;

public class Prob602 {
    private final ModuloCalculator moduloCalculator;

    private Prob602(final ModuloCalculator moduloCalculator) {
        this.moduloCalculator = moduloCalculator;
    }

    public static void main(String[] args) {
        final long MOD_BASE = 1_000_000_007L;
        final ModuloCalculator moduloCalculator = new ModuloCalculator(MOD_BASE);
        final Prob602 solver = new Prob602(moduloCalculator);
        System.out.println(solver.solve(100, 40));
        System.out.println(solver.solve(10_000_000, 4_000_000));
    }

    private long solve(final int n, final int k_) {
        long res = moduloCalculator.power(k_, n);
        long nCj = 1L;
        for (int j = 1; j < k_; j++) {
            nCj = moduloCalculator.multiply(nCj, moduloCalculator.getExactQuotient(n + 2 - j, j));
            final long pow = moduloCalculator.power(k_ - j, n);
            final long addendum = moduloCalculator.multiply(nCj, pow);
            if ((j & 1) != 0) {
                res = moduloCalculator.subtract(res, addendum);
            } else {
                res = moduloCalculator.add(res, addendum);
            }
        }
        return res;
    }
}
