package math;

import org.junit.Assert;
import org.junit.Test;

public class MatrixCalculatorTest {

    @Test
    public void multiply() {
        final long[][] A = {{1L, 2L}, {3L, 4L}};
        final long[][] B = {{100L, 101L, 102L}, {103L, 104L, 105L}};
        final MatrixCalculator matrixCalculator = new MatrixCalculator(ModuloCalculator.getWithoutMod());
        final long[][] expectedResult = {{306, 309, 312}, {712, 719, 726}};
        Assert.assertArrayEquals(expectedResult, matrixCalculator.multiply(A, B));
    }

    @Test
    public void add() {
        final long[][] A = {{1L, 2L}, {3L, 4L}};
        final long[][] B = {{100L, 101L}, {103L, 104L}};
        final MatrixCalculator matrixCalculator = new MatrixCalculator(ModuloCalculator.getWithoutMod());
        final long[][] expectedResult = {{101L, 103L}, {106L, 108L}};
        Assert.assertArrayEquals(expectedResult, matrixCalculator.add(A, B));
    }

    @Test
    public void power() {
        final long[][] A = {{1L, 2L}, {3L, 4L}};
        final MatrixCalculator matrixCalculator = new MatrixCalculator(ModuloCalculator.getWithoutMod());
        final long[][] expectedResult = {{1069L, 1558L}, {2337L, 3406L}};
        Assert.assertArrayEquals(expectedResult, matrixCalculator.power(A, 5));
    }
}