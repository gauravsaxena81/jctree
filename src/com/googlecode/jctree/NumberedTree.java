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
/**
 * The nodes in its implementing classes always have a particular number of children. It is not possible to add more children 
 * than the number defined in the implementing class. Since the children are numbered, it is possible to access them
 * by index.
 * add(parent, child) of {@link Tree} adds child to the first available slot. Thus, it is better to add nodes using 
 * add(parent, child, index)
 * @author Gaurav Saxena
 * @param <E>
 */
public interface NumberedTree<E> extends Tree<E>{
	/**
	 * Adds a child to a parent at a particular index
	 * If another object is present at child index then that is replaced with the new one
	 * Implementations which number children according to their own logic may throw {@link UnsupportedOperationException}
	 * @param parent object to which child object needs to be added. Parent is allowed to be null
	 * only when there are no other nodes present in the tree. In that case, the child will be added as root
	 * @param child object 
	 * @param index index at which child needs to be added. It should be between 0 and k-1, 
	 * k being the maximum number of children allowed
	 * @return true if parent was found and child is not already in the tree, otherwise false
	 * @throws NodeNotFoundException
	 */
	public boolean add(E parent, E child, int index) throws NodeNotFoundException;
	/**
	 * @param parent
	 * @param index
	 * @return the child node of parent which is present at the index and null if there is no child present at 
	 * this index 
	 * @throws NodeNotFoundException
	 */
	public E child(E parent, int index) throws NodeNotFoundException;
}
