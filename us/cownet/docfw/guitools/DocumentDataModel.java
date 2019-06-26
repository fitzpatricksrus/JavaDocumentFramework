/*
 * $Id: DocumentDataModel.java /main/14 1998/08/06 14:37:44 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/DocumentDataModel.java $
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
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.Serializable;

/**
 * THIS CLASS IS OBSOLETE.  SEE THE GUIFactory AND StandardGUIFactory
 * CLASSES FOR DETAILS.  THE ModelTest_UT UNIT TEST ALSO SERVES AS
 * AN EXAMPLE OF A GUI APP THAT DOES NOT USE THIS CLASS.
 * <p>
 * The DocumentDataModel interface represents the data in a document.
 * Classes the implement this interface should have an empty constructor
 * and should be serializable without serializing their gui.
 *
 * @author jfitzpat
 * @version $Version$
 */
public interface DocumentDataModel extends Serializable {

	/**
	 * Create's a view for this DocumentComponent that is suitable for
	 * embedding directly in a frame.  The view must create its own
	 * menubar if it needs one.  Standard menus that are usable as-is
	 * are passed as parameters.
	 *
	 * @param selection The selection listener that keeps "Edit" menu updated
	 * @param editListener The listener that keeps the Undo/Redo menu item
	 * and undo history up to date.
	 * @param menuKeys The listener that handles menu keys
	 * @param fileMenu the standard file menu
	 * @param editMenu the standard edit menu
	 * @return the view
	 */
	Component createRootGUI(AppMain app, Frame f,
			SelectionListener selection, UndoableEditListener editListener,
			KeyListener menuKeys, JMenu fileMenu, JMenu editMenu);

}

