package codeJam._2019;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class YouCanGoYourOwnWay2 {
    private final int N;
    private final int pathLength;
    private final String lydiaMoveSequence;
    private final char firstLydiaMove;
    private final char reverseOfFirstLydiaMove;

    public YouCanGoYourOwnWay2(final String lydiaMoveSequence, final int N) {
        this.lydiaMoveSequence = lydiaMoveSequence;
        this.N = N;
        this.pathLength = lydiaMoveSequence.length();
        firstLydiaMove = this.lydiaMoveSequence.charAt(0);
        reverseOfFirstLydiaMove = getComlimentaryDirection(firstLydiaMove);
    }


    private char[] getSolution() {
        final char lastLydiaMove = lydiaMoveSequence.charAt(pathLength - 1);
        if (firstLydiaMove != lastLydiaMove) {
            return getLShapeSolution();
        }
        return _3LineSolution();
    }

    private char[] _3LineSolution() {
        final char[] solution = new char[pathLength];
        final int firstArmLength = getFirstArmLength();
        for (int i = 0; i < firstArmLength; i++) {
            solution[i] = reverseOfFirstLydiaMove;
        }
        final int secondArmLength = N - 1;
        for (int j = 0; j < secondArmLength; j++) {
            solution[j + firstArmLength] = firstLydiaMove;
        }
        final int thirdArmLength = N - 1 - firstArmLength;
        for (int i = 0; i < thirdArmLength; i++) {
            solution[pathLength - i - 1] = reverseOfFirstLydiaMove;
        }
        return  solution;
    }

    private int getFirstArmLength() {
        int ret = 0;
        for (int i = 0; i < pathLength; i++) {
            if (lydiaMoveSequence.charAt(i) == reverseOfFirstLydiaMove) {
                ret++;
                if (lydiaMoveSequence.charAt(i + 1) == reverseOfFirstLydiaMove) {
                    return ret;
                }
            }
        }
        throw new IllegalStateException();
    }

    private char[] getLShapeSolution() {
        final char[] solution = new char[pathLength];
        for (int i = 0; i < N - 1; i++) {
            solution[i] = reverseOfFirstLydiaMove;
        }
        for (int i = N - 1; i < pathLength; i++) {
            solution[i] = firstLydiaMove;
        }
        return solution;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int T = reader.nextInt();
        for (int t = 1; t <= T; t++) {
            final int N = reader.nextInt();
            final String lydiaMoves = reader.next();
            final YouCanGoYourOwnWay2 youCanGoYourOwnWay = new YouCanGoYourOwnWay2(lydiaMoves, N);
            System.out.print(String.format("Case #%d: ", t));
            final char[] solution = youCanGoYourOwnWay.getSolution();
            for (final char c: solution) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static char getComlimentaryDirection(final char dir) {
        switch (dir) {
            case 'S':
                return 'E';
            case 'E':
                return 'S';
            default:
                throw new IllegalStateException("Unexpected direction: " + dir);
        }

    }
}
