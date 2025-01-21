/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */

 public class BinomialHeap
 {
	 public int size;
	 public HeapNode last;
	 public HeapNode min;

	 /**
	  *
	  * Default BinomialHeap Constructor
	  */
	 public BinomialHeap() { }
 
	 /**
	  * 
	  * BinomialHeap Constructor that accepts node that is a root with maximal rank in heap
	  */
	 public BinomialHeap(HeapNode n) {
		 // n is root, therefore has no parent
		 n.parent = null;
		 last = n;
		 min = n;
		 size = (int)Math.pow(2, n.rank);
	 }
 
	 /**
	  * 
	  * pre: key > 0
	  *
	  * Insert (key,info) into the heap and return the newly generated HeapItem.
	  *
	  */
	 public HeapItem insert(int key, String info) 
	 {    
		 // Create HeapItem and HeapNode
		 HeapItem itemToInsert = new HeapItem(key, info);
		 HeapNode nodeToInsert = new HeapNode(itemToInsert);
 
		 // Create new heap that contains only the inserted node
		 BinomialHeap heapToInsert = new BinomialHeap(nodeToInsert);
 
		 // Meld new heap with this
		 meld(heapToInsert);
 
		 return itemToInsert;
	 }
 
	/**
	 * 
	* Delete the minimal item
	*
	*/
	public void deleteMin()
	{
		if (empty()) {
			return;
		}
		
		boolean isMinLast = last == min;

		// Find new min for this and end when curr.next is min
		HeapNode curr = min;
		HeapNode newMin = curr.next;
		do {
			curr = curr.next;
			if (curr.item.key <= newMin.item.key) {
				newMin = curr;
			}
		}
		while (curr.next != min);

		// In case the min is the only root in the heap
		if (newMin == min) {
			newMin = null;
			last = null;
		}
		// Min is not the only root in the heap
		else {
			// Delete minimum by changing pointers
			curr.next = min.next;
			min.next = null;

			// Check if updating the last field is needed (min was last)
			if (isMinLast) {
				last = curr;
			}
		}

		// Start of creating new heap that is needed to be created after deleting min

		BinomialHeap newHeap;

		if (min.rank == 0) {
			newHeap = new BinomialHeap();
		}
		else {
			// Save min.child as the last of the new heap
			HeapNode newLast = min.child;

			// Create new BinomialHeap containing min's child as last
			newHeap = new BinomialHeap(newLast);

			// Initial new heap size - 2^rank(min.child)
			newHeap.size = (int)Math.pow(2, min.rank) - 1;

			// Find minimum for new heap
			HeapNode currMin = newLast;
			HeapNode currNode = newLast.next;
			while (currNode != newLast) {
				// Update minimum
				if (currNode.item.key < currMin.item.key) {
					currMin = currNode;
				}

				// Update parent to null (detach from min)
				currNode.parent = null;

				currNode = currNode.next;
			}

			// Update min for new heap according to the minimum found
			newHeap.min = currMin;
		}

		// End of creating new heap
		
		// Update size for this
		size -= Math.pow(2, min.rank);

		// Update new minimum for this
		min = newMin;

		// Meld the new heap with this
		meld(newHeap);
	}
 
	 /**
	  * 
	  * Return the minimal HeapItem
	  *
	  */
	 public HeapItem findMin()
	 {
		 if (empty()) {
			 return null;
		 }
 
		 return min.item;
	 } 
 
	 /**
	  * 
	  * pre: 0 < diff < item.key
	  * 
	  * Decrease the key of item by diff and fix the heap. 
	  * 
	  */
	 public void decreaseKey(HeapItem item, int diff) 
	 {    
		 item.key -= diff;
 
		 HeapNode currNode = item.node;
 
		 // Shift up
		 while (currNode.parent != null && currNode.parent.item.key > currNode.item.key) {
			 // Swap items between current node and its parent (items)
			 swapItems(currNode, currNode.parent);
 
			 // Update current node to parent
			 currNode = currNode.parent;
		 }
 
		 // If decreased node became the root of its binomial tree, check if it is now min of heap
		 if (currNode.parent == null && currNode.item.key < min.item.key) {
			 min = currNode;
		 }
	 }
 
	 /**
	  * 
	  * Delete the item from the heap.
	  *
	  */
	 public void delete(HeapItem item) 
	 {    
		 if (item.node == min) {
			 deleteMin();
		 }
		 else {
			 // Find diff between item.key and min.key
			 int diff = item.key - min.item.key;
 
			 // Decrease key to make item.node the new min
			 decreaseKey(item, diff + 1);
 
			 // Delete item.node (the new min)
			 deleteMin();
		 }
	 }
 

	/**
	 * 
	* Meld the heap with heap2
	*
	*/
	public void meld(BinomialHeap heap2)
	{
		// Check if any of the heaps are empty
		// If heap2 or both are empty, do nothing
		if (heap2.empty()) {
			return;
		}

		// If this is empty and heap2 is not empty, this = heap2
		if (empty()) {
			// Make this heap2
			this.last = heap2.last;
			this.min = heap2.min;
			this.size = heap2.size;

			// Empty heap2
			heap2.last = null;
			heap2.min = null;
			heap2.size = 0;

			return;
		}

		// If both are not empty

		// Init result heap
		BinomialHeap result = new BinomialHeap();

		// Find lower ranked heap and higher ranked heap
		BinomialHeap high = new BinomialHeap(); 
		BinomialHeap low = new BinomialHeap();

		// If this.last is higher ranked
		if (last.rank >= heap2.last.rank) {
			high.last = last;
			high.min = min;
			high.size = size;
			low.last = heap2.last;
			low.min = heap2.min;
			low.size = heap2.size;
		}

		// If heap2.last is higher ranked
		else {
			low.last = last;
			low.min = min;
			low.size = size;
			high.last = heap2.last;
			high.min = heap2.min;
			high.size = heap2.size;
		}

		// Init curr and next pointers
		HeapNode highCurr = high.last.next;
		HeapNode lowCurr = low.last.next;
		HeapNode highNext = null;
		HeapNode lowNext = null;
		HeapNode carry = null;

		while (!low.empty()) {

			// If carry is null
			if (carry == null) {

				// If lowCurr and highCurr have same rank
				if (lowCurr.rank == highCurr.rank) {

					// Save next pointers
					lowNext = lowCurr.next;
					highNext = highCurr.next;

					// Update sizes
					low.size -= (int)Math.pow(2, lowCurr.rank);
					high.size -= (int)Math.pow(2, highCurr.rank);

					// Link and save carry
					carry = link(highCurr, lowCurr);

					// Move pointers to next
					lowCurr = lowNext;
					highCurr = highNext;
				}

				// If low curr is lesser ranked than high curr
				else if (lowCurr.rank < highCurr.rank) {
					
					// Save next
					lowNext = lowCurr.next;

					// Add low curr to result
					addToResult(result, lowCurr);

					// Update size
					low.size -= (int)Math.pow(2, lowCurr.rank);

					// Update curr pointer to next
					lowCurr = lowNext;
				}

				// If high curr is lesser ranked than low curr
				else {
					
					// Save next
					highNext = highCurr.next;

					// Add low curr to result
					addToResult(result, highCurr);

					// Update size
					high.size -= (int)Math.pow(2, highCurr.rank);

					// Update curr pointer to next
					highCurr = highNext;
				}
			}

			// If carry is not null
			else {
				// If lowCurr and highCurr have same rank
				if (lowCurr.rank == highCurr.rank) {

					// Add carry to result
					addToResult(result, carry);

					// Save next pointers
					lowNext = lowCurr.next;
					highNext = highCurr.next;

					// Update sizes
					low.size -= (int)Math.pow(2, lowCurr.rank);
					high.size -= (int)Math.pow(2, highCurr.rank);

					// Link and save carry
					carry = link(highCurr, lowCurr);

					// Move pointers to next
					lowCurr = lowNext;
					highCurr = highNext;
				}

				// If carry rank is low rank
				else if (carry.rank == lowCurr.rank) {
					// Save next
					lowNext = lowCurr.next;

					// Update size
					low.size -= (int)Math.pow(2, lowCurr.rank);

					// Link and save carry
					carry = link(carry, lowCurr);

					// Update curr pointer to next
					lowCurr = lowNext;					
				}

				// If carry rank is high rank
				else if (carry.rank == highCurr.rank) {
					// Save next
					highNext = highCurr.next;

					// Update size
					high.size -= (int)Math.pow(2, highCurr.rank);

					// Link and save carry
					carry = link(carry, highCurr);

					// Update curr pointer to next
					highCurr = highNext;		
				}

				// Carry is the lowest ranked
				else {

					// Add to result
					addToResult(result, carry);

					// Nullify carry
					carry = null;
				}
			}
		}

		// Link until there is no carry (or high is empty)
		while (carry != null && !high.empty()) {
			if (carry.rank == highCurr.rank) {
				highNext = highCurr.next;
				high.size -= (int)Math.pow(2, highCurr.rank);
				carry = link(carry, highCurr);
				highCurr = highNext;
			}
			else {
				addToResult(result, carry);
				carry = null;
			}
		}

		// If carry is null and high is not empty
		if (carry == null && !high.empty()) {
			high.last.next = result.last.next;
			result.last.next = highCurr;
			result.last = high.last;
			result.size += high.size;

			// Update minimum if needed
			if (result.min.item.key > high.min.item.key) {
				result.min = high.min;
			}
		}

		// If carry is not null and high is empty
		else if (carry != null && high.empty()) {
			addToResult(result, carry);
			carry = null;
		}

		last = result.last;
		min = result.min;
		size = result.size;	
	}

	/**
	 * 
	* Add binomial tree to result heap for meld
	*   
	*/
	private void addToResult(BinomialHeap res, HeapNode node) {
		int sizeToAdd = (int)Math.pow(2, node.rank);
		
		// If result heap is empty, add node to it for the first time
		if (res.empty()) {
			res.last = node;
			res.last.next = res.last;
			res.min = node;
			res.size = sizeToAdd;
		}

		// If result is not empty, concat tree to end of heap
		else {
			node.next = res.last.next;
			res.last.next = node;
			res.last = node;
			res.size += sizeToAdd;

			// Update result heap min if needed
			if (node.item.key < res.min.item.key) {
				res.min = node;
			}
		}
	}

	/**
	 * 
	* Link two binomial trees and return the root
	*   
	*/
	private HeapNode link(HeapNode x, HeapNode y) {

		// Nullify next pointers
		x.next = null;
		y.next = null;

		// Replace to make x the lower key node
		if (x.item.key > y.item.key) {
			HeapNode tmp = x;
			x = y;
			y = tmp;
		}

		// Update pointers for replacing
		if (x.child == null) {
			y.next = y;
		}
		else {
			y.next = x.child.next;
			x.child.next = y;
		}

		x.child = y;
		y.parent = x;

		// Update rank
		x.rank += 1;
		
		return x;
	}
 
	 /**
	  * 
	  * Return the number of elements in the heap
	  *   
	  */
	 public int size()
	 {
		 return size;
	 }
 
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
	  * 
	  * Return the number of trees in the heap.
	  * 
	  */
	 public int numTrees()
	 {
		 if (empty()) {
			 return 0;
		 }
 
		 int numOfTrees = 1;
		 HeapNode curr = last.next;
		 while (curr != last) {
			 numOfTrees++;
			 curr = curr.next;
		 }
		 return numOfTrees;
	 }
 
	 /**
	  * 
	  * Swaps items between Heap Nodes
	  * 
	  */
	 public void swapItems(HeapNode x, HeapNode y)
	 {
		 // Swap between items
		 HeapItem tmp = x.item;
		 x.item = y.item;
		 y.item = tmp;
 
		 // Update node pointers
		 x.item.node = x;
		 y.item.node = y;
	 }
 
	 /**
	  * Class implementing a node in a Binomial Heap.
	  *  
	  */
	 public class HeapNode{
		 public HeapItem item;
		 public HeapNode child;
		 public HeapNode next;
		 public HeapNode parent;
		 public int rank; // Default 0
		 
		 /**
		  * Default HeapNode Constructor
		  *  
		  */
		 public HeapNode() { }
 
		 /**
		  * HeapNode Constructor that receives an HeapItem item and updates it with himself
		  *  
		  */
		  public HeapNode(HeapItem i) { 
			 item = i;
			 next = this;
			 item.node = this;
		  }
 
	 }
 
	 /**
	  * Class implementing an item in a Binomial Heap.
	  *  
	  */
	 public class HeapItem{
		 public HeapNode node;
		 public int key;
		 public String info;
		 
		 /**
		  * Default HeapItem Constructor
		  *  
		  */
		 public HeapItem() { }
 
		 public HeapItem(int k, String i) {
			 key = k;
			 info = i;
		 }
	 }
 
 }
 