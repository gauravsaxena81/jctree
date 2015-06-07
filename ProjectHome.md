## This project is renamed version of [Jc-Tree](https://code.google.com/p/jc-tree/) ##

This project aims to develop Tree as an implementation of Java collection. Java collection has been mysteriously devoid of a tree implementation.

This project tries to address this by providing classes which are of **Java Collection** type and support tree like hierarchical structures.

To use jctree in your maven based projects, please use the following:
```
<dependency>
  <groupId>com.googlecode.jctree</groupId>
  <artifactId>jctree</artifactId>
  <version>2.0.0</version>
</dependency>
```

You can also download the jar and other artifacts from [https://oss.sonatype.org/#nexus-search;quick~jctree](https://oss.sonatype.org/#nexus-search;quick~jctree)

### Enhancements (for release 2.1) ###
1. Addition of addSubtree method in Tree interface

### Links ###
  1. [Jctree Forum](https://groups.google.com/forum/#!forum/jctree)
  1. [Javadoc](https://jctree.googlecode.com/git/doc/index.html)
  1. [Getting Started](http://code.google.com/p/jctree/wiki/GettingStarted)
  1. http://code.google.com/p/gwt-structs/wiki/ClientJcTree GWT implementation of jctree in [gwt-structs](https://gwt-structs.googlecode.com/files/gwt-structs-0.1.0.1.jar)

### Types of trees (Interfaces) ###
There are currently two types of implementations of the trees. The **Tree** type of implementation allows variable number of children to each node.

The **NumberedTree** implementations indexes child of parents. Thus it is possible to insert the first child at index 3 without having children at position 1 and 2. This implementation takes a _pre-conceived number of children_ provided to the constructor. It also provides random access to the children. Thus it is possible to query the children present at index 3 using overloaded child method

The concrete classes implements various kinds of trees like:

  1. ArrayListTree (n-ary tree) implements a simple n-ary tree which allows each node to have any number of children. This tree uses ArrayList to implement the hierarchy
  1. LinkedTree implements a regular n-ary tree which uses parent and children references to link the nodes
  1. KAryTree implements a simple n-ary tree which allows each node to have at most k children.
  1. BinaryTree (Binary Tree) implements a binary tree which allows each node to have at most 2 childten. It also has a left and right methods to access children directly
  1. ArrayTree (random access tree) implements a n-ary tree which allows indexed access to children of each node and allows at most n children, which is to be provided to the constructor. This tree uses Arrays to implement the hierarchy
  1. BinarySearchTree (binary search tree) implements a binary search tree which keeps children of a node sorted as in BST. It also has a left and right methods to access children directly
  1. SortedChildrenTree implements a regular n-ary tree which extends ArrayListTree and keeps its childten sorted
  1. BinaryRedBlackTree(Beta)- Implementation of self balancing BST red-black trees according to algorithms given in [wikipedia](http://en.wikipedia.org/wiki/Red%E2%80%93black_tree)