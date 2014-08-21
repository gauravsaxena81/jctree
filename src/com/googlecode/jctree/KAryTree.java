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
import java.util.List;


/**
 * This tree allows addition of at most k-children to any parent
 * @author Gaurav Saxena
 * @param <E>
 */
public class KAryTree<E> extends ArrayListTree<E> {
	private int k;
	public KAryTree(int k) {
		if(k <= 0)
			throw new IllegalArgumentException("Number of children cannot be less than 1");
		else
			this.k = k;
	}
	@Override
	public boolean add(E child) {
		int numberOfChildrenAllowed = 0;
		if(super.isEmpty())
			numberOfChildrenAllowed = k + 1;
		else {
			try {
				numberOfChildrenAllowed = k - super.children(super.root()).size();
			} catch (NodeNotFoundException e) {
				//not possible
			}
		}
		if(numberOfChildrenAllowed <= 0)
			throw new IllegalArgumentException("Size of collection is more than tree can hold");
		else
			return super.add(child);
	}
	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		if(parent == null) //if parent is null
			return super.add(parent, child);//add root
		else if (children(parent).size() < k)
			return super.add(parent, child);
		else
			throw new IndexOutOfBoundsException("Cannot add more than " + k +" children to a parent");
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		int numberOfChildrenAllowed = 0;
		if(super.isEmpty())
			numberOfChildrenAllowed = k + 1;
		else {
			try {
				numberOfChildrenAllowed = k - super.children(super.root()).size();
			} catch (NodeNotFoundException e) {
				//not possible
			}
		}
		if(c.size() > numberOfChildrenAllowed)
			throw new IllegalArgumentException("Size of collection is more than tree can hold");
		else
			return super.addAll(c);
	}
	public boolean addAll(E parent, Collection<? extends E> c) {
		int numberOfChildrenAllowed = k;
		if(parent == null && super.isEmpty())
			numberOfChildrenAllowed++;
		if(c.size() > numberOfChildrenAllowed)
			throw new IllegalArgumentException("Size of collection is more than tree can hold");
		else
			return super.addAll(parent, c);
	}
	/**
	 * @param parent the parent node
	 * @param index index of child requested
	 * @return child present at index among the children of e, if the index is less than number of children of e,
	 * null otherwise
	 * @throws NodeNotFoundException
	 */
	public E child(E parent, int index) throws NodeNotFoundException
	{
		List<E> children;
		if(index > k)
			throw new IndexOutOfBoundsException(index + " cannot be more than " + k);
		else if(index > (children = children(parent)).size())
			return null;
		else
			return children.get(index);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		KAryTree<E> v = (KAryTree<E>) super.clone();
		v.k = k;
		return v;
	}
}