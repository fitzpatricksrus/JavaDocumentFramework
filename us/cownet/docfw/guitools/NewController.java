/*
 * $Id: NewController.java /main/11 1998/08/11 12:44:53 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/NewController.java $
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
 * A New controller creates a new empty document.
 *
 * @author jfitzpat
 * @version $Version$
 */
final class NewController {
	//The application that should control the document.
	private AppMain app;

	//The type of document to create.
	private DocumentTypeInfo info;

	/**
	 * Create a new NewController.
	 *
	 * @param app the AppMain that should control the new document
	 * @param item the menu item the controller should listen to.
	 * @param info the TypeInfo used to create new documents.
	 */
	public NewController(AppMain app, JMenuItem item, DocumentTypeInfo info) {
		this.app = app;
		this.info = info;

		item.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						createNewDocument();
					}
				}
		);
	}

	/**
	 * Create a new document
	 */
	public void createNewDocument() {
		try {
			Object model = info.createNewModel();
			app.addDocument(model);
		} catch (Exception e) {
			MessageDialog.displayExceptionMessage(null, "", e);
		}
	}
}
