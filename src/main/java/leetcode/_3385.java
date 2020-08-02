package leetcode;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

public class _3385 {
    public static final int ROOT_INDEX = 1;
    public static final int ROOT_LEVEL = 0;
    public static final int MIN_WIDTH = 0;
    public static final int DEFAULT_LEVEL_WIDTH = 0;
    private static final boolean isDebugEnabled = false;
    private final Queue<TreeNodeInfo> queue = new LinkedList<>();
    private int currentLevel = ROOT_LEVEL;
    private int maxWidthTillNow = MIN_WIDTH;
    private int currLevelBeginning = ROOT_INDEX;
    private int currLevelWidthTillNow = DEFAULT_LEVEL_WIDTH;

    public int widthOfBinaryTree(final TreeNode root) {
        final TreeNodeInfo rootNodeInfo = new TreeNodeInfo(root, ROOT_LEVEL, ROOT_INDEX);
        queue.add(rootNodeInfo);
        while (!queue.isEmpty()) {
            final TreeNodeInfo headNodeInfo = queue.poll();
            final int headLevel = headNodeInfo.getLevel();
            final int headIndex = headNodeInfo.getIndex();
            debug("Got head node with %d index and %d level\n", headIndex, headLevel);
            if (currentLevel < headLevel) {
                processLevelEnd(headLevel, headIndex);
            }
            if (currentLevel == headLevel) {
                currLevelWidthTillNow = headIndex - currLevelBeginning + 1;
            } else {
                throw new IllegalStateException("Not possible");
            }
            printState();
            populateChildren(headNodeInfo);
        }
        maxWidthTillNow = Math.max(maxWidthTillNow, currLevelWidthTillNow);
        printState();
        return maxWidthTillNow;
    }

    private PrintStream printState() {
        return debug("Current state is currentLevel=%d, currLevelBeginning=%d, currLevelWidthTillNow=%d\n",
                     currentLevel,
                     currLevelBeginning,
                     currLevelWidthTillNow);
    }

    public PrintStream debug(String format, Object... args) {
        if (isDebugEnabled) {
            return System.out.format(format, args);
        }
        return null;
    }

    private void processLevelEnd(final int headLevel, final int headIndex) {
        maxWidthTillNow = Math.max(maxWidthTillNow, currLevelWidthTillNow);
        currLevelBeginning = headIndex;
        currentLevel = headLevel;
    }

    private void populateChildren(final TreeNodeInfo parentNodeInfo) {
        final TreeNode parentNode = parentNodeInfo.getTreeNode();
        final int childLevel = parentNodeInfo.getLevel() + 1;
        final TreeNode leftChild = parentNode.left;
        if (leftChild != null) {
            final int leftIndex = parentNodeInfo.getIndex() * 2;
            final TreeNodeInfo leftChildInfo = new TreeNodeInfo(leftChild, childLevel, leftIndex);
            debug("Pushing child with %d index and %d level\n", leftIndex, childLevel);
            queue.add(leftChildInfo);
        }
        final TreeNode rightChild = parentNode.right;
        if (rightChild != null) {
            final int rightIndex = parentNodeInfo.getIndex() * 2 + 1;
            final TreeNodeInfo rightChildInfo = new TreeNodeInfo(rightChild, childLevel, rightIndex);
            System.out.format("Pushing child with %d index and %d level\n", rightIndex, childLevel);
            queue.add(rightChildInfo);
        }
    }

    private static class TreeNodeInfo {
        private final TreeNode treeNode;
        // level is actually a deterministic function of index, and can be computed on the fly also.
        private final int level;
        private final int index;

        private TreeNodeInfo(final TreeNode treeNode, final int level, final int index) {
            this.treeNode = treeNode;
            this.level = level;
            this.index = index;
        }

        private TreeNode getTreeNode() {
            return treeNode;
        }

        private int getLevel() {
            return level;
        }

        private int getIndex() {
            return index;
        }
    }
}

// Definition for a binary tree node.
// This class has been supplied by leetcode
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
