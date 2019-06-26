/*
 * $Id: IdentityVector.java /main/16 1998/08/06 14:37:53 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/IdentityVector.java $
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
package us.cownet.docfw.guitools;

import java.util.Enumeration;
import java.util.Vector;

/**
 * The IdentityVector class is a replacement for the Vector class that
 * uses object identity rather than object equality when doing
 * lookups.
 *
 * @author jfitzpat
 * @version $Version$
 */
public final class IdentityVector {
	public static final boolean DUPLICATES = true;
	public static final boolean NO_DUPLICATES = false;
	//The collection
	private Vector docs = new Vector();
	private Comparitor comp = new IdentityComparitor();
	private boolean allowDuplicates = true;

	public IdentityVector() {
	}

	public IdentityVector(Comparitor comp) {
		this.comp = comp;
	}

	public IdentityVector(Comparitor comp, boolean allowDuplicates) {
		this.comp = comp;
		this.allowDuplicates = allowDuplicates;
	}

	public static IdentityVector newIdentitySet() {
		return new IdentityVector(new IdentityComparitor(), NO_DUPLICATES);
	}

	public static IdentityVector newValueSet() {
		return new IdentityVector(new ValueComparitor(), NO_DUPLICATES);
	}

	public static IdentityVector newIdentityBag() {
		return new IdentityVector(new IdentityComparitor(), DUPLICATES);
	}

	public static IdentityVector newValueBag() {
		return new IdentityVector(new ValueComparitor(), DUPLICATES);
	}

	public boolean allowsDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allow) {
		allowDuplicates = allow;
	}

	/**
	 * Add an element to the collection.
	 */
	public synchronized void addElement(Object x) {
		if (x == null) throw new IllegalArgumentException();
		if (allowDuplicates || !contains(x)) {
			docs.addElement(x);
		}
	}

	/**
	 * Add an element to the collection.
	 */
	public void addElements(Enumeration x) {
		while (x.hasMoreElements()) {
			addElement(x.nextElement());
		}
	}

	/**
	 * Add an element to the collection.
	 */
	public void addElements(Object[] x) {
		for (int i = 0; i < x.length; i++) {
			addElement(x[i]);
		}
	}

	/**
	 * Remove an element in the collection.
	 */
	public synchronized Object removeElement(Object x) {
		comp.setValue(x);
		int ndx = docs.indexOf(comp);
		if (ndx < 0) {
			comp.setValue(null);    //allow garbage collection of x
			return null;
		} else {
			Object result = docs.elementAt(ndx);
			docs.removeElement(comp);
			comp.setValue(null);    //allow garbage collection of x
			return result;
		}
	}

	/**
	 * Remove an element in the collection.
	 */
	public void removeElements(Enumeration x) {
		while (x.hasMoreElements()) {
			removeElement(x.nextElement());
		}
	}

	public void removeAllElements() {
		docs.removeAllElements();
	}

	/**
	 * Return true if an object is in this collection.
	 */
	public synchronized boolean contains(Object x) {
		comp.setValue(x);
		boolean result = docs.contains(comp);
		comp.setValue(null);    //allow garbage collection of x
		return result;
	}

	/**
	 * Return true if an object is in this collection.
	 */
	public synchronized int indexOf(Object x) {
		comp.setValue(x);
		int result = docs.indexOf(comp);
		comp.setValue(null);    //allow garbage collection of x
		return result;
	}

	/**
	 * Return true if an object is in this collection.
	 */
	public synchronized Object find(Object x) {
		comp.setValue(x);
		int ndx = docs.indexOf(comp);
		comp.setValue(null);    //allow garbage collection of x
		if (ndx < 0) {
			return null;
		} else {
			return docs.elementAt(ndx);
		}
	}

	/**
	 * Return true if an object is in this collection.
	 */
	public synchronized boolean containsAll(Enumeration x) {
		boolean result = true;
		while (result && x.hasMoreElements()) {
			result = contains(x.nextElement());
		}
		return result;
	}

	/**
	 * Return true if an object is in this collection.
	 */
	public synchronized boolean containsSome(Enumeration x) {
		boolean result = false;
		while (!result && x.hasMoreElements()) {
			result = contains(x.nextElement());
		}
		return result;
	}

	/**
	 * Return an enumeration of the contents of this collection.
	 */
	public Enumeration elements() {
		return docs.elements();
	}

	/**
	 * Return the number of elements in this collection.
	 */
	public int size() {
		return docs.size();
	}

	/**
	 * Returns the component at the specified index.
	 *
	 * @param index an index into this vector.
	 * @return the component at the specified index.
	 * @throws ArrayIndexOutOfBoundsException if an invalid index was
	 * given.
	 * @since JDK1.0
	 */
	public Object elementAt(int index) {
		return docs.elementAt(index);
	}

	public void swap(int i, int j) {
		Object temp = docs.elementAt(i);
		docs.setElementAt(docs.elementAt(j), i);
		docs.setElementAt(temp, j);
	}

	public Object[] toArray() {
		Object[] result = new Object[size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = elementAt(i);
		}
		return result;
	}

	public Object[] toArray(Object[] result) {
		if (result.length != size()) {
			result = new Object[size()];
		}
		for (int i = 0; i < result.length; i++) {
			result[i] = elementAt(i);
		}
		return result;
	}

	//The Comparitor class compares two object references
	//to see if they are equal.
	public static abstract class Comparitor {
		private Object x;

		public void setValue(Object x) {
			this.x = x;
		}

		public boolean equals(Object other) {
			return compare(other, x);
		}

		public abstract boolean compare(Object x, Object y);
	}

	public static class ValueComparitor extends Comparitor {
		public boolean compare(Object x, Object y) {
			return x.equals(y);
		}
	}

	public static class IdentityComparitor extends Comparitor {
		public boolean compare(Object x, Object y) {
			return x == y;
		}
	}
}
