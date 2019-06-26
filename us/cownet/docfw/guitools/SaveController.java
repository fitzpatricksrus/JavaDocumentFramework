/*
 * $Id: SaveController.java /main/20 1998/08/07 13:38:34 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/SaveController.java $
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A Save controller handles saving a document.  It provides methods to
 * perform "Save" and "Save As..." operations.  It also prompts the user
 * if a document is about to be closed and with unsaved changes.
 *
 * @author jfitzpat
 * @version $Version$
 */
final class SaveController {
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private Document doc;
	private Frame frame;

	/**
	 * Create a new save controller.  The controller for the specified
	 * document.
	 *
	 * @param frame the frame to use as parent for any dialogs
	 * @param doc the document to dave
	 * @param saveItem the menu item to listen to
	 * @param saveAsItem the menu item to listen to
	 */
	public SaveController(final Frame frame, final Document doc,
			JMenuItem saveItem, JMenuItem saveAsItem) {
		this.saveItem = saveItem;
		this.saveAsItem = saveAsItem;
		this.doc = doc;
		this.frame = frame;

		//listen to the save menu item
		saveItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleSave();
					}
				}
		);
		//listen to the saveAs menu item
		saveAsItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleSaveAs();
					}
				}
		);

		NotifierListener docChangeListener =
				new NotifierListener() {
					public void ping() {
						handleDocumentChanged();
					}
				};

		//listen to the undo log.  When the document
		//changes, we need to update the menus
		doc.getHistory().changeNotifier.addListener(
				docChangeListener);

		//listen to the document just in case someon
		//changes the document programmatically.
		doc.dataNotifier.addListener(docChangeListener);

		//listen for when the storage changes so
		//the window title can be updated
		doc.storageNotifier.addListener(
				new NotifierListener() {
					public void ping() {
						handleStorageChanged();
					}
				}
		);

		//listen for when our view is closed.
		doc.viewRemovedNotifier.addListener(
				new NotifierListener() {
					public void ping(Object view) {
						//if this view has been removed, we don't need to
						//listen anymore this is what we were listening for.
						if (view == frame) {
							doc.viewRemovedNotifier.removeListener(this);
							handleViewClosed();
						}
					}
				}
		);

		//update the menus
		handleDocumentChanged();
	}

	/**
	 * Save the document puttin up a GUI save dialog if needed.
	 */
	public void handleSave() {
		try {
			DocumentStorage store = doc.getStorage();
			boolean doSave = store.isReadyForWriting();
			if (doSave) {
				doc.save();
				doc.getHistory().reset();
			} else {
				doSave = store.selectForWriting(frame);
				if (doSave) {
//                    frame.setTitle(store.getPresentationName());
					doc.save();
					doc.getHistory().reset();
				}
			}
		} catch (IOException e) {
			MessageDialog.displayExceptionMessage(frame, "", e);
		}
	}

	/**
	 * Perform a "Save As.." by putting up a GUI dialog
	 * and then saving the document.
	 */
	public void handleSaveAs() {
		DocumentStorage store = doc.getStorage();
		if (store.selectForWriting(frame)) {
//            frame.setTitle(store.getPresentationName());
			handleSave();
		}
	}

	/**
	 * Update the menus to reflect changes in the document.
	 * If the document is dirty, the save menu should be
	 * enabled
	 */
	public void handleDocumentChanged() {
		if (doc.getHistory().isClean()) {
			saveItem.setEnabled(false);
		} else {
			saveItem.setEnabled(true);
		}
	}

	/**
	 * If the last view has been closed,
	 * check to see if the document is dirtly and put up a
	 * "Do you want to save changes?" dialog before the
	 * document is closed
	 */
	public void handleViewClosed() {
		//if we're the last view to close - check to see if there are any
		//unsaved changes.
		if (doc.getViewCount() == 0) {
			if (!doc.getHistory().isClean()) {
				GUIToolsResources resources = GUIToolsResources.getResource();
				boolean needsSave = ConfirmDialog.confirm(frame,
						resources.getString(GUIToolsResources.DO_YOU_WANT_TO_SAVE),
						resources.getString(GUIToolsResources.DOCUMENT_UNSAVED));
				if (needsSave) {
					handleSave();
				}
			}
		}
	}

	/**
	 * Update the gui (window title) to reflect changes
	 * in the document storage.
	 */
	public void handleStorageChanged() {
		frame.setTitle(doc.getPresentationName());
	}
}

