/*
 * $Id: Sort.java /main/5 1998/08/06 14:57:29 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/util/Sort.java $
 *
 * MOTOROLA CONFIDENTIAL PROPRIETARY
 *
 * Copyright 1998 Motorola Australia Pty. Ltd.
 * All Rights Reserved
 *
 * This is unpublished proprietary source code
 * of Motorola Australia Pty. Ltd.
 *
 * The copyright notice does not evidence any actual
 * or intended publication of such source code.
 */

package us.cownet.docfw.utils;

/**
 * The Sort class is the base class for sorting algorithms.
 * See the "Strategy" pattern on p315 of Design patterns.
 *
 * @author jfitzpat
 * @version $Revision: /main/5 $
 */
public abstract class Sort {
	/**
	 * Sort the specified sortable
	 */
	public abstract void sort(Sortable s);

	/**
	 * An Orderable is a collection of items that have an implicit ordering.
	 */
	public interface Orderable {
		/** Return the number of items in the Orderable */
		int getSize();

		/** Return ture if the items are in the desired order */
		boolean areOrdered(int ndx1, int ndx2);
	}

	/**
	 * A Sortable is a collection of items that can be reordered.
	 */
	public interface Sortable extends Orderable {
		/** Swap the items at the designated indices */
		void swap(int ndx1, int ndx2);
	}

	/**
	 * A selection sort
	 */
	public static class SelectionSort extends Sort {
		public static final SelectionSort global = new SelectionSort();

		public static void doSort(Sortable s) {
			global.sort(s);
		}

		// Convenience functions

		public static void doSort(int[] values) {
			SelectionSort.doSort(new IntDataArray(values));
		}

		public static void doSort(double[] values) {
			SelectionSort.doSort(new DoubleDataArray(values));
		}

		public static int[] doSort(Orderable s) {
			OutOfPlaceSortable p = new OutOfPlaceSortable(s);
			QuickSort.doSort(p);
			return p.getIndices();
		}

		public static int[] doSortOutOfPlace(Sortable s) {
			OutOfPlaceSortable p = new OutOfPlaceSortable(s);
			QuickSort.doSort(p);
			return p.getIndices();
		}

		public void sort(Sortable s) {
			for (int i = 0; i < s.getSize() - 1; i++) {
				int min = i;
				for (int j = i + 1; j < s.getSize(); j++) {
					if (!s.areOrdered(min, j)) {
						min = j;
					}
				}
				if (i != min) {
					s.swap(i, min);
				}
			}
		}
	}

	/**
	 * An implementation of Sort using the Quicksort algoritm.
	 * Examples:
	 * //sorting an array of integers
	 * int values[] = { 5, 2, 9, 5, 6 };
	 * Sort.QuickSort.doSort(values);
	 * <p>
	 * //sorting an array of objects
	 * SomeClass value[] = {...};
	 * Sortable s = new ObjectDataArray(values) {
	 * public boolean areOrdered(Object i, Object j) {
	 * return ((SomeClass)i).someField <= ((SomeClass)j).someField;
	 * }
	 * };
	 * Sort.QuickSort.doSort(s);
	 * <p>
	 * //sorting the same data in reverse order
	 * Sortable reverseS = new ObjectDataArray(values) {
	 * public boolean areOrdered(Object i, Object j) {
	 * return ((SomeClass)i).someField > ((SomeClass)j).someField;
	 * }
	 * };
	 * Sort.QuickSort.doSort(reverseS);
	 * <p>
	 * //sorting the same data out of place
	 * int[] indices = Sort.QuickSort.doSortOutOfPlace(s);
	 */
	public static class QuickSort extends Sort {
		public static final QuickSort global = new QuickSort();

		private static void QuickSort(Sortable s, int lo0, int hi0) {
			int lo = lo0;
			int hi = hi0;
			int mid;

			if (hi0 > lo0) {
				mid = (lo0 + hi0) / 2;
				while (lo <= hi) {
					while ((lo < hi0) && !s.areOrdered(lo, mid)) ++lo;
					while ((hi > lo0) && !s.areOrdered(mid, hi)) --hi;
					if (lo <= hi) {
						s.swap(lo, hi);
						++lo;
						--hi;
					}
					if (lo0 < hi) QuickSort(s, lo0, hi);
					if (lo < hi0) QuickSort(s, lo, hi0);
				}
			}
		}

		public static void doSort(Sortable s) {
			global.sort(s);
		}

		// Convenience functions

		public static void doSort(int[] values) {
			QuickSort.doSort(new IntDataArray(values));
		}

		public static void doSort(double[] values) {
			QuickSort.doSort(new DoubleDataArray(values));
		}

		public static int[] doSort(Orderable s) {
			OutOfPlaceSortable p = new OutOfPlaceSortable(s);
			QuickSort.doSort(p);
			return p.getIndices();
		}

