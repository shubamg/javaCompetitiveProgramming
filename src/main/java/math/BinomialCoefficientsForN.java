package math;

/**
 * The time complexity of this class is O(NlgN) if mod is involved, and O(N) if mod is not involved.
 * The log N factor in case of mod comes from inverses
 * Space complexity is O(N) in both cases
 */
public class BinomialCoefficientsForN {
    private final int N;
    private final long[] coeffs;
    private final ModuloCalculator mod;

    public BinomialCoefficientsForN(final int N, final ModuloCalculator mod) {
        assert N > 0;
        this.N = N;
        this.mod = mod;
        this.coeffs = new long[N / 2 + 1];
        compute();
    }

    public void compute() {
        coeffs[0] = 1;
        for (int i = 1; i <= N / 2; i++) {
            coeffs[i] = mod.getExactQuotient(mod.multiply(coeffs[i - 1], N - i + 1), i);
        }
    }

    public long getCoefficient(final int r) {
        if (r > N / 2) {
            return coeffs[N - r];
        }
        return coeffs[r];
    }
}
