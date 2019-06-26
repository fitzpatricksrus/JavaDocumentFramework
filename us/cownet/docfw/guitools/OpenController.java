/*
 * $Id: OpenController.java /main/16 1998/08/12 14:48:15 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/OpenController.java $
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

import us.cownet.docfw.resources.ExceptionMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The SelectionListener interface allows clients to be notified when the
 * document selection has changed.
 *
 * @author jfitzpat
 * @version $Version$
 */
//TODO hey {jf} - This controller needs a mechanism for mapping file
//extensions to some sort of storage mechanism.  Currently it
//always creates DiskDocumentStorage, but this won't work
//correctly when we add new storage types that aren't based
//on serialization.  The obvious place to put the mapping is
//in the DocumentTypeInfo object.  You'll need some other
//way to  do file selection since it requires that a storage
//object already exist.
final class OpenController {
	//if this flag is true, the open controller attempts to
	//open documents of unknown format with a DiskDocumentStorage.
	//if this flag is false, documents of unknown format are
	//not opened and a dialog is displayed.
	private static final boolean openUnknownDocuments = true;
	private AppMain app;
	private Frame frame;

	/**
	 * Create a new OpenController
	 *
	 * @param app the application that should control new documents.
	 * @param frame the window the should be the parent to any
	 * gui dialogs needed for the open.
	 * @param item the menu item to listen to.
	 */
	public OpenController(AppMain app, Frame frame, JMenuItem item) {
		this.app = app;
		this.frame = frame;
		item.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openAFile();
					}
				}
		);
	}

	/**
	 * Open a file.
	 */
	public void openAFile() {
		FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
		fd.show();
		String name = fd.getFile();
		if (name != null) {
			File dataFile = new File(fd.getDirectory(), name);
			//the user has selected a file, search doc types to see
			//if its a file type we support.  Searh all doc types
			//and find the one that can best represent the contents
			//of the file
			DocumentTypeInfo[] info = app.getDocInfo();
			DocumentTypeInfo winner = null;
			short winnerUse = DocumentTypeInfo.UNUSABLE;
			for (int i = 0; i < info.length; i++) {
				short canUse = info[i].canUseStorage(dataFile);
				if (canUse > winnerUse) {
					winner = info[i];
					winnerUse = canUse;
				}
			}

			if (winner != null) {
				//we support this document type, so try to open
				//it as a document.
				try {
					DocumentStorage store = winner.storageFor(dataFile);
					app.addDocument(store);
				} catch (Exception e) {
					MessageDialog.displayExceptionMessage(frame, "", e);
				}
			} else if (openUnknownDocuments) {
				//we support this document type, so try to open
				//it as a document.
				try {
					DocumentStorage store = new DiskDocumentStorage(dataFile);
					app.addDocument(store);
				} catch (Exception e) {
					MessageDialog.displayExceptionMessage(frame, "", e);
				}
			} else {
				MessageDialog.displayExceptionMessage(frame, "",
						ExceptionMessages.DOCUMENT_FORMAT_ERROR);
			}
		}
	}
}
