package math;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;

public class PrimeSieve {
    private final int n;
    private final IntList allPrimes;
    private final boolean[] isPrime;

    public PrimeSieve(final int n) {
        this.n = n;
        this.allPrimes = new IntArrayList(n + 1);
        this.isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
        computePrimes();
    }

    public IntList getAllPrimes() {
        return allPrimes;
    }

    private void computePrimes() {
        markCompositeNos();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                allPrimes.add(i);
            }
        }
    }

    private void markCompositeNos() {
        for (int divisor = 2; divisor * divisor <= n; divisor++) {
            if (!isPrime[divisor]) {
                continue;
            }
            markAllMultiplesComposite(divisor);
        }
    }

    private void markAllMultiplesComposite(final int divisor) {
        for (int multiple = divisor * divisor; multiple <= n; multiple += divisor) {
            isPrime[multiple] = false;
        }
    }
}
