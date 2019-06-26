/*
 * $Source: /project/wamali/code/com/mot/wamali/guitools/NullCommand.java $
 * $Id: NullCommand.java /main/2 1998/07/16 09:31:29 jfitzpat $
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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * The NullCommand is a command that does nothing.  It can't be
 * undone or redone, but it does make a document "dirty", requiring
 * it to be saved.
 *
 * @author jfitzpat
 * @version $Version$
 */
public class NullCommand implements UndoableEdit {
	public void undo() throws CannotUndoException {
	}

	public boolean canUndo() {
		return false;
	}

	public void redo() throws CannotRedoException {
	}

	public boolean canRedo() {
		return false;
	}

	public void die() {
	}

	public boolean addEdit(UndoableEdit anEdit) {
		return false;
	}

	public boolean replaceEdit(UndoableEdit anEdit) {
		return false;
	}

	public boolean isSignificant() {
		return false;
	}

	public String getPresentationName() {
		return "";
	}

	public String getUndoPresentationName() {
		return "";
	}

	public String getRedoPresentationName() {
		return "";
	}
}



