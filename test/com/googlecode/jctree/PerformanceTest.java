package com.googlecode.jctree;

import java.util.List;

import com.googlecode.jctree.ArrayListTree;
import com.googlecode.jctree.ArrayTree;
import com.googlecode.jctree.LinkedTree;
import com.googlecode.jctree.MapIndexedArrayListTree;
import com.googlecode.jctree.NodeNotFoundException;


//@Test
public class PerformanceTest {
	public static void main(String[] args) throws NodeNotFoundException {
		ArrayListTreeTest();
		MapIndexedArrayListTreeTest();
	}
	public static void ArrayListTreeTest() throws NodeNotFoundException {
		long freeMemory = Runtime.getRuntime().freeMemory();
		ArrayListTree<Integer> tree = new ArrayListTree<Integer>();
		tree.add(0);
		for(int i = 1; i < 5000; i++)
			tree.add((int) (Math.random() * i), i);
		System.out.println("ArrayListTree Memory - " + (freeMemory - Runtime.getRuntime().freeMemory()));
		long currentTimeMillis = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++)
			tree.children((int) (Math.random() * 5000));
		System.out.println("ArrayListTreeTest - " + (System.currentTimeMillis() - currentTimeMillis));
	}
	public static void MapIndexedArrayListTreeTest() throws NodeNotFoundException {
		long freeMemory = Runtime.getRuntime().freeMemory();
		MapIndexedArrayListTree<Integer> tree = new MapIndexedArrayListTree<Integer>();
		tree.add(0);
		for(int i = 1; i < 24000; i++)
			tree.add((int) (Math.random() * i), i);
		System.out.println("MapIndexedArrayListTreeTest Memory - " + (freeMemory - Runtime.getRuntime().freeMemory()));
		long currentTimeMillis = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++) {
			List<Integer> children = tree.children((int) (Math.random() * 24000));
			for(Integer j : children) {
				boolean x = j == 2;
			}
		}
		System.out.println("MapIndexedArrayListTreeTest - " + (System.currentTimeMillis() - currentTimeMillis));
	}
	public void ArrayTreeTest() throws NodeNotFoundException {
		ArrayTree<Integer> tree = new ArrayTree<Integer>(100);
		tree.add(0);
		for(int i = 1; i < 5000; i++)
			tree.add((int) (Math.random() * i), i);
		long currentTimeMillis = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++)
			tree.children((int) (Math.random() * 5000));
		System.out.println("ArrayTreeTest - " + (System.currentTimeMillis() - currentTimeMillis));
	}
	//10 times slower
	public void LinkedTreeTest() throws NodeNotFoundException {
		LinkedTree<Integer> tree = new LinkedTree<Integer>();
		tree.add(0);
		for(int i = 1; i < 5000; i++)
			tree.add((int) (Math.random() * i), i);
		long currentTimeMillis = System.currentTimeMillis();
		for(int i = 0; i < 1000; i++)
			tree.children((int) (Math.random() * 5000));
		System.out.println("LinkedTreeTest - " + (System.currentTimeMillis() - currentTimeMillis));
	}
}
