public class BasicTests {

    public static void main(String[] args) {
        testInsertAndFindMin();
        testDeleteMin();
        testDecreaseKey();
        testDelete();
        testMeld();
        testTotalLinksAndCuts();
        testNumTrees();
        System.out.println("All tests passed.");
    }

    public static void testInsertAndFindMin() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");

        assertCondition(heap.findMin().key == 5, "testInsertAndFindMin: Minimum key should be 5");
        assertCondition("B".equals(heap.findMin().info), "testInsertAndFindMin: Minimum info should be 'B'");
    }

    public static void testDeleteMin() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");

        heap.deleteMin();
        assertCondition(heap.findMin().key == 10, "testDeleteMin: Minimum key should be 10");
        assertCondition("A".equals(heap.findMin().info), "testDeleteMin: Minimum info should be 'A'");
    }

    public static void testDecreaseKey() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10, "A");
        FibonacciHeap.HeapNode node2 = heap.insert(5, "B");
        FibonacciHeap.HeapNode node3 = heap.insert(20, "C");

        heap.decreaseKey(node3, 15); // Decrease 20 -> 5
        assertCondition(heap.findMin().key == 5, "testDecreaseKey: Minimum key should be 5");
        assertCondition(heap.findMin() == node3, "testDecreaseKey: Minimum node should be node3");
    }

    public static void testDelete() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10, "A");
        FibonacciHeap.HeapNode node2 = heap.insert(5, "B");
        FibonacciHeap.HeapNode node3 = heap.insert(20, "C");

        heap.display(); 
        heap.delete(node1);
        heap.display(); 
        assertCondition(heap.findMin().key == 5, "testDelete: Minimum key should be 5 after deleting node1");
        heap.delete(node2);
        assertCondition(heap.findMin().key == 20, "testDelete: Minimum key should be 20 after deleting node2");
    }

    public static void testMeld() {
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        heap1.insert(10, "A");
        heap1.insert(20, "B");

        heap2.insert(5, "C");
        heap2.insert(15, "D");

        heap1.meld(heap2);
        assertCondition(heap1.findMin().key == 5, "testMeld: Minimum key should be 5 after melding");
        assertCondition(heap1.size() == 4, "testMeld: Size should be 4 after melding");
    }

    public static void testTotalLinksAndCuts() {
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node1 = heap.insert(10, "A");
        FibonacciHeap.HeapNode node2 = heap.insert(5, "B");
        FibonacciHeap.HeapNode node3 = heap.insert(20, "C");

        heap.display(); 
        heap.decreaseKey(node3, 15); // This should trigger a cut
        heap.display(); 
        assertCondition(heap.totalCuts() == 0, "testTotalLinksAndCuts: Total cuts should be 0 because node3 has no parent");
        assertCondition(heap.totalLinks() == 0, "testTotalLinksAndCuts: Total links should be 0");

        heap.deleteMin(); // This should trigger consolidations and links
        assertCondition(heap.totalLinks() > 0, "testTotalLinksAndCuts: Total links should be greater than 0 after deleteMin");
    }

    public static void testNumTrees() {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(10, "A");
        heap.insert(5, "B");
        heap.insert(20, "C");

        assertCondition(heap.numTrees() == 3, "testNumTrees: Number of trees should be 3");
        heap.deleteMin();
        assertCondition(heap.numTrees() < 3, "testNumTrees: Number of trees should be less than 3 after deleteMin");
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}