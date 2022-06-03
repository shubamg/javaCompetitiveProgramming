package hackerrank;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class CountWays1 {
    private final int L;

    private CountWays1(final int l) {
        L = l;
    }

    public static int countWays(List<Integer> arr, long l, long r) {
        // Write your code here

    }

    private int getL(final List<Integer> arr) {
        assert arr.size() > 0;
        return new HashSet<>(arr).stream().mapToInt(e -> e).reduce((a, b) -> a * b).getAsInt();
    }
}
