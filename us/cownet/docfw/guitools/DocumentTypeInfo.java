/*
 * $Id: DocumentTypeInfo.java /main/5 1998/08/12 09:53:16 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/DocumentTypeInfo.java $
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

import java.io.File;

/**
 * A TypeInfo is used to generate new, empty documents.  If can
 * generate a DocumentDataModel, a DocumentStorage, and a Document
 * to associate the two.  It is used primarily for the "New" item
 * in the GUI and by the "Save" item when saving a new document.
 * User's create a TypeInfo, passing it the names of the classes
 * for the components of a document.  The TypeInfo should then
 * be verified to ensure that all the necessary classes exist.
 *
 * @author jfitzpat
 * @version $Version$
 */
public class DocumentTypeInfo {
	//TODO hey {jf} - the exception stuff in this class needs to
	//be cleaned up.  Currently this class throws an
	//IllegalArgumentException if it can't create a new
	//model.  It should probably throw its own exception
	//type so an appropriate message dialog can be displayed.

	/** Store is in this document's native format */
	public static final short NATIVE_NO_LOSS = Short.MAX_VALUE;
	/** Store is in a format that can be converted and used without data loss */
	public static final short NON_NATIVE_NO_LOSS = (short) (NATIVE_NO_LOSS / 4 * 3);
	/**
	 * Store is in old version of this document's native format and there may
	 * be data loss when converting to the new format
	 */
	public static final short NATIVE_WITH_LOSS = (short) (NATIVE_NO_LOSS / 4 * 2);
	/**
	 * Store is in a foreign format and a loss of data may result in coversion
	 * to a usable format
	 */
	public static final short NON_NATIVE_WITH_LOSS = (short) (NATIVE_NO_LOSS / 4);

	// Usefull constants for use with canUseStorage
	/** Store is in an unknown format and can't be converted by this DocumentTypeInfo */
	public static final short UNUSABLE = 0;
	/* A name suitable for presentation in the GUI */
	private String presentationName;
	/* The name of the DocumentModel class */
	private String modelType;
	/* Cache of the actual DocumentModel class */
	private Class modelClass;
	/* Flag indicated the validity of the cached objects above */
	private boolean inited = false;

	/**
	 * Create a new TypeInfo
	 */
	public DocumentTypeInfo(String name, String modelType)
			throws IllegalArgumentException {
		if (name == null) throw new IllegalArgumentException();
		if (modelType == null) throw new IllegalArgumentException();
		presentationName = name;
		this.modelType = modelType;
	}

	/**
	 * Examine the storage reference and determine if this DocumentTypeInfo
	 * can create a DocumentStorage object for it.
	 *
	 * @param storageReference the storage reference. The storage reference
	 * can be any object, but it typically a file.
	 * @return an indication of how well the data pointed to by the
	 * storage reference can be interpreted by the DocumentStorage.
	 * The higher the value, the better the interpretation.  A return
	 * value of 0 or less indicates that this DocumentTypeInfo can't
	 * create a DocumentStorage object from this storage reference.
	 */
	public short canUseStorage(Object storageReference) {
		if (storageReference instanceof File) {
			//TODO hey {jf} - this function should somehow ensure that the
			//file contains a serialized object.  Right now, it just
			//assumes that all files do. So, to allow for the possibility
			//that someone may actually do something intelligent (sp?),
			//we let anyone who knows anything about this file
			//to have a go at it if they want to.
			return UNUSABLE + 1;
		} else {
			return UNUSABLE;
		}
	}

	/**
	 * Return a DocumentStorage appropriate for reading a model
	 * of this type if the specified file is the proper type
	 * for this model
	 */
	public DocumentStorage storageFor(Object storageReference) {
		if (canUseStorage(storageReference) <= UNUSABLE) {
			//Hey!  We said we couldn't do anything with this reference.
			//Stop bothering us about it.
			throw new IllegalArgumentException();
		}
		return new DiskDocumentStorage((File) storageReference);
	}

	/**
	 * Verify that the TypeInfo is able to create new models,
	 * document storage, and documents.
	 */
	public void verify() {
		init();
	}

	/**
	 * Return a name for this type of document that is suitable for
	 * presentation in the GUI
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * Return the DocumentModel subclass used in this type of document.
	 */
	public Class getModelClass() {
		init();
		return modelClass;
	}

	/**
	 * Create a new data model
	 */
	public Object createNewModel() {
		init();
		try {
			Object result = modelClass.newInstance();
			return result;
		} catch (InstantiationException e) {
			//This should never occur.  The init() method
			//tested to make sure it wouldn't
			return null;
		} catch (IllegalAccessException e) {
			//This should never occur.  The init() method
			//tested to make sure it wouldn't
			return null;
		} catch (ClassCastException e) {
			//This should never occur.  The init() method
			//tested to make sure it wouldn't
			return null;
		}
	}

	/**
	 * Verify that the TypeInfo is able to create new models,
	 * document storage, and documents.
	 */
	private void init() {
		//TODO hey {jf} - this method should be removed.  See
		//the note at the top of the class.
		if (inited == false) {
			try {
				modelClass = Class.forName(modelType);
				//the cast here is important.  it ensures
				//that the class actually implements Serializable
				Object x = modelClass.newInstance();
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException();
			} catch (InstantiationException e) {
				throw new IllegalArgumentException();
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException();
			} catch (ClassCastException e) {
				throw new IllegalArgumentException();
			}
			inited = true;
		}
	}
}
