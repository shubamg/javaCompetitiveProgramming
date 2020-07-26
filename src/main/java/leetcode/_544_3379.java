package leetcode;

import java.util.Arrays;

public class _544_3379 {
    private final static int MODBASE = 2;
    private final static int MATRIX_LEN = 6;
    private final static int CELL_SIZE = 8;
    private final static int[][] I = new int[MATRIX_LEN][MATRIX_LEN];
    private final static int[][] _M = new int[MATRIX_LEN][MATRIX_LEN];
    private final static int[][] Delta0 = new int[MATRIX_LEN][1];

    static {
        for (int i = 0; i < MATRIX_LEN; i++) {
            I[i][i] = 1;
            if (i > 0) {
                _M[i][i - 1] = 1;
            }
            if (i + 1 < MATRIX_LEN) {
                _M[i][i + 1] = 1;
            }
            Delta0[i][0] = 1;
        }
    }

    public static void main(String[] args) {
        final int[] input = {1, 0, 0, 1, 0, 0, 1, 0};
        System.out.println(Arrays.toString(prisonAfterNDays(input, 1_000_000_000)));
    }

    public static int[] prisonAfterNDays(final int[] cells, final int N) {
        if (cells == null) {
            return null;
        }
        if (N == 0) {
            return cells;
        }
        final int[][] X1_2D = new int[MATRIX_LEN][1];
        for (int i = 0; i < MATRIX_LEN; i++) {
            X1_2D[i][0] = (cells[i] == cells[i + 2]) ? 1 : 0;
        }
        final int[][] MPowNMinus1 = power(_M, N - 1, MODBASE);
        final int[][] delta = mult(subtract(MPowNMinus1, I, MODBASE), Delta0, MODBASE);
        final int[][] M_MinusI = subtract(_M, I, MODBASE);
        final int[][] coeffX1 = mult(M_MinusI, MPowNMinus1, MODBASE);
        final int[][] rhs = add(mult(coeffX1, X1_2D, MODBASE), delta, MODBASE);
        for (int i = 0; i < (1 << MATRIX_LEN); i++) {
            final int[][] answer = getMatrix(i);
            final int[][] lhs = mult(M_MinusI, answer, MODBASE);
            if (Arrays.deepEquals(lhs, rhs)) {
                final int[] ret = new int[CELL_SIZE];
                ret[0] = 0;
                ret[CELL_SIZE - 1] = 0;
                for (int j = 0; j < MATRIX_LEN; j++) {
                    ret[j + 1] = answer[j][0];
                }
                return ret;
            }
        }
        throw new RuntimeException("No Solution found");
    }

    private static int[][] getMatrix(final int bitSet) {
        final int[][] ret = new int[MATRIX_LEN][1];
        for (int i = 0; i < MATRIX_LEN; i++) {
            ret[i][0] = ((1 << i) & bitSet) > 0 ? 1 : 0;
        }
        return ret;
    }

    private static int[][] power(final int[][] M, final int pow, final int modBase) {
        if (pow == 0) {
            return I;
        }
        final int[][] temp = power(M, pow / 2, modBase);
        if (pow % 2 == 1) {
            return mult(mult(temp, temp, modBase), M, modBase);
        }
        return mult(temp, temp, modBase);
    }

    private static int[][] mult(final int[][] A, final int[][] B, final int modBase) {
        final int[][] C = new int[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                C[i][j] = 0;
                for (int k = 0; k < A.length; k++) {
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % modBase;
                }
            }
        }
        return C;
    }

    private static int[][] subtract(final int[][] A, final int[][] B, final int modBase) {
        final int[][] C = new int[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                C[i][j] = (A[i][j] - B[i][j] + modBase) % modBase;
            }
        }
        return C;
    }

    private static int[][] add(final int[][] A, final int[][] B, final int modBase) {
        final int[][] C = new int[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                C[i][j] = (A[i][j] + B[i][j]) % modBase;
            }
        }
        return C;
    }
}

