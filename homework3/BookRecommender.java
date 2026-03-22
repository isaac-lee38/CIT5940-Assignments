//package homework3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BookRecommender {

    // Co-Like Graph: Book -> (Neighbor Book -> Weight)
    private Map<String, Map<String, Double>> coLikeGraph;
    
    // User-based Graph: User -> Set of Books
    private Map<String, Set<String>> userToBooks;
    
    // Book-based Graph: Book -> Set of Users (Great for Part 4 Jaccard denominator)
    private Map<String, Set<String>> bookToUsers;

    public BookRecommender() {
        coLikeGraph = new HashMap<>();
        userToBooks = new HashMap<>();
        bookToUsers = new HashMap<>();
    }

    // ==========================================
    // MIN-HEAP (WEIGHT, THEN Alphabetical Order)
    // ==========================================
    
    /**
     * This lays out the rule of the custom comparator
     */
    private PriorityQueue<Map.Entry<String, Double>> createTop5Heap() {
        return new PriorityQueue<>(
            (a, b) -> {
                // If weights/scores are tied, reverse alphabetical sort for the heap
                if (a.getValue().equals(b.getValue())) {
                    return b.getKey().compareTo(a.getKey()); 
                }
                // Otherwise, lowest score stays at the top of the Min-Heap
                return Double.compare(a.getValue(), b.getValue());
            }
        );
    }
    
    // ==========================================
    // Print Array
    // ==========================================

    public void printArray(List<String> given){
        if (given.size()==0) { System.out.println("NONE");return;}
        System.out.println(String.join(",", given));
    }


    // ==========================================
    // PART 0: DATA LOADING
    // ==========================================
    
    public void loadData(String filename) {
        // --- PHASE 1: JUST READ THE DATA ---
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("User_ID")) continue; 
                
                String[] parts = line.split(",", 2); 
                if (parts.length < 2) continue;
                
                String user = parts[0].trim();
                String book = parts[1].trim();
                
                // Just populate the basic sets
                userToBooks.computeIfAbsent(user, k -> new HashSet<>()).add(book);
                bookToUsers.computeIfAbsent(book, k -> new HashSet<>()).add(user);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return; // Stop if the file failed to load
        }

        // --- PHASE 2: BUILD THE CO-LIKE GRAPH ---
        for (Set<String> books : userToBooks.values()) {
            // Convert to a List so we can do a traditional double-for-loop
            List<String> bookList = new ArrayList<>(books);
            
            for (int i = 0; i < bookList.size(); i++) {
                for (int j = i + 1; j < bookList.size(); j++) {
                    String b1 = bookList.get(i);
                    String b2 = bookList.get(j);
                    
                    coLikeGraph.computeIfAbsent(b1, k -> new HashMap<>())
                               .merge(b2, 1.0, Double::sum); // Make sure this is 1.0!
                    
                    coLikeGraph.computeIfAbsent(b2, k -> new HashMap<>())
                               .merge(b1, 1.0, Double::sum);
                }
            }
        }
    }

    // ==========================================
    // PART 2a: SINGLE-BOOK NEAREST NEIGHBORS
    // ==========================================
    
    public void singleBookNeighbors(String bookId) {
        Map<String, Double> neighbors = coLikeGraph.get(bookId);
        if (neighbors==null || neighbors.isEmpty()){
            printArray(new ArrayList<>()); 
            return;
        }
        PriorityQueue<Map.Entry<String, Double>> top5 = createTop5Heap();
        for ( Map.Entry<String, Double> entry : coLikeGraph.get(bookId).entrySet()) {
            top5.offer(entry);
            //minHeap, pop size size >5
            if (top5.size() > 5) {
                top5.poll(); // Removes the smallest value currently in the heap
            }
        }
        List<String> output = new ArrayList<>();
        while (top5.size()>0){
            Map.Entry<String,Double> cur=top5.poll();
            output.add(cur.getKey());
        }
        Collections.reverse(output);
        printArray(output);
    }

    // ==========================================
    // PART 2b: LIKE-HISTORY NEAREST NEIGHBORS
    // ==========================================
    
    public void likeHistoryNeighbors(List<String> history) {
        // Nested loop
        // Open a hashmap <book, frequency count>
        Map<String, Double> count = new HashMap<>();
        // outer loop: the list of book in history
        for (String bookId: history){
        // inner loop use the adj matrix: For each book, you iterate through the map, then add weight to favourite
            Map<String, Double> neighbors = coLikeGraph.get(bookId);
            if (neighbors == null) continue;
            for ( Map.Entry<String, Double> entry : neighbors.entrySet()) {
                count.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }

        // dump the hashmap into the top5 heap
        
        PriorityQueue<Map.Entry<String, Double>> top5 = createTop5Heap();
        for ( Map.Entry<String, Double> entry : count.entrySet()) {
            if(!history.contains(entry.getKey())) top5.offer(entry);
            //minHeap, pop size size >5
            if (top5.size() > 5) {
                top5.poll(); // Removes the smallest value currently in the heap
            }
        }
        List<String> output = new ArrayList<>();
        while (top5.size()>0){
            Map.Entry<String,Double> cur=top5.poll();
            output.add(cur.getKey());
        }
        // print
        Collections.reverse(output);
        printArray(output);
    }

    // ==========================================
    // PART 4: USER-BASED COLLABORATIVE FILTERING
    // ==========================================
    
    public void tasteTwinsRecommendations(String targetUser) {
        Set<String> potentialTwins = new HashSet<>();
        Set<String> targetBooks = userToBooks.get(targetUser);

        // if the user does not have any books
        if (targetBooks == null || targetBooks.isEmpty()) {
            System.out.println("NONE");
            return;
        }
        // 1. Find users who share at least 1 book.
        for (String book : targetBooks) {
            potentialTwins.addAll(bookToUsers.get(book));
        }
        potentialTwins.remove(targetUser);
        if (potentialTwins.isEmpty()) {
            System.out.println("NONE");
            return;
        }

        // 2. Calculate Jaccard Similarity, keep top 5 users.
        Map<String, Double> jaccard =new HashMap<>();
        for(String twin:potentialTwins){
            Set<String> intersection = new HashSet<>(userToBooks.get(twin));
            Set<String> union = new HashSet<>(userToBooks.get(twin));

            intersection.retainAll(targetBooks);
            union.addAll(targetBooks);

            double score = (double) intersection.size() / union.size();

            jaccard.put(twin,score);
        }
        PriorityQueue<Map.Entry<String, Double>> top5 = createTop5Heap();
        for ( Map.Entry<String, Double> entry : jaccard.entrySet()) {
            top5.offer(entry);
            //minHeap, pop size size >5
            if (top5.size() > 5) {
                top5.poll(); // Removes the smallest value currently in the heap
            }
        }
        List<String> output = new ArrayList<>();
        while (top5.size()>0){
            Map.Entry<String,Double> cur=top5.poll();
            output.add(cur.getKey());
        }
        
        // 3. Pool their books, calculate final score = sum(Jaccard) / total_readers.
        // Open a hashmap <book, frequency count>
        Map<String, Double> count = new HashMap<>();
        for (String twin: output){
            Set<String> twinBooks = userToBooks.get(twin);
            if (twinBooks == null) continue;
            for (String book:twinBooks){
                if (targetBooks.contains(book)) {
                    continue; 
                }
                    count.merge(book, 1.0, Double::sum);
            }

        }

        for (Map.Entry<String, Double> entry : count.entrySet()) {
            String book = entry.getKey();
            double tasteTwinsWhoLikedIt = entry.getValue(); 
            
            // The denominator!
            int totalReaders = bookToUsers.get(book).size(); 
            
            double finalScore = tasteTwinsWhoLikedIt / totalReaders;
            entry.setValue(finalScore);
        }
        // 4. Keep top 5 books, print.
        PriorityQueue<Map.Entry<String, Double>> top5book = createTop5Heap();
        for ( Map.Entry<String, Double> entry : count.entrySet()) {
            top5book.offer(entry);
            //minHeap, pop size size >5
            if (top5book.size() > 5) {
                top5book.poll(); // Removes the smallest value currently in the heap
            }
        }
        List<String> outputBook = new ArrayList<>();
        while (top5book.size()>0){
            Map.Entry<String,Double> cur=top5book.poll();
            outputBook.add(cur.getKey());
        }
        Collections.reverse(outputBook);
        printArray(outputBook);


    }

    // ==========================================
    // PART 5: GENRE HOPPER (SHORTEST PATH)
    // ==========================================
    
    public void genreHopper(String source, String target) {
        if (!coLikeGraph.containsKey(source) || !coLikeGraph.containsKey(target)) {
            System.out.println("NONE");
            return;
        }
        if (source.equals(target)) {
            System.out.println(source);
            return;
        }
        //1. Find Median
        List<Double> edgeWeights = new ArrayList<>();
        for (String u : coLikeGraph.keySet()) {
            for (Map.Entry<String, Double> entry : coLikeGraph.get(u).entrySet()) {
                String v = entry.getKey();
                // Trick to avoid double counting undirected edges (A->B and B->A)
                if (u.compareTo(v) < 0) { 
                    edgeWeights.add(entry.getValue());
                }
            }
        }
        
        if (edgeWeights.isEmpty()) {
            System.out.println("NONE");
            return;
        }

        Collections.sort(edgeWeights);
        double median;
        int size = edgeWeights.size();
        if (size % 2 == 0) {
            median = (edgeWeights.get(size / 2 - 1) + edgeWeights.get(size / 2)) / 2.0;
        } else {
            median = edgeWeights.get(size / 2);
        }
        // 2. Run dual-source BFS - using ArrayDeque to enchance cache locality
        Deque<String> forwardQueue=new ArrayDeque<>();
        Deque<String> backwardQueue=new ArrayDeque<>();

        Map<String, String> forwardParent = new HashMap<>();
        Map<String, String> backwardParent = new HashMap<>();

        forwardQueue.addLast(source);
        forwardParent.put(source,null);

        backwardQueue.addLast(target);
        backwardParent.put(target,null);

        String intersectionNode = null;
        // Dual-source BFS
        while (!forwardQueue.isEmpty() && !backwardQueue.isEmpty()) {
            // Pass the median down to filter on the fly
            intersectionNode = expandLayer(forwardQueue, forwardParent, backwardParent, median);
            if (intersectionNode != null) break;

            intersectionNode = expandLayer(backwardQueue, backwardParent, forwardParent, median);
            if (intersectionNode != null) break;
        }
        // Reconstruct Path
        if (intersectionNode == null) {
            System.out.println("NONE");
        } else {
            printHopperPath(intersectionNode, forwardParent, backwardParent);
        }
    }
    private String expandLayer(Deque<String> queue, Map<String, String> myParent, 
                               Map<String, String> otherParent, double median) {
        
        int nodesInCurrentLayer = queue.size();
        for (int i=0;i<nodesInCurrentLayer;i++){
            String curr = queue.removeFirst();
            Map<String, Double> neighbors = coLikeGraph.get(curr);
            
            if (neighbors == null) continue;
            
            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                String neighbor = entry.getKey();
                double weight = entry.getValue();
                
                // ON-THE-FLY FILTERING: Ignore edges strictly less than the median
                if (weight < median) continue;
                
                if (!myParent.containsKey(neighbor)) {
                    myParent.put(neighbor, curr);
                    queue.addLast(neighbor);
                }
                if (otherParent.containsKey(neighbor)) {
                    return neighbor;
                }
            }
        }
        return null;

    }
    private void printHopperPath(String meetingPoint, Map<String, String> forwardParent, 
                                 Map<String, String> backwardParent) {
        List<String> path = new ArrayList<>();
        
        String curr = meetingPoint;
        while (curr != null) {
            path.add(curr);
            curr = forwardParent.get(curr);
        }
        Collections.reverse(path);
        
        curr = backwardParent.get(meetingPoint);
        while (curr != null) {
            path.add(curr);
            curr = backwardParent.get(curr);
        }
        
        System.out.println(String.join("->", path));
    }
    // ==========================================
    // DEBUG HELPER
    // ==========================================
    public void printGraphStates() {
        System.out.println("=== USER TO BOOKS ===");
        for (Map.Entry<String, Set<String>> entry : userToBooks.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\n=== BOOK TO USERS ===");
        for (Map.Entry<String, Set<String>> entry : bookToUsers.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\n=== CO-LIKE GRAPH ===");
        for (Map.Entry<String, Map<String, Double>> entry : coLikeGraph.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }


    // ==========================================
    // MAIN METHOD FOR TESTING
    // ==========================================
    
    public static void main(String[] args) {
        if (args.length < 2) {
        System.out.println("Invalid arguments");
        return;
    }

    String filename = args[0];
    String command = args[1];

    BookRecommender recommender = new BookRecommender();
    recommender.loadData(filename); // Pass args[0] directly!
    //recommender.printGraphStates();

    switch (command) {
        case "single_book_mn":
            // Command format: single_book_mn book_id
            String bookId = args[2];
            recommender.singleBookNeighbors(bookId);
            break;

        case "like_history_mn":
            // Command format: like_history_mn book_id_1 book_id_2 ...
            String[] historyArray = Arrays.copyOfRange(args, 2, args.length);
            // 2. Convert that array into a List
            List<String> history = Arrays.asList(historyArray);
            recommender.likeHistoryNeighbors(history);
            break;

        case "user_cf":
            // Command format: user_cf target_user_id
            String targetUserId = args[2];
            recommender.tasteTwinsRecommendations(targetUserId);
            break;

        case "shortest_path":
            // Command format: shortest_path source_book_id target_book_id
            String sourceId = args[2];
            String targetId = args[3];
            recommender.genreHopper(sourceId, targetId);
            break;

        default:
            System.out.println("Unknown command: " + command);
    }
    }
}