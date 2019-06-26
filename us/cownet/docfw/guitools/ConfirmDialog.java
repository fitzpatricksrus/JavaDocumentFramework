/*
 * $Id: AppMain.java /main/11 1998/06/18 17:00:48 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/AppMain.java $
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
import java.awt.*;

/**
 * A Confirm Dialog displays a message and waits for the user
 * two choose one of two options.  The title of the dialog, the
 * message, and the button labels can all be customized.
 *
 * @author jfitzpat
 * @version $Revision: /main/11 $
 */
public final class ConfirmDialog {
	/**
	 * Display a dialog and return true if the user presses the
	 * "Yes" button.  The button labels default to "Yes" and "No".
	 *
	 * @param owner the owner of the dialog
	 * @param title the title of the dialog.  If null, the dialog
	 * will not have a title.
	 * @param messge the massage to be displayed.
	 */
	public static boolean confirm(Frame owner, String title,
			String message) {
		int x = JOptionPane.showConfirmDialog(owner,
				message, title, JOptionPane.YES_NO_OPTION);
		return x == 0;
	}
}
