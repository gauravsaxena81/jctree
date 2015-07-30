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
 * 
 * @author Gaurav Saxena
 * Implements Self-balancing red black tree as given in <a href='http://en.wikipedia.org/wiki/Red%E2%80%93black_tree'>Wikipedia</a>
 * @param <E>
 */
public class BinaryRedBlackTree<E extends Comparable<E>> implements SortedTree<E>, Cloneable {
	private class Node {
		Node parent, left, right;
		E value;
		COLOR color;
	}
	private enum COLOR {RED, BLACK};
	private int size = 0;
	private int depth = 0;
	private Node root;
	
	@Override
	public boolean add(E child) {
		try {
			if(size == 0) {
				addRoot(child);
				return true;
			} else {
				Node parent = findParent(root, child);
				if(parent != null) {
					return addNode(parent, child);
				} else {
					Node node = node(root, child);
					node.value = child;
					return false;
				}
			}
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean addNode(Node parent, E child) throws NodeNotFoundException {
		checkNode(child);
		mendTree(parent, addChild(parent, child));
		size++;
		depth = recalculateDepth(root, 0);
		return true;
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
	private void mendTree(Node parent, Node child) throws NodeNotFoundException {
		inserCase1(parent, child);
	}
	private void inserCase1(Node parent, Node child) throws NodeNotFoundException {
		if(parent == null)
			child.color = COLOR.BLACK;
		else
			inserCase2(parent, child);
	}
	private void inserCase2(Node parent, Node child) throws NodeNotFoundException {
		if(!(parent.color == COLOR.BLACK)) //case 2
			insertCase3(parent, child);
	}
	private void insertCase3(Node parent, Node child) throws NodeNotFoundException {
		Node uncle = uncle(child);
		if(uncle != null) {
			if(uncle.color == COLOR.RED) {
				parent.color = COLOR.BLACK;
				uncle.color = COLOR.BLACK;
				child.parent.parent.color = COLOR.RED;
				if(parent.parent != null)
					inserCase1(parent.parent.parent, parent.parent);
			} else
				insertCase4(parent, child);
		} else
			insertCase4(parent, child);
	}
	private void insertCase4(Node parent, Node child) throws NodeNotFoundException {
		Node grandParent = parent.parent;
		if(parent.right != null && parent.right.value.equals(child.value) && grandParent.left != null && grandParent.left.value.equals(parent.value)) {
			rotateLeft(parent);
		} else if(parent.left != null && parent.left.value.equals(child.value) && grandParent.right != null && grandParent.right.value.equals(parent.value)) {
			rotateRight(parent);
		} else
			insertCase5(parent, child);
	}
	private void insertCase5(Node parent, Node child) throws NodeNotFoundException {
		Node grandParent = parent.parent;
		parent.color = COLOR.BLACK;
		child.color = COLOR.RED;
		if(parent.left != null && parent.left.value.equals(child.value))
			rotateRight(grandParent);
		else
			rotateLeft(grandParent);
	}
	//http://upload.wikimedia.org/wikipedia/commons/2/23/Tree_rotation.png
	private void rotateRight(Node q) throws NodeNotFoundException {
		if(!q.value.equals(root.value)) {
			Node p = q.left;
			Node b = p.right;
			
			p.parent = q.parent;
			if(q.parent.left != null && q.parent.left.value.equals(q.value))
				q.parent.left = p;
			else
				q.parent.right = p;
			p.right = q;
			q.parent = p;
			q.left = b;
			if(b != null)
				b.parent = q;
		}
	}
	//http://upload.wikimedia.org/wikipedia/commons/2/23/Tree_rotation.png
	private void rotateLeft(Node p) throws NodeNotFoundException {
		if(!p.value.equals(root.value)) {
			Node q = p.right;
			Node b = q.left;
			q.parent = p.parent;
			if(p.parent.left != null && p.parent.left.value.equals(p.value)) 
				p.parent.left = q;
			else
				p.parent.right = q;
			q.left = p;
			p.parent = q;
			p.right = b;
			if(b != null)
				b.parent = p;
		}
	}
	private Node uncle(Node child) throws NodeNotFoundException {
		Node parentNode = child.parent;
		Node grandParentNode;
		if(parentNode != null)
			grandParentNode = parentNode.parent;
		else
			return null;
		if(grandParentNode != null && grandParentNode.left != null) {
			if(grandParentNode.left.value.equals(parentNode.value))
				return grandParentNode.right;
			else
				return grandParentNode.left;
		} else
			return null;
	}
	/**
	 * Unsupported Operation
	 * A red-black tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use {@link #add(Comparable)})
	 * @see com.googlecode.jctree.Tree#add(java.lang.Object, java.lang.Object)
	 **/
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		throw new UnsupportedOperationException("A red-black tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)");
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();)
			retVal |= add(iterator.next());
		return retVal;
	}
	/**
	 * Unsupported Operation
	 * A red-black tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use {@link #addAll(Collection)})
	 * @see com.googlecode.jctree.Tree#add(java.lang.Object, java.lang.Object)
	 **/
	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		throw new UnsupportedOperationException("A red-black tree determines parent of a child on its own and hence it is not possible to add the child to any given parent. Please use add(child)");
	}
	@Override
	public List<E> children(E e) throws NodeNotFoundException {
		checkNode(e);
		ArrayList<E> list = new ArrayList<E>(2);
		if(size != 0) {
			Node node = node(root, e);
			if(node.left != null)
				list.add(node.left.value);
			if(node.right != null)
				list.add(node.right.value);
		} else
			throw new NodeNotFoundException("No node was found for object");
		return list;
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
		BinaryRedBlackTree<E> clone = null;
		try {
			clone = (BinaryRedBlackTree<E>) super.clone();
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
			cloneNode.color = node.color;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean contains(Object o) {
		if (o == null || size == 0)
			return false;
		else if (o instanceof Comparable) {
			try {
				node(root, (Comparable) o);
				return true;
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
		if(size == 0)
			throw new NodeNotFoundException("No node was found for object");
		Node node = node(root, e);
		if(node != root)
			return node.parent.value;
		else
			return null;
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
	@Override
	public E successor(E value) throws NodeNotFoundException {
		if(isEmpty())
			throw new NodeNotFoundException("No node was found for the parameter");
		else
			return successorNode(node(root, value)).value;
	}
	private Node successorNode(Node node) throws NodeNotFoundException {
		BinaryRedBlackTree<E>.Node right = node.right;
		if(right != null) {
			while(right.left != null) {
				right = right.left;
			}
			return right;
		} else {
			BinaryRedBlackTree<E>.Node parent = node.parent;
			while(parent != null) {
				if(parent.left == node) { //I am right node
					return parent;
				} else { //I am left node
					node = parent;
					parent = parent.parent;
				}
			}
			return null;//I am left most node, so there is no predecessor
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
		BinaryRedBlackTree<E>.Node left = node.left;
		if(left != null) {
			while(left.right != null) {
				left = left.right;
			}
			return left;
		} else {
			BinaryRedBlackTree<E>.Node parent = node.parent;
			while(parent != null) {
				if(parent.right == node) { //I am right node
					return parent;
				} else { //I am left node
					node = parent;
					parent = parent.parent;
				}
			}
			return null;//I am left most node, so there is no predecessor
		}
	}
	private boolean remove(Node node) {
		try {
			
			if(node.left != null && node.right != null)
				deferDelete(node);
			else
				deleteCaseLeaf(node);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
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
	private void deleteCaseLeaf(Node node) throws NodeNotFoundException {
		if(node.left == null && node.right == null) {
			if(node.parent.left != null && node.parent.left == node)
				node.parent.left = null;
			else
				node.parent.right = null;
			node = null;
		} else
			deleteCaseRedNode(node);
	}
	private void deleteCaseRedNode(Node node) throws NodeNotFoundException {
		if(node.color == COLOR.RED) {
			Node child = node.left != null ? node.left : node.right;
			child.parent = node.parent;
			if(node.parent.left != null && node.parent.left.value.equals(node.value))
				node.parent.left = child;
			else
				node.parent.right = child;
		} else
			deleteCase0(node);
	}
	/** For testing
	 * @param o
	 * @param red
	 * @throws NodeNotFoundException
	 */
	void setColor(E o, boolean red) throws NodeNotFoundException {
		Node node = node(root, o);
		if(red)
			node.color = COLOR.RED;
		else
			node.color = COLOR.BLACK;
	}
	//Assuming there is only one non-leaf children attached to node
	private void deleteCase0(Node node) throws NodeNotFoundException {
		if(node.color == COLOR.BLACK) {
			Node child = node.left != null ? node.left : node.right;
			child.parent = node.parent;
			if(node.parent.left != null && node.parent.left.value.equals(node.value))
				node.parent.left = child;
			else
				node.parent.right = child;
			if(child.color == COLOR.RED)
				child.color = COLOR.BLACK;
			else
				deleteCase1(child);
		}
	}
	private void deleteCase1(Node node) throws NodeNotFoundException {
		if(!node.value.equals(root.value))
			deleteCase2(node);
	}
	private void deleteCase2(Node node) throws NodeNotFoundException {
		Node sibling = (node.parent.left != null && node.parent.left.value.equals(node.value)) ? 
				node.parent.right 
				: node.parent.left;
		if(sibling.color == COLOR.RED) {
			node.parent.color = COLOR.RED;
			sibling.color = COLOR.RED;
			if(node.parent.left != null && node.value.equals(node.parent.left.value))
				rotateLeft(node.parent);
			else
				rotateRight(node.parent);
		} else
			deleteCase3(node, sibling, node.parent);
	}
	private void deleteCase3(Node node, Node sibling, Node parent) throws NodeNotFoundException {
		Node left = sibling.left;
		Node right = sibling.right;
		if(parent.color == COLOR.BLACK && sibling.color == COLOR.BLACK &&
				(left == null || left.color == COLOR.BLACK) &&
				(right == null || right.color == COLOR.BLACK)) {
			sibling.color = COLOR.RED;
			deleteCase1(parent);
		} else
			deleteCase4(node, sibling, parent, left, right);
	}
	private void deleteCase4(Node node, Node sibling, Node parent, Node left, Node right) throws NodeNotFoundException {
		if(parent.color == COLOR.RED &&
				sibling.color == COLOR.BLACK &&
				(left == null || left.color == COLOR.BLACK) &&
				(right == null || right.color == COLOR.BLACK)) {
			sibling.color = COLOR.RED;
			parent.color = COLOR.BLACK;
		} else
			deleteCase5(node, sibling, parent, left, right);
	}
	private void deleteCase5(Node node, Node sibling, Node parent, Node left, Node right) throws NodeNotFoundException {
		if(sibling.color == COLOR.BLACK) {
			if(parent.left != null && parent.left.value.equals(node.value) &&
					(left.color == COLOR.RED) &&
					(right == null || right.color == COLOR.BLACK)) {
				sibling.color = COLOR.RED;
				left.color = COLOR.BLACK;
				rotateRight(sibling);
			} else if(parent.right != null && parent.right.value.equals(node.value) &&
					(left == null || left.color == COLOR.BLACK) &&
					right.color == COLOR.RED) {
				sibling.color = COLOR.RED;
				right.color = COLOR.BLACK;
				rotateLeft(sibling);
			}
		}
		deleteCase6(node, sibling, parent, left, right);
	}
	private void deleteCase6(Node node, Node sibling, Node parent, Node left, Node right) throws NodeNotFoundException {
		sibling.color = parent.color;
		parent.color = COLOR.BLACK;
		if(parent.left != null && node.value.equals(parent.left.value)) {
			right.color = COLOR.BLACK;
			rotateLeft(parent);
		} else {
			left.color = COLOR.BLACK;
			rotateRight(parent);
		}
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
		if(size == 0)
			throw new NodeNotFoundException("No node was found for the object");
		else {
			Node parent = node(root, e).parent;
			ArrayList<E> children = new ArrayList<E>(1);
			if(parent != null) {
				if(parent.left != null && parent.left.value.equals(e)) {
					if(parent.right != null)
						children.add(parent.right.value);
				} else
					children.add(parent.left.value);
			}
			return children;
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

	private Node addChild(Node parentNode, E child) {
		Node childNode = new Node();
		childNode.parent = parentNode;
		childNode.color = COLOR.RED;
		childNode.value = child;
		if(parentNode.value.compareTo(child) < 0)
			parentNode.right = childNode;
		else
			parentNode.left = childNode;
		return childNode;
	}
	private void addRoot(E child) {
		root = new Node();
		root.value = child;
		root.color = COLOR.BLACK;
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
			Node node = node(root, parent);
			if(node.left != null)
				return node.left.value;
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
			Node node = node(root, parent);
			if(node.right != null)
				return node.right.value;
			else
				return null;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof BinaryRedBlackTree) {
			try {
				return new TreeHelper().isEqual((BinaryRedBlackTree<E>) o, this, ((BinaryRedBlackTree<E>) o).root(), root());
			} catch (NodeNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}
}