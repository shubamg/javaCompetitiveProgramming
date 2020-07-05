package leetcode;

import org.junit.Assert;
import org.junit.Test;

public class _1467Test {

    private static final double DELTA = 1e-5;

    @Test
    public void solve1() {
        final _1467 solver = new _1467(new int[]{1, 1});
        Assert.assertEquals(1.00000, solver.solve(), DELTA);
    }

    @Test
    public void solve2() {
        final _1467 solver = new _1467(new int[]{2, 1, 1});
        Assert.assertEquals(0.66667, solver.solve(), DELTA);
    }

    @Test
    public void solve3() {
        final _1467 solver = new _1467(new int[]{1, 2, 1, 2});
        Assert.assertEquals(0.60000, solver.solve(), DELTA);
    }

    @Test
    public void solve4() {
        final _1467 solver = new _1467(new int[]{3, 2, 1});
        Assert.assertEquals(0.30000, solver.solve(), DELTA);
    }

    @Test
    public void solve5() {
        final _1467 solver = new _1467(new int[]{6, 6, 6, 6, 6, 6});
        Assert.assertEquals(0.90327, solver.solve(), DELTA);
    }

    @Test
    public void solve6() {
        final _1467 solver = new _1467(new int[]{6, 6, 6, 6, 6, 6, 6, 6});
        Assert.assertEquals(0.85571, solver.solve(), DELTA);
    }
}