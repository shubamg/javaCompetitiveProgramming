package math;

public class MatrixCalculator {
    private final Calculator calculator;

    public MatrixCalculator(final Calculator calculator) {
        this.calculator = calculator;
    }

    private static long[][] identity(final int n) {
        final long[][] result = new long[n][n];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (i == j) {
                    result[i][j] = 1L;
                } else {
                    result[i][j] = 0L;
                }
            }
        }
        return result;
    }

    private void isAMatrix(final long[][] matrix) {
        assert matrix.length > 0;
        final int numColumns = matrix[0].length;
        for (final long[] row : matrix) {
            assert row.length == numColumns;
        }
    }

    private void doMultiplicationPrechecks(final long[][] A, final long[][] B) {
        isAMatrix(A);
        isAMatrix(B);
        final int numRowsInB = B.length;
        for (final long[] rowOfA : A) {
            assert rowOfA.length == numRowsInB;
        }
    }

    private void additionPrechecks(final long[][] A, final long[][] B) {
        isAMatrix(A);
        isAMatrix(B);
        assert A.length == B.length;
        assert A[0].length == B.length;
    }

    final long[][] add(final long[][] A, final long[][] B) {
        isAMatrix(A);
        isAMatrix(B);
        additionPrechecks(A, B);
        final long[][] result = new long[A.length][A[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = calculator.add(A[i][j], B[i][j]);
            }
        }
        return result;
    }

    final long[][] multiply(final long[][] A, final long[][] B) {
        doMultiplicationPrechecks(A, B);
        final long[][] result = new long[A.length][B[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = 0L;
                for (int k = 0; k < A[i].length; k++) {
                    result[i][j] = calculator.add(result[i][j], calculator.multiply(A[i][k], B[k][j]));
                }
            }
        }
        return result;
    }

    public final long[][] power(final long[][] base, final long pow) {
        isAMatrix(base);
        assert base.length == base[0].length;

        if (pow == 0) {
            return identity(base.length);
        }
        assert pow > 0;
        long[][] partialResult = power(base, pow / 2);
        partialResult = multiply(partialResult, partialResult);
        if (pow % 2 == 1) {
            partialResult = multiply(partialResult, base);
        }
        return partialResult;
    }

    public static long[][] getZeroMatrix(final int size) {
        final long[][] matrix = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 0L;
            }
        }
        return matrix;
    }
}
