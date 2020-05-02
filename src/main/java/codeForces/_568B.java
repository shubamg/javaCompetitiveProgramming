package codeForces;

import io.InputReader;
import math.BinomialCoefficientsForN;
import math.ModuloCalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class _568B {
    private final int N;
    // dp[i][j] is number of equivalence relations of set [j] with i cliques (i <= j)
    private final long[][] dp;
    private final ModuloCalculator modularizer;
    private final BinomialCoefficientsForN biCoeffs;

    private _568B(final int n, final ModuloCalculator modularizer) {
        this.N = n;
        this.modularizer = modularizer;
        this.biCoeffs = new BinomialCoefficientsForN(N, modularizer);
        this.dp = new long[N][N];
    }

    private long solve() {
        populateDp();
        long result = 0;
        for (int i = 0; i < N; i++) {
            long dp_sum = 0;
            for (int k = 0; k <= i; k++) {
                dp_sum = modularizer.add(dp_sum, dp[k][i]);
            }
            result = modularizer.add(result, modularizer.multiply(dp_sum, biCoeffs.getCoefficient(i)));
        }
        return result;
    }

    private void populateDp() {
        dp[0][0] = 1;
        // dp[i][j] = dp[i-1][j-1] + i * dp[i][j-1]
        for (int j = 1; j < N; j++) {
            for (int i = 1; i <= j; i++) {
                dp[i][j] = modularizer.add(dp[i - 1][j - 1], modularizer.multiply(i, dp[i][j - 1]));
            }
        }
    }

    public static void main(String[] args) {
        final long BASE = 1_000_000_007L;
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int n = reader.nextInt();
        final ModuloCalculator moduloCalculator = new ModuloCalculator(BASE);
        final _568B solver = new _568B(n, moduloCalculator);
        System.out.println(solver.solve());
    }
}
