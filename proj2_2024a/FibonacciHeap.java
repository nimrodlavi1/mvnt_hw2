/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 */
public class FibonacciHeap {

    public HeapNode min;
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

        if (x.key <= min.key) {
            min = x;
        }
    }

    /**
     * Delete x from the heap.
     *
     * @param x The node to delete.
     */
    public void delete(HeapNode x) {
        if (x == min) {
            deleteMin();
        } else {
            if (x.parent != null) {
                cut(x);
                cascadingCut(x.parent);
            }
            // TODO: Does we need to update cuts?
            removeNode(x);
            size--;
        }
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
     *s
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

    /**
     * Display the heap structure visually in a tree-like format.
     */
    public void display() {
        if (min == null) {
            System.out.println("The heap is empty.");
            return;
        }

        System.out.println("Fibonacci Heap:");
        System.out.println("Minimum Node: (" + min.key + ", \"" + min.info + "\")");
        System.out.println("totalCuts: " + totalCuts + ", totalLinks: " + totalLinks + ", size: " + size);

        HeapNode current = min;
        int treeNumber = 1;
        do {
            System.out.println("Tree " + treeNumber + ":");
            printTree(current, "", true);
            current = current.next;
            treeNumber++;
        } while (current != min);
    }

    /**
     * Print a tree starting from the given node visually.
     *
     * @param node    The root node of the tree to print.
     * @param prefix  The prefix for indentation.
     * @param isLast  Indicates whether the current node is the last child.
     */
    private void printTree(HeapNode node, String prefix, boolean isLast) {
        if (node == null) return;

        // Print the current node
        System.out.print(prefix);
        System.out.print(isLast ? "└── " : "├── ");
        System.out.println("(" + node.key + ", \"" + node.info + "\")");

        // Prepare prefix for the next level
        prefix += isLast ? "    " : "│   ";

        // Recursively print children
        if (node.child != null) {
            HeapNode child = node.child;
            do {
                printTree(child, prefix, child.next == node.child);
                child = child.next;
            } while (child != node.child);
        }
    }

    // Helper functions

    /**
	  * 
	  * The method returns true if and only if the heap
	  * is empty.
	  *   
	  */
	 public boolean empty()
	 {
		 return size == 0;
	 }

    /**
     * Consolidate trees of the same rank.
     *
     * This function merges trees in the root list to ensure that no two trees have the same rank.
     */
    private void consolidate() {
        HeapNode[] aux = new HeapNode[logBase2(size) + 1];
        HeapNode current = min;

        // Process each node in the root list
        do {
            HeapNode next = current.next;
            while (aux[current.rank] != null) {
                current = link(current, aux[current.rank]);
                aux[current.rank - 1] = null;
            }
            aux[current.rank] = current;
            current = next;
        } while (current != min);

        // Reconstruct the root list and find the new min
        min = null;
        for (HeapNode node : aux) {
            if (node != null) {
                if (min == null) {
                    min = node;
                    min.next = min.prev = min;
                } else {
                    node.next = min.next;
                    node.prev = min;
                    min.next.prev = node;
                    min.next = node;
                    if (node.key < min.key) {
                        min = node;
                    }
                }
            }
        }
    }

    /**
     * Helper method to calculate log base 2 of an integer.
     *
     * @param n The number to calculate the logarithm for.
     * @return The integer value of log base 2 of n.
     */
    private int logBase2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }

    /**
     * Link two trees of the same rank.
     *
     * Makes b the child of a.
     *
     * @param a The first tree (parent after linking).
     * @param b The second tree (child after linking).
     * @return The resulting tree after linking.
     */
    private HeapNode link(HeapNode a, HeapNode b) {
        if (b.key < a.key) {
            HeapNode temp = a;
            a = b;
            b = temp;
        }
        // Remove b from the root list
        // TODO: Does this need to be done?
        // removeNode(b);
        // Make b a child of a
        b.parent = a;
        if (a.child == null) {
            a.child = b;
            b.next = b.prev = b;
        } else {
            b.next = a.child.next;
            b.prev = a.child;
            a.child.next.prev = b;
            a.child.next = b;
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
            // Add to root list
            x.next = min.next;
            x.prev = min;
            min.next.prev = x;
            min.next = x;
            totalCuts++;
        }
    }

    /**
     * Perform cascading cuts for a node and its ancestors.
     *
     * @param x The node to start cascading cuts from.
     */
    private void cascadingCut(HeapNode x) {
        if (x == null) {
            return;
        }
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


        public int getKey() {
            return key;
        }
    }
}
