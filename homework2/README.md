# Homework Assignment - Hash Table

**Name:** LEE Kwan Yuen
**Date:** 14 Feb 2026

## Reflection

**1. How long did it take you to complete this assignment (in hours)?**
It took me approximately 6 hours to complete this assignment.

**2. What parts of this assignment did you find most difficult?**
The most difficult part was implementing the `remove` method in `MyTree`. Specifically, handling the case where the node to be removed has two children was tricky because the `MyNode` class does not have a setter for the `item` field. I had to manually adjust the parent, left, and right pointers to physically swap the node with its in-order successor instead of just copying the value.

**3. Did you use any outside resources to complete this assignment?**
I consulted the course textbook to refresh my memory on the specific algorithm for BST deletion (finding the in-order successor). I also used the Java documentation to check the syntax for generic array creation.

**4. Please write one or two sentences about something that you learned while completing this assignment.**
I learned that when implementing low-level data structures, immutable node values (no setters) significantly increase the complexity of operations like deletion, requiring structural changes to the tree rather than simple value swapping. I also gained a better understanding of how Hash Tables handle collisions using chaining with BSTs.