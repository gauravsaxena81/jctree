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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.googlecode.jctree.BinaryTree;
import com.googlecode.jctree.NodeNotFoundException;

@Test
public class BinaryTreeTest {
	private BinaryTree<String> binaryTree = new BinaryTree<String>();
	public BinaryTreeTest() throws NodeNotFoundException {
		Assert.assertEquals(true, binaryTree.add(null, "0"));
		Assert.assertEquals(true, binaryTree.add("0", "00"));
		Assert.assertEquals(true, binaryTree.add("0", "01"));
	}
	public void left() throws NodeNotFoundException {
		Assert.assertEquals("00", binaryTree.left("0"));
	}
	public void right() throws NodeNotFoundException {
		Assert.assertEquals("01", binaryTree.left("0"));
	}
}