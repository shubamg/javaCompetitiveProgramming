package hackerrank;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class CountWays1 {
    private final int commonMultiple;

    private CountWays1(final int commonMultiple) {
        this.commonMultiple = commonMultiple;
    }

    public static int countWays(List<Integer> arr, long l, long r) {
        // Write your code here

    }

    private int getCommonMultiple(final List<Integer> arr) {
        assert arr.size() > 0;
        return new HashSet<>(arr).stream().mapToInt(e -> e).reduce((a, b) -> a * b).getAsInt();
    }
}
