## Please enter your personal info here:
Name:
LEE Kwan Yuen
PennKey (e.g., taliem):
lee38

# Part 1:
## Are Alicia and Lloyd both wrong, or perhaps both right? Is only one of them correct? Why?

They are likely both right, depending on the implementation. Use of ArrayList supports Lloyd: Access is O(1), but removing the head is O(N) due to shifting. Use of LinkedList supports Alicia: Access is O(N), but removing the head is O(1). However, assuming a standard vector-based list (ArrayList) as is common in systems programming, Lloyd is correct that removing from the front is the performance bottleneck.
Snippet B is O(N). .get(0), get(last item), remove(last item) is all O(1). Yet, the remove(0) will shift the entire array forward by 1, thus is O(N). Overall, this will be O(N).


# Part 2:
## What are the Big O and Big Ω times for snippets C and D?
Snippet C: Best case Ω = Ω(1), Worse case O = O(nm).
Have inner break + global flag ot break outer loop once result is found.
Snippet D: Best case Ω = Ω(nm), Worse case O = O(nm)
Loop continues for full O(nm) even after result is found, due to (1) Lack of inner break (2) no global flag to break outer loop.

## When measuring actual runtime, does one of the snippets run faster than the other? In what situations? Why do you think this is the case?


GridOne + MethodOne average time: 2.8814E-4 ms 
Since target is in the first cell and we break early once the target is found.
GridOne + MethodTwo average time: 9.614777791 ms
Since target is in the first cell but we run the entire O(nm) loop.
GridTwo + MethodOne average time: 5.199962082 ms
Target is in the last cell, so have to run the entire O(nm) loop to reach there.
GridTwo + MethodTwo average time: 10.453247539 ms
Target is in the last cell, so have to run the entire O(nm) loop to reach there.
How come this is nearly double of 5.2ms? I ran benchmark by removing the && result[0] == -1, and its around 5.2ns. Thus, we can single out the culprit being this extra check in the if condition statement. 

Snippet C is significantly faster when the target is early in the grid. Snippet D is consistently slow. Even when the target is at the very end, Snippet D is slightly slower (~10ms vs ~5ms) because the if statement evaluates a compound condition (grid[r][c] == target && result[0] == -1) on every single iteration, adding constant-factor overhead compared to Snippet C's simpler check.

## What else do you notice about the reported runtime? Is it 100% consistent every time you run it?
It tends to vary when i first run it. However, after writing a benchmark class to pre-run the findFirst function for 100 times in JVM before i do the actual benchmark, it is much more consistent.


# Part 3:
## Before you make any changes, explain whether you think a LinkedList or an ArrayList makes more sense in this instance. Which do you think will be faster? Why?
You are basically doing 2 jobs with this: (1) Add ticket, then process the oldest ticket and remove it (2) Calculate the number of tickets you have on hand.

LinkedList will make more sense. While size() operator is O(1) on both the ArrayList and the LinkedList, when you remove the oldest ticket ie index(0), it is a O(N) operation for ArrayList, but a O(1) for LinkedList.


## When measuring actual runtime, is the LinkedList version Suho wrote, or your ArrayList version faster? Does this change when the list size is small versus when it is very large?


Results (Average of 100 runs):
Format: [Type] [Size] | Create: X ms | Process: Y ms | Total: Z ms
------------------------------------------------------------------
ArrayList  Short (50)    | Create:   0.1318 ms | Process:   0.0031 ms | Total:   0.1349 ms
LinkedList Short (50)    | Create:   0.0175 ms | Process:   0.0048 ms | Total:   0.0223 ms
ArrayList  Long  (20000) | Create:   0.7077 ms | Process:  11.6727 ms | Total:  12.3804 ms
LinkedList Long  (20000) | Create:   1.5670 ms | Process:   0.9487 ms | Total:   2.5158 ms

For small queues, constant factors dominate and both implementations perform similarly.
For large queues, asymptotic complexity dominates, making LinkedList significantly faster for front removals.


