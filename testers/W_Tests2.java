//FibonacciHeap Tester

import java.util.ArrayList;
import java.util.Collections;

public class W_Tests2 {

    public static void main(String[] args) {
        W_Tests2 tester = new W_Tests2();
        tester.runAllTests();
    }

    public void runAllTests() {
        try {
            // Basic tests
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
            // Additional tests
            test0_BasicOperations(); // test0
            test1_InsertAndDelete(); // test1
            test2_DeleteMiddleNodes(); // test2
            test3_EdgeCases(); // test3
            test4_LargeInput(); // test4
            test5_DecreaseKeyBehavior(); // test5
            test6_CascadingCuts(); // test6
            test7_LargeRandomInsertions(); // test7
            test8_HeapSizeConsistency(); // test8
            test9_EdgeCaseSingleNode(); // test9
            test10_EdgeCaseEmptyHeap(); // test10
            test11(); // test11
            test12(); // test12
            test13(); // test13
            test14(); // test14
            test15(); // test15
            test16(); // test16
            test17(); // test17
            test18(); // test18
            test19(); // test19
            test20(); // test20
            test21(); // test21
            test22(); // test22
            test23(); // test23
            test24(); // test24
            test25(); // test25
            test26(); // test26
            test27(); // test27
            test28(); // test28
            test29(); // test29
            test30(); // test30
            // Extended test cases
            testLargeBasicOperations();
            testLargeMeld();
            testExtremeDecreaseKey();
            testExtremeDelete();
            testLargeEdgeCases();
            testLargeStressTest();
            System.out.println("All tests passed successfully! ✅");
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Helper methods to validate heap structure
    private boolean checkMinHeapProperty(FibonacciHeap heap) {
        FibonacciHeap.HeapNode current = heap.min;
        if (current == null) return true;

        // Traverse the root list and check the min-heap property
        do {
            // Check if the current node's key is smaller than its children's keys
            if (current.child != null) {
                FibonacciHeap.HeapNode child = current.child;
                do {
                    if (current.key > child.key) {
                        return false; // Min-heap property violated
                    }
                    child = child.next;
                } while (child != current.child);
            }
            current = current.next;
        } while (current != heap.min);

        return true; // Min-heap property is satisfied
    }


    private void testBasicOperations() {
        System.out.println("Testing basic operations...");
        FibonacciHeap heap = new FibonacciHeap();

        heap.insert(4, "four");
        heap.insert(2, "two");
        heap.insert(6, "six");
        if (heap.findMin().key != 2) throw new AssertionError("Minimum should be 2");
        if (heap.size() != 3) throw new AssertionError("Size should be 3");

        heap.deleteMin();
        if (heap.findMin().key != 4) throw new AssertionError("After deleteMin, minimum should be 4");
        if (heap.size() != 2) throw new AssertionError("After deleteMin, size should be 2");

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
        if (heap1.findMin().key != 1) throw new AssertionError("After meld, minimum should be 1");
        if (heap1.size() != originalSize) throw new AssertionError("After meld, size should be sum of original sizes");

        System.out.println("✅ Meld test passed");
    }

    private void testDecreaseKey() {
        System.out.println("Testing decreaseKey operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node1 = heap.insert(5, "five");
        heap.insert(3, "three");

        heap.decreaseKey(node1, 4);
        if (heap.findMin().key != 1) throw new AssertionError("After decreaseKey, minimum should be 1");
        if (heap.findMin() != node1) throw new AssertionError("After decreaseKey, node1 should be minimum");

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
        if (heap.size() != originalSize - 1) throw new AssertionError("After delete, size should decrease by 1");
        if (heap.findMin().key != 3) throw new AssertionError("After delete, minimum should be 3");

        System.out.println("✅ Delete test passed");
    }

    private void testEdgeCases() {
        System.out.println("Testing edge cases...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.findMin() != null) throw new AssertionError("Empty heap should have null minimum");
        if (heap.size() != 0) throw new AssertionError("Empty heap should have size 0");

        FibonacciHeap.HeapNode node = heap.insert(1, "one");
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after single insert");
        heap.delete(node);
        if (heap.size() != 0) throw new AssertionError("Size should be 0 after deleting only node");
        if (heap.findMin() != null) throw new AssertionError("Minimum should be null after deleting only node");

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
        if (heap.size() != 1000) throw new AssertionError("Size should be 1000 after insertions");
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1");

        for (int i = 0; i < 500; i++) {
            heap.delete(nodes[i]);
        }
        if (heap.size() != 500) throw new AssertionError("Size should be 500 after deletions");
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after deletions");

        for (int i = 500; i < 1000; i++) {
            heap.decreaseKey(nodes[i], 100);
        }
        if (heap.findMin().key != -99) throw new AssertionError("Minimum should be -99 after decreaseKey operations");

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
            if (heap1.findMin().key != expectedMin) throw new AssertionError("Incorrect minimum after random meld");
            if (heap1.size() != size1 + size2) throw new AssertionError("Incorrect size after random meld");
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
            if (heap.findMin().key != expectedMin)
                throw new AssertionError("Incorrect minimum after random decreaseKey. Expected: " + expectedMin);
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
        if (heap.numTrees() != 10) throw new AssertionError("Initial number of trees should be 10");
        
        heap.display();

        heap.deleteMin();
        heap.deleteMin();
        heap.deleteMin();
        if (heap.numTrees() != 3) throw new AssertionError("Number of trees after delete operations should be 3");

        FibonacciHeap heap2 = new FibonacciHeap();
        for (int i = 0; i < 3; i++) {
            heap2.insert(i, "key" + i);
        }

        heap.meld(heap2);
        if (heap.numTrees() != 6) throw new AssertionError("Number of trees after meld should be 6");

        System.out.println("✅ Number of trees test passed");
    }

    private void testSize() {
        System.out.println("Testing size...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.size() != 0) throw new AssertionError("Initial size should be 0");

        heap.insert(1, "one");
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after insert");

        heap.insert(2, "two");
        if (heap.size() != 2) throw new AssertionError("Size should be 2 after another insert");

        heap.deleteMin();
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after deleteMin");

        heap.deleteMin();
        if (heap.size() != 0) throw new AssertionError("Size should be 0 after another deleteMin");

        System.out.println("✅ Size test passed");
    }

    private void testFindMin() {
        System.out.println("Testing findMin...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.findMin() != null) throw new AssertionError("Empty heap should have null minimum");

        heap.insert(1, "one");
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after insert");

        heap.insert(2, "two");
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after another insert");

        heap.deleteMin();
        if (heap.findMin().key != 2) throw new AssertionError("Minimum should be 2 after deleteMin");

        heap.insert(0, "zero");
        if (heap.findMin().key != 0) throw new AssertionError("Minimum should be 0 after insert");

        System.out.println("✅ FindMin test passed");
    }

    private void testInsert() {
        System.out.println("Testing insert...");
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode node = heap.insert(1, "one");
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after insert");
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after insert");
        if (heap.findMin() != node) throw new AssertionError("Node should be minimum after insert");

        System.out.println("✅ Insert test passed");
    }

    private void testDeleteMin() {
        System.out.println("Testing deleteMin...");
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(1, "one");
        heap.insert(2, "two");
        heap.insert(3, "three");

        heap.deleteMin();
        if (heap.findMin().key != 2) throw new AssertionError("Minimum should be 2 after deleteMin");
        if (heap.size() != 2) throw new AssertionError("Size should be 2 after deleteMin");

        heap.deleteMin();
        if (heap.findMin().key != 3) throw new AssertionError("Minimum should be 3 after another deleteMin");
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after another deleteMin");

        heap.deleteMin();
        if (heap.findMin() != null) throw new AssertionError("Minimum should be null after last deleteMin");
        if (heap.size() != 0) throw new AssertionError("Size should be 0 after last deleteMin");

        // This should not throw an exception
        heap.deleteMin();

        java.util.Random rand = new java.util.Random(42);
        heap.insert(1000, "thousand");
        for (int i = 0; i < 999; i++) heap.insert(rand.nextInt(999), "key" + i);
        for (int i = 0; i < 999; i++) heap.deleteMin();
        if (heap.findMin().key != 1000) throw new AssertionError("Minimum should be 1000 after deleting random nodes");

        System.out.println("✅ DeleteMin test passed");
    }

    private void test0_BasicOperations() { // test0
        System.out.println("Testing basic operations...");
        FibonacciHeap heap = new FibonacciHeap();

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        for (int num : numbers) {
            heap.insert(num, "key" + num);
        }

        for (int i = 0; i < 100; i++) {
            assert heap.findMin().key == i : "Minimum should be " + i;
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Basic operations test passed, test0");
    }

    private void test1_InsertAndDelete() { // test1
        System.out.println("Testing insert and delete...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 1; i <= 10; i++) {
            heap.insert(i, "key" + i);
        }

        for (int i = 1; i <= 10; i++) {
            assert heap.findMin().getKey() == i : "Minimum should be " + i;
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Insert and delete test passed, test1");
    }

    private void test2_DeleteMiddleNodes() { // test2
        System.out.println("Testing deletion of middle nodes...");
        FibonacciHeap heap = new FibonacciHeap();

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            nodes.add(heap.insert(i, "key" + i));
        }

        heap.delete(nodes.get(4)); // Deleting node with key 5

        for (int i = 1; i <= 4; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }

        for (int i = 6; i <= 10; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Middle node deletion test passed, test2");
    }

    private void test3_EdgeCases() { // test3
        System.out.println("Testing edge cases...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.findMin() != null) throw new AssertionError("Minimum of empty heap should be null");
        if (!heap.empty()) throw new AssertionError("Heap should be empty initially");

        FibonacciHeap.HeapNode node = heap.insert(1, "key1");
        heap.delete(node);
        if (heap.findMin() != null) throw new AssertionError("Heap should be empty after deleting the only node");
        if (!heap.empty()) throw new AssertionError("Heap should be empty after deleting the only node");
        System.out.println("✅ Edge cases test passed, test3");
    }

    private void test4_LargeInput() { // test4
        System.out.println("Testing large input handling...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 1000; i >= 1; i--) {
            heap.insert(i, "key" + i);
        }

        for (int i = 1; i <= 1000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Large input test passed, test4");
    }

    private void test5_DecreaseKeyBehavior() { // test5
        System.out.println("Testing decreaseKey operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node1 = heap.insert(10, "key10");
        FibonacciHeap.HeapNode node2 = heap.insert(20, "key20");

        heap.decreaseKey(node2, 10);
        if (heap.findMin().getKey() != 10) throw new AssertionError("Minimum should still be 10");

        heap.decreaseKey(node2, 5);
        if (heap.findMin().getKey() != 5) throw new AssertionError("Minimum should now be 5");
        System.out.println("✅ DecreaseKey behavior test passed, test5");
    }

    private void test6_CascadingCuts() { // test6
        System.out.println("Testing cascading cuts...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node1 = heap.insert(10, "key10");
        FibonacciHeap.HeapNode node2 = heap.insert(20, "key20");
        FibonacciHeap.HeapNode node3 = heap.insert(30, "key30");

        heap.decreaseKey(node3, 15); // node3 becomes 15
        heap.decreaseKey(node2, 15);  // node2 becomes 5
        if (heap.findMin().getKey() != 5) throw new AssertionError("Minimum should now be 5");
        System.out.println("✅ Cascading cuts test passed, test6");
    }

    private void test7_LargeRandomInsertions() { // test7
        System.out.println("Testing large random insertions...");
        FibonacciHeap heap = new FibonacciHeap();

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        for (int num : numbers) {
            heap.insert(num, "key" + num);
        }

        for (int i = 1; i <= 10000; i++) {
            assert heap.findMin().getKey() == i : "Minimum should be " + i;
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Large random insertions test passed, test7");
    }

    private void test8_HeapSizeConsistency() { // test8
        System.out.println("Testing heap size consistency...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 1; i <= 500; i++) {
            heap.insert(i, "key" + i);
            if (heap.size() != i) throw new AssertionError("Heap size should be " + i);
        }

        for (int i = 1; i <= 500; i++) {
            heap.deleteMin();
            if (heap.size() != 500 - i) throw new AssertionError("Heap size should be " + (500 - i));
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Heap size consistency test passed, test8");
    }

    private void test9_EdgeCaseSingleNode() { // test9
        System.out.println("Testing edge case with a single node...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode node = heap.insert(42, "key42");
        if (heap.findMin() != node) throw new AssertionError("Minimum should be the only node");

        heap.delete(node);
        if (!heap.empty()) throw new AssertionError("Heap should be empty after deleting the only node");
        System.out.println("✅ Single node edge case test passed, test9");
    }

    private void test10_EdgeCaseEmptyHeap() { // test10
        System.out.println("Testing edge case with an empty heap...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.findMin() != null) throw new AssertionError("Minimum of empty heap should be null");
        if (heap.size() != 0) throw new AssertionError("Size of empty heap should be 0");

        System.out.println("✅ Empty heap edge case test passed, test10");
    }
    private void test11() { // test11
        System.out.println("Running test11...");
        FibonacciHeap heap = new FibonacciHeap();
        addKeys(heap, 1000);
        FibonacciHeap.HeapNode h = heap.insert(9999, "9999");
        heap.decreaseKey(h, 9999);
        if (heap.findMin().getKey() != 0) throw new AssertionError("Minimum should be 0 after decreaseKey");

        heap.deleteMin();
        for (int i = 1000; i < 2000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test11 passed");
    }

    private void test12() { // test12
        System.out.println("Running test12...");
        FibonacciHeap heap = new FibonacciHeap();
        addKeys(heap, 1000);
        FibonacciHeap.HeapNode h = heap.insert(5000, "5000");
        heap.decreaseKey(h, 4000);

        for (int i = 0; i < 2; i++) {
            if (heap.findMin().getKey() != 1000) throw new AssertionError("Minimum should be 1000");
            heap.deleteMin();
        }

        for (int i = 1001; i < 2000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test12 passed");
    }

    private void test13() { // test13
        System.out.println("Running test13...");
        FibonacciHeap heap = new FibonacciHeap();
        addKeys(heap, 1000);
        FibonacciHeap.HeapNode h = heap.insert(9000, "9000");
        heap.decreaseKey(h, 4000);

        for (int i = 1000; i < 2000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (heap.findMin().getKey() != 5000) throw new AssertionError("Minimum should now be 5000");
        heap.deleteMin();
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test13 passed");
    }

    private void test14() { // test14
        System.out.println("Running test14...");
        FibonacciHeap heap = new FibonacciHeap();
        addKeys(heap, 1000);
        addKeysReverse(heap, 7000);
        FibonacciHeap.HeapNode h = heap.insert(9000, "9000");
        heap.decreaseKey(h, 4000);

        for (int i = 1000; i < 2000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (heap.findMin().getKey() != 5000) throw new AssertionError("Minimum should now be 5000");
        heap.deleteMin();

        for (int i = 7000; i < 8000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test14 passed");
    }

    private void test15() { // test15
        System.out.println("Running test15...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 1000; i < 10000; i += 1000) {
            addKeys(heap, i);
        }

        heap.deleteMin();

        FibonacciHeap.HeapNode h = heap.insert(99999, "99999");
        heap.decreaseKey(h, 99999);
        if (heap.findMin().getKey() != 0) throw new AssertionError("Minimum should be 0 after decreaseKey");
        heap.deleteMin();

        for (int i = 1001; i < 10000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should be " + i);
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test15 passed");
    }

    private void test16() { // test16
        System.out.println("Running test16...");
        FibonacciHeap heap = new FibonacciHeap();

        int cuts = heap.totalCuts();
        int links = heap.totalLinks();

        heap.insert(1, "1");
        heap.insert(2, "2");
        heap.insert(3, "3");
        // Check the heap structure
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts or links have occurred yet
        if (heap.totalCuts() - cuts != 0) throw new AssertionError("No cuts should have occurred");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");

        System.out.println("✅ Test16 passed");
    }

    private void test17() { // test17
        System.out.println("Running test17...");
        FibonacciHeap heap = new FibonacciHeap();

        int cuts = heap.totalCuts();
        int links = heap.totalLinks();

        heap.insert(1, "1");
        heap.insert(2, "2");
        heap.insert(3, "3");
        heap.deleteMin();
        // Check the heap structure
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts occurred after the deleteMin()
        if (heap.totalCuts() - cuts != 0) throw new AssertionError("No cuts should have occurred");
        // Ensure one link occurred due to tree consolidation
        if (heap.totalLinks() - links != 1) throw new AssertionError("One link should have occurred");

        // Ensure rank distribution is correct
        if (heap.min.rank != 1 || heap.min.child.rank != 0) throw new AssertionError("Invalid rank distribution.");

        System.out.println("✅ Test17 passed");
    }

    private void test18() { // test18
        System.out.println("Running test18...");
        FibonacciHeap heap = new FibonacciHeap();

        int cuts = heap.totalCuts();
        int links = heap.totalLinks();

        heap.insert(4, "4");
        heap.insert(5, "5");
        heap.insert(6, "6");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.insert(2, "2");
        heap.insert(3, "3");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.deleteMin();
        // Check the heap structure
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts occurred after operations
        if (heap.totalCuts() - cuts != 0) throw new AssertionError("No cuts should have occurred");
        // Ensure three links occurred due to tree consolidation
        if (heap.totalLinks() - links != 3) throw new AssertionError("Three links should have occurred");

        // Check rank distribution
        if (heap.min.rank != 2 && heap.min.child.rank != 0 && heap.min.child.next.rank != 1 && heap.min.child.next.child.rank != 0) throw new AssertionError("Invalid rank distribution.");

        System.out.println("✅ Test18 passed");
    }

    private void test19() { // test19
        System.out.println("Running test19...");
        FibonacciHeap heap = new FibonacciHeap();

        int cuts = heap.totalCuts();
        int links = heap.totalLinks();

        heap.insert(4, "4");
        heap.insert(5, "5");
        FibonacciHeap.HeapNode node = heap.insert(6, "6");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.insert(2, "2");
        heap.insert(3, "3");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.deleteMin();
        heap.display();
        heap.decreaseKey(node, 2);
        if (heap.size() != 4) {
            throw new AssertionError("Expected 4 nodes, but found " + heap.size());
        }
        // Ensure the heap structure
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Check total cuts and links
        if (heap.totalCuts() - cuts != 1) throw new AssertionError("One cut should have occurred");
        if (heap.totalLinks() - links != 3) throw new AssertionError("Three links should have occurred");

        System.out.println("✅ Test19 passed");
    }

    private void test20() { // test20
        System.out.println("Running test20...");
        FibonacciHeap heap = new FibonacciHeap();

        heap.insert(4, "4");
        FibonacciHeap.HeapNode node5 = heap.insert(5, "5");
        FibonacciHeap.HeapNode node6 = heap.insert(6, "6");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.insert(2, "2");
        heap.insert(3, "3");
        heap.deleteMin();

        heap.insert(1, "1");
        heap.deleteMin();

        int cuts = heap.totalCuts();
        int links = heap.totalLinks();

        heap.decreaseKey(node6, 2);
        heap.decreaseKey(node5, 1);
        // Ensure heap's structure is correct
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Check total cuts and links
        if (heap.totalCuts() - cuts != 1) throw new AssertionError("One cut should have occurred");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");

        System.out.println("✅ Test20 passed");
    }

    private void test21() { // test21
        System.out.println("Running test21...");
        FibonacciHeap heap = new FibonacciHeap();

        int treeSize = 32768;
        int sizeToDelete = 1000;

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        for (int i = treeSize; i < treeSize * 2; i++) {
            nodes.add(heap.insert(i, String.valueOf(i)));
        }

        for (int i = 0; i < sizeToDelete; i++) {
            heap.insert(i, String.valueOf(i));
        }

        for (int i = 0; i < sizeToDelete; i++) {
            heap.deleteMin();
        }
        // Ensure heap's structure is correct after deletions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        System.out.println("✅ Test21 passed");
    }

    private void test22() { // test22
        System.out.println("Running test22...");
        FibonacciHeap heap = new FibonacciHeap();

        int treeSize = 32768;
        int sizeToDelete = 1000;

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        for (int i = treeSize; i < treeSize * 2; i++) {
            nodes.add(heap.insert(i, String.valueOf(i)));
        }

        for (int i = 0; i < sizeToDelete; i++) {
            heap.insert(i, String.valueOf(i));
        }

        for (int i = 0; i < sizeToDelete; i++) {
            heap.deleteMin();
        }
        // Check the heap structure after deletions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        boolean noCascading = true;
        int iterationCuts;

        Collections.shuffle(nodes);

        for (int i = 0; i < treeSize; i++) {
            iterationCuts = heap.totalCuts();

            heap.decreaseKey(nodes.get(i), nodes.get(i).getKey() - (treeSize - i));

            if (heap.totalCuts() - iterationCuts > 1) noCascading = false;
        }
        // Check cuts, links, and cascading cut behavior
        if (heap.totalCuts() - totalCuts != treeSize - 1) throw new AssertionError("Cuts count mismatch");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");
        if (!noCascading) throw new AssertionError("Cascading cuts detected");

        // Ensure number of trees is as expected
        if (heap.size() != treeSize) throw new AssertionError("Tree size mismatch");

        System.out.println("✅ Test22 passed");
    }

    private void test23() { // test23
        System.out.println("Running test23...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 1000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }
        // Check the heap structure after insertions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts or links occurred
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");

        // Ensure the number of trees matches the expected size
        if (heap.size() != size) throw new AssertionError("Size mismatch");

        System.out.println("✅ Test23 passed");
    }

    private void test24() { // test24
        System.out.println("Running test24...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 2000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }
        // Check the heap structure after insertions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts or links occurred
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");

        // Ensure the number of trees matches the expected size
        if (heap.size() != size) throw new AssertionError("Size mismatch");

        System.out.println("✅ Test24 passed");
    }

    private void test25() { // test25
        System.out.println("Running test25...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 3000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }
        // Check the heap structure after insertions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts or links occurred
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");
        if (heap.totalLinks() - links != 0) throw new AssertionError("No links should have occurred");

        // Ensure the number of trees matches the expected size
        if (heap.size() != size) throw new AssertionError("Size mismatch");

        System.out.println("✅ Test25 passed");
    }

    private void test26() { // test26
        System.out.println("Running test26...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 1000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }

        for (int i = 0; i < size / 2; i++) {
            if (heap.findMin().getKey() != i + 1) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }

        // Check the heap structure after deletions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts occurred after deletions
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");

        // Ensure links count is within expected range
        if (heap.totalLinks() - links < size - 100) throw new AssertionError("Links count mismatch");

        System.out.println("✅ Test26 passed");
    }

    private void test27() { // test27
        System.out.println("Running test27...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 2000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }

        for (int i = 0; i < size / 2; i++) {
            if (heap.findMin().getKey() != i + 1) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }
        // Check the heap structure after deletions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts occurred after deletions
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");

        // Ensure links count is within expected range
        if (heap.totalLinks() - links < size - 100) throw new AssertionError("Links count mismatch");

        System.out.println("✅ Test27 passed");
    }

    private void test28() { // test28
        System.out.println("Running test28...");
        FibonacciHeap heap = new FibonacciHeap();

        int size = 3000;
        int totalCuts = heap.totalCuts();
        int links = heap.totalLinks();

        for (int i = size; i > 0; i--) {
            heap.insert(i, String.valueOf(i));
        }

        for (int i = 0; i < size / 2; i++) {
            if (heap.findMin().getKey() != i + 1) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }
        // Check the heap structure after deletions
        if (!checkMinHeapProperty(heap)) throw new AssertionError("Min-Heap property violated.");

        // Ensure no cuts occurred after deletions
        if (heap.totalCuts() - totalCuts != 0) throw new AssertionError("No cuts should have occurred");

        // Ensure links count is within expected range
        if (heap.totalLinks() - links < size - 100) throw new AssertionError("Links count mismatch");

        System.out.println("✅ Test28 passed");
    }

    private void test29() { // test29
        System.out.println("Running test29...");
        FibonacciHeap heap = new FibonacciHeap();

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            nodes.add(heap.insert(i, "key" + i));
        }

        for (int i = 0; i < 500; i++) {
            heap.delete(nodes.get(i));
        }

        for (int i = 500; i < 1000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test29 passed");
    }

    private void test30() { // test30
        System.out.println("Running test30...");
        FibonacciHeap heap = new FibonacciHeap();

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        for (int i = 1000; i > 0; i--) {
            nodes.add(heap.insert(i, "key" + i));
        }

        heap.delete(nodes.get(500)); // Deleting a middle node

        for (int i = 1; i < 500; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }

        for (int i = 501; i <= 1000; i++) {
            if (heap.findMin().getKey() != i) throw new AssertionError("Minimum should match");
            heap.deleteMin();
        }
        if (!heap.empty()) throw new AssertionError("Heap should be empty after all deletions");
        System.out.println("✅ Test30 passed");
    }
    private void addKeys(FibonacciHeap heap, int start) {
        for (int i = start; i < start + 1000; i++) {
            heap.insert(i, "key" + i);
        }
    }
    private void addKeysReverse(FibonacciHeap heap, int start) {
        for (int i = start + 999; i >= start; i--) {
            heap.insert(i, "key" + i);
        }
    }
    private void testLargeBasicOperations() {
        System.out.println("Testing large basic operations...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 1; i <= 10000; i++) {
            heap.insert(i, "key" + i);
        }
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1");
        if (heap.size() != 10000) throw new AssertionError("Size should be 10000");

        for (int i = 0; i < 5000; i++) {
            heap.deleteMin();
        }
        if (heap.findMin().key != 5001) throw new AssertionError("Minimum should be 5001 after deleting 5000 elements");
        if (heap.size() != 5000) throw new AssertionError("Size should be 5000 after deleting 5000 elements");

        System.out.println("✅ Large basic operations test passed");
    }

    private void testLargeMeld() {
        System.out.println("Testing large meld operation...");
        FibonacciHeap heap1 = new FibonacciHeap();
        FibonacciHeap heap2 = new FibonacciHeap();

        for (int i = 1; i <= 5000; i++) {
            heap1.insert(i, "key" + i);
            heap2.insert(5000 + i, "key" + (5000 + i));
        }

        heap1.meld(heap2);
        if (heap1.findMin().key != 1) throw new AssertionError("Minimum should be 1 after meld");
        if (heap1.size() != 10000) throw new AssertionError("Size should be 10000 after meld");

        System.out.println("✅ Large meld test passed");
    }

    private void testExtremeDecreaseKey() {
        System.out.println("Testing extreme decreaseKey operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[10000];

        for (int i = 0; i < 9999; i++) {
            nodes[i] = heap.insert(10000 - i, "key" + (10000 - i));
        }

        heap.decreaseKey(nodes[0], 9999);
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after decreaseKey");
        if (heap.findMin() != nodes[0]) throw new AssertionError("Node with key 1 should be the minimum");

        System.out.println("✅ Extreme decreaseKey test passed");
    }

    private void testExtremeDelete() {
        System.out.println("Testing extreme delete operation...");
        FibonacciHeap heap = new FibonacciHeap();

        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[10000];

        for (int i = 1; i <= 10000; i++) {
            nodes[i - 1] = heap.insert(i, "key" + i);
        }

        for (int i = 9999; i >= 0; i--) {
            heap.delete(nodes[i]);
        }
        if (heap.size() != 0) throw new AssertionError("Heap size should be 0 after deleting all elements");
        if (heap.findMin() != null) throw new AssertionError("Minimum should be null after deleting all elements");

        System.out.println("✅ Extreme delete test passed");
    }

    private void testLargeEdgeCases() {
        System.out.println("Testing large edge cases...");
        FibonacciHeap heap = new FibonacciHeap();
        if (heap.findMin() != null) throw new AssertionError("Empty heap should have null minimum");
        if (heap.size() != 0) throw new AssertionError("Empty heap should have size 0");

        FibonacciHeap.HeapNode node = heap.insert(Integer.MAX_VALUE, "maxValue");
        if (heap.size() != 1) throw new AssertionError("Size should be 1 after single insert");
        if (heap.findMin().key != Integer.MAX_VALUE)
            throw new AssertionError("Minimum should be the max integer value");

        heap.decreaseKey(node, Integer.MAX_VALUE - 1);
        if (heap.findMin().key != 1) throw new AssertionError("Minimum should be 1 after decreasing key to 1");

        heap.delete(node);
        if (heap.size() != 0) throw new AssertionError("Heap size should be 0 after deleting only node");

        System.out.println("✅ Large edge cases test passed");
    }

    private void testLargeStressTest() {
        System.out.println("Running large stress test...");
        FibonacciHeap heap = new FibonacciHeap();

        for (int i = 0; i < 100000; i++) {
            heap.insert(i, "key" + i);
        }
        if (heap.size() != 100000) throw new AssertionError("Heap size should be 100000 after insertions");
        if (heap.findMin().key != 0) throw new AssertionError("Minimum should be 0");

        for (int i = 0; i < 50000; i++) {
            heap.deleteMin();
        }
        if (heap.size() != 50000) throw new AssertionError("Heap size should be 50000 after 50000 deletions");
        if (heap.findMin().key != 50000) throw new AssertionError("Minimum should be 50000 after deletions");

        System.out.println("✅ Large stress test passed");
    }
}
