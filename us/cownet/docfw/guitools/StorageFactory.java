/*
 * $Id: GUIFactory.java /main/5 1998/08/06 14:37:50 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/GUIFactory.java $
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
 * A StorageFactory creates a DocumentStorage for a given model.
 *
 * @author jfitzpat
 * @version $Version$
 */
public abstract class StorageFactory {
	public static final String EXTENSION = "_STORE";

	/**
	 * Create a DocumentStorage object for the specified model.
	 *
	 * @param model the document holding the data model
	 * @return the storage
	 */
	public static DocumentStorage createDocumentStorage(Object model)
			throws CouldNotCreateStorageException {
		String className = model.getClass().getName();
		return createDocumentStorage(className);
	}

	/**
	 * Create a DocumentStorage object for the specified model.
	 *
	 * @param model the document holding the data model
	 * @return the storage
	 */
	public static DocumentStorage createDocumentStorage(String modelClassName)
			throws CouldNotCreateStorageException {
		return createDocumentStorage(modelClassName, modelClassName + EXTENSION);
	}

	/**
	 * Create a DocumentStorage object for the specified model.
	 *
	 * @param model the document holding the data model
	 * @return the storage
	 */
	public static DocumentStorage createDocumentStorage(String modelClassName,
			String factoryClassName) throws CouldNotCreateStorageException {
		DocumentStorage store;
		try {
			Class factoryClass = Class.forName(factoryClassName);
			StorageFactory f = (StorageFactory) factoryClass.newInstance();
			store = f.createStorage(modelClassName);
		} catch (ClassNotFoundException e) {
			store = new DiskDocumentStorage();
		} catch (InstantiationException e) {
			store = new DiskDocumentStorage();
		} catch (IllegalAccessException e) {
			store = new DiskDocumentStorage();
		} catch (ClassCastException e) {
			store = new DiskDocumentStorage();
		} catch (CouldNotCreateStorageException e) {
			store = new DiskDocumentStorage();
		}
		if (!store.canStore(modelClassName)) {
			throw new CouldNotCreateStorageException();
		} else {
			return store;
		}
	}

	/**
	 * Create a DocumentStorage object for the specified model.
	 *
	 * @param model the document holding the data model
	 * @return the storage
	 */
	protected abstract DocumentStorage createStorage(String modelClassName)
			throws CouldNotCreateStorageException;

	public static class CouldNotCreateStorageException extends Exception {
	}
}

