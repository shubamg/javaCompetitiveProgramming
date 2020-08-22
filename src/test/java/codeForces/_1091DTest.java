package codeForces;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class _1091DTest {
    @Test
    public void solveFor2() {
        final _1091D solver = new _1091D(2);
        MatcherAssert.assertThat(solver.solve(), is(equalTo(2L)));
    }

    @Test
    public void solveFor3() {
        final _1091D solver = new _1091D(3);
        MatcherAssert.assertThat(solver.solve(), is(equalTo(9L)));
    }

    @Test
    public void solveFor4() {
        final _1091D solver = new _1091D(4);
        MatcherAssert.assertThat(solver.solve(), is(equalTo(56L)));
    }

    @Test
    public void solveFor10() {
        final _1091D solver = new _1091D(10);
        MatcherAssert.assertThat(solver.solve(), is(equalTo(30052700L)));
    }

    @Test
    public void solveFor12() {
        final _1091D solver = new _1091D(12);
        MatcherAssert.assertThat(solver.solve(), is(equalTo(931982044L)));
    }
}