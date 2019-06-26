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

import us.cownet.docfw.resources.ExceptionMessages;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * The ClipController class handles cut, copy, paste, and clear operations.
 * The controller maintains the current document selection and enables
 * the standard edit menu items.  The controller expects to have complete
 * control over the menu items.  If other components enable/disable the
 * menu items, the ClipController might not function correctly.
 *
 * @author jfitzpat
 * @version $Revision$
 */
final class ClipController implements SelectionListener {
	/* If true, the ClipController will create an application
	   private clipboard */
	private static final boolean USE_PRIVATE_CLIPBOARD = true;
	/* The application private clipboard */
	private static Clipboard privateClipboard;

	/* The cut menu item watched by the controller */
	private JMenuItem cutItem;
	/* The copy menu item watched by the controller */
	private JMenuItem copyItem;
	/* The paste menu item watched by the controller */
	private JMenuItem pasteItem;
	/* The clean menu item watched by the controller */
	private JMenuItem clearItem;
	/* The history used to log cut, paste, and clear commands */
	private GUIUndoManager history;
	/* The current document selection */
	private DocumentSelection currentSelection =
			EmptyDocumentSelection.EMPTY_SELECTION;

	/**
	 * Construct a new clip controller.
	 *
	 * @param history the history where undoable edits are logged
	 * @param frame the window that the controller is editing.  This
	 * is used to detect possible clipboard changed (the system
	 * clipboard does not offer change notification).
	 * @param cutItem the cut menu item
	 * @param copyItem the copy menu item
	 * @param pasteItem the paste menu item
	 * @param clearItem the clear menu item
	 */
	public ClipController(GUIUndoManager history, Frame frame,
			JMenuItem cutItem, JMenuItem copyItem, JMenuItem pasteItem,
			JMenuItem clearItem) {
		this.cutItem = cutItem;
		this.copyItem = copyItem;
		this.pasteItem = pasteItem;
		this.clearItem = clearItem;
		this.history = history;

		//set all menus to a knows state
		resyncMenuItems();

		//add listeners for each of the menu items
		cutItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performCut();
					}
				}
		);

		copyItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performCopy();
					}
				}
		);

		pasteItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performPaste();
					}
				}
		);

		clearItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performClear();
					}
				}
		);

		//Add listener to the window.  We want to
		//reset the menus when the window is reactivated
		//becuase another application might have changed
		//the clipboard.
		frame.addWindowListener(
				new WindowAdapter() {
					public void windowActivated(WindowEvent e) {
						resyncMenuItems();
					}
				}
		);
	}

	/**
	 * Search the supplied list of data flavors for one that the
	 * specified selection will accept.
	 *
	 * @param selection the selection
	 * @param flavors a list of flavors
	 * @return The first data flavor in flavors that is supported
	 * by selection.
	 */
	protected static DataFlavor selectPasteableFlavor(DocumentSelection selection,
			DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (selection.isDataFlavorSupported(flavors[i])) {
				return flavors[i];
			}
		}
		return null;
	}

	/**
	 * Cut the current selection.
	 */
	public void performCut() {
		try {
			UndoableEdit cmd = ClipCommand.doCut(getClip(), currentSelection);
			history.addEdit(cmd);
			resyncMenuItems();
		} catch (UnsupportedFlavorException e) {
			MessageDialog.displayExceptionMessage(null, null,
					ExceptionMessages.COULD_NOT_CUT);
		}
	}

	/**
	 * Copy the current selection to the clipboard.
	 */
	public void performCopy() {
		try {
			ClipboardData data = new ClipboardData(currentSelection);
			getClip().setContents(data, data);
			resyncMenuItems();
		} catch (UnsupportedFlavorException e) {
			MessageDialog.displayExceptionMessage(null, null,
					ExceptionMessages.COULD_NOT_COPY);
		}
	}

	/**
	 * Paste the contents of the clipboard.
	 */
	public void performPaste() {
		try {
			UndoableEdit cmd = ClipCommand.doPaste(getClip(), currentSelection);
			history.addEdit(cmd);
			resyncMenuItems();
		} catch (UnsupportedFlavorException e) {
			MessageDialog.displayExceptionMessage(null, null,
					ExceptionMessages.COULD_NOT_PASTE);
		} catch (IOException e) {
			MessageDialog.displayExceptionMessage(null, null,
					ExceptionMessages.COULD_NOT_PASTE);
		}
	}

	/**
	 * Clear the contents of the current selection.
	 */
	public void performClear() {
		UndoableEdit cmd = ClipCommand.doClear(currentSelection);
		history.addEdit(cmd);
		resyncMenuItems();
	}

	/**
	 * Set a new current document selection and adjust the menus.
	 *
	 * @param selection the new document selection.  If null, an
	 * empty selection is set as the current document selection.
	 */
	public void handleSelectionChanged(DocumentSelection selection) {
		if (selection != null) {
			currentSelection = selection;
		} else {
			currentSelection = EmptyDocumentSelection.EMPTY_SELECTION;
		}
		resyncMenuItems();
	}

	/**
	 * Return the clipboard that should be used by the controller.
	 *
	 * @return the clipboard
	 */
	protected Clipboard getClip() {
		if (USE_PRIVATE_CLIPBOARD) {
			if (privateClipboard == null) {
				privateClipboard = new Clipboard("private");
			}
			return privateClipboard;
		} else {
			return cutItem.getToolkit().getSystemClipboard();
		}
	}

	/**
	 * Enable and disable menus according to the clipboard contents.
	 */
	protected void resyncMenuItems() {
		cutItem.setEnabled(currentSelection.canCopy()
				&& currentSelection.canClear());
		copyItem.setEnabled(currentSelection.canCopy());
		clearItem.setEnabled(currentSelection.canClear());

		boolean enablePaste = false;
		Transferable data = getClip().getContents(this);
		if (data != null) {
			DataFlavor[] flavors = data.getTransferDataFlavors();
			if (currentSelection.canPaste()) {
				if (null != selectPasteableFlavor(currentSelection, flavors)) {
					enablePaste = true;
				}
			}
		}
		pasteItem.setEnabled(enablePaste);
	}

	/**
	 * The ClipCommand class represents an undoable cut/paste/clear
	 * operation.  To the command instance, cut and clear look
	 * identical; The only difference being that cut affects the clipboard
	 * and clear does not.  Because of this, the command instance does
	 * not worry about the clipboard.  Instead, three static functions
	 * are supplied which do manage the clipboard interaction.  These
	 * statics modify the clipboard and selection and produce an
	 * appropriate ClipCommand instance to undo the operation.
	 */
	private static class ClipCommand implements UndoableEdit {
		/* The selection modified by this command */
		private DocumentSelection sel;
		/* The flavor of the data being pasted */
		private DataFlavor flavor;
		/* The data being pasted if any */
		private Object doData;
		/* Data needed to undo the paste or clear operation */
		private Object undoData;

		/**
		 * Construct a clip command.  The command requires a
		 * selection, but the flavor and data are optional.
		 *
		 * @param sel the selected data.  This data will be
		 * pasted over or cleared.
		 * @param flavor the flavor of the data being pasted.  If null,
		 * the command will assume a clear operation.
		 * @param doData the data to be pasted.  If null, the command
		 * will assume a clear operation.
		 */
		private ClipCommand(DocumentSelection sel, DataFlavor flavor,
				Object doData) {
			this.sel = sel;
			this.flavor = flavor;
			this.doData = doData;
		}

		/**
		 * Perform a cut operation using the specified clipboard and
		 * selection.
		 *
		 * @param clip the clipboard
		 * @param selection the selection to modify
		 * @return an undoable edit
		 */
		public static UndoableEdit doCut(Clipboard clip, DocumentSelection sel)
				throws UnsupportedFlavorException {
			ClipboardData data = new ClipboardData(sel);
			clip.setContents(data, data);
			ClipCommand cmd = new ClipCommand(sel, null, null);
			cmd.redo();
			return cmd;
		}

		/**
		 * Perform a paste operation using the specified clipboard and
		 * selection.
		 *
		 * @param clip the clipboard
		 * @param selection the selection to modify
		 * @return an undoable edit
		 */
		public static UndoableEdit doPaste(Clipboard clip, DocumentSelection sel)
				throws UnsupportedFlavorException, IOException {
			Transferable data = clip.getContents(sel);
			DataFlavor pasteFlavor = selectPasteableFlavor(
					sel, data.getTransferDataFlavors());
			ClipCommand cmd = null;
			cmd = new ClipCommand(sel, pasteFlavor,
					data.getTransferData(pasteFlavor));
			cmd.redo();
			return cmd;
		}

		/**
		 * Perform a clear operation using the specified selection.
		 *
		 * @param selection the selection to modify
		 * @return an undoable edit
		 */
		public static UndoableEdit doClear(DocumentSelection sel) {
			ClipCommand cmd = new ClipCommand(sel, null, null);
			cmd.redo();
			return cmd;
		}

		/**
		 * Undo the edit that was made.
		 */
		public void undo() throws CannotUndoException {
			try {
				sel.pasteData(null, undoData);
			} catch (UnsupportedFlavorException e) {
				throw new CannotUndoException();
			}
		}

		/**
		 * Return true if edit is undoable.
		 */
		public boolean canUndo() {
			return true;
		}

		/**
		 * Redo the edit.
		 */
		public void redo() throws CannotRedoException {
			try {
				if (doData != null && flavor != null) {
					undoData = sel.pasteData(flavor, doData);
				} else {
					undoData = sel.clearData();
				}
			} catch (UnsupportedFlavorException e) {
				throw new CannotRedoException();
			}
		}

		/**
		 * Return true if the edit can be redone.
		 */
		public boolean canRedo() {
			return true;
		}

		/**
		 * May be sent to inform an edit that it should no longer be
		 * used. This is a useful hook for cleaning up state no longer
		 * needed once undoing or redoing is impossible--for example,
		 * deleting file resources used by objects that can no longer be
		 * undeleted. UndoManager calls this before it dequeues edits.
		 */
		public void die() {
		}

		/**
		 * Return false because ClipCommand can't absorb other edits.
		 */
		public boolean addEdit(UndoableEdit anEdit) {
			return false;
		}

		/**
		 * Return false because ClipCommand can't be replaced by othe
		 * edits.
		 */
		public boolean replaceEdit(UndoableEdit anEdit) {
			return false;
		}

		/**
		 * Return true because clip edits are always significant.
		 */
		public boolean isSignificant() {
			return true;
		}

		//TODO hey {jf} - these should be in a resouce

		/**
		 * Return a user presentable name for this edit
		 */
		public String getPresentationName() {
			return (flavor == null) ? "Clear" : "Paste";
		}

		/**
		 * Return a user presentable name for this edit
		 */
		public String getUndoPresentationName() {
			return "Undo " + getPresentationName();
		}

		/**
		 * Return a user presentable name for this edit
		 */
		public String getRedoPresentationName() {
			return "Redo " + getPresentationName();
		}
	}
}

