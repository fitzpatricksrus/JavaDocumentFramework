/*
 * $Id: SelectionListener.java /main/12 1998/06/25 11:29:24 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/SelectionListener.java $
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

/**
 * The SelectionListener interface allows clients to be notified when the
 * document selection has changed.
 *
 * @author jfitzpat
 * @version $Version$
 */
public interface SelectionListener {

	/**
	 * This is called when the document selection has changed.  This occurs
	 * when the selection is modified to select different data, not when the
	 * data itself has changed.  For example, changing the color of a selected
	 * object would not change the selection. Selecting a different object
	 * would.
	 *
	 * @param selection the new document selection
	 */
	void handleSelectionChanged(DocumentSelection selection);
}
