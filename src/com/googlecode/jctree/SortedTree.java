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

/**
 * Trees of this type keep nodes sorted e.g. BST etc.
 * @author Gaurav Saxena
 * @param <E>
 */
public interface SortedTree<E extends Comparable<E>> extends Tree<E> {

	E successor(E node) throws NodeNotFoundException;

	E predecessor(E node) throws NodeNotFoundException;

}
