/*
 * $Id: DummyModel.java /main/10 1998/06/05 16:53:10 jfitzpat CHECKEDOUT $
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
package us.cownet.docfw.resources;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * The resource bundle for a GUIDocument.
 */
public class GUIToolsResources extends ListResourceBundle {
	public static final String DO_YOU_WANT_TO_SAVE =
			"Do you want to save?";
	public static final String DOCUMENT_UNSAVED =
			"This document has been modified since it was last saved.\n" +
					"Do you want to save the changes?";
	// Resource data.
	private static final String UNTITLED = "Untitled";
	private static final String FILE_MENU_NAME = "File";
	private static final String NEW_ITEM_NAME = "New";
	private static final String OPEN_ITEM_NAME = "Open";
	private static final String SAVE_ITEM_NAME = "Save";
	private static final String SAVE_AS_ITEM_NAME = "Save As...";
	private static final String REVERT_ITEM_NAME = "Revert";
	private static final String CLOSE_ITEM_NAME = "Close";
	private static final String QUIT_ITEM_NAME = "Quit";
	private static final String EDIT_MENU_NAME = "Edit";
	private static final String UNDO_ITEM_NAME = "Undo";
	private static final String REDO_ITEM_NAME = "Redo";
	private static final String CUT_ITEM_NAME = "Cut";
	private static final String COPY_ITEM_NAME = "Copy";
	private static final String PASTE_ITEM_NAME = "Paste";
	private static final String CLEAR_ITEM_NAME = "Clear";
	private static final String OK_ITEM_NAME = "OK";
	private static final String CANCEL_ITEM_NAME = "Cancel";
	private static final String YES_ITEM_NAME = "Yes";
	private static final String NO_ITEM_NAME = "No";
	private static final Object[][] contents = {
			{UNTITLED, UNTITLED},
			{FILE_MENU_NAME, FILE_MENU_NAME},
			{NEW_ITEM_NAME, NEW_ITEM_NAME},
			{OPEN_ITEM_NAME, OPEN_ITEM_NAME},
			{SAVE_ITEM_NAME, SAVE_ITEM_NAME},
			{SAVE_AS_ITEM_NAME, SAVE_AS_ITEM_NAME},
			{REVERT_ITEM_NAME, REVERT_ITEM_NAME},
			{CLOSE_ITEM_NAME, CLOSE_ITEM_NAME},
			{QUIT_ITEM_NAME, QUIT_ITEM_NAME},
			{EDIT_MENU_NAME, EDIT_MENU_NAME},
			{UNDO_ITEM_NAME, UNDO_ITEM_NAME},
			{REDO_ITEM_NAME, REDO_ITEM_NAME},
			{CUT_ITEM_NAME, CUT_ITEM_NAME},
			{COPY_ITEM_NAME, COPY_ITEM_NAME},
			{PASTE_ITEM_NAME, PASTE_ITEM_NAME},
			{CLEAR_ITEM_NAME, CLEAR_ITEM_NAME},
			{OK_ITEM_NAME, OK_ITEM_NAME},
			{CANCEL_ITEM_NAME, CANCEL_ITEM_NAME},
			{YES_ITEM_NAME, YES_ITEM_NAME},
			{NO_ITEM_NAME, NO_ITEM_NAME},
			{DO_YOU_WANT_TO_SAVE, DO_YOU_WANT_TO_SAVE},
			{DOCUMENT_UNSAVED, DOCUMENT_UNSAVED},
	};

	//the name of the base resource class.
	private static final String RESOURCE_CLASS_NAME =
			"us.cownet.docfw.resources.GUIToolsResources";

	/**
	 * Get the localized message for a particular exception.
	 *
	 * @param msg the message string from
	 */
	public static GUIToolsResources getResource() {
		//This routine is NOT synchronized.  It isn't
		//possible for multiple entries to invalidate
		//the data. The worst thing that can happen is
		//that the resource is loaded a second time (something
		//the class loader should handle).
		return (GUIToolsResources) ResourceBundle.getBundle(
				RESOURCE_CLASS_NAME);
	}

	public final String getUntitledName() {
		return getString(UNTITLED);
	}

	public final String getFileMenuName() {
		return getString(FILE_MENU_NAME);
	}

	public final String getNewItemName() {
		return getString(NEW_ITEM_NAME);
	}

	public final String getOpenItemName() {
		return getString(OPEN_ITEM_NAME);
	}

	public final String getSaveItemName() {
		return getString(SAVE_ITEM_NAME);
	}

	public final String getSaveAsItemName() {
		return getString(SAVE_AS_ITEM_NAME);
	}

	public final String getRevertItemName() {
		return getString(REVERT_ITEM_NAME);
	}

	public final String getCloseItemName() {
		return getString(CLOSE_ITEM_NAME);
	}

	public final String getQuitItemName() {
		return getString(QUIT_ITEM_NAME);
	}

	public final String getEditMenuName() {
		return getString(EDIT_MENU_NAME);
	}

	public final String getUndoItemName() {
		return getString(UNDO_ITEM_NAME);
	}

	public final String getRedoItemName() {
		return getString(REDO_ITEM_NAME);
	}

	public final String getCutItemName() {
		return getString(CUT_ITEM_NAME);
	}

	public final String getCopyItemName() {
		return getString(COPY_ITEM_NAME);
	}

	public final String getPasteItemName() {
		return getString(PASTE_ITEM_NAME);
	}

	public final String getClearItemName() {
		return getString(CLEAR_ITEM_NAME);
	}

	public final String getOKItemName() {
		return getString(OK_ITEM_NAME);
	}

	public final String getCancelItemName() {
		return getString(CANCEL_ITEM_NAME);
	}

	public final String getYesItemName() {
		return getString(YES_ITEM_NAME);
	}

	public final String getNoItemName() {
		return getString(NO_ITEM_NAME);
	}

	public Object[][] getContents() {
		return contents;
	}
}
