public class MyHashTable<T extends Comparable<T>> {
    private MyTree<T> [] body;
    private int size;
    private int capacity;
    
    // empty constructor with default capacity
    public MyHashTable() {
        this.size=0;
        this.capacity=701;
        this.body=new MyTree[this.capacity];
    }
    // constructor with default capacity
    public MyHashTable(int capacity) {
        this.size=0;
        this.capacity=capacity;
        this.body=new MyTree[capacity];
    }
    // Get bucket for hashtable
    private int getBucketIndex(T item){
        int hashCode= item.hashCode();
        return ((hashCode % capacity) + capacity ) % capacity;
    }
    // add method to hashtable
    public MyNode<T> add(T item) {
        if (item == null) {throw new IllegalArgumentException("Cannot add null item");}
        int index = getBucketIndex(item);
        if (body[index] == null) {
            body[index] = new MyTree<>();
        }

        MyTree<T> bucket = body[index];
        int oldBucketSize = bucket.getSize();
        MyNode<T> result = bucket.insert(item);

        if (bucket.getSize() > oldBucketSize) {
            this.size++; // Only increment if the tree actually grew
        }
        

        return result;
    }
    // contains method to hashtable
    public MyNode<T> contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot search for null item");
        }
        int index = getBucketIndex(item);
        if (body[index] == null) {
            return null;
        }
        return body[index].contains(item);
    }
    public boolean remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot remove null item");
        }
        int index = getBucketIndex(item);
        if (body[index] == null) {
            return false;
        }
        
        boolean removed = body[index].remove(item);
        if (removed) {
            this.size--;
        }
        return removed;
    }
    //isEmpty method
    public boolean isEmpty() {
        return size == 0;
    }
    //size()
    public int size() {
        return size;
    }
    //clear()
    public void clear() {
        this.body = new MyTree[this.capacity];
        this.size = 0;
    }
    public static void main(String[] args) {
        MyHashTable<String> table = new MyHashTable<>();

        // Insert at least 5 Strings
        System.out.println("Adding items...");
        System.out.println("Added 'Apple': " + table.add("Apple").getItem());
        System.out.println("Added 'Banana': " + table.add("Banana").getItem());
        System.out.println("Added 'Cherry': " + table.add("Cherry").getItem());
        System.out.println("Added 'Date': " + table.add("Date").getItem());
        System.out.println("Added 'Orange': " + table.add("Orange").getItem());

        // Print results of size()
        System.out.println("Current Size: " + table.size()); // Should be 5

        // Contains() on existing and missing
        MyNode<String> found = table.contains("Banana");
        System.out.println("Contains 'Banana': " + (found != null ? found.getItem() : "false"));
        
        MyNode<String> missing = table.contains("Fig");
        System.out.println("Contains 'Fig': " + (missing != null ? missing.getItem() : "false"));

        // Insert duplicate
        System.out.println("Adding duplicate 'Apple'...");
        table.add("Apple");
        System.out.println("Size after duplicate add: " + table.size()); // Should still be 5

        // Remove item
        System.out.println("Removing 'Date': " + table.remove("Date"));
        System.out.println("Size after remove: " + table.size()); // Should be 4

        // Remove same item again
        System.out.println("Removing 'Date' again: " + table.remove("Date"));
    }
}