		public static int[] doSortOutOfPlace(Sortable s) {
			OutOfPlaceSortable p = new OutOfPlaceSortable(s);
			QuickSort.doSort(p);
			return p.getIndices();
		}

		public void sort(Sortable s) {
			QuickSort(s, 0, s.getSize() - 1);
		}
	}


	//------------------------------------------------
	//  Behavioral Adaptors.  These affect the way
	//  the sort if performed
	//------------------------------------------------

	/**
	 * Adaptor for sorting out of place.  Only an Orderable is required.
	 * The result of the sort is returned as an array of indices.
	 */
	public static final class OutOfPlaceSortable implements Sortable {
		private final Orderable s;
		private final int[] indices;

		public OutOfPlaceSortable(Orderable s) {
			this.s = s;
			this.indices = new int[s.getSize()];
			resetIndices();
		}

		public int getSize() {
			return s.getSize();
		}

		public boolean areOrdered(int ndx1, int ndx2) {
			return s.areOrdered(indices[ndx1], indices[ndx2]);
		}

		public void swap(int ndx1, int ndx2) {
			int temp = indices[ndx1];
			indices[ndx1] = indices[ndx2];
			indices[ndx2] = temp;
		}

		public int[] getIndices() {
			return indices;
		}

		public void resetIndices() {
			for (int i = 0; i < this.indices.length; i++) {
				this.indices[i] = i;
			}
		}
	}

	/**
	 * Adaptor for sorting in reverse order.
	 */
	public static class ReverseSortableAdapter implements Sortable {
		private Sortable s;

		public ReverseSortableAdapter(Sortable s) {
			this.s = s;
		}

		public int getSize() {
			return s.getSize();
		}

		public boolean areOrdered(int ndx1, int ndx2) {
			return s.areOrdered(ndx2, ndx1);
		}

		public void swap(int ndx1, int ndx2) {
			s.swap(ndx1, ndx2);
		}
	}

	/**
	 * An adaptor for sorting a portion of a sortable collection
	 */
	public static class RangeAdaptor implements Sortable {
		private int start;
		private int limit;
		private Sortable sortable;

		public RangeAdaptor(int start, int limit, Sortable sortable) {
			if (limit <= start) {
				throw new IllegalArgumentException();
			}
			if (limit - start > sortable.getSize()) {
				throw new IllegalArgumentException();
			}
			this.start = start;
			this.limit = limit;
			this.sortable = sortable;
		}

		public int getSize() {
			return limit - start;
		}

		public boolean areOrdered(int i, int j) {
			return sortable.areOrdered(i + start, j + start);
		}

		public void swap(int i, int j) {
			sortable.swap(i + start, j + start);
		}
	}

	//------------------------------------------------
	//  Data type adaptors.  These are used to
	//  sort common data structures such as Vectors
	//  or arrays of numbers.
	//------------------------------------------------

	//-- utilities for sorting int
	public static class IntDataArray implements Sortable {
		private int[] data;

		public IntDataArray(int[] data) {
			this.data = data;
		}

		public int getSize() {
			return data.length;
		}

		public boolean areOrdered(int i, int j) {
			return data[i] <= data[j];
		}

		public void swap(int i, int j) {
			int temp = data[i];
			data[i] = data[j];
			data[j] = temp;
		}
	}

	//-- utilities for sorting double
	public static class DoubleDataArray implements Sortable {
		private double[] data;

		public DoubleDataArray(double[] data) {
			this.data = data;
		}

		public int getSize() {
			return data.length;
		}

		public boolean areOrdered(int i, int j) {
			return data[i] <= data[j];
		}

		public void swap(int i, int j) {
			double temp = data[i];
			data[i] = data[j];
			data[j] = temp;
		}
	}

	//-- utilities for sorting an array of objects
	public static abstract class ObjectDataArray implements Sortable {
		private Object[] data;

		public ObjectDataArray(Object[] data) {
			this.data = data;
		}

		public boolean areOrdered(int i, int j) {
			return areOrdered(data[i], data[j]);
		}

		public abstract boolean areOrdered(Object i, Object j);

		public int getSize() {
			return data.length;
		}

		public void swap(int i, int j) {
			Object temp = data[i];
			data[i] = data[j];
			data[j] = temp;
		}
	}

	//-- utilities for sorting a Vector
	public static abstract class VectorData implements Sortable {
		private java.util.Vector data;

		public VectorData(java.util.Vector data) {
			this.data = data;
		}

		public int getSize() {
			return data.size();
		}

		public boolean areOrdered(int i, int j) {
			return areOrdered(data.elementAt(i), data.elementAt(j));
		}

		public abstract boolean areOrdered(Object i, Object j);

		public void swap(int i, int j) {
			Object temp = data.elementAt(i);
			data.setElementAt(data.elementAt(j), i);
			data.setElementAt(temp, j);
		}
	}
}
