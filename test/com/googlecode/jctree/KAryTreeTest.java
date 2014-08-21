package com.googlecode.jctree;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class KAryTreeTest {

  @Test
  public void add() throws NodeNotFoundException {
      KAryTree<Integer> kAryTree = new KAryTree<>(3);
      for(int i = 0; i < 4; i++)
    	  kAryTree.add(i);
      Assert.assertEquals(0, kAryTree.root().intValue());
      List<Integer> children = kAryTree.children(kAryTree.root());
      children.removeAll(Arrays.asList(1, 2, 3));
      Assert.assertEquals(0, children.size());
  }
  @Test(expectedExceptions=IllegalArgumentException.class)
  public void addMore() throws NodeNotFoundException {
      KAryTree<Integer> kAryTree = new KAryTree<>(3);
      for(int i = 0; i < 5; i++)
    	  kAryTree.add(i);
  }
  @Test
  public void addAll() throws NodeNotFoundException {
      KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.addAll(Arrays.asList(1, 2, 3, 4));
      Assert.assertEquals(1, kAryTree.root().intValue());
      List<Integer> children = kAryTree.children(kAryTree.root());
      children.removeAll(Arrays.asList(2, 3, 4));
      Assert.assertEquals(0, children.size());
  }
  @Test(expectedExceptions=IllegalArgumentException.class)
  public void addAllMore() {
	  KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.addAll(Arrays.asList(1, 2, 3, 4, 5));
  }
  @Test
  public void addParent() throws NodeNotFoundException {
	  KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.add(1);
	  kAryTree.add(1, 2);
	  kAryTree.add(1, 3);
	  kAryTree.add(2, 4);
	  kAryTree.add(2, 5);
	  kAryTree.add(2, 6);
	  List<Integer> children = kAryTree.children(2);
	  children.removeAll(Arrays.asList(4, 5, 6));
	  Assert.assertEquals(0, children.size());
  }
  @Test
  public void addAllParent() throws NodeNotFoundException {
	  KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.add(1);
	  kAryTree.add(1, 2);
	  kAryTree.add(1, 3);
	  kAryTree.addAll(2, Arrays.asList(4, 5, 6));
	  List<Integer> children = kAryTree.children(2);
	  children.removeAll(Arrays.asList(4, 5, 6));
	  Assert.assertEquals(0, children.size());
  }
  @Test(expectedExceptions=IllegalArgumentException.class)
  public void addAllParentMore() throws NodeNotFoundException {
	  KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.add(1);
	  kAryTree.add(1, 2);
	  kAryTree.add(1, 3);
	  kAryTree.addAll(2, Arrays.asList(4, 5, 6, 7));
  }
  @Test
  public void child() throws NodeNotFoundException {
	  KAryTree<Integer> kAryTree = new KAryTree<>(3);
	  kAryTree.add(1);
	  kAryTree.add(1, 2);
	  kAryTree.add(1, 3);
	  kAryTree.add(2, 4);
	  kAryTree.add(2, 5);
	  kAryTree.add(2, 6);
	  Assert.assertEquals(2, kAryTree.child(1, 0).intValue());
	  Assert.assertEquals(3, kAryTree.child(1, 1).intValue());
	  Assert.assertEquals(6, kAryTree.child(2, 2).intValue());
  }
}
