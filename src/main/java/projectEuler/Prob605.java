package projectEuler;

import math.ModuloCalculator;

public class Prob605 {
    private static final long TEN_POW_8 = 100_000_000; // 10 ^ 8
    private static final long TEN_POW_4 = 10_000;

    public static void main(final String[] args) {
        final ModuloCalculator moduloCalculator = new ModuloCalculator(TEN_POW_8);
        final long A = TEN_POW_8 - TEN_POW_4;
        System.out.println(A);
        final long B = moduloCalculator.power(2, A);
        System.out.println(B);
        final long C = moduloCalculator.subtract(moduloCalculator.power(2, TEN_POW_8 + 7L), 1L);
        System.out.println(C);
        final long D = moduloCalculator.multiply(TEN_POW_4 + 6, C);
        System.out.println(D);
        final long E = moduloCalculator.add(TEN_POW_8 + 7, D);
        System.out.println(E);
        final long F = moduloCalculator.multiply(B, E);
        System.out.println(F);
        final long G = moduloCalculator.multiply(C, C);
        System.out.println(G);
        final long result = moduloCalculator.multiply(F, G);
        System.out.println(result);


        final ModuloCalculator moduloCalculator2 = new ModuloCalculator(TEN_POW_8 + 7);
        System.out.println(moduloCalculator2.power(2, TEN_POW_8 + 7));
    }
}
