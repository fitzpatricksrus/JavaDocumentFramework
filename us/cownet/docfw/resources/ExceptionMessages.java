/*
 * $Id: ExceptionMessages.java /main/14 1998/07/16 09:27:33 eong $
 * $Source: /project/wamali/code/com/mot/wamali/resources/ExceptionMessages.java $
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
package us.cownet.docfw.resources;

import java.util.ListResourceBundle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ExceptionMessages is the base resource class for (suprise!) exception
 * messages.  Adding a message to the resource is a two step process.
 * First, define a static final String for the message in the area
 * near the definition of UNKNOWN_ERROR. Second, add an entry to the
 * "contents" array for the message.  Use the definitions for
 * UNKNOWN_ERROR and MYSTERIOUS_ERROR as examples.
 *
 * @author jfitzpat
 * @version $version$
 */
public class ExceptionMessages extends ListResourceBundle {
	/** An unknown error occured.  Probably a bug in the program */
	public static final String UNKNOWN_ERROR =
			"An unknown error occurred.";
	/**
	 * An unknown error occured.  Probably a bug in the VM.
	 * This error is also returned for exceptions without
	 * messages.
	 */
	public static final String MYSTERIOUS_ERROR =
			"A strange and perplexing error occurred.";

	//---------------------------------------------
	// Resource data.  Add your messages here.
	//---------------------------------------------
	/** A document could not be opened for some reason */
	public static final String DOCUMENT_WOULDNT_OPEN =
			"The document could not be opened.";
	/** A document could not be opened for some reason */
	public static final String DOCUMENT_FORMAT_ERROR =
			"The document is in an unknown format.";

	// ---- Guitools Messages ----
	/** The copy command in the edit menu didn't work */
	public static final String COULD_NOT_READ =
			"An error occured while attempting to read a file.";
	/** The copy command in the edit menu didn't work */
	public static final String COULD_NOT_WRITE =
			"An error occured while attempting to write a file.";
	/** The copy command in the edit menu didn't work */
	public static final String COULD_NOT_COPY =
			"The selected data could not be copied.";
	/** The paste command in the edit menu didn't work */
	public static final String COULD_NOT_PASTE =
			"The paster operation could not be performed.";
	/** The cut command in the edit menu didn't work */
	public static final String COULD_NOT_CUT =
			"The selected data could not be cut to the clipboard.";
	/** The last operation could not be undone */
	public static final String COULD_NOT_UNDO =
			"The last operation could not be undone.";
	/** The last operation could not be redone */
	public static final String COULD_NOT_REDO =
			"The last operation could not be redone.";
	/** The command or response entered does not have the correct format */
	public static final String WRONG_APDU_FORMAT =
			"The command or response entered has incorrect format.";
	/** The test could not run */
	public static final String TEST_COULD_NOT_RUN =
			"The test could not be run.";

	// ---- TestSystem Messages ----
	public static final String COULD_NOT_CREATE_TEST =
			"The new test could not be created.";
	public static final String INVALID_TEST_ID =
			"The specified ID is invalid.";
	/** Duplicate class name in applet */
	public static final String DUP_CLASS =
			"There is already a class of this name in the applet.";
	/** Duplicate data item name in applet */
	public static final String DUP_DATAITEM =
			"There is already a data item of this name in the applet.";

