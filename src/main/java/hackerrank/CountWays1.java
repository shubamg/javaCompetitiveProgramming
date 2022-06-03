package hackerrank;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class CountWays1 {
    private final int commonMultiple;
    private final List<Integer> arr;
    private final long l;
    private final long r;

    private CountWays1(final List<Integer> arr, final int commonMultiple, final long l, final long r) {
        this.commonMultiple = commonMultiple;
        this.arr = arr;
        this.l = l;
        this.r = r;
    }

    public static int countWays(List<Integer> arr, long l, long r) {
        final CountWays1 solver = new CountWays1(arr, getCommonMultiple(arr), l, r);

    }

    private static int getCommonMultiple(final List<Integer> arr) {
        assert arr.size() > 0;
        return new HashSet<>(arr).stream().mapToInt(e -> e).reduce((a, b) -> a * b).getAsInt();
    }
}
