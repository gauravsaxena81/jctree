## Introduction ##
Java collections framework has been most surprisingly bereft of tree implementations. This project is an endeavor to remove this deficiency.

Jc-Tree stands for Java Collection-tree. It is so named because its primary interface Tree has been derived from Collections interface just like other collections like Set and List are.

The concrete classes implementing this interface provides a tree structure themselves and implement methods to query this tree structure.

## Getting Started ##

### Maven integration ###

To use jctree in your maven based projects, please use the following:
```
<dependency>
  <groupId>com.googlecode.jctree</groupId>
  <artifactId>jctree</artifactId>
  <version>2.0.0</version>
</dependency>
```

### Usage ###

Let us try to build a n-ary tree.
```
 Tree<String> tree = new ArrayListTree<String>
 tree.add("Level-1");
 tree.add("Level-1", "Level-11");
 tree.add("Level-1", "Level-12");
 tree.add("Level-1", "Level-13");
 tree.add("Level-12", "Level-121");
 tree.add("Level-12", "Level-122");
 tree.add("Level-122", "Level-1221");
 tree.add("Level-13", "Level-131");
 tree.add("Level-13", "Level-132");
 tree.add("Level-13", "Level-133");
 tree.add("Level-11", "Level-111");
 tree.add("Level-11", "Level-112");
```
Find the parent of node "Level-121"
```
 tree.parent("Level-121");
```
Find Children of node "Level-12"
```
 tree.children("Level-12");
```
Get Iterator on the tree. Iterator gives out nodes in the insertion order.
```
 tree.iterator();
```
Do tree operations
```
 tree.isAncestor("Level-1", "Level-1221");
 tree.isDescendant("Level-1221", "Level-1");
 tree.depth();
 tree.preOrderTraversal();
```
Do collections operations
```
 tree.isEmpty();
 tree.size();
 tree.add("Level-1311");//this add operation will add to the root.
```
## Types of trees (Interfaces) ##
There are currently two types of implementations of the trees. The **Tree** type of implementation allows variable number of children to each node.

The **NumberedTree** implementations indexes child of parents. Thus it is possible to insert the first child at index 3 without having children at position 1 and 2. This implementation takes a _pre-conceived number of children_ provided to the constructor. It also provides random access to the children. Thus it is possible to query the children present at index 3 using overloaded child method

The concrete classes implements various kinds of trees like:

  1. ArrayListTree implements a simple n-ary tree which allows each node to have any number of children. This tree uses ArrayList to implement the hierarchy
  1. KAryTree implements a simple n-ary tree which allows each node to have at most k children.
  1. BinaryTree implements a binary tree which allows each node to have at most 2 childten. It also has a left and right methods to access children directly
  1. ArrayTree implements a n-ary tree which allows indexed access to children of each node and allows at most n children, which is to be provided to the constructor. This tree uses Arrays to implement the hierarchy
  1. BinarySearchTree implements a binary search tree which keeps children of a node sorted as in BST. It also has a left and right methods to access children directly