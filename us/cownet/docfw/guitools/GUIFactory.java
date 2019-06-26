/*
 * $Id: GUIFactory.java /main/5 1998/08/06 14:37:50 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/GUIFactory.java $
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

/**
 * A GUIFactory creates a GUI for a document model.
 *
 * @author jfitzpat
 * @version $Version$
 */
public abstract class GUIFactory {
	public static final String EXTENSION = "_GUI";

	/**
	 * Create a GUI for a particular document in a particular application.
	 * are passed as parameters.
	 *
	 * @param app the application the document will be added to.
	 * @param doc the document holding the data model
	 * @param model the data model that the gui should display
	 * @param factoryClassName the name of the GUIFactory class to use when
	 * creating the gui.
	 * @return a frame containing the gui
	 */
	public static Object createRootGUIFor(Document doc, String factoryClassName)
			throws CouldNotCreateGUIException {
		try {
			Class factoryClass = Class.forName(factoryClassName);
			GUIFactory f = (GUIFactory) factoryClass.newInstance();
			return f.createRootGUI(doc);
		} catch (ClassNotFoundException e) {
			throw new CouldNotCreateGUIException();
		} catch (InstantiationException e) {
			throw new CouldNotCreateGUIException();
		} catch (IllegalAccessException e) {
			throw new CouldNotCreateGUIException();
		} catch (ClassCastException e) {
			throw new CouldNotCreateGUIException();
		}
	}

	/**
	 * Create a GUI for a particular document in a particular application.
	 * are passed as parameters.  The factory class is assumed to
	 * be the same as the class name with the EXTENSION appended.
	 *
	 * @param app the application the document will be added to.
	 * @param doc the document holding the data model
	 * @param model the data model that the gui should display
	 * @return a frame containing the gui
	 */
	public static Object createRootGUIFor(Document doc)
			throws CouldNotCreateGUIException {
		String factoryClassName = doc.getDataModel().getClass().getName() + EXTENSION;
		return createRootGUIFor(doc, factoryClassName);
	}

	/**
	 * Create's a view for this DocumentComponent that is suitable for
	 * embedding directly in a frame.  The view must create its own
	 * menubar if it needs one.  Standard menus that are usable as-is
	 * are passed as parameters.
	 *
	 * @param app the application the document will be added to.
	 * @param doc the document holding the data model
	 * @param model the data model that the gui should display
	 * @param selection The selection listener that keeps "Edit" menu updated
	 * @param editListener The listener that keeps the Undo/Redo menu item
	 * and undo history up to date.
	 * @param menuKeys The listener that handles menu keys
	 * @param fileMenu the standard file menu
	 * @param editMenu the standard edit menu
	 * @return the view
	 */
	public abstract Component createGUI(Document doc, Frame frame,
			SelectionListener selection, UndoableEditListener editListener,
			KeyListener menuKeys, JMenu fileMenu, JMenu editMenu)
			throws CouldNotCreateGUIException;

	/**
	 * Create's a view for this document. The "view" can be
	 * an arbitrary object and is not manipulated by the
	 * caller.
	 *
	 * @param doc the document holding the data model
	 * @return the view
	 */
	public abstract Object createRootGUI(Document doc)
			throws CouldNotCreateGUIException;

	public static class CouldNotCreateGUIException extends Exception {
	}
}

