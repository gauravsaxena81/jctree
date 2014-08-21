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

import java.util.Collection;
import java.util.Iterator;

class TreeHelper {
	public <E, F> boolean isEqual(Tree<E> testTree, Tree<F> thisTree, E testNode, F thisNode) throws NodeNotFoundException {
		if((thisNode == null && testNode == null))
			return true;
		else if(thisNode != null && testNode != null && thisNode.equals(testNode)) {
			Collection<E> testChildren = testTree.children(testNode);
			Collection<F> thisChildren = thisTree.children(thisNode);
			if(testChildren.equals(thisChildren)) {
				Iterator<E> i = testChildren.iterator();
				Iterator<F> j = thisChildren.iterator();
				for(; i.hasNext() && j.hasNext();)
					if(!isEqual(testTree, thisTree, i.next(), j.next()))
						return false;
				return true;
			} else
				return false;
		} else
			return false;
	}
	//node cannot be false for this implementation
	public <E> boolean isAncestor(Tree<E> tree, E node, E child) throws NodeNotFoundException {
		if(tree.contains(child))
			child = tree.parent(child);
		else
			throw new NodeNotFoundException("child node not found in the tree");
		if(node != null) {
			if(!node.equals(tree.root())) {
				while(child != null) {
					if(child.equals(node))
						return true;
					else
						child = tree.parent(child);
				}
				return false;
			} else
				return true;
		} else
			return false;
	}
	//node cannot be false for this implementation
	public <E> boolean isDescendant(Tree<E> tree, E parent, E node) throws NodeNotFoundException {
		if(tree.contains(parent)) {
			if(node == null)
				return false;
			else return isAncestor(tree, parent, node);
		} else
			throw new NodeNotFoundException("parent node not found in the tree");
	}
	public <E> E commonAncestor(Tree<E> tree, E node1, E node2) throws NodeNotFoundException {
		int height1 = 0;
		E e1 = node1; 
		while(e1 != null) {
			height1++;
			e1 = tree.parent(e1);
		}
		int height2 = 0;
		E e2 = node2; 
		while(e2 != null) {
			height2++;
			e2 = tree.parent(e2);
		}
		if(height1 > height2) {
			while(height1 - height2 > 0) {
				node1 = tree.parent(node1);
				height1--;
			}
		} else {
			while(height2 - height1 > 0) {
				node2 = tree.parent(node2);
				height2--;
			}
		}
		while(node1 != null && !node1.equals(node2)) {
			node1 = tree.parent(node1);
			node2 = tree.parent(node2);
		}
		return node1;
	}
}
