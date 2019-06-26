/*
 * $Source: /project/wamali/code/com/mot/wamali/guitools/textdatamodel/TextDataModel.java $
 * $Id: TextDataModel.java /main/11 1998/08/11 14:50:37 jfitzpat $
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
package us.cownet.docfw.guitools.textdatamodel;

import us.cownet.docfw.guitools.AppMain;
import us.cownet.docfw.guitools.DocumentDataModel;
import us.cownet.docfw.guitools.DocumentSelection;
import us.cownet.docfw.guitools.SelectionListener;
import us.cownet.docfw.resources.ExceptionMessages;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyListener;
import java.io.*;

/**
 * A TextDataModel represents a document containing plain text.  The
 * document is NOT user editable but can be modified programmatically.
 * Example:
 * //create a new model
 * TextDataModel model = new TextDataModel();
 * //"print" whatever you want to the model
 * PrintWriter w = model.getPrinter();
 * w.println("hello");
 * w.println("world");
 * //display the document to the user.  Note that you
 * //are passed an AppMain when you create a GUI.
 * someAppMain.addGUIDocument(model);
 *
 * @author jfitzpat
 * @version $Version$
 */
public class TextDataModel implements DocumentDataModel, Externalizable {
	//TODO hey {jf} - this is a hack.  See the preferredSize method;
	private static final Dimension theSizeWeWant = new Dimension(400, 400);
//  private EditorKit kit = new RTFEditorKit();
	//  private EditorKit kit = new StyledEditorKit();
	private EditorKit kit = new HTMLEditorKit();
	//the actual data model
	private transient Document data;

	/**
	 * Create an empty TextDataModel
	 */
	public TextDataModel() {
	}

	/**
	 * Test code.
	 */
	public static void main(String[] args) {
		try {
			AppMain app = new AppMain();
			//create a new model
			TextDataModel model = new TextDataModel();
			//"print" whatever you want to the model
			PrintWriter w = model.getPrinter();
			w.println("hello");
			w.println("<B>world</B>");
			w.println("");
			w.print("Hello");
			w.print(" again. 87");
			//display the document to the user
			app.addDocument(model);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
		}
	}

	/**
	 * Create's a view for this DocumentComponent.  The view will be
	 * read-only and will have correspondingly simplified menus.
	 *
	 * @param selection The selection listener that keeps "Edit" menu updated
	 * @param editListener The listener that keeps the Undo/Redo menu item
	 * and undo history up to date.
	 * @param menuKeys The listener that handles menu keys
	 * @param fileMenu the standard file menu
	 * @param editMenu the standard edit menu
	 * @return the view
	 */
	public java.awt.Component createRootGUI(AppMain app, Frame f,
			SelectionListener selection, UndoableEditListener editListener,
			KeyListener menuKeys, JMenu fileMenu, JMenu editMenu) {

		return new View(app, selection, editListener,
				menuKeys, fileMenu, editMenu);
	}

	/**
	 * Return a PrintWriter that will append to the document.
	 */
	public PrintWriter getPrinter() {
		return new PrintWriter(
				new Writer() {
					public void write(char[] cbuf, int off, int len)
							throws IOException {
						appendToDocument(cbuf, off, len);
					}

					public void flush() throws IOException {
						//flush();
					}

					public void close() throws IOException {
						//close();
					}

				}, true);
	}

	//append text to the document.
	private void appendToDocument(final char[] cbuf, final int off,
			final int len) throws IOException {
		try {
			getDocument().insertString(data.getLength(),
					new String(cbuf, off, len), null);
		} catch (BadLocationException b) {
			//this should never happen.
			throw new IOException(ExceptionMessages.MYSTERIOUS_ERROR);
		}
	}

	//get the documents storage model.
	private Document getDocument() {
		if (data == null) {
			data = kit.createDefaultDocument();
		}
		return data;
	}

	/**
	 * Serialization method.  The document's contents are written out
	 * in UTF format.
	 */
	public void writeExternal(final ObjectOutput s) throws IOException {
		try {
			String content = getDocument().getText(0,
					getDocument().getLength());
			//convert the contents to bytes so that it goes through
			//the platform transcoder to the preferred platform encoding.
			//in most cases, this will be ASCII.
			byte[] buf = content.getBytes();
			s.write(buf);
		} catch (BadLocationException e) {
			throw new IOException();
		}
	}

