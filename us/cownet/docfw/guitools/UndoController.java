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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An UndoController keeps the undo/redo menu items in sync with
 * the undo log.
 *
 * @author jfitzpat
 * @version $Version$
 */
final class UndoController {
	//the undo log.
	private GUIUndoManager history;

	//the undo menu item to listen to and update.
	private JMenuItem undoItem;

	//the redo menu item to listen to and update.
	private JMenuItem redoItem;

	/**
	 * Create a new UndoController.
	 *
	 * @param the history to listen to and undo/redo
	 * @param undoItem the undo menu item to control
	 * @param redoItem the redo item to control
	 */
	public UndoController(GUIUndoManager history, JMenuItem undoItem,
			JMenuItem redoItem) {
		this.history = history;
		this.undoItem = undoItem;
		this.redoItem = redoItem;

		resyncMenuItems();

		history.changeNotifier.addListener(
				new NotifierListener() {
					public void ping() {
						resyncMenuItems();
					}
				}
		);

		undoItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performUndo();
					}
				}
		);

		redoItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performRedo();
					}
				}
		);
	}

	/**
	 * Undo the last undoable edit in the history.
	 */
	public void performUndo() {
		if (history.canUndo()) {
			history.undo();
			resyncMenuItems();
		}
	}

	/**
	 * Redo the last undoable edit in the history.
	 */
	public void performRedo() {
		if (history.canRedo()) {
			history.redo();
			resyncMenuItems();
		}
	}

	/**
	 * Update the undo and redo menu items to reflect changes
	 * in the undo history.
	 */
	public void resyncMenuItems() {
		undoItem.setEnabled(history.canUndo());
		redoItem.setEnabled(history.canRedo());
	}
}
