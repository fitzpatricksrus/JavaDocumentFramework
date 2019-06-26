/*
 * $Id: NotifierListener.java /main/2 1998/08/07 09:35:37 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/NotifierListener.java $
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


/**
 * Listeners implement the Listener interface
 */
public class NotifierListener {
	/**
	 * Called when the notifier notifies.
	 */
	public void ping() {
	}

	/**
	 * Called when the notifier notifies.
	 */
	public void ping(Object message) {
		ping();
	}
}
