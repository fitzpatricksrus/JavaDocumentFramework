/*
 * $Source: /project/wamali/code/com/mot/wamali/guitools/dummy/DummyModel.java $
 * $Id: DummyModel.java /main/10 1998/06/05 16:53:10 jfitzpat CHECKEDOUT $
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
import java.util.ResourceBundle;

/**
 * The resource bundle for a GUIDocument.
 */
public class SMIResources extends ListResourceBundle {
	// Resource data.
	private static final String COULD_NOT_SMI = "SMI stubs could not be generated";
	private static final String MISSING_CMDTYPE_LINKAGE = "Missing Linkage";
	private static final String CLASS_NOT_SPECIFIED = "Class not specified";
	private static final String MISSING_PARAM_TYPE = "Parameter type not specified";
	private static final String NO_COMMANDS_FOUND = "Could not find any complete command types";
	// Resource contents table
	private static final Object[][] contents = {
			{COULD_NOT_SMI, COULD_NOT_SMI},
			{MISSING_CMDTYPE_LINKAGE, MISSING_CMDTYPE_LINKAGE},
			{CLASS_NOT_SPECIFIED, CLASS_NOT_SPECIFIED},
			{MISSING_PARAM_TYPE, MISSING_PARAM_TYPE},
			{NO_COMMANDS_FOUND, NO_COMMANDS_FOUND}
	};
	//the name of the base resource class.
	private static final String RESOURCE_CLASS_NAME =
			"us.cownet.docfw.resources.SMIResources";
	private static SMIResources theResource;

	/**
	 * Return the localized version of the specified message string.
	 *
	 * @param key the message key
	 */
	public static synchronized String getMessage(String key) {
		if (theResource == null) {
			theResource = (SMIResources) ResourceBundle.getBundle(RESOURCE_CLASS_NAME);
		}
		return theResource.getString(key);
	}

	public static String getCouldNotSMI() {
		return getMessage(COULD_NOT_SMI);
	}

	public static String getMissingCmdTypeLinkage() {
		return getMessage(MISSING_CMDTYPE_LINKAGE);
	}

	public static String getClassNotSpecified() {
		return getMessage(CLASS_NOT_SPECIFIED);
	}

	public static String getMissingParameterType() {
		return getMessage(MISSING_PARAM_TYPE);
	}

	public static String getNoCommandTypes() {
		return getMessage(NO_COMMANDS_FOUND);
	}

	/**
	 * Override for ListResourceBundle
	 */
	public Object[][] getContents() {
		return contents;
	}
}
