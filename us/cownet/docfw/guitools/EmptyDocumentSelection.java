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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * The EmptyDocumentSelection represents a selection that
 * can't interact with the clipboard.  There is a single
 * global instance of this class.
 *
 * @author jfitzpat
 * @version $Version$
 */
public final class EmptyDocumentSelection implements DocumentSelection {
	/** The global empty document selection */
	public static final DocumentSelection EMPTY_SELECTION =
			new EmptyDocumentSelection();

	/*
	 * This constructor is private because only a single
	 * EmptyDocumentSelection exists.
	 */
	private EmptyDocumentSelection() {
	}

	/**
	 * Return null indicated that this selection can't export
	 * data in any format.
	 */
	public DataFlavor[] getDataFlavors() {
		return null;
	}

	/**
	 * Return false indicating that this selection can't
	 * accept data in any format.
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}

	/**
	 * Return false indicating that this selection does not
	 * support copy operations.
	 */
	public boolean canCopy() {
		return false;
	}

	/**
	 * Throw an exception indicating that this selection does
	 * not support copy operations.
	 */
	public Object copyData(DataFlavor flavor)
			throws UnsupportedFlavorException {
		throw new UnsupportedFlavorException(flavor);
	}

	/**
	 * Return false indicating that this selection does not
	 * support paste operations.
	 */
	public boolean canPaste() {
		return false;
	}

	/**
	 * Throw an exception indicating that this selection does
	 * not accept pasted data.
	 */
	public Object pasteData(DataFlavor flavor, Object data)
			throws UnsupportedFlavorException {
		throw new UnsupportedFlavorException(flavor);
	}

	/**
	 * Return false indicating that this selection does not
	 * support clear operations.
	 */
	public boolean canClear() {
		return false;
	}

	/**
	 * Return null indicating that nothing was cleared.
	 */
	public Object clearData() {
		return null;
	}
}
