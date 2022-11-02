package topcoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChessMetric {

    public static void main(String[] args) {
        System.out.println(new ChessMetric().howMany(100, new int[]{0, 0}, new int[]{0, 99}, 50));
    }

    public long howMany(int size, int[] startAsArr, int[] endAsArr, int numMoves) {
        final Point start = new Point(startAsArr[0], startAsArr[1]);
        final Point end = new Point(endAsArr[0], endAsArr[1]);
        final Solver solver = new Solver(size, numMoves, start, end);
        return solver.solve();
    }

    private static class Point {
        private final int row;
        private final int column;

        private Point(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        private Point add(final Point delta) {
            final int newRow = this.row + delta.row;
            final int newColumn = this.column + delta.column;
            return new Point(newRow, newColumn);
        }

        private Point invert() {
            return new Point(-row, -column);
        }
    }

    private static class Solver {
        private static final Set<Point> deltas = getAllDeltas();
        private final int size;
        private final int numMoves;
        private final Point end;
        private int stateNumber = -1;
        private final long[][][] numWaysDp; // moves, rows, cols

        private Solver(final int size, final int numMoves, final Point start, final Point end) {
            this.size = size;
            this.numMoves = numMoves;
            this.end = end;
            numWaysDp = new long[numMoves + 1][size][size];
            initDpState(start);
        }

        private void initDpState(final Point start) {
            numWaysDp[0][start.row][start.column] = 1;
            stateNumber = 0;
        }

        private long solve() {
            while (stateNumber < numMoves) {
                transitionToNextState();
                stateNumber++;
            }
            return numWaysDp[numMoves][end.row][end.column];
        }

        private void transitionToNextState() {
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    final Point currPoint = new Point(row, column);
                    contributeToNextState(currPoint);
                }
            }
        }

        private void contributeToNextState(final Point currPoint) {
            final int currRow = currPoint.row;
            final int currColumn = currPoint.column;
            final long currNumWays = numWaysDp[stateNumber][currRow][currColumn];
            deltas.stream()
                  .map(currPoint::add)
                  .filter(this::isPointValid)
                  .forEach(newPoint -> addNumWays(newPoint, currNumWays));
        }

        private void addNumWays(final Point point, final long toAdd) {
            numWaysDp[stateNumber + 1][point.row][point.column] += toAdd;
        }

        private boolean isPointValid(final Point point) {
            return isValidCoordinate(point.row) && isValidCoordinate(point.column);
        }

        private boolean isValidCoordinate(final int coordinate) {
            return coordinate >= 0 && coordinate < size;
        }

        private static Set<Point> getAllDeltas() {
            final Set<Point> deltas = new HashSet<>(16);
            final Point kingUp = new Point(-1, 0);
            final Point kingRight = new Point(0, 1);
            final Point[] verticalKings = {kingUp, kingUp.invert()};
            final Point[] horizontalKings = {kingRight.invert(), kingRight};
            deltas.addAll(Arrays.asList(verticalKings));
            deltas.addAll(Arrays.asList(horizontalKings));
            for (final Point verticalKing : verticalKings) {
                for (final Point horizontalKing : horizontalKings) {
                    deltas.add(verticalKing.add(horizontalKing));
                    deltas.add(verticalKing.add(verticalKing).add(horizontalKing));
                    deltas.add(horizontalKing.add(horizontalKing).add(verticalKing));
                }
            }
            return deltas;
        }
    }
}

