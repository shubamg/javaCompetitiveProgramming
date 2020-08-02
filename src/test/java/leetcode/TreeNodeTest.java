package leetcode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeNodeTest {

    private TreeNode[] nodes = new TreeNode[8];

    @Before
    public void setup() {
        nodes[7] = new TreeNode(9);
        nodes[6] = null;
        nodes[5] = new TreeNode(3);
        nodes[4] = new TreeNode(5);
        nodes[3] = new TreeNode(2, null, nodes[7]);
        nodes[2] = new TreeNode(3, nodes[4], nodes[5]);
        nodes[1] = new TreeNode(1, nodes[2], nodes[3]);
    }

    @Test
    public void test() {
        final _3385 solver = new _3385();
        Assert.assertEquals(4, solver.widthOfBinaryTree(nodes[1]));
    }
}