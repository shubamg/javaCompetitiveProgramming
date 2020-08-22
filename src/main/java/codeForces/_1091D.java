package codeForces;

import io.InputReader;
import math.ModuloCalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class _1091D {
    private final static long MOD_BASE = 998244353L;
    private final int N;
    private final ModuloCalculator moduloCalculator = new ModuloCalculator(MOD_BASE);

    _1091D(final int N) {
        this.N = N;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int N = reader.nextInt();
        final _1091D solver = new _1091D(N);
        System.out.println(solver.solve());
    }

    long solve() {
        long factorial = 1;
        long badCuts = 0;
        for (int i = 2; i <= N; i++) {
            factorial = moduloCalculator.multiply(factorial, i);
            badCuts = moduloCalculator.add(moduloCalculator.multiply(badCuts, i), moduloCalculator.power(i - 1, 2));
        }
        final long totalCuts = moduloCalculator.subtract(moduloCalculator.multiply(N, factorial), N - 1);
        return moduloCalculator.subtract(totalCuts, badCuts);
    }
}
