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

import java.util.Collection;

/**
 * The tree is a general interface for all tree types and their implementations. It extends {@link Collection}
 * and thus all its methods.
 * <br>
 * {@link Collection}::add(Object) and {@link Collection}::addAll(Collection c) methods always adds the first element as root and later
 * ones as children of the root. It is better to use methods of {@link Tree}::add(E, E) and 
 * {@link Tree}::add(E, Collection&lt;E&gt; c) as give better control on the tree structure.
 * <br>
 * This interface doesn't guarantee any order to be maintained among the children of the tree. All the elements
 * in the tree are guaranteed to be unique. 
 * <br>
 * Null elements are not allowed in the tree.
 * @author Gaurav Saxena
 *
 * @param <E> object type contained in the tree
 */
public interface Tree<E> extends Collection<E>{
	/**
	 * 
	 * In case an equal object is already present in the tree then the method returns false and the original 
	 * object is replaced. 
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root
	 * @param child object 
	 * @return true if parent was found and child is not already in the tree, otherwise false
	 * @throws NodeNotFoundException if parent is not found
	 *@deprecated Not all trees can add any child to any parent e.g. BST
	 */
	public boolean add(E parent, E child) throws NodeNotFoundException;
	/**
	 * Uses add(E parent, E e) to add all the objects present in the collection
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root 
	 * @param c collection of children to be added to parent
	 * @return true if collection changed as a result of the operation, otherwise false
	 * @throws NodeNotFoundException 
	 * @deprecated Not all trees can add any child to any parent e.g. BST
	 */
	public boolean addAll(E parent, Collection<? extends E> c) throws NodeNotFoundException;
	/**
	 * @param e parent object
	 * @return collection of children
	 * @throws NodeNotFoundException if e is not found
	 */
	public Collection<E> children(E e) throws NodeNotFoundException;
	/**
	 * Finds the common ancestor of node1 and node2
	 * @param node1
	 * @param node2
	 * @return the common ancestor of the nodes
	 * @throws NodeNotFoundException 
	 */
	public E commonAncestor(E node1, E node2) throws NodeNotFoundException;
	/**
	 * @return depth of the tree i.e. the length of the path which has maximum number of nodes
	 */
	public int depth();
	/**
	 * Deprecated in favor of better named {@link #inOrderTraversal()} <br> 
	 * This will be deleted in upcoming versions
	 * @return collection of children arranged as inorderOrderTraversal of underlying tree. In order traversal
	 * publishes children.size / 2 first and then the node and then nodes from children.size / 2 + 1 to
	 * children.size
	 */
	@Deprecated
	public Collection<E> inorderOrderTraversal();
	/**
	 * @return collection of children arranged as inorderOrderTraversal of underlying tree. In order traversal
	 * publishes Ceiling(children.size / 2) first and then the node and then nodes from Ceiling(children.size / 2) + 1 to
	 * children.size
	 */
	public Collection<E> inOrderTraversal();
	
	/**
	 * Finds if the given node is an ancestor of the child node.
	 * @param node the node to search
	 * @param child the ancestors of this will be searched for the node
	 * @return true if any of the parent of child is equal to node, false otherwise. if node and child node are 
	 * equal to each other then it returns false
	 * @throws NodeNotFoundException if child is not found in the tree
	 */
	public boolean isAncestor(E node, E child) throws NodeNotFoundException;
	/**
	 * Finds if the given node is a descendant of the parent node
	 * @param node the node to search
	 * @param parent the descendents of this will be searched for the node
	 * @return true if any of the node belongs to the tree rooted at parent, false otherwise. if node and child 
	 * node are equal to each other then it returns false
	 * @throws NodeNotFoundException if parent is not found in the tree
	 */
	public boolean isDescendant(E parent, E node) throws NodeNotFoundException;
	/**
	 * @return all the leaves of the tree i.e. those nodes which do not have children
	 */
	public Collection<E> leaves();
	/**
	 * @return collection of children arranged as levelOrderTraversal of underlying tree
	 */
	public Collection<E> levelOrderTraversal();
	/**
	 * @param e child object
	 * @return the parent object. Null if e is root
	 * @throws NodeNotFoundException if e is not found
	 */
	public E parent(E e) throws NodeNotFoundException;
	/**
	 * @return collection of children arranged as postOrderTraversal of underlying tree
	 */
	public Collection<E> postOrderTraversal();
	/**
	 * @return collection of children arranged as preOrderTraversal of underlying tree
	 */
	public Collection<E> preOrderTraversal();
	/**
	 * @return the root node or null if tree is empty
	 */
	public E root();
	/**
	 * @return all the siblings of the node i.e. those nodes which have the same parent as the parameter
	 * @throws NodeNotFoundException 
	 */
	public Collection<E> siblings(E e) throws NodeNotFoundException;
}
