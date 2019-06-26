/*
 * $Id: StandardGUIFactory.java /main/9 1998/08/07 13:25:28 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/StandardGUIFactory.java $
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

import us.cownet.docfw.resources.GUIToolsResources;

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
public class StandardGUIFactory extends GUIFactory {
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int NEW_ITEM_INDEX = 0;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int OPEN_ITEM_INDEX = 1;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int SAVE_ITEM_INDEX = 2;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int SAVE_AS_ITEM_INDEX = 3;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int CLOSE_ITEM_INDEX = 4;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int QUIT_ITEM_INDEX = 5;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int UNDO_ITEM_INDEX = 0;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int REDO_ITEM_INDEX = 1;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int CUT_ITEM_INDEX = 2;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int COPY_ITEM_INDEX = 3;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int PASTE_ITEM_INDEX = 4;
	/** Constant identifying the placement of a menu item in a standard menu */
	public static final int CLEAR_ITEM_INDEX = 5;
	private static final GUIFactory DEFAULT = new StandardGUIFactory();

	public static Object createRootGUIFor(Document doc, String factoryClassName)
			throws GUIFactory.CouldNotCreateGUIException {
		GUIFactory f = DEFAULT;
		try {
			Class factoryClass = Class.forName(factoryClassName);
			f = (GUIFactory) factoryClass.newInstance();
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassCastException e) {
		}

		return f.createRootGUI(doc);
	}

	public static Object createRootGUIFor(Document doc)
			throws GUIFactory.CouldNotCreateGUIException {
		String factoryClassName = doc.getDataModel().getClass().getName()
				+ EXTENSION;
		return createRootGUIFor(doc, factoryClassName);
	}

	public Object createRootGUI(Document doc)
			throws GUIFactory.CouldNotCreateGUIException {

		AppMain app = doc.getApp();
		Object model = doc.getDataModel();

		GUIToolsResources resources = GUIToolsResources.getResource();

		JFrame frame = new JFrame(doc.getPresentationName());

		//create file menu
		JMenu fileMenu = new JMenu(resources.getFileMenuName());

		//create the "new" menu items.  It can be either a single
		//item or a sub menu.
		DocumentTypeInfo[] docInfo = app.getDocInfo();
		if (docInfo.length == 1) {
			String itemName = resources.getNewItemName() + " " +
					docInfo[0].getPresentationName();
			JMenuItem newItem = new JMenuItem(itemName);
			new NewController(app, newItem, docInfo[0]);
			fileMenu.add(newItem);
		} else if (docInfo.length > 0) {
			JMenu popUp = new JMenu(resources.getNewItemName());
			for (int i = 0; i < docInfo.length; i++) {
				JMenuItem newItem = new JMenuItem(docInfo[i].getPresentationName());
				new NewController(app, newItem, docInfo[i]);
				popUp.add(newItem);
			}
			fileMenu.add(popUp);
		} else {
			JMenuItem dummy = new JMenuItem(resources.getNewItemName());
			dummy.setEnabled(false);
			fileMenu.add(dummy);
		}

		JMenuItem openItem = new JMenuItem(resources.getOpenItemName());
		JMenuItem saveItem = new JMenuItem(resources.getSaveItemName());
		JMenuItem saveAsItem = new JMenuItem(resources.getSaveAsItemName());
		JMenuItem closeItem = new JMenuItem(resources.getCloseItemName());
		JMenuItem quitItem = new JMenuItem(resources.getQuitItemName());
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(closeItem);
		fileMenu.add(quitItem);

		//create edit menu
		JMenu editMenu = new JMenu(resources.getEditMenuName());
		JMenuItem undoItem = new JMenuItem(resources.getUndoItemName());
		JMenuItem redoItem = new JMenuItem(resources.getRedoItemName());
		JMenuItem cutItem = new JMenuItem(resources.getCutItemName());
		JMenuItem copyItem = new JMenuItem(resources.getCopyItemName());
		JMenuItem pasteItem = new JMenuItem(resources.getPasteItemName());
		JMenuItem clearItem = new JMenuItem(resources.getClearItemName());
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(clearItem);

		//attach file menu controllers
		new OpenController(app, frame, openItem);
		new SaveController(frame, doc, saveItem, saveAsItem);
		new CloseController(doc, frame, closeItem);
		new QuitController(app, quitItem);

		//attach edit menu controllers
		new UndoController(doc.getHistory(), undoItem, redoItem);
		SelectionListener sel = new ClipController(doc.getHistory(),
				frame, cutItem, copyItem, pasteItem, clearItem);

		//build window
		frame.getContentPane().setLayout(new BorderLayout());
		Component innerGUI = createGUI(
				doc, //the document
				frame,
				sel, //SelectionListener selection,
				doc.getHistory(), //UndoableEditListener
				null, //KeyListener     menuKeys,
				fileMenu, //JMenu fileMenu,
				editMenu); //JMenu editMenu)

		frame.getContentPane().add("Center", innerGUI);
		frame.pack();
		frame.show();
		return frame;
	}

	/**
	 * Create the content view for the gui.  If the gui is for a model
	 * that is not a subclass of DocumentDataModel, the default implementation
	 * must be replaced.
	 */
	public Component createGUI(final Document doc, final Frame frame,
			SelectionListener selection, UndoableEditListener editListener,
			KeyListener menuKeys, JMenu fileMenu, JMenu editMenu)
			throws GUIFactory.CouldNotCreateGUIException {
		final AppMain app = doc.getApp();
		final Object model = doc.getDataModel();

		if (model instanceof DocumentDataModel) {
			return ((DocumentDataModel) model).createRootGUI(
					app, //AppMain app
					frame,
					selection, //SelectionListener selection,
					editListener, //UndoableEditListener
					menuKeys, //KeyListener     menuKeys,
					fileMenu, //JMenu fileMenu,
					editMenu); //JMenu editMenu)
		} else {
			throw new GUIFactory.CouldNotCreateGUIException();
		}
	}
}

