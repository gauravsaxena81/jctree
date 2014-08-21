/*
 * Copyright 2013 Gaurav Saxena
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
 * This is a general purpose tree where each node is free to have any number of children
 * This implementation of tree interface is done using {@link ArrayList}as underlying data structure. 
 * As a result, children are maintained in insertion order under their respective parents 
 * @author Gaurav Saxena
 *
 * @param <E>
 */
public class LinkedTree<E> implements Tree<E>, Cloneable{
	private int size = 0;
	private int depth = 0;
	private Entry<E> root;
	/* 
	 * @see java.util.Collection#add(java.lang.Object)
	 * If tree is empty, it adds a root. In case tree is not empty, it will attempt to add parameter as a child of the root
	 */
	@Override
	public boolean add(E e) {
		try{
			if(isEmpty())
				return add(null, e);
			else
				return add(root.element, e);
		} catch(NodeNotFoundException ex) {
			throw new IllegalArgumentException(ex);//This should never happen as when tree is empty, we are adding the root and when it is not then we are adding to the root, which will always be present in a non-empty tree
		}
	}
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		checkNode(child);
		if(parent == null) {
			if(isEmpty()) {
				root = new Entry<E>(child, null);
				size++;
				depth++;
				return true;
			} else
				throw new NullPointerException("parent cannot be null except for root element");
		}
		Entry<E> parentEntry = getNode(parent);
		Entry<E> childEntry = getNode(child);
		if(parentEntry != null) {
			if(childEntry == null) {
				parentEntry.children.add(new Entry<E>(child, parentEntry));
				size++;
				int currentDepth = 1;
				//TODO extract to a new method
				while(parentEntry != null) {
					currentDepth++;
					parentEntry = parentEntry.parent;
				}
				depth = Math.max(currentDepth, depth);
				return true;
			} else {
				if(childEntry.parent != null)
					childEntry.parent.children.set(childEntry.parent.children.indexOf(childEntry), new Entry<E>(child, parentEntry));
				else
					root.element = child;
				return false;
			}
		} else
			throw new NodeNotFoundException("No node was found for parent object");
	}
	private Entry<E> getNode(Object node) {
		if(!isEmpty()) {
			LinkedList<Entry<E>> queue = new LinkedList<Entry<E>>();
			queue.add(root);
			while(!queue.isEmpty()) {
				if(queue.getFirst().element.equals(node))
					return queue.getFirst();
				else for(Entry<E> i : queue.poll().children)
					queue.add(i);
			}
		}
		return null;
	}
	protected int getChildAddPosition(List<E> children, E child) {
		return children.size();
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (E e : c)
			retVal |= add(e);
		return retVal;
	}
	public boolean addAll(E parent, Collection<? extends E> c) throws NodeNotFoundException {
		boolean retVal = false;
		for (E e : c)
			retVal |= add(parent, e);
		return retVal;
	}
	@Override
	public List<E> children(E e) throws NodeNotFoundException {
		checkNode(e);
		List<Entry<E>> childrenEntries = getNode(e).children;
		ArrayList<E> children = new ArrayList<E>();
		for(Entry<E> i : childrenEntries)
			children.add(i.element);
		return children;
	}
	@Override
	public void clear() {
		root = null;
		size = 0;
		depth = 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
	    LinkedTree<E> v = null;
		try {
			v = (LinkedTree<E>) super.clone();
			makeTree(v);
		} catch (CloneNotSupportedException e) {
			//This should't happen because we are cloneable
		}
		return v;
	}
	private void makeTree(LinkedTree<E> v) {
		LinkedList<Entry<E>> queue = new LinkedList<Entry<E>>();
		LinkedList<Entry<E>> newQueue = new LinkedList<Entry<E>>();
		queue.add(root);
		newQueue.add(new Entry<E>(root.element, null));
		v.root = newQueue.getFirst();
		while(!queue.isEmpty()) {
			Entry<E> parent = newQueue.poll();
			for(Entry<E> i : queue.poll().children) {
				queue.add(i);
				parent.children.add(new Entry<E>(i.element, parent));
			}
			newQueue.addAll(parent.children);
		}
	}
	@Override
	public E commonAncestor(E node1, E node2) throws NodeNotFoundException {
		int height1 = 0;
		E e1 = node1; 
		while(e1 != null) {
			height1++;
			e1 = parent(e1);
		}
		int height2 = 0;
		E e2 = node2; 
		while(e2 != null) {
			height2++;
			e2 = parent(e2);
		}
		if(height1 > height2) {
			while(height1 - height2 > 0) {
				node1 = parent(node1);
				height1--;
			}
		} else	{
			while(height2 - height1 > 0) {
				node2 = parent(node2);
				height2--;
			}
		}
		while(node1 != null && !node1.equals(node2)) {
			node1 = parent(node1);
			node2 = parent(node2);
		}
		return node1;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		if(o == null)
			return false;
		else
			return getNode((E) o) != null;
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
		return inOrderTraversal(root);
	}
	@Override
	public List<E> inOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return inOrderTraversal(root);
	}
	@Override
	public boolean isAncestor(E node, E child) throws NodeNotFoundException {
		child = parent(child);
		while(child != null) {
			if(child.equals(node))
				return true;
			else
				child = parent(child);
		}
		return false;
	}
	@Override
	public boolean isDescendant(E parent, E node) throws NodeNotFoundException {
		checkNode(node);
		Entry<E> nodeEntry = getNode(node);
		if(nodeEntry != null) {
			E child = parent(node);
			while(child != null) {
				if(child.equals(parent))
					return true;
				else
					child = parent(child);
			}
			return false;
		} else
			throw new NodeNotFoundException("No node was found for object");
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
			return leaves(root);
	}
	private List<E> leaves(Entry<E> node) {
		List<Entry<E>> children = node.children;
		List<E> list = new ArrayList<E>();
		if(children.size() > 0) {
			int i = 0;
			for(int len = (int)Math.ceil((double)children.size() / 2); i < len; i++)
				list.addAll(leaves(node.children.get(i)));
			if(node.children.isEmpty())
				list.add(node.element);
			for(int len = children.size(); i < len; i++)
				list.addAll(leaves(node.children.get(i)));
		} else if(node.children.isEmpty())
			list.add(node.element);
		return list;
	}
	@Override
	public List<E> levelOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else {
			LinkedList<Entry<E>> queue = new LinkedList<Entry<E>>();
			queue.add(root);
			return levelOrderTraversal(queue);
		}
	}
	@Override
	public E parent(E e) throws NodeNotFoundException {
		checkNode(e);
		Entry<E> childEntry = getNode(e);
		if(childEntry != null) {
			if(childEntry.parent != null)
				return childEntry.parent.element;
			else
				return null;
		} else
			throw new NodeNotFoundException("No node was found for object");
	}
	@Override
	public List<E> postOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
			
		else
			return postOrderTraversal(root);
	}
	@Override
	public List<E> preOrderTraversal() {
		if(isEmpty())
			return new ArrayList<E>();
		else
			return preOrderTraversal(root);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		checkNode(o);
		Entry<E> node = getNode((E) o);
		if(node != null) {
			boolean isRemoved; 
			if(node.equals(root)) {
				root = null;
				isRemoved = true;
				depth = 0;
				size = 0;
			} else {
				isRemoved = node.parent.children.remove(node);
				size = 0;
				depth = 0;
				recalculateDepthAndSize(root, 0);
			}
			return isRemoved;
		} else
			return false;
	}

	private int recalculateDepthAndSize(Entry<E> node, int currentDepth) {
		int childDepth = currentDepth + 1;
		size++;
		if(node.children.isEmpty())
			return childDepth;
		else for(Entry<E> i : node.children)
			depth = Math.max(depth, recalculateDepthAndSize(i, childDepth));
		return depth;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retVal = false;
		for (Object e: c)
			retVal |= remove(e);
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
			return root.element;
	}
	@Override
	public List<E> siblings(E e) throws NodeNotFoundException {
		checkNode(e);
		E parent = parent(e);
		if(parent != null) {
			List<E> children = children(parent);
			children.remove(e);
			return children;
		} else
			return new ArrayList<E>();
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

	private void checkNode(Object child) {
		if(child == null)
			throw new IllegalArgumentException("null nodes are not allowed");
	}
	private List<E> getCurrentList() {
		return inOrderTraversal();
	}
	private List<E> inOrderTraversal(Entry<E> node) {
		List<Entry<E>> children = node.children;
		List<E> list = new ArrayList<E>();
		if(children.size() > 0) {
			int i = 0;
			for(int len = (int)Math.ceil((double)children.size() / 2); i < len; i++)
				list.addAll(inOrderTraversal(node.children.get(i)));
			list.add(node.element);
			for(int len = children.size(); i < len; i++)
				list.addAll(inOrderTraversal(node.children.get(i)));
		} else
			list.add(node.element);
		return list;
	}
	private List<E> levelOrderTraversal( LinkedList<Entry<E>> queue) {
		ArrayList<E> list = new ArrayList<E>();
		while(!queue.isEmpty()) {
			for(Entry<E> i : queue.getFirst().children)
				queue.add(i);
			list.add(queue.poll().element);
		}
		return list;
	}
	private List<E> postOrderTraversal(Entry<E> node) {
		ArrayList<E> list = new ArrayList<E>();
		for(Entry<E> i : node.children)
			list.addAll(postOrderTraversal(i));
		list.add(node.element);
		return list;
	}
	private List<E> preOrderTraversal(Entry<E> node) {
		ArrayList<E> list = new ArrayList<E>();
		list.add(node.element);
		for(Entry<E> i : node.children)
			list.addAll(preOrderTraversal(i));
		return list;
	}
	@Override
	public String toString() {
		return getCurrentList().toString();
	}
	private static class Entry<E> {
		E element;
		Entry<E> parent;
		ArrayList<Entry<E>> children;
		public Entry(E element, Entry<E> parent) {
			super();
			this.element = element;
			this.parent = parent;
			this.children = new ArrayList<LinkedTree.Entry<E>>();
		}
	}
}