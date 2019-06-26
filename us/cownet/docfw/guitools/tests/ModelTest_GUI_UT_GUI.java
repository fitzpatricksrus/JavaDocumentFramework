/*
 * $Source: /project/wamali/code/com/mot/wamali/guitools/tests/ModelTest_GUI_UT_GUI.java $
 * $Id: ModelTest_GUI_UT_GUI.java /main/4 1998/08/07 13:25:33 jfitzpat $
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
package us.cownet.docfw.guitools.tests;

import us.cownet.docfw.guitools.*;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * The ModelTest_GUI_UT_GUI class creates a gui for the ModelTest_GUI_UT
 * class.  It extends StandardGUIFactory and hence inherits the standard
 * menus.  If the standard menus had not been desired, it could have
 * descended from GUIFactory and overriden the createRootGUI method.
 * <p>
 * This class is associated wit the ModelTest_GUI_UT class by virtue of
 * its name only: "ModelTest_GUI_UT" + "_GUI".  Any change in the name
 * will break this association.
 */
public class ModelTest_GUI_UT_GUI extends StandardGUIFactory {
	private static final DataFlavor[] FLAVORS = {DataFlavor.stringFlavor};

	/**
	 * Create's a view for this DocumentComponent that is suitable for embedding
	 * directly in a frame.  The view must create its own menubar if it needs
	 * one.  Standard menus that are usable as-is are passed as parameters.
	 *
	 * @param selection The selection listener that keeps "Edit" menu updated
	 * @param editListener The listener that keeps the Undo/Redo menu item
	 * and undo history up to date.
	 * @param menuKeys The listener that handles menu keys
	 * @param fileMenu the standard file menu
	 * @param editMenu the standard edit menu
	 * @return the view
	 */
	public Component createGUI(final Document doc, final Frame f,
			SelectionListener selection, UndoableEditListener editListener,
			KeyListener menuKeys, JMenu fileMenu, JMenu editMenu)
			throws GUIFactory.CouldNotCreateGUIException {
		final Object model = doc.getDataModel();
		if (model instanceof us.cownet.docfw.guitools.tests.ModelTest_GUI_UT) {
			us.cownet.docfw.guitools.tests.ModelTest_GUI_UT theModel = (us.cownet.docfw.guitools.tests.ModelTest_GUI_UT) model;
			//Since the selection in this application never
			//changes, I can set it once and just leave it.  This
			//seemed like a good place to do that "one time"
			//setting.  If the application allowed the user
			//to select several different objects in the document,
			//the controller would do this every time the user
			//selected something new.
			selection.handleSelectionChanged(new Selection(theModel));

			JMenu viewMenu = new JMenu("View");
			JMenuItem newViewItem = new JMenuItem("New");
			viewMenu.add(newViewItem);

			newViewItem.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								doc.openNewView();
							} catch (GUIFactory.CouldNotCreateGUIException e1) {
								MessageDialog.displayExceptionMessage(f, "", e1);
							}
						}
					}
			);

			//Create a view.  The view creates its own controller
			//so I pass it the editListener (so it can log commands
			//for undo/redo).  I also pass the menus so a menu
			//bar can be constructed.
			return new View(theModel, editListener, fileMenu, editMenu, viewMenu);
		} else {
			//If the model isn't of the proper type, throw up your
			//hand in disgust.
			throw new GUIFactory.CouldNotCreateGUIException();
		}
	}

	/**
	 * The View class is the applications main GUI view.  If creates
	 * a button, the appropriate controllers, connects for change
	 * notification from the model.
	 */
	private static class View extends JComponent {
		public View(final us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model,
				UndoableEditListener editListener,
				JMenu fileMenu, JMenu editMenu, JMenu viewMenu) {
			//Create the main contents of the view
			final JButton button = new JButton();
			button.setText(Integer.toString(model.getLabel()));
			//Create a new controller that listens for
			//button clicks.  The controller will create
			//undoableEdits and send them to the editListener
			//to be logged for undo/redo.
			button.addActionListener(
					new ClickController(model, button, editListener));
			//add the button to the view
			setLayout(new BorderLayout());
			add("Center", button);
			//create and add the menu bar to the view
			JMenuBar bar = new JMenuBar();
			bar.add(fileMenu);
			bar.add(editMenu);
			bar.add(viewMenu);
			add("North", bar);

			//attach to the model for change notification.  When
			//the model changes, it will call the "ping" method.
			model.getNotifier().addListener(
					new NotifierListener() {
						public void ping() {
							//set the text of the button with the new value
							//from the model.
							button.setText(Integer.toString(model.getLabel()));
						}
					}
			);
		}
	}

	/*
	 * This is the main controller class.   It listens for
	 * button clicks and increments the value in the model
	 * by one.  It sends a SetLabelCommand to the undo log
	 * so the change can be undone.
	 */
	private static class ClickController implements ActionListener {
		private UndoableEditListener editListener;
		private us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model;

		public ClickController(us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model, JButton button,
				UndoableEditListener editListener) {
			this.editListener = editListener;
			this.model = model;
		}

		public void actionPerformed(ActionEvent e) {
			SetLabelCommand cmd =
					new SetLabelCommand(model, model.getLabel(), model.getLabel() + 1);
			cmd.redo();
			editListener.undoableEditHappened(new UndoableEditEvent(e, cmd));
		}
	}

	/**
	 * The SetLabelCommand class simply toggles between
	 * two model values.
	 */
	private static class SetLabelCommand implements UndoableEdit {
		private int doValue;
		private int undoValue;
		private us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model;

		public SetLabelCommand(us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model, int undoValue, int doValue) {
			this.doValue = doValue;
			this.undoValue = undoValue;
			this.model = model;
		}

		public void undo() throws CannotUndoException {
			model.setLabel(undoValue);
		}

		public boolean canUndo() {
			return true;
		}

		public void redo() throws CannotRedoException {
			model.setLabel(doValue);
		}

		public boolean canRedo() {
			return true;
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
			return true;
		}

		public String getPresentationName() {
			return "Click";
		}

		public String getUndoPresentationName() {
			return "Undo Click";
		}

		public String getRedoPresentationName() {
			return "Redo Click";
		}
	}

	/**
	 * See the java.awt.datatransfer.Transferable class
	 * to see how this interface works.  It is simply a
	 * two way Transferable interface.
	 */
	private class Selection implements DocumentSelection {
		private us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model;

		public Selection(us.cownet.docfw.guitools.tests.ModelTest_GUI_UT model) {
			this.model = model;
		}

		public DataFlavor[] getDataFlavors() {
			return FLAVORS;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == null || DataFlavor.stringFlavor.equals(flavor);
		}

		public boolean canCopy() {
			return true;
		}

		public Object copyData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (isDataFlavorSupported(flavor)) {
				return Integer.toString(model.getLabel());
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean canPaste() {
			return true;
		}

		public Object pasteData(DataFlavor flavor, Object data)
				throws UnsupportedFlavorException {
			if (isDataFlavorSupported(flavor)) {
				String undoValue = Integer.toString(model.getLabel());
				try {
					String x = (String) data;
					int value = Integer.parseInt(x);
					model.setLabel(value);
					return undoValue;
				} catch (java.lang.NumberFormatException e) {
					return undoValue;
				}
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean canClear() {
			return true;
		}

		public Object clearData() {
			String undoValue = Integer.toString(model.getLabel());
			model.setLabel(0);
			return undoValue;
		}
	}
}
