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
        if (head.child == null && head.next == null) {
            head.prev = prev;
            return head;
        }
        if (head.child == null) {
            return getFlattenedTail(head, head.next);
        }
        final Node tailOfChild = getFlattenedTail(head, head.child);
        if (head.next == null) {
            head.next = head.child;
            head.child = null;
            head.prev = prev;
            return tailOfChild;
        }
        // both tail and child exist
        final Node tailOfNext = getFlattenedTail(tailOfChild, head.next);
        tailOfChild.next = head.next;
        head.next = head.child;
        head.child = null;
        head.prev = prev;
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

    // this has been added by Shubham for testing
    public Node(final int val, final Node prev, final Node next, final Node child) {
        this.val = val;
        this.prev = prev;
        this.next = next;
        this.child = child;
    }
}
