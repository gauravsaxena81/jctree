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

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.googlecode.jctree.LinkedTree;
import com.googlecode.jctree.NodeNotFoundException;

public class LinkedTreeTest {
	@DataProvider
	public Object[][] getTree() {
		try {
			LinkedTree<String> linkedTree = new LinkedTree<String>();
			linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");
			return new Object[][]{{0, new LinkedTree<String>()},{1, linkedTree}};
		} catch(NodeNotFoundException e) {
			throw new RuntimeException();
		}
	  }

  @Test(dataProvider = "getTree")
  public void addE(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	int initialSize = tree.size();
    Assert.assertEquals(true, tree.add("New"));
    Assert.assertEquals(initialSize, tree.size() - 1);
    Assert.assertEquals(true, tree.contains("New"));
    if(initialSize == 0)
    	Assert.assertEquals("New", tree.root());
    else
    	Assert.assertEquals(true, tree.children(tree.root()).contains("New"));
  }

  @Test(dataProvider = "getTree")
  public void addEE(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  int initialSize = tree.size();
	  if(initialSize == 0) {
		  Assert.assertEquals(true, tree.add(null, "New"));
		  Assert.assertEquals("New", tree.root());
		try {
			Assert.assertEquals(true, tree.add(null, "New"));
		} catch(IllegalArgumentException e) {
			//passed
		}
	  } else {
		  Assert.assertEquals(true, tree.add(tree.root(), "New"));
		  Assert.assertEquals(true, tree.contains("New"));
		  Assert.assertEquals(initialSize + 1, tree.size());
		  Assert.assertEquals(true, tree.add("New","Child"));
		  Assert.assertEquals(false, tree.add("Child"));
		  Assert.assertEquals(true, tree.contains("Child"));
		  Assert.assertEquals(initialSize + 2, tree.size());
	  }
  }

  @Test(dataProvider = "getTree")
  public void addAllCollectionextendsE(int testCaseNumber, LinkedTree<String> tree) {
	int initialSize = tree.size();
	for (String i : Arrays.asList(new String[]{"1","2","3"}))
		tree.add(i);
	Assert.assertEquals(initialSize + 3, tree.size());
	Assert.assertEquals(true, tree.contains("1"));
	Assert.assertEquals(true, tree.contains("2"));
	Assert.assertEquals(true, tree.contains("3"));
  }

  @Test(dataProvider = "getTree")
  public void addAllECollectionextendsE(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  int initialSize = tree.size();
	  if(initialSize > 0) {
		String p = tree.leaves().get(0);
	      for (String i : Arrays.asList(new String[]{"1","2","3"}))
		    tree.add(p, i);
	      Assert.assertEquals(initialSize + 3, tree.size());
		  Assert.assertEquals(true, tree.contains("1"));
		  Assert.assertEquals(true, tree.contains("2"));
		  Assert.assertEquals(true, tree.contains("3"));
	  }
  }

  @Test(dataProvider = "getTree")
  public void children(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  try {
		  tree.children(null);
		  Assert.assertEquals(false, true);
	  } catch (IllegalArgumentException e) {
		  //passed
	  }
	  try {
		  tree.children("Not present");
		  Assert.assertEquals(false, true);
	  } catch (NodeNotFoundException e) {
		  //passed
	  }
	  if(testCaseNumber == 1)
		for(String i : tree.children(tree.root()))
		  Assert.assertEquals(tree.root(), tree.parent(i));
  }

  @Test(dataProvider = "getTree")
  public void clear(int testCaseNumber, LinkedTree<String> tree) {
	tree.clear();
    Assert.assertEquals(0, tree.size());
  }

  @Test(dataProvider = "getTree")
  public void commonAncestor(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	for(String i: tree)
		for(String j: tree)
			Assert.assertNotEquals(null, tree.commonAncestor(i, j));
	if(testCaseNumber == 1) {
	  Assert.assertEquals(tree.root(), tree.commonAncestor(tree.root(), tree.leaves().get(0)));
	  List<String> leaves = tree.leaves();
	  Assert.assertEquals(tree.root(), tree.commonAncestor(tree.children(tree.root()).get(0), leaves.get(leaves.size() - 1)));
	}
  }

