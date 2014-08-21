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