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

import java.util.Collections;
import java.util.List;

public class SortedChildrenTree<E extends Comparable<E>> extends ArrayListTree<E> {
	/**
	 * This adds the child at a insertion position defined by {@link Collections#binarySearch(List, Object)}
	 * @see com.googlecode.jctree.ArrayListTree#getChildAddPosition(java.util.List, java.lang.Object)
	 */
	@Override
	protected int getChildAddPosition(List<E> children, E newChild) {
		return -Collections.binarySearch(children, newChild) - 1;
	}
}
