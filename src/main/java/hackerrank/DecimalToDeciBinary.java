package hackerrank;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubham Gupta
 * on 28 Jan 2023.
 */
public class DecimalToDeciBinary {

    private final long deciBinariesNeeded;
    private final Map<Integer, Long> decimal2CountOfDeciB;
    private long totalGenerated = 0;

    DecimalToDeciBinary(final long deciBinariesNeeded) {
        this.deciBinariesNeeded = deciBinariesNeeded;
        decimal2CountOfDeciB = new HashMap<>();
        initBaseCase();
        generateDeciBinaries();
    }

    private void initBaseCase() {
        decimal2CountOfDeciB.put(0, 1L);
        totalGenerated++;
    }

    private long countDeciBinariesFor(final int deci) {
        long ret = 0;
        final int mod2 = deci % 2;
        for (int unitDigit = mod2; unitDigit <= Math.min(9, deci); unitDigit += 2) {
            ret += decimal2CountOfDeciB.get((deci - unitDigit) / 2);
        }
        return ret;
    }

    private void generateDeciBinaries() {
        int currDeci = 0;
        while (totalGenerated < deciBinariesNeeded) {
            final long deciBCountForCurrDeci = countDeciBinariesFor(currDeci);
            decimal2CountOfDeciB.put(currDeci, deciBCountForCurrDeci);
            totalGenerated += deciBCountForCurrDeci;
            currDeci++;
        }
    }

    public static void main(final String[] args) {
        final long totalNeeded = 10_000_000_000_000_000L;
        final long startNanos = System.nanoTime();
        final DecimalToDeciBinary counter = new DecimalToDeciBinary(totalNeeded);
        final long durationNanos = System.nanoTime() - startNanos;
        for (int i = 0; counter.decimal2CountOfDeciB.containsKey(i); i++) {
            System.out.printf("%d:%d%n", i, counter.decimal2CountOfDeciB.get(i));
        }
        System.out.printf("Time taken = %d nanos%n", durationNanos);
    }

    public long getCountFor(final Key key) {
        return -1;
    }

    static class Key {
        public Key(final int decimal, final int numDigits) {
        }
    }
}

