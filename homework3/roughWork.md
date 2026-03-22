



















Data structure to use

1) hashmap<user, set<books>>, then create a adj matrix by iterating through each user's book list and adding to the weight
2a) Make a heap. given book, iterate through the adj.matrix for that book, then add it to a minHeap with size 5.
2b) Given user's set, you sum the recommendation into HashMap <Book, score>. Add all the books in user's set to a bfs queue + run a multi-source BFS, with distiance of 1.
With the Hashmap done, you put it into a minHeap again, then get the top 5 books

3) you build an extra hashmap<book,set<users>> to store the bipartie graph + create hashmap <book,int> to store the size of the set<users> for each book.

4) given the target user,s you run N-1 comparsion with peers. Do size of AND(set A,B)/ size of OR (setA, set B). get the top 5 users. 
Create HashMap <Book, score>. For those 5 uesr,s you iterative thethrough their book list, then add it to the HashMap. 
Iterate through the HashMap, calucalte the score of score/ total number of people liking. then pop into minHeap of size 5

5) Find median?
Based on the co-like graph run dual-source BFS from both ends, only consider the edges< median. if you have a match then return path.