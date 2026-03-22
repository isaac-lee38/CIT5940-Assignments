package midterm;

import java.util.Arrays;

public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new int[capacity];
    }

    // --- ARRAY INDEX HELPERS ---
    private int parent(int i)      { return (i - 1) / 2; }
    private int leftChild(int i)   { return 2 * i + 1; }
    private int rightChild(int i)  { return 2 * i + 2; }

    // --- INSERT ---
    public void insert(int val) {
        if (size == capacity) {
            throw new RuntimeException("Heap is full!");
        }
        // 1. Put the new element at the very end of the array
        heap[size] = val;
        
        // 2. "Bubble up" (sift up) the element to restore heap property
        siftUp(size);
        size++;
    }

    private void siftUp(int index) {
        // While we aren't at the root, and the parent is smaller than the current node
        while (index > 0 && heap[parent(index)] < heap[index]) {
            swap(index, parent(index));
            index = parent(index); // Move up the tree
        }
    }

    // --- POP MAX (Extract Max) ---
    public int popMax() {
        if (size == 0) {
            throw new RuntimeException("Heap is empty!");
        }
        int max = heap[0]; // The root is always the max

        // 1. Move the very last element to the root
        heap[0] = heap[size - 1];
        size--;

        // 2. "Sink down" (sift down) the new root to restore heap property
        siftDown(0);

        return max;
    }

    private void siftDown(int index) {
        int maxIndex = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Check if left child is larger than current max
        if (left < size && heap[left] > heap[maxIndex]) {
            maxIndex = left;
        }
        // Check if right child is larger than current max
        if (right < size && heap[right] > heap[maxIndex]) {
            maxIndex = right;
        }

        // If the largest is not the parent, swap and continue sinking
        if (index != maxIndex) {
            swap(index, maxIndex);
            siftDown(maxIndex); 
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // --- HELPER TO VIEW STATE ---
    public void printHeap() {
        System.out.println(Arrays.toString(Arrays.copyOfRange(heap, 0, size)));
    }

    // --- DRIVER TO TEST RESULTS ---
    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap(10);

        System.out.println("--- Inserting elements ---");
        int[] elementsToInsert = {10, 20, 15, 40, 50, 100, 25};
        for (int el : elementsToInsert) {
            maxHeap.insert(el);
            System.out.print("Inserted " + el + " -> Current Heap: ");
            maxHeap.printHeap();
        }

        System.out.println("\n--- Popping elements (Extract Max) ---");
        for (int i = 0; i < 3; i++) {
            int max = maxHeap.popMax();
            System.out.print("Popped Max: " + max + " -> Remaining Heap: ");
            maxHeap.printHeap();
        }
    }
}