	// ---- Specmodel Messages ----
	/** Duplicate command name in applet */
	public static final String DUP_COMMAND =
			"There is already a command type of this name in the applet.";
	/** Duplicate method name in class */
	public static final String DUP_METHOD =
			"There is already a method of this name in the class.";
	/** Duplicate state name in applet */
	public static final String DUP_STATE =
			"There is already a state of this name in the applet.";
	/** The class is not in an applet */
	public static final String NO_CLASS_APPLET =
			"This class must be in an applet in order to perform this operation.";
	/** The class is not in an applet */
	public static final String NO_DI_APPLET =
			"This data item must be in an applet in order to perform this operation.";
	/** The command type is not in an applet. */
	public static final String NO_CMD_APPLET =
			"This command type must be in an applet in order to perform this operation.";
	/** The state is not in an applet. */
	public static final String NO_STATE_APPLET =
			"This state must be in an applet in order to perform this operation.";
	/** The object is in the wrong applet */
	public static final String WRONG_OBJECT_APPLET =
			"The object being linked to this class must be in the same applet as the class.";
	/** Attempt to add a CmdMethod object to more than one class */
	public static final String WRONG_METHOD_CLASS =
			"This method is already in a different class.";
	/** The instance variable is in the wrong applet */
	public static final String WRONG_IVAR_APPLET =
			"The instance variable must be in the same applet as this data item.";
	/** The method is in the wrong applet */
	public static final String WRONG_METHOD_APPLET =
			"The method must be in the same applet as this command type.";
	/** The data item is in the wrong applet */
	public static final String WRONG_DI_APPLET =
			"The data item must be in the same applet as this object.";
	/** State transition properties in wrong applet */
	public static final String WRONG_STATE_APPLET =
			"One or more properties of this state transition is not in the same applet.";
	/** Can't get another legal INS value */
	public static final String OUT_OF_INS =
			"Automapping failed because the applet has used all available INS byte values.";
	/** Method has incompatible signature to command type. */
	public static final String INCOMPATIBLE_SIGNATURE =
			"A method of this name already exists in the class, and has an incompatible signature.";
	/** Data item to object linkage would cause recursion */
	public static final String DI_RECURSION =
			"Cannot link this object as it would cause recursion.";
	//the name of the base resource class.
	private static final String RESOURCE_CLASS_NAME =
			"us.cownet.docfw.resources.ExceptionMessages";
	//the global list of messages.  This table associates
	//a message key with a particular text message
	private static final Object[][] contents = {
			{UNKNOWN_ERROR, UNKNOWN_ERROR},

			// Guitools

			{MYSTERIOUS_ERROR, MYSTERIOUS_ERROR},
			{DOCUMENT_WOULDNT_OPEN, DOCUMENT_WOULDNT_OPEN},
			{DOCUMENT_FORMAT_ERROR, DOCUMENT_FORMAT_ERROR},
			{COULD_NOT_READ, COULD_NOT_READ},
			{COULD_NOT_WRITE, COULD_NOT_WRITE},
			{COULD_NOT_COPY, COULD_NOT_COPY},
			{COULD_NOT_PASTE, COULD_NOT_PASTE},
			{COULD_NOT_CUT, COULD_NOT_CUT},
			{COULD_NOT_UNDO, COULD_NOT_UNDO},
			{COULD_NOT_REDO, COULD_NOT_REDO},

			// Testsystem

			{WRONG_APDU_FORMAT, WRONG_APDU_FORMAT},
			{TEST_COULD_NOT_RUN, TEST_COULD_NOT_RUN},
			{COULD_NOT_CREATE_TEST, COULD_NOT_CREATE_TEST},
			{INVALID_TEST_ID, INVALID_TEST_ID},

			// Specmodel

			{DUP_CLASS, DUP_CLASS},
			{DUP_DATAITEM, DUP_DATAITEM},
			{DUP_COMMAND, DUP_COMMAND},
			{DUP_METHOD, DUP_METHOD},
			{DUP_STATE, DUP_STATE},
			{NO_CLASS_APPLET, NO_CLASS_APPLET},
			{NO_DI_APPLET, NO_DI_APPLET},
			{NO_CMD_APPLET, NO_CMD_APPLET},
			{NO_STATE_APPLET, NO_STATE_APPLET},
			{WRONG_OBJECT_APPLET, WRONG_OBJECT_APPLET},
			{WRONG_METHOD_CLASS, WRONG_METHOD_CLASS},
			{WRONG_IVAR_APPLET, WRONG_IVAR_APPLET},
			{WRONG_METHOD_APPLET, WRONG_METHOD_APPLET},
			{WRONG_DI_APPLET, WRONG_DI_APPLET},
			{WRONG_STATE_APPLET, WRONG_STATE_APPLET},
			{OUT_OF_INS, OUT_OF_INS},
			{INCOMPATIBLE_SIGNATURE, INCOMPATIBLE_SIGNATURE},
			{DI_RECURSION, DI_RECURSION},
	};


	//---------------------------------------------
	// The resource table.
	//---------------------------------------------
	//a cache of the exception messages resource for the
	//current locale.
	private static ExceptionMessages localMessages = null;

	/**
	 * Get the localized message for a particular exception.
	 *
	 * @param msg the message string from
	 */
	public static String getExceptionMessage(String msg) {
		//This routine is NOT synchronized.  It isn't
		//possible for multiple entries to invalidate
		//the data. The worst thing that can happen is
		//that the resource is loaded a second time (something
		//the class loader should handle).
		if (localMessages == null) {
			localMessages = (ExceptionMessages) ResourceBundle.getBundle(
					RESOURCE_CLASS_NAME);
		}
		String result;
		try {
			result = localMessages.getString(msg);
		} catch (MissingResourceException e) {
			result = MYSTERIOUS_ERROR;
		}
		return result;
	}

	public Object[][] getContents() {
		return contents;
	}
}
