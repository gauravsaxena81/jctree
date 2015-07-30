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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.googlecode.jctree.BinaryTree;
import com.googlecode.jctree.NodeNotFoundException;

public class BinaryTreeTest {
	private BinaryTree<String> binaryTree;
	@BeforeTest
	public void setUp() throws NodeNotFoundException {
		binaryTree = new BinaryTree<String>();
		Assert.assertTrue(binaryTree.add(null, "0"));
		Assert.assertTrue(binaryTree.add("0", "00"));
		Assert.assertTrue(binaryTree.add("0", "01"));
		Assert.assertTrue(binaryTree.add("00", "000"));
		Assert.assertTrue(binaryTree.add("00", "001"));
		Assert.assertTrue(binaryTree.add("01", "010"));
		Assert.assertTrue(binaryTree.add("01", "011"));
	}
	@Test
	public void left() throws NodeNotFoundException {
		Assert.assertEquals("00", binaryTree.left("0"));
		Assert.assertEquals("000", binaryTree.left("00"));
		Assert.assertEquals("010", binaryTree.left("01"));
	}
	@Test
	public void right() throws NodeNotFoundException {
		Assert.assertEquals("01", binaryTree.right("0"));
		Assert.assertEquals("001", binaryTree.right("00"));
		Assert.assertEquals("011", binaryTree.right("01"));
	}
}