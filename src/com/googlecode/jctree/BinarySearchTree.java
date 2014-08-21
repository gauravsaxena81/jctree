/*
 * Copyright 2014 Gaurav Saxena
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.jctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements traditional BST as left-right-parent links of a node. This gives log(n) complexity for operations unlike {@link ArrayListBinarySearchTree}.
 * @author Gaurav Saxena
 *
 * @param <E>
 */
public class BinarySearchTree<E extends Comparable<E>> implements SortedTree<E>, Cloneable {
	private class Node {
		Node parent,
		left,
		right;
		E value;
	}
	private int size = 0;
	private int depth = 0;
	private Node root;
	/** 
	 * A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)
	 * The method throws {@link UnsupportedOperationException}
	 */
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		throw new UnsupportedOperationException("A binary search tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)");
	}
	/**
	 * If tree is empty, it adds a root. In case tree is not empty, it will attempt to add parameter as a child of the root 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E child) {
		try {
			if(size == 0) {
				addRoot(child);
				return true;
			} else {
				Node parent = findParent(root, child);
				if(parent != null) {
					addChild(parent, child, parent.value.compareTo(child) > 0);
					return true;
				} else
					return false;
			}
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void addChild(Node parent, E child, boolean isLeft) throws NodeNotFoundException {
		checkNode(child);
		Node childNode = new Node();
		childNode.value = child;
		childNode.parent = parent;
		if(isLeft)
			parent.left = childNode;
		else
			parent.right = childNode;
		size++;
		depth = recalculateDepth(root, 0);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
			retVal |= add(iterator.next());
		return retVal;
	}
	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		try{
			for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
				add(parent, iterator.next());
			return true;
		} catch(NodeNotFoundException ex) {
			return false;
		}
	}
	private Node node(Node parent, Comparable<E> child) throws NodeNotFoundException {
		if(child.compareTo(parent.value) > 0) {
			Node right = parent.right;
			if(right != null)
				return node(right, child);
			else
				throw new NodeNotFoundException("No node was found for object");
		} else if(child.compareTo(parent.value) < 0) {
			Node left = parent.left;
			if(left != null)
				return node(left, child);
			else
				throw new NodeNotFoundException("No node was found for object");
		} else
			return parent;
	}
	@Override
	public List<E> children(E e) throws NodeNotFoundException {
		checkNode(e);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the parameter");
		else {
			ArrayList<E> children = new ArrayList<E>(2);
			Node node = node(root, e);
			if(node.left != null)
				children.add(node.left.value);
			if(node.right != null)
				children.add(node.right.value);
			return children;
		}
	}
	@Override
	public void clear() {
		root = null;
		size = 0;
		depth = 0;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		BinarySearchTree<E> clone = null;
		try {
			clone = (BinarySearchTree<E>) super.clone();
			clone.depth = this.depth;
			clone.root = new Node();
			clone.size = this.size;
			copy(clone.root, this.root);
		} catch (CloneNotSupportedException e) {
			//This should't happen because we are cloneable
		}
		return clone;
	}
	//TODO change this implementation to an iterative one to avoid StackOverflow in large trees
	private void copy(Node cloneNode, Node node) {
		if(node.left != null) {
			cloneNode.left = new Node();
			cloneNode.left.parent = cloneNode;
			copy(cloneNode.left, node.left);
		}
		cloneNode.value = node.value;
		if(node.right != null) {
			cloneNode.right = new Node();
			cloneNode.right.parent = cloneNode;
			copy(cloneNode.right, node.right);
		}
	}
	@Override
	public E commonAncestor(E node1, E node2) throws NodeNotFoundException {
		checkNode(node1);
		checkNode(node2);
		return new TreeHelper().commonAncestor(this, node1, node2);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		if(o == null || isEmpty())
			return false;
		else if (o instanceof Comparable){
			try {
				return node(root, (Comparable<E>) o) != null;
			} catch (NodeNotFoundException e) {
				return false;
			}
		} else
			return searchTree(root, o) != null;
			
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object i : c)
			if(!contains(i))
				return false;
		return true;
	}
	@Override
	public int depth() {
		return depth;
	}
	@Override
	@Deprecated
	public List<E> inorderOrderTraversal() {
		return inOrderTraversal(root, new ArrayList<E>());
	}
	@Override
	public List<E> inOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return inOrderTraversal(root, new ArrayList<E>());
	}
	@Override
	public boolean isAncestor(E node, E child) throws NodeNotFoundException {
		checkNode(child);
		return new TreeHelper().isAncestor(this, node, child);
	}
	@Override
	public boolean isDescendant(E parent, E node) throws NodeNotFoundException {
		checkNode(parent);
		return new TreeHelper().isDescendant(this, parent, node);
	}
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	@Override
	public Iterator<E> iterator() {
		return getCurrentList().iterator();
	}
	@Override
	public List<E> leaves() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return leaves(root, new ArrayList<E>());
	}
	private List<E> leaves(Node node, ArrayList<E> list) {
		if(node.left != null)
			leaves(node.left, list);
		if(node.left == null && node.right == null)
			list.add(node.value);
		if(node.right != null)
			leaves(node.right, list);
		return list;
	}
	@Override
	public List<E> levelOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else {
			LinkedList<Node> queue = new LinkedList<Node>();
			queue.add(root);
			return levelOrderTraversal(new ArrayList<E>(), queue);
		}
	}
	@Override
	public E parent(E e) throws NodeNotFoundException {
		checkNode(e);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the parameter");
		else {
			Node parent = node(root, e).parent;
			if(parent != null)
				return parent.value;
			else
				return null;
		}
	}
	@Override
	public List<E> postOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return postOrderTraversal(root, new ArrayList<E>());
	}
	public List<E> preOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return preOrderTraversal(root, new ArrayList<E>());
	}
	/**
	 * Deletes node as mentioned in <a href="http://en.wikipedia.org/wiki/Binary_search_tree#Deletion">BST</a>.
	 * Randomizes node to replace (predecessor / successor) when deleting an inner node. This helps keep tree balanced
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		checkNode(o);
		try {
			Node node;
			if(isEmpty())
				return false;
			else if(o instanceof Comparable) {
				node = node(root, (Comparable<E>) o);
			} else
				node = searchTree(root, o);
			boolean remove = remove(node);
			size--;
			depth = recalculateDepth(root, 0);
			return remove;
		} catch (NodeNotFoundException e) {
			return false;
		}
	}
	private Node searchTree(Node node, Object o) {
		if(node.left != null) {
			Node nodeReturned = searchTree(node.left, o);
			if(nodeReturned != null)
				return nodeReturned;
		}
		if(node.right != null) {
			Node nodeReturned = searchTree(node.right, o);
			if(nodeReturned != null)
				return nodeReturned;
		}
		if(o.equals(node.value))
			return node;
		else
			return null;
	}
	private boolean remove(Node node) throws NodeNotFoundException {
		int children = 0;
		if(node.left != null)
			children++;
		if(node.right != null)
			children++;
		if(children == 0)
			deleteCase1(node);
		else if(children == 1)
			deleteCase2(node);
		else
			deferDelete(node);
		return true;
	}
	private void deferDelete(Node node) throws NodeNotFoundException {
		Node nodeToReplace;
		if(Math.random() > 0.5)
			nodeToReplace = successorNode(node);
		else
			nodeToReplace = predecessorNode(node);
		node.value = nodeToReplace.value;
		remove(nodeToReplace);
	}
	private void deleteCase2(Node node) throws NodeNotFoundException {
		Node child;
		if(node.left != null)
			child = node.left;
		else
			child = node.right;
		node.value = child.value;
		remove(child);
	}
	private void deleteCase1(Node node) {
		if(node.parent.left == node)
			node.parent.left = null;
		else
			node.parent.right = null;
		node = null;
	}
	@Override
	public E successor(E value) throws NodeNotFoundException {
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the parameter");
		else
			return successorNode(node(root, value)).value;
	}
	private Node successorNode(Node node) throws NodeNotFoundException {
		Node right = node.right;
		if(right != null) {
			node = right;
			while(node.left != null)
				node = node.left;
			return node;
		} else {
			while(!node.parent.right.value.equals(node.value))
				node = node.parent;
			return node;
		}
	}
	@Override
	public E predecessor(E value) throws NodeNotFoundException {
		checkNode(value);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the parameter");
		else
			return predecessorNode(node(root, value)).value;
	}
	private Node predecessorNode(Node node) throws NodeNotFoundException {
		Node left = node.left;
		if(left != null) {
			node = left;
			while(node.right != null)
				node = node.right;
			return node;
		} else {
			while(!node.parent.left.value.equals(node.value))
				node = node.parent;
			return node;
		}
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retVal = false;
		for (Iterator<?> iterator = c.iterator(); iterator.hasNext();)
			retVal |= remove(iterator.next());
		return retVal;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Tree interface doesn't support retainAll");
	}

	@Override
	public E root() {
		if(isEmpty())
			return null;
		else
			return root.value;
	}
	@Override
	public List<E> siblings(E e) throws NodeNotFoundException {
		checkNode(e);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the object");
		else {
			E parent = parent(e);
			if(parent != null) {
				List<E> children = children(parent);
				children.remove(e);
				return children;
			}
			else
				return new ArrayList<E>();
		}
	}
	@Override
	public int size() {
		return size;
	}
	@Override
	public Object[] toArray() {
		return getCurrentList().toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return getCurrentList().toArray(a);
	}

	private void addRoot(E root) {
		Node rootNode = new Node();
		rootNode.value = root;
		this.root = rootNode;
		size++;
		depth++;
	}

	private void checkNode(Object child) {
		if(child == null)
			throw new IllegalArgumentException("null nodes are not allowed");
	}

	private List<E> getCurrentList() {
		return inOrderTraversal();
	}
	private List<E> inOrderTraversal(Node node, ArrayList<E> list) {
		if(node.left != null)
			inOrderTraversal(node.left, list);
		list.add(node.value);
		if(node.right != null)
			inOrderTraversal(node.right, list);
		return list;
	}
	private List<E> levelOrderTraversal(ArrayList<E> list, LinkedList<Node> queue) {
		while(!queue.isEmpty()) {
			list.add(queue.getFirst().value);
			if(queue.getFirst().left != null)
				queue.add(queue.getFirst().left);
			if(queue.getFirst().right != null)
				queue.add(queue.getFirst().right);
			queue.remove();
		}
		return list;
	}
	private List<E> postOrderTraversal(Node node, ArrayList<E> list) {
		if(node.left != null)
			postOrderTraversal(node.left, list);
		if(node.right != null)
			postOrderTraversal(node.right, list);
		list.add(node.value);
		return list;
	}
	private List<E> preOrderTraversal(Node node, ArrayList<E> list) {
		list.add(node.value);
		if(node.left != null)
			preOrderTraversal(node.left, list);
		if(node.right != null)
			preOrderTraversal(node.right, list);
		return list;
	}
	private int recalculateDepth(Node node, int depth) {
		int childDepth = depth + 1;
		if(node.left == null && node.right == null)
			return childDepth;
		else {
			if(node.left != null)
				depth = Math.max(depth, recalculateDepth(node.left, childDepth));
			if(node.right != null)
				depth = Math.max(depth, recalculateDepth(node.right, childDepth));
		}
		return depth;
	}
	@Override
	public String toString() {
		return getCurrentList().toString();
	}

	private Node findParent(Node parent, E child) throws NodeNotFoundException {
		if(child.compareTo(parent.value) > 0) {
			Node right = parent.right;
			if(right != null)
				return findParent(right, child);
			else
				return parent;
		} else if(child.compareTo(parent.value) < 0) {
			Node left = parent.left;
			if(left != null)
				return findParent(left, child);
			else
				return parent;
		} else
			return null;//Such a node already exists
	}
	/**
	 * @param parent
	 * @return the left child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
	public E left(E parent) throws NodeNotFoundException {
		checkNode(parent);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for object");
		else {
			Node left = node(root, parent).left;
			if(left != null)
				return node(root, parent).left.value;
			else
				return null;
		}
	}
	/**
	 * @param parent
	 * @return the right child if present, or null otherwise
	 * @throws NodeNotFoundException
	 */
	public E right(E parent) throws NodeNotFoundException {
		checkNode(parent);
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for object");
		else {
			Node right = node(root, parent).right;
			return right.value;
		}
	}
	@Override
	public int hashCode() {
		return getCurrentList().hashCode();
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof BinarySearchTree) {
			try {
				return new TreeHelper().isEqual((BinarySearchTree<E>) o, this, ((BinarySearchTree<E>) o).root(), root());
			} catch (NodeNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}
}
