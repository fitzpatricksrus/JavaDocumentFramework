/*
 * $Id: MessageDialog.java /main/5 1998/07/15 16:35:17 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/MessageDialog.java $
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

/**
 * @author jfitzpat
 * @version $Revision: /main/5 $
 */
public class MessageDialog {
	/**
	 * Display a message dialog.
	 *
	 * @param owner the window owning the dialog
	 * @param title the title for the dialog.  If null, the dialog
	 * will be untitled.
	 * @param message the message to be displayed.
	 */
	public static void displayMessage(Frame owner, String title,
			String message) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display a message dialog.
	 *
	 * @param owner the window owning the dialog
	 * @param title the title for the dialog.  If null, the dialog
	 * will be untitled.
	 * @param message the message to be displayed.
	 */
	public static void displayExceptionMessage(Frame owner, String title,
			Exception e) {

		String msgSelector = e.getMessage();
		displayMessage(owner, title,
				ExceptionMessages.getExceptionMessage(msgSelector));
		e.printStackTrace(System.err);
	}

	/**
	 * Display a message dialog.
	 *
	 * @param owner the window owning the dialog
	 * @param title the title for the dialog.  If null, the dialog
	 * will be untitled.
	 * @param message the message to be displayed.
	 */
	public static void displayExceptionMessage(Frame owner, String title,
			String msgSelector) {

		displayMessage(owner, title,
				ExceptionMessages.getExceptionMessage(msgSelector));
	}


}
