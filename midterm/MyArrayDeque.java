package midterm;
import java.util.ArrayDeque;
import java.util.Deque;

public class MyArrayDeque {
    public static void main(String[] args) {
        // Initialize the Deque
        Deque<Integer> deque = new ArrayDeque<>();

        // --- ADDING ELEMENTS ---
        deque.addLast(10);  // Queue: [10]
        deque.addLast(20);  // Queue: [10, 20]
        deque.addFirst(5);  // Queue: [5, 10, 20] (Added to the front!)

        System.out.println("Current Deque: " + deque); // Output: [5, 10, 20]

        // --- PEEKING (Looking without removing) ---
        System.out.println("Front element: " + deque.peekFirst()); // Output: 5
        System.out.println("Back element: " + deque.peekLast());   // Output: 20

        // --- REMOVING ELEMENTS ---
        deque.removeFirst(); // Removes 5. Queue is now [10, 20]
        deque.removeLast();  // Removes 20. Queue is now [10]

        System.out.println("Final Deque: " + deque); // Output: [10]
    }
}