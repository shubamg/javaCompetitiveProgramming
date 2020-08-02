package leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class _3386Test {

    private static Node getNodes(final Integer[] vals) {
        Node prevLevel = null;
        Node prevNode = null;
        Node firstNode = null;
        int numPrevNulls = 1;
        for (final Integer val : vals) {
            if (val == null) {
                if (prevLevel != null && numPrevNulls > 0) {
                    prevLevel = prevLevel.next;
                }
                prevNode = null;
                numPrevNulls++;
            } else {
                final Node newNode = new Node();
                newNode.val = val;
                newNode.prev = prevNode;
                newNode.next = null;
                newNode.child = null;
                if (numPrevNulls > 0) {
                    if (prevLevel != null) {
                        prevLevel.child = newNode;
                    }
                    prevLevel = newNode;
                } else if (prevNode != null) {
                    prevNode.next = newNode;
                }
                prevNode = newNode;
                numPrevNulls = 0;
                if (firstNode == null) {
                    firstNode = newNode;
                }
            }
        }
        return firstNode;
    }

    @Test
    public void flatten() {
        final Node input = getNodes(new Integer[]{1, 2, 3, 4, 5, 6, null, null, null, 7, 8, 9, 10, null, null, 11, 12});
        final _3386 solver = new _3386();
        Node node = solver.flatten(input);
        final int[] expectedOrder = new int[]{1, 2, 3, 7, 8, 11, 12, 9, 10, 4, 5, 6};
        for (int i = 0; i < expectedOrder.length; i++) {
            System.out.println(node);
            assert node != null;
            assertNull(node.child);

            assertEquals(expectedOrder[i], node.val);

            if (i == 0) {
                assertNull(node.prev);
            } else {
                assertEquals(expectedOrder[i - 1], node.prev.val);
            }

            if (i == expectedOrder.length - 1) {
                assertNull(node.next);
            } else {
                assertEquals(expectedOrder[i + 1], node.next.val);
            }

            node = node.next;
        }
    }
}