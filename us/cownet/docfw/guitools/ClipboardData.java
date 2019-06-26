/*
 * $Id$
 * $Source$
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

import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * The ClipboardData class is an internal class used to put data on the
 * java system clipboard.  It keeps an internal copy of all data supplied
 * from a DocumentSelection.  This type of behavior was exactly what the
 * java Transferable interface was meant to avoid, but assuming the data
 * being put on the clipboard is small, the overhead should not be high.
 *
 * @author jfitzpat
 * @version $Revision$
 */
final class ClipboardData implements Transferable, ClipboardOwner {
	/* The flavors of the data in the 'data' array */
	private DataFlavor[] flavors;
	/* The actual clipboard data in various flavors */
	private Object[] data;

	/**
	 * Create a new Clipboard data from the document selection.
	 * The is copied from the selection in all available
	 * flavors.  The selection is then no longer needed.
	 *
	 * @param selection The selection being copied to the clipboard
	 */
	public ClipboardData(DocumentSelection selection)
			throws UnsupportedFlavorException {
		flavors = selection.getDataFlavors();
		data = new Object[flavors.length];
		for (int i = 0; i < flavors.length; i++) {
			data[i] = selection.copyData(flavors[i]);
		}
	}

	/**
	 * Returns an array of DataFlavor objects indicating the flavors the
	 * data can be provided in.  The array should be ordered according to
	 * preference for providing the data (from most richly descriptive to
	 * east descriptive).
	 *
	 * @return an array of data flavors in which this data can be
	 * transferred
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return flavors.clone();
	}

	/**
	 * Returns whether or not the specified data flavor is supported for
	 * this object.
	 *
	 * @param flavor the requested flavor for the data
	 * @return boolean indicating wjether or not the data flavor is
	 * supported
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (indexOf(flavor) >= 0);
	}

	/**
	 * Returns an object which represents the data to be transferred.
	 * The class  of the object returned is defined by the representation
	 * class of the flavor.
	 *
	 * @param flavor the requested flavor for the data.
	 * @throws IOException if the data is no longer available
	 * in the requested flavor.
	 * @throws UnsupportedFlavorException if the requested data flavor is
	 * not supported.
	 * @see DataFlavor#getRepresentationClass
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		int ndx = indexOf(flavor);
		if (ndx < 0) {
			throw new UnsupportedFlavorException(flavor);
		}
		return data[ndx];
	}

	/**
	 * Notifies this object that it is no longer the owner of
	 * the contents of the clipboard.  The default behavior is
	 * to throw away all internal data so that its memory can
	 * be garbage collected.  This ClipboardData is effectively
	 * unusable after this method is called.
	 *
	 * @param clipboard the clipboard that is no longer owned
	 * @param contents the contents which this owner had placed on the clipboard
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		flavors = null;
		data = null;
	}

	// Find a matching dataflavor in the flavors array
	private int indexOf(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i].equals(flavor)) {
				return i;
			}
		}
		return -1;
	}
}