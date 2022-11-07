package topcoder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Shubham Gupta
 * on 06 Nov 2022.
 */
public class MiniPaint {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        System.out.println(new MiniPaint().leastBad(new String[]{"B"}, 1));
    }

    public int leastBad(final String[] picture, final int maxStrokes) {
        return new Solver(picture, maxStrokes).solve();
    }

    private static class Solver {
        private static final Point FIRST_POINT = new Point(0, 0);
        private final Point lastPoint;
        private static final int INFINITY = 2501;
        private static final String COLORS = "BWU"; // U is uncolored
        private static final int UNCOLORED = COLORS.indexOf('U');
        private final int totalRows;
        private final int totalColumns;
        private final String[] picture;
        private final int maxStrokes;
        // (row, column, end color, number of strokes used by all cells before this cell) -> min mis-painted spaces
        // More optimization may be achieved by maintaining dp state over contiguous scripts of the same color.
        private final int[][][][] dp;

        private Solver(final String[] picture, final int maxStrokes) {
            this.totalRows = picture.length;
            this.totalColumns = picture[0].length();
            this.picture = picture;
            this.maxStrokes = maxStrokes;
            this.dp = new int[totalRows][totalColumns][COLORS.length()][];
            this.lastPoint = new Point(totalRows - 1, totalColumns - 1);
        }

        private int fetchDpVal(final Point point, final int color, final int numStrokes) {
            final int[] numStrokes2MisPaintings = this.dp[point.row][point.column][color];
            if (numStrokes >= numStrokes2MisPaintings.length) {
                return numStrokes2MisPaintings[numStrokes2MisPaintings.length - 1];
            }
            return numStrokes2MisPaintings[numStrokes];
        }

        private boolean needNewStroke(final Point point, final int currColor, final int prevColor) {
            if (currColor == UNCOLORED) {
                return false;
                // No need to use new stroke for uncolored, at any column
            }
            if (point.column == 0) {
                return true;
                // For colored at column 0, need to use new stroke
            }
            return prevColor != currColor;
        }

        private boolean isThisMisPainted(final Point point, final int currColor) {
            return currColor != getPictureColor(point);
        }

        private int getPictureColor(final Point point) {
            return COLORS.indexOf(picture[point.row].charAt(point.column));
        }

        private int solve() {
            for (Point currPoint = FIRST_POINT; currPoint != null; currPoint = getNext(currPoint)) {
                evaluate(currPoint);
            }
            if (DEBUG) {
                printDp();
            }
            return IntStream.range(0, COLORS.length())
                            .map(color -> fetchDpVal(lastPoint, color, maxStrokes))
                            .min()
                            .getAsInt();
        }

        private void printDp() {
            for (int row = 0; row < totalRows; row++) {
                for (int column = 0; column < totalColumns; column++) {
                    for (int color = 0; color < COLORS.length(); color++) {
                        final int[] strokes2misPaintings = dp[row][column][color];
                        for (int stroke = 0; stroke < strokes2misPaintings.length; stroke++) {
                            System.out.printf("{(row=%d, column=%d, color=%s, stroke=%d):misPaintings=%d}",
                                              row,
                                              column,
                                              COLORS.charAt(color),
                                              stroke,
                                              strokes2misPaintings[stroke]);
                        }
                    }
                    System.out.println();
                }
            }
        }

        private void evaluate(final Point currPoint) {
            for (int currColor = 0; currColor < COLORS.length(); currColor++) {
                dp[currPoint.row][currPoint.column][currColor] = calculateDpValFor(currPoint, currColor);
            }
        }

        private int[] calculateDpValFor(final Point currPoint, final int currColor) {
            if (currPoint.equals(FIRST_POINT)) {
                return calculateDpValForFirstPoint(currColor);
            }
            final List<Integer> strokes2MisPaintings = new ArrayList<>(1);
            strokes2MisPaintings.add(calculateDpForZeroStrokes(currPoint, currColor));
            for (int stroke = 1; stroke <= maxStrokes; stroke++) {
                final int misPaintings = calculateDpValFor(currPoint, currColor, stroke);
                if (strokes2MisPaintings.size() >= 2 && misPaintings == strokes2MisPaintings.get(
                        strokes2MisPaintings.size() - 2)) {
                    break;
                }
                strokes2MisPaintings.add(misPaintings);
            }
            return strokes2MisPaintings.stream().mapToInt(i -> i).toArray();
        }

        private int calculateDpForZeroStrokes(final Point currPoint, final int currColor) {
            if (currColor == UNCOLORED) {
                final Point prevPoint = getPrevious(currPoint);
                assert prevPoint != null;
                return fetchDpVal(prevPoint, UNCOLORED, 0) + 1; // All points are misPainted
            }
            return INFINITY;
        }

        private int[] calculateDpValForFirstPoint(final int currColor) {
            if (currColor == UNCOLORED) {
                return new int[]{1}; // 1 mis-painting with 0 strokes for uncolored
            }
            final int[] strokes2MisPaintings = new int[2];
            strokes2MisPaintings[0] = INFINITY;
            strokes2MisPaintings[1] = isThisMisPainted(FIRST_POINT, currColor) ? 1 : 0;
            return strokes2MisPaintings;
        }

        private int calculateDpValFor(final Point currPoint, final int currColor, final int stroke) {
            assert stroke > 0;
            final Point prevPoint = getPrevious(currPoint);
            assert prevPoint != null;
            return IntStream.range(0, COLORS.length()).map(prevColor -> {
                final int newMisPaintings = isThisMisPainted(currPoint, currColor) ? 1 : 0;
                final int remainingStrokes = needNewStroke(currPoint, currColor, prevColor) ? stroke - 1 : stroke;
                if (DEBUG) {
                    System.out.printf("currPoint=%s,currColor=%s,stroke=%d," + "fetchDpVal(prevPoint=%s,prevColor=%s,"
                                              + "remainingStrokes=%d)" + "=%d+newMisPaintings=%d=%d%n",
                                      currPoint,
                                      COLORS.charAt(currColor),
                                      stroke,
                                      prevPoint,
                                      COLORS.charAt(prevColor),
                                      remainingStrokes,
                                      fetchDpVal(prevPoint, prevColor, remainingStrokes),
                                      newMisPaintings,
                                      fetchDpVal(prevPoint, prevColor, remainingStrokes) + newMisPaintings);
                }
                return fetchDpVal(prevPoint, prevColor, remainingStrokes) + newMisPaintings;
            }).min().getAsInt();
        }

        @Nullable
        private Point getNext(final Point p) {
            if (p.equals(lastPoint)) {
                return null;
            }
            if (p.column == totalColumns - 1) {
                return new Point(p.row + 1, 0);
            }
            return new Point(p.row, p.column + 1);
        }

        @Nullable
        private Point getPrevious(final Point p) {
            if (p.equals(FIRST_POINT)) {
                return null;
            }
            if (p.column == 0) {
                return new Point(p.row - 1, totalColumns - 1);
            }
            return new Point(p.row, p.column - 1);
        }
    }

    private static class Point {
        private final int row;
        private final int column;

        private Point(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Point point = (Point) o;

            if (row != point.row) {
                return false;
            }
            return column == point.column;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }

        @Override
        public String toString() {
            return "Point{" + "row=" + row + ", column=" + column + '}';
        }
    }
}
