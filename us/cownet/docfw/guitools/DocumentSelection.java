/*
 * $Id: DocumentSelection.java /main/13 1998/08/11 12:35:37 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/DocumentSelection.java $
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * A DocumentSelection specifies data in a document. This default
 * GUI uses this class to implement CUT/COPY/PASTE.  Whenever the
 * user selects something in a document, the document should create
 * a new DocumentSelection and tell the SelectionListener that
 * the current selection changed.
 * cut/copy/paste.
 *
 * @author jfitzpat
 * @version $Version$
 */
public interface DocumentSelection {
	/**
	 * Returns an array of DataFlavor objects indicating the flavors the
	 * data can be provided in.  The array should be ordered according to
	 * preference for providing the data (from most richly descriptive to
	 * east descriptive).
	 *
	 * @return an array of data flavors in which this data can be
	 * transferred
	 */
    DataFlavor[] getDataFlavors();

	/**
	 * Returns whether or not the specified data flavor is supported for
	 * this object.
	 *
	 * @param flavor the requested flavor for the data
	 * @return boolean indicating wjether or not the data flavor is
	 * supported
	 */
    boolean isDataFlavorSupported(DataFlavor flavor);

	//copy support

	/**
	 * Returns true if this selection can be copied.
	 */
    boolean canCopy();

	/**
	 * Returns an object which represents the data to be transferred.
	 * The class  of the object returned is defined by the representation
	 * class of the flavor.
	 *
	 * @param flavor the requested flavor for the data.
	 * @throws UnsupportedFlavorException if the requested data flavor is
	 * not supported.
	 * @see DataFlavor#getRepresentationClass
	 */
    Object copyData(DataFlavor flavor)
			throws UnsupportedFlavorException;

	//paste support

	/**
	 * Returns true if this selection can be pasted over.
	 */
    boolean canPaste();

	/**
	 * Replaces data specified by this selection.  The selection should
	 * modify itself to select the pasted data.
	 *
	 * @param flavor flavor of data to be pasted.  If null, the object
	 * was produced by a putTransferData or clearTransferData operation.
	 * @param data data being pasted.
	 * @return an object representing the data that was replaced.
	 * @throws UnsupportedFlavorException if the requested data flavor is
	 * not supported.
	 * @see DataFlavor#getRepresentationClass
	 */
    Object pasteData(DataFlavor flavor, Object data)
			throws UnsupportedFlavorException;

	//cut/clear support

	/**
	 * Returns true if this selection can be cleared.
	 */
    boolean canClear();

	/**
	 * Clears the data specified by this selection.  The selection should
	 * modify itself to be an insertion point such that a paste of the
	 * returned object will effectively undo the clear operation.
	 *
	 * @return an object representing the data that was cleared.
	 */
    Object clearData();
}
