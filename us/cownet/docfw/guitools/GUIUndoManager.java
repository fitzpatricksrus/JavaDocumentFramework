/*
 * $Id: GUIUndoManager.java /main/5 1998/08/11 12:35:34 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/GUIUndoManager.java $
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

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * A GUIUndoManager is a log of UndoableActions that provides notification
 * when undoable actions are added or removed from the log.  The log
 * has a special marked positions, the "saved" position.  When the undoable
 * edit in the saved position is the next undoable edit to be undone,
 * the log is concidered to be "clean".  NOTE: The GUIUndoManager ignores
 * edits that are received during an undo/redo operation.  They are
 * concidered to be artifacts of the undo/redo.
 *
 * @author jfitzpat
 * @version $Version$
 */
final class GUIUndoManager implements UndoableEditListener {

	/**
	 * A notifier called when an undoable edit is added to
	 * the log.
	 */
	public final Notifier changeNotifier = new Notifier();
	//The undo log
	private UndoManager history = new UndoManager();
	//A index representing the point
	//at which the document was saved.
	private int size;
	//true while an undo/redo operation is in progress
	private boolean undoRedoInProgress = false;

	/**
	 * Add an undoable edit to the log and notify listeners.
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		size++;
		history.undoableEditHappened(e);
		changeNotifier.notifyListeners();
	}

	/**
	 * Add an undoable edit to the log and notify listeners.
	 */
	public void addEdit(UndoableEdit edit) {
		size++;
		history.addEdit(edit);
		changeNotifier.notifyListeners();
	}

	/**
	 * Returns true if an undo
	 * operation would be successful now, false otherwise.
	 */
	public boolean canUndo() {
		return history.canUndo();
	}

	/**
	 * Returns true if a redo operation would be successful now,
	 * false otherwise.
	 */
	public boolean canRedo() {
		return history.canRedo();
	}

	/**
	 * Undo the last significant UndoableEdit.
	 */
	public void undo() {
		try {
			undoRedoInProgress = true;
			history.undo();
		} finally {
			undoRedoInProgress = false;
		}
		size--;
		changeNotifier.notifyListeners();
	}

	/**
	 * Redo the previous significant UndoableEdit.
	 */
	public void redo() {
		try {
			undoRedoInProgress = true;
			history.redo();
		} finally {
			undoRedoInProgress = false;
		}
		size++;
		changeNotifier.notifyListeners();
	}

	/**
	 * Remove all undoable edits from the log and reset the log.
	 */
	public void clear() {
		history.discardAllEdits();
		reset();
	}

	/**
	 * Mark the current log position as the "saved" position.
	 */
	public void reset() {
		size = 0;
		changeNotifier.notifyListeners();
	}

	/**
	 * Return true if the log is in the saved position. false otherwise.
	 */
	public boolean isClean() {
		return size == 0;
	}

	/**
	 * Return a string suitable for display in the GUI undo item.
	 */
	public String getUndoPresentationName() {
		return history.getUndoPresentationName();
	}

	/**
	 * Return a string suitable for display in the GUI redo item.
	 */
	public String getRedoPresentationName() {
		return history.getRedoPresentationName();
	}
}
