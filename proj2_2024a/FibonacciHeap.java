/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 */
public class FibonacciHeap {

    private HeapNode min;
    private int size;
    private int totalLinks;
    private int totalCuts;

    /**
     * Constructor to initialize an empty heap.
     */
    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
        this.totalLinks = 0;
        this.totalCuts = 0;
    }

    /**
     * Insert (key, info) into the heap and return the newly generated HeapNode.
     *
     * @param key  The key of the new node (must be > 0).
     * @param info The associated data for the new node.
     * @return The newly created HeapNode.
     */
    public HeapNode insert(int key, String info) {
        HeapNode newNode = new HeapNode(key, info);
        if (min == null) {
            min = newNode;
        } else {
            // Add the new node to the root list
            newNode.next = min.next;
            newNode.prev = min;
            min.next.prev = newNode;
            min.next = newNode;
            // Update the minimum pointer if necessary
            if (key < min.key) {
                min = newNode;
            }
        }
        size++;
        return newNode;
    }

    /**
     * Return the minimal HeapNode, or null if the heap is empty.
     *
     * @return The node with the minimum key, or null if the heap is empty.
     */
    public HeapNode findMin() {
        return min;
    }

    /**
     * Delete the minimal item.
     *
     * Removes the node with the minimum key from the heap and adjusts the structure.
     */
    public void deleteMin() {
        if (min == null) {
            return;
        }

        HeapNode oldMin = min;

        // Add all children of the minimum node to the root list
        if (min.child != null) {
            HeapNode child = min.child;
            do {
                HeapNode nextChild = child.next;
                // Detach the child and add to root list
                child.parent = null;
                child.next = min.next;
                child.prev = min;
                min.next.prev = child;
                min.next = child;
                child = nextChild;
            } while (child != min.child);
        }

        // Remove the minimum node from the root list
        removeNode(min);
        size--;

        if (size == 0) {
            min = null;
        } else {
            min = oldMin.next;
            consolidate();
        }
    }

    /**
     * Decrease the key of x by diff and fix the heap.
     *
     * @param x    The node whose key will be decreased.
     * @param diff The amount to decrease the key (must be > 0).
     */
    public void decreaseKey(HeapNode x, int diff) {
        if (diff <= 0 || x == null) {
            return;
        }

        x.key -= diff;
        if (x.parent != null && x.key < x.parent.key) {
            cut(x);
            cascadingCut(x.parent);
        }

        if (x.key < min.key) {
            min = x;
        }
    }

    /**
     * Delete x from the heap.
     *
     * @param x The node to delete.
     */
    public void delete(HeapNode x) {
        decreaseKey(x, x.key - Integer.MIN_VALUE);
        deleteMin();
    }

    /**
     * Return the total number of links performed.
     *
     * @return The total number of tree merges (links) performed in the heap.
     */
    public int totalLinks() {
        return totalLinks;
    }

    /**
     * Return the total number of cuts performed.
     *
     * @return The total number of cuts (node separations) performed in the heap.
     */
    public int totalCuts() {
        return totalCuts;
    }

    /**
     * Meld the heap with another heap.
     *
     * @param heap2 The other FibonacciHeap to meld with.
     */
    public void meld(FibonacciHeap heap2) {
        if (heap2 == null || heap2.min == null) {
            return;
        }

        mergeNodes(this.min, heap2.min);
        if (heap2.min.key < this.min.key) {
            this.min = heap2.min;
        }

        this.size += heap2.size;
        heap2.size = 0;
        heap2.min = null;
    }

    /**
     * Return the number of elements in the heap.
     *
     * @return The total number of nodes in the heap.
     */
    public int size() {
        return size;
    }

    /**
     * Return the number of trees in the heap.
     *
     * @return The total number of trees in the heap's root list.
     */
    public int numTrees() {
        int count = 0;
        if (min == null) {
            return count;
        }

        HeapNode node = min;
        do {
            count++;
            node = node.next;
        } while (node != min);

        return count;
    }

    // Helper functions

    /**
     * Consolidate trees of the same rank.
     *
     * This function merges trees in the root list to ensure that no two trees have the same rank.
     */
    private void consolidate() {
        HeapNode[] aux = new HeapNode[size];
        HeapNode current = min;
        do {
            HeapNode next = current.next;
            while (aux[current.rank] != null) {
                current = link(current, aux[current.rank]);
                aux[current.rank - 1] = null;
            }
            aux[current.rank] = current;
            current = next;
        } while (current != min);

        min = null;
        for (HeapNode node : aux) {
            if (node != null) {
                if (min == null || node.key < min.key) {
                    min = node;
                }
            }
        }
    }

    /**
     * Link two trees of the same rank.
     *
     * @param a The first tree.
     * @param b The second tree.
     * @return The resulting tree after linking.
     */
    private HeapNode link(HeapNode a, HeapNode b) {
        if (b.key < a.key) {
            HeapNode temp = a;
            a = b;
            b = temp;
        }
        removeNode(b);
        b.parent = a;
        b.next = b.prev = b;
        if (a.child == null) {
            a.child = b;
        } else {
            mergeNodes(a.child, b);
        }
        a.rank++;
        totalLinks++;
        return a;
    }

    /**
     * Cut a node from its parent and move it to the root list.
     *
     * @param x The node to cut.
     */
    private void cut(HeapNode x) {
        if (x.parent != null) {
            if (x.parent.child == x) {
                x.parent.child = x.next == x ? null : x.next;
            }
            removeNode(x);
            x.parent.rank--;
            x.parent = null;
            x.mark = false;
            mergeNodes(min, x);
            totalCuts++;
        }
    }

    /**
     * Perform cascading cuts for a node and its ancestors.
     *
     * @param x The node to start cascading cuts from.
     */
    private void cascadingCut(HeapNode x) {
        HeapNode parent = x.parent;
        if (parent != null) {
            if (!x.mark) {
                x.mark = true;
            } else {
                cut(x);
                cascadingCut(parent);
            }
        }
    }

    /**
     * Merge two circular doubly linked lists.
     *
     * @param a The first circular doubly linked list.
     * @param b The second circular doubly linked list.
     */
    private void mergeNodes(HeapNode a, HeapNode b) {
        if (a == null || b == null) {
            return;
        }
        HeapNode aNext = a.next;
        a.next = b.next;
        b.next.prev = a;
        b.next = aNext;
        aNext.prev = b;
    }

    /**
     * Remove a node from its circular doubly linked list.
     *
     * @param x The node to remove.
     */
    private void removeNode(HeapNode x) {
        if (x.next == x) {
            return;
        }
        x.prev.next = x.next;
        x.next.prev = x.prev;
    }

    /**
     * Class implementing a node in a Fibonacci Heap.
     */
    public static class HeapNode {
        public int key;
        public String info;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean mark;

        /**
         * Constructor to initialize a heap node.
         *
         * @param key  The key of the node.
         * @param info The associated data for the node.
         */
        public HeapNode(int key, String info) {
            this.key = key;
            this.info = info;
            this.child = null;
            this.next = this;
            this.prev = this;
            this.parent = null;
            this.rank = 0;
            this.mark = false;
        }
    }
}