	/**
	 * Read the document from a stream.
	 */
	public void readExternal(ObjectInput s)
			throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[s.available()];
		s.read(buffer, 0, buffer.length);
		String content = new String(buffer);
		try {
			getDocument().remove(0, getDocument().getLength());
			getDocument().insertString(0, content, null);
		} catch (BadLocationException e) {
			//TODO hey {jf} - clean up this exception
			throw new IOException();
		}
	}

	//This class filters incomming undoable edits.  In the case
	//of the Swing document model, the model generates undoable
	//edits when ever it is modified.  This is great for normal
	//user edits like typing.  It is a problem for modifications to
	//the model for things like undo/redo.  You don't want the
	//changes made by an undone command to generate yet another
	//undoable edit in order to undo the undone command (tell
	//JW that emacs is strange).  So, during operations like undo/redo
	//and cut/clear/paste, all undoable edits generated by the model
	//are ignored becuase there is already a command that represents
	//the edits.  This little class is the class that allows us
	//to switch the logging of these edits on/off.
	private static final class EditController implements UndoableEditListener {
		private UndoableEditListener listener;
		private boolean editsShouldBePosted;

		public EditController(UndoableEditListener listener) {
			this.listener = listener;
			editsShouldBePosted = true;
		}

		public void ignoreEdits() {
			editsShouldBePosted = false;
		}

		public void postEdits() {
			editsShouldBePosted = true;
		}

		public void undoableEditHappened(UndoableEditEvent e) {
			if (editsShouldBePosted) {
				listener.undoableEditHappened(e);
			}
		}
	}

	//This class allows text to be copied from the model to the clipboard.
	private static final class Selection implements DocumentSelection {
		//the pane to copy from
		private JTextPane pane;
		private int start;
		private int end;
		private Document doc;
		private EditController editController;

		//create a new selection
		public Selection(JTextPane pane, EditController editController) {
			this.pane = pane;
			doc = pane.getDocument();
			start = pane.getSelectionStart();
			end = pane.getSelectionEnd();
			this.editController = editController;
		}

		/**
		 * Returns an array of DataFlavor objects indicating the flavors the
		 * data can be provided in.  The array should be ordered according to
		 * preference for providing the data (from most richly descriptive to
		 * east descriptive).
		 *
		 * @return an array of data flavors in which this data can be
		 * transferred
		 */
		public DataFlavor[] getDataFlavors() {
			return new DataFlavor[]{DataFlavor.stringFlavor};
		}

		/**
		 * Returns whether or not the specified data flavor is supported for
		 * this object.
		 *
		 * @param flavor the requested flavor for the data
		 * @return boolean indicating wjether or not the data flavor is
		 * supported
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == null || DataFlavor.stringFlavor.equals(flavor);
		}

		//copy support

		/**
		 * Returns true if this selection can be copied.
		 */
		public boolean canCopy() {
			return start != end;
		}

		/**
		 * Returns an object which represents the data to be transferred.
		 * The class  of the object returned is defined by the representation
		 * class of the flavor.
		 *
		 * @param flavor the requested flavor for the data.
		 * @throws UnsupportedFlavorException if the requested data flavor is
		 * not supported.
		 * @see DataFlavor#getRepresentationClass
		 */
		public Object copyData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			try {
				return doc.getText(start, end - start);
			} catch (BadLocationException e) {
				//TODO hey {jf} - do something here
				return "";
			}
		}

		//paste support

		/**
		 * Returns true if this selection can be pasted over.
		 */
		public boolean canPaste() {
			return true;
		}

		/**
		 * Replaces data specified by this selection.
		 *
		 * @param flavor flavor of data to be pasted.  If null, the object
		 * was produced by a putTransferData or clearTransferData
		 * operation.
		 * @param data data being pasted.
		 * @return an object representing the data that was replaced.
		 * @throws IOException if the data is no longer available
		 * in the requested flavor.
		 * @throws UnsupportedFlavorException if the requested data flavor is
		 * not supported.
		 * @see DataFlavor#getRepresentationClass
		 */
		public Object pasteData(DataFlavor flavor, Object data)
				throws UnsupportedFlavorException {
			if (flavor != null && !isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}

			String result = null;
			String value = (String) data;
			try {
				editController.ignoreEdits();
				if (start != end) {
					result = doc.getText(start, end - start);
					doc.remove(start, end - start);
				}
				doc.insertString(start, value, null);
				//start = start;
				end = start + value.length();
			} catch (BadLocationException e) {
				//TODO hey {jf} - do something here.
				//this should never happen
			} finally {
				editController.postEdits();
			}

			return result;
		}

		//cut/clear support

		/**
		 * Returns true if this selection can be cleared.
		 */
		public boolean canClear() {
			return (start != end);
		}

		/**
		 * Clears the data specified by this selection and sets the current
		 * selection to an empty selection.
		 *
		 * @return an object representing the data that was cleared.
		 * @throws IOException if the data is no longer available
		 * in the requested flavor.
		 * @throws UnsupportedFlavorException if the requested data flavor is
		 * not supported.
		 * @see DataFlavor#getRepresentationClass
		 */
		public Object clearData() {
			String result = null;
			try {
				editController.ignoreEdits();
				if (start != end) {
					result = doc.getText(start, end - start);
					doc.remove(start, end - start);
				}
				//start = start;
				end = start;
			} catch (BadLocationException e) {
				//TODO hey {jf} - do something here.
				//this should never happen
			} finally {
				editController.postEdits();
			}
			return result;
		}
	}

	/**
	 * The View class implements the view of a text model.  It is read-only
	 * and has truncated menus.
	 */
	private final class View extends JComponent {
		//the actual editor view
		private JTextPane editor;
		//the current selection listener
		private SelectionListener currentSelection;
		//size of the menubar for preferred size calculation
		private Dimension menuBarSize;

		private EditController editController;

		// create a new view
		public View(AppMain app, SelectionListener selection,
				UndoableEditListener editListener, KeyListener menuKeys,
				JMenu fileMenu, JMenu editMenu) {

			editController = new EditController(editListener);
			getDocument().addUndoableEditListener(editController);

			currentSelection = selection;
			//create a new editor panel for this document
			editor = new JTextPane();
			editor.setEditorKit(kit);
			editor.setDocument(getDocument());
			editor.setEditable(true);
			Dimension d = editor.getPreferredScrollableViewportSize();
			//add scroll bars and all that guck.
			JScrollPane scroller = new JScrollPane();
			JViewport vp = scroller.getViewport();
			vp.add(editor);
			vp.setBackingStoreEnabled(true);    //turn on back buffer

			setLayout(new BorderLayout());
			add("Center", scroller);


			//edit the menus to remove unused items
/*            fileMenu.remove(StandardGUIFactory.SAVE_ITEM_INDEX);
            fileMenu.remove(StandardGUIFactory.NEW_ITEM_INDEX);

            editMenu.remove(StandardGUIFactory.CLEAR_ITEM_INDEX);
            editMenu.remove(StandardGUIFactory.PASTE_ITEM_INDEX);
            editMenu.remove(StandardGUIFactory.CUT_ITEM_INDEX);
            editMenu.remove(StandardGUIFactory.REDO_ITEM_INDEX);
            editMenu.remove(StandardGUIFactory.UNDO_ITEM_INDEX);
            */
			JMenuBar bar = new JMenuBar();
			bar.add(fileMenu);
			bar.add(editMenu);
			add("North", bar);

			menuBarSize = bar.getPreferredSize();

			//add a listener to set a new selection whenever
			//the caret is moved.
			editor.addCaretListener(
					new CaretListener() {
						public void caretUpdate(CaretEvent e) {
							handleSelectionChanged();
						}
					}
			);
		}

		//The caret has moved, set a new selection so that the
		//copy menu item is updated correctly.
		private void handleSelectionChanged() {
			currentSelection.handleSelectionChanged(
					new Selection(editor, editController));
		}

		/**
		 * Gets the preferred size of this component.
		 *
		 * @return A dimension object indicating this component's
		 * preferred size.
		 * @see #getMinimumSize
		 * @see java.awt.LayoutManager
		 */
		public Dimension getPreferredSize() {
			Dimension size = editor.getPreferredScrollableViewportSize();
			size.width += menuBarSize.width;
			size.height += menuBarSize.height;
			//TODO hey {jf} - this is a hack.  The editor is not returning
			//it's real preferred size.
			size.width = Math.max(size.width, theSizeWeWant.width);
			size.height = Math.max(size.height, theSizeWeWant.height);
			return size;
		}
	}
}
