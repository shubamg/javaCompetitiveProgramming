package topcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Shubham Gupta
 * on 31 Oct 2022.
 */
public class AvoidRoads {
    private Set<Block> badBlocks;
    private long[][] numWays;

    public static void main(String[] args) {
        System.out.println(new AvoidRoads().numWays(2, 2, new String[]{"0 0 1 0", "1 2 2 2", "1 1 2 1"}));
    }

    public long numWays(final int width, final int height, final String[] bad) {
        badBlocks = getBadBlocks(bad);
        numWays = new long[width + 1][height + 1];
        for (int w = 0; w <= width; w++) {
            for (int h = 0; h <= height; h++) {
                numWays[w][h] = calculateNumWays(w, h);
            }
        }
        return numWays[width][height];
    }

    private long calculateNumWays(final int w, final int h) {
        if (w == 0 && h == 0) {
            return 1L;
        }
        return getNumWaysOnLeft(w, h) + getNumWaysDown(w, h);
    }

    private long getNumWaysOnLeft(final int w, final int h) {
        if (w == 0) {
            return 0L;
        }
        final Block horizontalBlock = getHorizontalBlock(w, h);
        if (isBad(horizontalBlock)) {
            return 0L;
        }
        return numWays[w - 1][h];
    }

    private Block getHorizontalBlock(final int w, final int h) {
        final Point right = new Point(w, h);
        final Point left = new Point(w - 1, h);
        return new Block(right, left);
    }

    private boolean isBad(final Block block) {
        return badBlocks.contains(block);
    }

    private long getNumWaysDown(final int w, final int h) {
        if (h == 0) {
            return 0L;
        }
        final Block verticalBlock = getVerticalBlock(w, h);
        if (isBad(verticalBlock)) {
            return 0L;
        }
        return numWays[w][h - 1];
    }

    private Block getVerticalBlock(final int w, final int h) {
        final Point upper = new Point(w, h);
        final Point lower = new Point(w, h - 1);
        final Block block = new Block(upper, lower);
        return block;
    }

    private Set<Block> getBadBlocks(final String[] bad) {
        return Arrays.stream(bad).map(this::toBlock).collect(Collectors.toSet());
    }

    private Block toBlock(final String blockStr) {
        final List<Integer> coordinates = Arrays.stream(blockStr.split(" "))
                                                .map(Integer::parseInt)
                                                .collect(Collectors.toList());
        assert coordinates.size() == 4;
        final Point point1 = new Point(coordinates.get(0), coordinates.get(1));
        final Point point2 = new Point(coordinates.get(2), coordinates.get(3));
        return new Block(point1, point2);
    }

    private static class Point {
        private final int x;
        private final int y;

        private Point(final int x, final int y) {
            this.x = x;
            this.y = y;
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

            if (x != point.x) {
                return false;
            }
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    private static class Block {
        private final Point higher;
        private final Point lower;

        private Block(final Point point1, final Point point2) {
            final List<Point> pairOfPoints = new ArrayList<>(2);
            pairOfPoints.add(point1);
            pairOfPoints.add(point2);
            if (point1.x == point2.x) {
                sort(pairOfPoints, p -> p.y);
            } else if (point1.y == point2.y) {
                sort(pairOfPoints, p -> p.x);
            } else {
                throw new IllegalStateException("Neither x nor y coordinate is matching");
            }
            higher = pairOfPoints.get(1);
            lower = pairOfPoints.get(0);
        }

        private static void sort(final List<Point> pairOfPoints,
                                 final Function<Point, Integer> differingCoordinateExtractor) {
            pairOfPoints.sort(Comparator.comparing(differingCoordinateExtractor));
            final Point higher = pairOfPoints.get(1);
            final Point lower = pairOfPoints.get(0);
            assert differingCoordinateExtractor.apply(higher) - differingCoordinateExtractor.apply(lower) == 1;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Block block = (Block) o;

            if (!higher.equals(block.higher)) {
                return false;
            }
            return lower.equals(block.lower);
        }

        @Override
        public int hashCode() {
            int result = higher.hashCode();
            result = 31 * result + lower.hashCode();
            return result;
        }
    }
}
