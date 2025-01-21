import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TheoryPart {
    public static void main(String[] args) {
        
        for (int i = 1; i < 7; i++) {
            System.out.println("\ni: " + i);
            int n = (int)Math.pow(3, i + 5) - 1;
            System.out.println("n: " + n);
            BinomialHeap firstTestHeap = new BinomialHeap();

            long startTime = System.currentTimeMillis();

            /*for (int j = 1; j <= n; j++) {
                firstTestHeap.insert(j, Integer.toString(j));
                // if (numOfOnesInBinaryRep(j) != firstTestHeap.numTrees()) {
                //     System.out.println("ERROR in j: " + j);
                //     break;4
                // }

            }*/

            /*for (int j = n; j >= 1; j--) {
                //if (j == 1) {
                //    System.out.println("");
                //}
                firstTestHeap.insert(j, Integer.toString(j));
                // if (numOfOnesInBinaryRep(j) != firstTestHeap.numTrees()) {
                //     System.out.println("ERROR in j: " + j);
                //     break;4
                // }
            }*/

            //System.out.println("Num of trees before: " + firstTestHeap.numTrees());
            //System.out.println("Total links before: " + firstTestHeap.linkCounter);


            // Create a set to track which numbers have been inserted to avoid duplications
            Set<Integer> insertedNumbers = new HashSet<>();

            // Create a random number generator
            Random random = new Random();

            // Call insert on numbers 1 to n randomly
            for (int j = 1; j <= n; j++) {
                int randomNumber;
                
                // Generate a random number not already inserted
                do {
                    randomNumber = random.nextInt(n) + 1;
                } while (!insertedNumbers.add(randomNumber));

                // Call the insert function with the current number
                firstTestHeap.insert(randomNumber, Integer.toString(randomNumber));
            }

            for (int k = 1; k <= n/2; k++) {
                //System.out.println("k: " + k);
                firstTestHeap.deleteMin();
                //System.out.println("hi" + firstTestHeap.size);
                //if (firstTestHeap.size == Math.pow(2, 5) - 1) {
                //    break;
                //}

            }

            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;
            System.out.println("Runtime: " + runtime + " milliseconds");

            System.out.println("Total links: " + firstTestHeap.linkCounter);
            System.out.println("Num of trees: " + firstTestHeap.numTrees());
            System.out.println("Sum of deleted ranks: " + firstTestHeap.rankSum);
            //System.out.println("Min: " + firstTestHeap.findMin().key);
            //System.out.println("size: " + firstTestHeap.size);


        }


    }

    public static int numOfOnesInBinaryRep(int n) {
        int count = 0;

        // Count the number of 1's using bitwise operations
        while (n > 0) {
            count += n & 1; // Check the rightmost bit
            n >>= 1;       // Shift bits to the right
        }

        return count;
    }

    public static int findRealMin(BinomialHeap bh) {
        BinomialHeap.HeapNode curr = bh.last;
        BinomialHeap.HeapNode currMin = bh.last;
        do {
            if (curr.item.key < currMin.item.key) {
                currMin = curr;
            }
            curr = curr.next;
        }
        while (curr != bh.last);

        return currMin.item.key;
    }
    

}