## If you ignore queue creation times, does that affect which ticket processor version is faster?
There is an impact on the results, but the initial obvseration stands - the linkedlist version is way faster than the arraylist due to the O(N) when doing front removal.

## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?).
Your report should answer the following questions:
* What did you learn from this experience?
* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?
* How does the theoretical time complexity compare with your findings?

This benchmark demonstrates that while constant factors (such as JVM allocation overhead) dominate performance for small datasets, asymptotic complexity becomes the deciding factor as input size scales. Our empirical results align perfectly with theoretical Big O analysis: the ArrayList implementation suffered massive latency degradation on large datasets due to the O(n) cost of removing elements from index 0, which forces an internal memory shift of all subsequent elements. Conversely, the LinkedList implementation maintained consistent performance efficiency for removals, validating its O(1) theoretical complexity for head-node deletion.

By implementing an optimized "swap-and-pop" strategy for the ArrayList, we successfully reduced the deletion time from O(N) to O(1), bringing its runtime performance in line with the LinkedList. However, this optimization comes at the cost of strict ordering; it transforms the queue into an unordered collection.

For FIFO (First-In-First-Out) queue workflows where front-removal is a frequent operation, the LinkedList implementation is the strictly superior choice. The performance penalty of ArrayList element shifting is prohibitive at scale. However, ArrayList should remain the standard for use cases requiring O(1) random access or where memory locality and cache coherence are prioritized over queue operations. 


# Part 4
## What are the Big O and Big Ω times for Javier's algorithm? What are the Big O and Big Ω for space use?
Time Complexity: Big O: O(NlogN), Big Ω: O(NlogN)
MergeSort relies on the divide-and-conquer strategy. Regardless of whether the data is already sorted, the algorithm always splits the list down to single elements (log N levels) and iterates through every element to merge them back up N work per level.Master Theorem: With a=2 (subproblems), b=2 (size division), and f(n)=O(n) (merge step), we get O(NlogN).

Space complexity: Big O: O(N), Big Ω: O(N)
While Javier creates new arrays at every level, the peak memory usage at any single moment is linear (O(N)), as the arrays from previous recursive steps are discarded.

## Write a paragraph or two in the style of a technical report (think about – how would I write this professionally if I needed to explain my findings to my manager?). 
Your report should answer the following questions:
* Which of the two algorithms (yours versus Javier's) is more efficient in time and space (in terms of Big O)
    * What about in actual runtime?
* Which implementation do you suggest should be used? Are there certain situations that might call for the other approach?

Comparing the 2 algorithms (mine vs Javier's), our algorithm is roughly the same in Big-O time complexity- O(NLogN). Both algorithms have a theoretical space complexity of O(N). However, Javier's implementation has a much higher constant factor for memory overhead. It allocates new array objects during every merge step, triggering frequent Garbage Collection and creating memory fragmentation. My implementation allocates a single auxiliary buffer of size N once, reusing it for all operations.  One further optimization we can do is "ping-pong", instead of copying from buffer to input every time we are done in the recursive call, we could just swap the input and buffer arguments in recursive calls.

Array Size: 1000
Warmup Runs: 100
Measured Runs: 100
--------------------------
--- Results (Average per run) ---
Standard Recursive MergeSort: 48867.42 ns (0.0489 ms)
Javier (Queue) MergeSort:     156074.96 ns (0.1561 ms)

Actual runtimes are supportive of our hypothesis. My algorithm is roughly 3x faster.

I would recommend my array-based approach over Javier's LinkedList one. Some exceptions are: (1) The data is large and I have limited RAM e.g. 500 GB data on 16GB RAM, then I would do Javier's version by loading the small chunks, sorting them and streaming them off disk. (2) The data is in the form of LinkedList. That being said, I would try to rewire the pointers instead of creating new arrays. This would allow me to sort a list with O(1) extra space.