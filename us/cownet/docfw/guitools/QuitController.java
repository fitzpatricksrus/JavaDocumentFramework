/*
 * $Id: QuitController.java /main/11 1998/08/06 14:37:58 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/QuitController.java $
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
 * The quit controller exits the application after first closing all open
 * documents.
 *
 * @author jfitzpat
 * @version $Version$
 */
final class QuitController {
	//The application to quit
	private AppMain app;

	/**
	 * Create a new controller
	 *
	 * @param app the application to quit
	 * @param the menu item to listen to
	 */
	public QuitController(AppMain app, JMenuItem item) {
		this.app = app;

		item.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						quit();
					}
				}
		);
	}

	/**
	 * Close all open documents and quit the application
	 */
	public void quit() {
		app.removeAllDocuments();
	}
}