  @Test(dataProvider = "getTree")
  public void contains(int testCaseNumber, LinkedTree<String> tree) {
	  Assert.assertEquals(false, tree.contains(null));
	  Assert.assertEquals(false, tree.contains("Not present"));
	  for(String i: tree)
		  Assert.assertEquals(true, tree.contains(i));
  }

  @Test(dataProvider = "getTree")
  public void containsAll(int testCaseNumber, LinkedTree<String> tree) {
	  Assert.assertEquals(false, tree.containsAll(Arrays.asList(new String[]{null})));
	  Assert.assertEquals(false, tree.containsAll(Arrays.asList(new String[]{"Not Present"})));
	  Assert.assertEquals(true, tree.containsAll(tree.inOrderTraversal())); 
  }

  @Test(dataProvider = "getTree")
  public void depth(int testCaseNumber, LinkedTree<String> tree) {
	  if(testCaseNumber == 0)
		  Assert.assertEquals(0, tree.depth());
	  else if(testCaseNumber == 1) {
		  Assert.assertEquals(4, tree.depth());
		  try {
			  tree.removeAll(tree.children("C1"));
			  Assert.assertEquals(4, tree.depth());
			  tree.removeAll(tree.children("C2"));
			  Assert.assertEquals(2, tree.depth());
		  } catch (NodeNotFoundException e) {
			  e.printStackTrace();
			  Assert.assertEquals(false, true);
		  }
		  tree.clear();
		  Assert.assertEquals(0, tree.depth());
	  }
  }

  
  @Test(dataProvider = "getTree")
  public void inOrderTraversal(int testCaseNumber, LinkedTree<String> tree) {
	  /*linkedTree.add("Root1");
		linkedTree.add("Root1", "C1");
		linkedTree.add("Root1", "C2");
		linkedTree.add("C1", "C1-1");
		linkedTree.add("C1", "C1-2");
		linkedTree.add("C1", "C1-3");
		linkedTree.add("C2", "C2-1");
		linkedTree.add("C2", "C2-2");
		linkedTree.add("C1-1", "C1-1-1");
		linkedTree.add("C1-1", "C1-1-2");
		linkedTree.add("C1-2", "C1-2-1");
		linkedTree.add("C2-1", "C2-1-1");
		linkedTree.add("C2-1", "C2-1-2");*/
	  switch(testCaseNumber) {
	  case 0:
		  Assert.assertEquals(true, tree.inOrderTraversal().isEmpty());
	  	  break;
	  case 1:
		  Assert.assertEquals(true, Arrays.equals(tree.inOrderTraversal().toArray(new String[0])
			, new String[]{"C1-1-1","C1-1","C1-1-2","C1-2-1","C1-2","C1","C1-3","Root1","C2-1-1","C2-1","C2-1-2","C2","C2-2"}));
		  tree.remove("Root1");
		  Assert.assertEquals(true, tree.inOrderTraversal().isEmpty());
		  break;
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void isAncestor(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  if(testCaseNumber == 1) {
		  Assert.assertEquals(true, tree.isAncestor("Root1", "C2-1-1"));
		  Assert.assertEquals(false, tree.isAncestor("C1", "C2-1-1"));
		  try {
			  Assert.assertEquals(false, tree.isAncestor(null, "C2-1-1"));
		  } catch (IllegalArgumentException e) {
			  //passed
		  }
		  try {
			  Assert.assertEquals(false, tree.isAncestor("C2-1-1", null));
		  } catch (IllegalArgumentException e) {
			  //passed
		  }
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void isDescendant(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  if(testCaseNumber == 1) {
		  Assert.assertEquals(true, tree.isDescendant("Root1", "C2-1-1"));
		  Assert.assertEquals(false, tree.isDescendant("C1", "C2-1-1"));
	  }
  }

  @Test(dataProvider = "getTree")
  public void isEmpty(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.isEmpty());
	  		break;
	  	case 1:
	  		Assert.assertEquals(false, tree.isEmpty());
	  		tree.remove("C1");
	  		tree.remove("C2");
	  		Assert.assertEquals(false, tree.isEmpty());
	  		tree.remove("Root1");
	  		Assert.assertEquals(true, tree.isEmpty());
	  		break;
	  }
		  
  }

  @Test(dataProvider = "getTree")
  public void leaves(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.leaves().isEmpty());
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.leaves().toArray(new String[0]), new String[]{"C1-1-1","C1-1-2","C1-2-1","C1-3","C2-1-1","C2-1-2","C2-2"});
	  		tree.remove("C1-2-1");
			Assert.assertEquals(true, Arrays.equals(tree.leaves().toArray(new String[0]), new String[]{"C1-1-1","C1-1-2","C1-2","C1-3","C2-1-1","C2-1-2","C2-2"}));
			tree.remove("Root1");
			Assert.assertEquals(true, tree.leaves().isEmpty());
	  		break;
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void levelOrderTraversal(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.levelOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.levelOrderTraversal().toArray(new String[0])
	  			, new String[]{"Root1","C1","C2","C1-1","C1-2","C1-3","C2-1","C2-2","C1-1-1","C1-1-2","C1-2-1","C2-1-1","C2-1-2"});
	  		break;
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void parent(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		 try {
	  			  tree.parent(null);
	  			  Assert.assertEquals(false, true);
	  		  } catch (IllegalArgumentException e) {
	  			  //passed
	  		  }
	  		  try {
	  			  tree.parent("Not present");
	  			  Assert.assertEquals(false, true);
	  		  } catch (NodeNotFoundException e) {
	  			  //passed
	  		  }
	  		  break;
	  	case 1:
	  		Assert.assertEquals(tree.parent("C1-1-1"), "C1-1");
	  		Assert.assertEquals(tree.parent("C2-1"), "C2");
	  		Assert.assertNull(tree.parent("Root1"));
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void postOrderTraversal(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.postOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.postOrderTraversal().toArray(new String[0])
	  			, new String[]{"C1-1-1","C1-1-2","C1-1","C1-2-1","C1-2","C1-3","C1","C2-1-1","C2-1-2","C2-1","C2-2","C2","Root1"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void preOrderTraversal(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.preOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.preOrderTraversal().toArray(new String[0])
	  			, new String[]{"Root1","C1","C1-1","C1-1-1","C1-1-2","C1-2","C1-2-1","C1-3","C2","C2-1","C2-1-1","C2-1-2","C2-2"});
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void remove(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		try {
	  		  tree.remove(null);
	  		  Assert.assertEquals(false, true);
	  	    } catch (IllegalArgumentException e) {
	  		  //passed
	  	    }
	  		Assert.assertEquals(false, tree.remove("Not present"));
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(false, tree.remove("Not present"));
	  		Assert.assertEquals(true, tree.remove("C1-1-1"));
	  		Assert.assertEquals(tree.preOrderTraversal().toArray(new String[0])
		  			, new String[]{"Root1","C1","C1-1","C1-1-2","C1-2","C1-2-1","C1-3","C2","C2-1","C2-1-1","C2-1-2","C2-2"});
	  		Assert.assertEquals(true, tree.remove("C1-1"));
	  		Assert.assertEquals(tree.preOrderTraversal().toArray(new String[0])
	  			, new String[]{"Root1","C1","C1-2","C1-2-1","C1-3","C2","C2-1","C2-1-1","C2-1-2","C2-2"});
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void removeAll(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.preOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.preOrderTraversal().toArray(new String[0])
	  			, new String[]{"Root1","C1","C1-1","C1-1-1","C1-1-2","C1-2","C1-2-1","C1-3","C2","C2-1","C2-1-1","C2-1-2","C2-2"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void retainAll(int testCaseNumber, LinkedTree<String> tree) {
   try {
	   tree.retainAll(Arrays.asList(new String[]{""}));
   } catch (UnsupportedOperationException e) {
	   //passed
   }
  }

  @Test(dataProvider = "getTree")
  public void root(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(null, tree.root());
	  		break;
	  	case 1:
	  		Assert.assertEquals("Root1", tree.root());
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void siblings(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		try {
	  		  tree.siblings(null);
	  		  Assert.assertEquals(false, true);
	  	    } catch (IllegalArgumentException e) {
	  		  //passed
	  	    }
	  	    try {
	  		  tree.parent("Not present");
	  		  Assert.assertEquals(false, true);
	  	    } catch (NodeNotFoundException e) {
	  		  //passed
	  	    }
	  		break;
	  	case 1:
	  		/*linkedTree.add("Root1");
			linkedTree.add("Root1", "C1");
			linkedTree.add("Root1", "C2");
			linkedTree.add("C1", "C1-1");
			linkedTree.add("C1", "C1-2");
			linkedTree.add("C1", "C1-3");
			linkedTree.add("C2", "C2-1");
			linkedTree.add("C2", "C2-2");
			linkedTree.add("C1-1", "C1-1-1");
			linkedTree.add("C1-1", "C1-1-2");
			linkedTree.add("C1-2", "C1-2-1");
			linkedTree.add("C2-1", "C2-1-1");
			linkedTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.siblings("Root1").toArray(new String[0]), new String[]{});
	  		Assert.assertEquals(tree.siblings("C1").toArray(new String[0]), new String[]{"C2"});
	  		Assert.assertEquals(tree.siblings("C1-1").toArray(new String[0]), new String[]{"C1-2","C1-3"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void size(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(0, tree.size());
	  		break;
	  	case 1:
	  		Assert.assertEquals(13, tree.size());
	  		tree.remove("C1-1-1");
	  		Assert.assertEquals(12, tree.size());
	  		tree.remove("C1-1");
	  		Assert.assertEquals(10, tree.size());
	  		tree.remove("C1");
	  		Assert.assertEquals(6, tree.size());
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void toArray(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(new String[]{}, tree.toArray());
	  		break;
	  	case 1:
	  		Assert.assertEquals(tree.toArray(), new String[]{"C1-1-1","C1-1","C1-1-2","C1-2-1","C1-2","C1","C1-3","Root1","C2-1-1","C2-1","C2-1-2","C2","C2-2"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void toArrayT(int testCaseNumber, LinkedTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(new String[]{}, tree.toArray(new String[0]));
	  		break;
	  	case 1:
	  		Assert.assertEquals(tree.toArray(new String[0])
	  				, new String[]{"C1-1-1","C1-1","C1-1-2","C1-2-1","C1-2","C1","C1-3","Root1","C2-1-1","C2-1","C2-1-2","C2","C2-2"});
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void equals(int testCaseNumber, LinkedTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		LinkedTree<String> linkedTree = new LinkedTree<String>();
	  		Assert.assertEquals(true, tree.equals(linkedTree));
	  		linkedTree.add("Root2");
	  		Assert.assertEquals(false, tree.equals(linkedTree));
	  		break;
	  	case 1:
	  		@SuppressWarnings("unchecked")
			LinkedTree<String> clone = (LinkedTree<String>) tree.clone();
	  		@SuppressWarnings("unchecked")
			LinkedTree<String> clone2 = (LinkedTree<String>) tree.clone();
	  		Assert.assertEquals(true, tree.equals(clone));
	  		clone.remove("C1-1-1");
	  		Assert.assertEquals(false, tree.equals(clone));
	  		clone2.add("C2-2", "C2-2-1");
	  		Assert.assertEquals(false, tree.equals(clone2));
	  		break;
	  }
  }
}
