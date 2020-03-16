package codeJam._2019;

import io.InputReader;
import util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

/**
 * This produces TLE on the code jam server as this is quadratic in N (Number of vertices in the grid are N*N)
 */
public class YouCanGoYourOwnWay {
    private final static String OUT_LINE_TMPL = "Case #%d: %s";
    private final static Pair<Integer, Integer> start = new Pair<>(0, 0);
    private final int N;
    private final int pathLength;
    private final Queue<Pair<Integer, Integer>> queue;
    private final Map<Pair<Integer, Integer>, Direction> blockedMoves;
    private final Pair<Integer, Integer> end;
    private final Map<Pair<Integer, Integer>, Direction> directionOfExploration;

    public YouCanGoYourOwnWay(final String lydiaMoveSequences, final int n) {
        this.N = n;
        this.queue = new LinkedList<>();
        blockedMoves = getBlockedMoves(lydiaMoveSequences.toCharArray());
        directionOfExploration = new HashMap<>();
        this.pathLength = 2 * N - 2;
        end = new Pair<>(N - 1, N - 1);
    }

    private Map<Pair<Integer, Integer>, Direction> getBlockedMoves(final char[] lydiaMoveSequences) {
        final Map<Pair<Integer, Integer>, Direction> blockedMoves = new HashMap<>(pathLength);
        Pair<Integer, Integer> currentPosition = start;
        for (final char _direction : lydiaMoveSequences) {
            final Direction direction = Direction.of(_direction);
            blockedMoves.put(currentPosition, direction);
            currentPosition = direction.movePoint(currentPosition);
        }
        return blockedMoves;
    }

    private void doBfs() {
        queue.add(start);
        // We know that the isEndReached will not happen afterwards isEmpty() = false
        // No need to store distance as it is just x + y.
        while (!queue.isEmpty() && isUnexplored(end)) {
            final Pair<Integer, Integer> u = queue.remove();
            final Map<Pair<Integer, Integer>, Direction> neighbours2Direction = getValidNeighbours2Direction(u);
            neighbours2Direction.forEach((point, dir) -> {
                queue.add(point);
                directionOfExploration.put(point, dir);
            });
        }
    }

    private List<Direction> getSolution() {
        final List<Direction> directions = new ArrayList<>(pathLength);
        doBfs();
        Pair<Integer, Integer> currentPoint = end;
        while (!currentPoint.equals(start)) {
            final Direction direction = directionOfExploration.get(currentPoint);
            directions.add(direction);
            final Direction reverseDirection = direction.reverse();
            currentPoint = reverseDirection.movePoint(currentPoint);
        }
        Collections.reverse(directions);
        return directions;
    }

    private boolean isUnexplored(final Pair<Integer, Integer> point) {
        // start is always explored
        return !point.equals(start) && !directionOfExploration.containsKey(point);
    }

    private Map<Pair<Integer, Integer>, Direction> getValidNeighbours2Direction(final Pair<Integer, Integer> point) {
        final Direction blockedDirection = blockedMoves.get(point);
        final Map<Pair<Integer, Integer>, Direction> neighbours2direction = new HashMap<>(2);
        final Direction[] validDirections = new Direction[]{Direction.SOUTH, Direction.EAST};
        for (final Direction direction : validDirections) {
            if (direction != blockedDirection) {
                final Pair<Integer, Integer> neighbour = direction.movePoint(point);
                if (shouldAddToNeighbours(neighbour)) {
                    neighbours2direction.put(neighbour, direction);
                }
            }
        }
        return neighbours2direction;
    }

    private boolean shouldAddToNeighbours(final Pair<Integer, Integer> neighbour) {
        return isWithinGrid(neighbour) && isUnexplored(neighbour);
    }

    private boolean isWithinGrid(final Pair<Integer, Integer> point) {
        final int x = point.getKey();
        final int y = point.getValue();
        return x < N && y < N && x >= 0 && y >= 0;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int T = reader.nextInt();
        for (int t = 1; t <= T; t++) {
            final int N = reader.nextInt();
            final String lydiaMoves = reader.nextLine();
            final YouCanGoYourOwnWay youCanGoYourOwnWay = new YouCanGoYourOwnWay(lydiaMoves, N);
            final StringBuilder output = new StringBuilder();
            youCanGoYourOwnWay.getSolution().stream().map(Direction::toChar).forEach(output::append);
            System.out.println(formatOutput(t, output.toString()));
        }
    }

    private static String formatOutput(final int t, final String output) {
        return String.format(OUT_LINE_TMPL, t, output);
    }

    private enum Direction {
        NORTH('S', p -> new Pair<>(p.getKey(), p.getValue() - 1)),
        SOUTH('N', p -> new Pair<>(p.getKey(), p.getValue() + 1)),
        WEST('E', p -> new Pair<>(p.getKey() - 1, p.getValue())),
        EAST('W', p -> new Pair<>(p.getKey() + 1, p.getValue()));

        private final char reverseDirection;
        private final Function<Pair<Integer, Integer>, Pair<Integer, Integer>> pointMover;

        Direction(final char reverseDirection,
                  final Function<Pair<Integer, Integer>, Pair<Integer, Integer>> pointMover) {
            this.reverseDirection = reverseDirection;
            this.pointMover = pointMover;
        }

        private static Direction of(final char dir) {
            switch (dir) {
                case 'N':
                    return NORTH;
                case 'S':
                    return SOUTH;
                case 'E':
                    return EAST;
                case 'W':
                    return WEST;
                default:
                    throw new IllegalStateException("Unexpected value of direction: " + dir);
            }
        }

        private Direction reverse() {
            return Direction.of(reverseDirection);
        }

        private Pair<Integer, Integer> movePoint(final Pair<Integer, Integer> input) {
            return pointMover.apply(input);
        }

        private char toChar() {
            return name().charAt(0);
        }
    }
}
