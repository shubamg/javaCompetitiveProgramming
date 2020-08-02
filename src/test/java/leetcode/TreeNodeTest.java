package leetcode;

import org.junit.Assert;
import org.junit.Test;

public class TreeNodeTest {

    /**
     * @param vals value at index 0 is ignored
     * @return an array of nodes with root at index 1
     */
    private static TreeNode[] getNodeArray(final Integer[] vals) {
        final TreeNode[] nodes = new TreeNode[vals.length];
        for (int i = vals.length - 1; i > 0; i--) {
            if (vals[i] == null) {
                nodes[i] = null;
                continue;
            }
            final int leftChildIndex = 2 * i;
            if (leftChildIndex < vals.length) {
                final TreeNode leftChild = nodes[leftChildIndex];
                final int rightChildIndex = 2 * i + 1;
                final TreeNode rightChild = rightChildIndex < vals.length ? nodes[rightChildIndex] : null;
                nodes[i] = new TreeNode(vals[i], leftChild, rightChild);
            } else {
                nodes[i] = new TreeNode(vals[i]);
            }
        }
        return nodes;
    }

    @Test
    public void test() {
        assertWidth(4, new Integer[]{null, 1, 2, 3, 4, 5, null, 7});
        assertWidth(2, new Integer[]{null, 1, 3, 2, 5});
    }

    private void assertWidth(final int expectedWidth, final Integer[] vals) {
        final TreeNode[] nodes = getNodeArray(vals);
        final _3385 solver = new _3385();
        Assert.assertEquals(expectedWidth, solver.widthOfBinaryTree(nodes[1]));
    }
}