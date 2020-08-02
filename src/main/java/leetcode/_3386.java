package leetcode;

public class _3386 {
    public Node flatten(final Node head) {
        if (head == null) {
            return null;
        }
        getFlattenedTail(null, head);
        return head;
    }

    private Node getFlattenedTail(final Node prev, final Node head) {
        assert head != null;
        head.prev = prev;
        if (head.child == null && head.next == null) {
            return head;
        }
        if (head.child == null) {
            return getFlattenedTail(head, head.next);
        }
        final Node tailOfChild = getFlattenedTail(head, head.child);
        if (head.next == null) {
            head.next = head.child;
            head.child = null;
            return tailOfChild;
        }
        // both tail and child exist
        final Node tailOfNext = getFlattenedTail(tailOfChild, head.next);
        tailOfChild.next = head.next;
        head.next = head.child;
        head.child = null;
        return tailOfNext;
    }
}


// Definition for a Node.
// This is from leetcode itself
class Node {
    public int val;
    public Node prev;
    public Node next;
    public Node child;

    public Node() {
    }

    public Node(final int val, final Node prev, final Node next, final Node child) {
        this.val = val;
        this.prev = prev;
        this.next = next;
        this.child = child;
    }

    @Override
    public String toString() {
        return "Node{" + "val=" + val + ", prev=" + (prev == null ? null : prev.val) + ", next=" + (next == null ?
                                                                                                    null : next.val)
                + ", child=" + (child == null ? null : child.val) + '}';
    }
}
