/*
 * $Id: TestFailureException.java /main/2 1998/07/01 11:57:42 jwebber $
 * $Source: /project/wamali/code/com/mot/wamali/util/TestFailureException.java $
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
package us.cownet.docfw.utils;

/**
 * This exception is used by test code througout Wamali Workbench
 * to indicate that a test case failed.
 * <p><strong>This class should not be used in product code.</strong>
 *
 * @version $Revision: /main/2 $
 */
public class TestFailureException extends Exception {
	/**
	 * Constructs a <code>TestException</code> with no specified detail
	 * message.
	 */
	public TestFailureException() {
		super();
	}

	/**
	 * Constructs a <code>TestException</code> with the specified
	 * detail message.
	 *
	 * @param s the detail message.
	 */
	public TestFailureException(String s) {
		super(s);
	}
}
