# Homework 3 - Book Recommendation

## How long did it take you to complete this assignment?

Approximately 3 hours.

## What parts of this assignment did you find most difficult?

The most challenging part was Part 5 (Genre Hopper). Getting the bidirectional BFS correct required careful thought around expanding full layers rather than single nodes, since alternating one node at a time can produce suboptimal paths. Ensuring the meeting point actually lies on a shortest path added some debugging time. The median-filtered edge pruning was straightforward, but combining it with the dual-source traversal and path reconstruction took the most iteration.

The heap-based top-k selection also required attention to detail — specifically getting the min-heap comparator right so that ties break alphabetically in the correct direction when polling in reverse order.

## Did you use any outside resources?

No.

## What did you learn?

Bidirectional BFS is a useful optimization for unweighted shortest-path problems, but it only guarantees correctness if each side fully expands its current depth frontier before switching. This assignment also reinforced how a well-chosen graph representation (adjacency map vs. matrix, bipartite lookup maps) simplifies downstream algorithm implementation significantly.




