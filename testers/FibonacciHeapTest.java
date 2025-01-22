public class FibonacciHeapTest {
    public static void main(String[] args) {
        FibonacciHeapTest tester = new FibonacciHeapTest();
        tester.runAllTests();
    }

    public void runAllTests() {
        try {
            testBasicOperations();
            testMeld();
            testDecreaseKey();
            testDelete();
            testEdgeCases();
            testStressTest();
            testRandomMeld();
            testRandomDecreaseKey();
            testNumTrees();
            testSize();
            testFindMin();
            testInsert();
            testDeleteMin();
            System.out.println("All tests passed successfully! ✅");
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testBasicOperations() {
        System.out.println("Testing basic operations...");
        FibonacciHeap heap = new FibonacciHeap();

        heap.insert(4, "four");
        heap.insert(2, "two");
        heap.insert(6, "six");
        assert heap.findMin().key == 2 : "Minimum should be 2";
        assert heap.size() == 3 : "Size should be 3";

        heap.deleteMin();
        assert heap.findMin().key == 4 : "After deleteMin, minimum should be 4";
        assert heap.size() == 2 : "After deleteMin, size should be 2";

        System.out.println("✅ Basic operations test passed");
    }

    private void testMeld() {
        System.out.println("Testing meld operation...");
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        heap1.insert(3, "three");
        heap1.insert(5, "five");
        heap2.insert(1, "one");
        heap2.insert(4, "four");

        int originalSize = heap1.size() + heap2.size();
        heap1.meld(heap2);

        assert heap1.findMin().key == 1 : "After meld, minimum should be 1";
        assert heap1.size() == originalSize : "After meld, size should be sum of original sizes";

        System.out.println("✅ Meld test passed");
    }

    private void testDecreaseKey() {
        System.out.println("Testing decreaseKey operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node1 = heap.insert(5, "five");
        heap.insert(3, "three");

        heap.decreaseKey(node1, 4);
        assert heap.findMin().key == 1 : "After decreaseKey, minimum should be 1";
        assert heap.findMin() == node1 : "After decreaseKey, node1 should be minimum";

        System.out.println("✅ DecreaseKey test passed");
    }

    private void testDelete() {
        System.out.println("Testing delete operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node1 = heap.insert(5, "five");
        heap.insert(3, "three");
        heap.insert(7, "seven");

        int originalSize = heap.size();
        heap.delete(node1);

        assert heap.size() == originalSize - 1 : "After delete, size should decrease by 1";
        assert heap.findMin().key == 3 : "After delete, minimum should be 3";

        System.out.println("✅ Delete test passed");
    }

    private void testEdgeCases() {
        System.out.println("Testing edge cases...");
        FibonacciHeap heap = new FibonacciHeap();

        assert heap.findMin() == null : "Empty heap should have null minimum";
        assert heap.size() == 0 : "Empty heap should have size 0";

        FibonacciHeap.HeapNode node = heap.insert(1, "one");
        assert heap.size() == 1 : "Size should be 1 after single insert";
        heap.delete(node);
        assert heap.size() == 0 : "Size should be 0 after deleting only node";
        assert heap.findMin() == null : "Minimum should be null after deleting only node";

        // These should not throw exceptions
        heap.delete(null);
        heap.decreaseKey(null, 1);
        heap.meld(null);

        System.out.println("✅ Edge cases test passed");
    }

    private void testStressTest() {
        System.out.println("Running stress test...");
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[1000];

        for (int i = 0; i < 1000; i++) {
            nodes[i] = heap.insert(1000 - i, String.valueOf(1000 - i));
        }

        assert heap.size() == 1000 : "Size should be 1000 after insertions";
        assert heap.findMin().key == 1 : "Minimum should be 1";

        for (int i = 0; i < 500; i++) {
            heap.delete(nodes[i]);
        }

        assert heap.size() == 500 : "Size should be 500 after deletions";
        assert heap.findMin().key == 1 : "Minimum should be 1 after deletions";

        for (int i = 500; i < 1000; i++) {
            heap.decreaseKey(nodes[i], 100);
        }

        assert heap.findMin().key == -99 : "Minimum should be -99 after decreaseKey operations";

        System.out.println("✅ Stress test passed");
    }

    private void testRandomMeld() {
        System.out.println("Testing random meld...");
        java.util.Random rand = new java.util.Random(42);

        for (int test = 0; test < 10; test++) {
            FibonacciHeap heap1 = new FibonacciHeap();
            FibonacciHeap heap2 = new FibonacciHeap();

            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int size1 = rand.nextInt(20) + 1;
            int size2 = rand.nextInt(20) + 1;

            for (int i = 0; i < size1; i++) {
                int key = rand.nextInt(1000);
                heap1.insert(key, "key" + key);
                min1 = Math.min(min1, key);
            }

            for (int i = 0; i < size2; i++) {
                int key = rand.nextInt(1000);
                heap2.insert(key, "key" + key);
                min2 = Math.min(min2, key);
            }

            int expectedMin = Math.min(min1, min2);
            heap1.meld(heap2);

            assert heap1.findMin().key == expectedMin : "Incorrect minimum after random meld";
            assert heap1.size() == size1 + size2 : "Incorrect size after random meld";
        }
        System.out.println("✅ Random meld test passed");
    }

    private void testRandomDecreaseKey() {
        System.out.println("Testing random decreaseKey...");
        FibonacciHeap heap = new FibonacciHeap();
        java.util.Random rand = new java.util.Random(42);

        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[50];
        int[] keys = new int[50];

        for (int i = 0; i < 50; i++) {
            int key = rand.nextInt(1000) + 1000;
            nodes[i] = heap.insert(key, "key" + key);
            keys[i] = key;
        }

        for (int i = 0; i < 100; i++) {
            int nodeIndex = rand.nextInt(50);
            int decreaseAmount = rand.nextInt(keys[nodeIndex]);

            heap.decreaseKey(nodes[nodeIndex], decreaseAmount);
            keys[nodeIndex] -= decreaseAmount;

            int expectedMin = Integer.MAX_VALUE;
            for (int key : keys) {
                expectedMin = Math.min(expectedMin, key);
            }

            assert heap.findMin().key == expectedMin :
                    "Incorrect minimum after random decreaseKey. Expected: " + expectedMin;
        }
        System.out.println("✅ Random decreaseKey test passed");
    }

    private void testNumTrees() {
        System.out.println("Testing number of trees...");
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[10];

        for (int i = 0; i < 10; i++) {
            nodes[i] = heap.insert(i, "key" + i);
        }

        assert heap.numTrees() == 10 : "Initial number of trees should be 10";

        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();

        assert heap.numTrees() == 3 : "Number of trees after delete operations should be 3";

        FibonacciHeap heap2 = new FibonacciHeap();
        for (int i = 0; i < 3; i++) {
            heap2.insert(i, "key" + i);
        }

        heap.meld(heap2);
        assert heap.numTrees() == 6 : "Number of trees after meld should be 6";

        System.out.println("✅ Number of trees test passed");
    }

    private void testSize() {
        System.out.println("Testing size...");
        FibonacciHeap heap = new FibonacciHeap();
        assert heap.size() == 0 : "Initial size should be 0";

        heap.insert(1, "one");
        assert heap.size() == 1 : "Size should be 1 after insert";

        heap.insert(2, "two");
        assert heap.size() == 2 : "Size should be 2 after another insert";

        heap.deleteMin();
        assert heap.size() == 1 : "Size should be 1 after deleteMin";

        heap.deleteMin();
        assert heap.size() == 0 : "Size should be 0 after another deleteMin";

        System.out.println("✅ Size test passed");
    }

    private void testFindMin() {
        System.out.println("Testing findMin...");
        FibonacciHeap heap = new FibonacciHeap();
        assert heap.findMin() == null : "Empty heap should have null minimum";

        heap.insert(1, "one");
        assert heap.findMin().key == 1 : "Minimum should be 1 after insert";

        heap.insert(2, "two");
        assert heap.findMin().key == 1 : "Minimum should be 1 after another insert";

        heap.deleteMin();
        assert heap.findMin().key == 2 : "Minimum should be 2 after deleteMin";

        heap.insert(0, "zero");
        assert heap.findMin().key == 0 : "Minimum should be 0 after insert";

        System.out.println("✅ FindMin test passed");
    }

    private void testInsert() {
        System.out.println("Testing insert...");
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node = heap.insert(1, "one");

        assert heap.size() == 1 : "Size should be 1 after insert";
        assert heap.findMin().key == 1 : "Minimum should be 1 after insert";
        assert heap.findMin() == node : "Node should be minimum after insert";

        System.out.println("✅ Insert test passed");
    }

    private void testDeleteMin() {
        System.out.println("Testing deleteMin...");
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(1, "one");
        heap.insert(2, "two");
        heap.insert(3, "three");

        heap.deleteMin();
        assert heap.findMin().key == 2 : "Minimum should be 2 after deleteMin";
        assert heap.size() == 2 : "Size should be 2 after deleteMin";

        heap.deleteMin();
        assert heap.findMin().key == 3 : "Minimum should be 3 after another deleteMin";
        assert heap.size() == 1 : "Size should be 1 after another deleteMin";

        heap.deleteMin();
        assert heap.findMin() == null : "Minimum should be null after last deleteMin";
        assert heap.size() == 0 : "Size should be 0 after last deleteMin";

        // This should not throw an exception
        heap.deleteMin();

        java.util.Random rand = new java.util.Random(42);
        heap.insert(1000, "thousand");
        for (int i = 0; i < 999; i++) heap.insert(rand.nextInt(999), "key" + i);
        for (int i = 0; i < 999; i++) heap.deleteMin();
        assert heap.findMin().key == 1000 : "Minimum should be 1000 after deleting random nodes";

        System.out.println("✅ DeleteMin test passed");
    }
}