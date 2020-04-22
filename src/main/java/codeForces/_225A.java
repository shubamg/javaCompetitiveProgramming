package codeForces;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class _225A {
    private final static int NUM_SIDES = 2;
    private final static char SUM_OF_OPPOSITE_FACES = 7;
    private final int numDices;
    private final char[][] sideNums;
    private final Set<Character> allowedSides = new HashSet<>(NUM_SIDES * 2);

    private _225A(final int numDices, final char[][] sideNums) {
        this.numDices = numDices;
        this.sideNums = sideNums;
    }

    private boolean isSolvable() {
        for (int i = 0; i < NUM_SIDES; i++) {
            allowedSides.add(sideNums[0][i]);
            allowedSides.add((char) (SUM_OF_OPPOSITE_FACES - sideNums[0][i]));
        }
        for (int i = 1; i < numDices; i++) {
            if (!areSidesCompatible(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean areSidesCompatible(final int i) {
        final char[] sides = sideNums[i];
        return allowedSides.contains(sides[0]) && allowedSides.contains(sides[1]);
    }


    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final String YES = "YES";
        final String NO = "NO";
        final int N = reader.nextInt();
        final char x = (char) reader.nextInt(); // not used
        final char[][] sideNums = new char[N][NUM_SIDES];
        for (int i = 0; i < N; i++) {
            sideNums[i][0] = (char) reader.nextInt();
            sideNums[i][1] = (char) reader.nextInt();
        }
        final _225A solver = new _225A(N, sideNums);
        final boolean isSolvable = solver.isSolvable();
        System.out.println(isSolvable ? YES : NO);
    }
}
