/*
 * $Id: DiskDocumentStorage.java /main/15 1998/08/11 09:38:11 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/DiskDocumentStorage.java $
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

import us.cownet.docfw.guitools.textdatamodel.TextDataModel;
import us.cownet.docfw.resources.ExceptionMessages;
import us.cownet.docfw.resources.GUIToolsResources;

import java.awt.*;
import java.io.*;

/**
 * DiskDocumentStorage managers document files on disk.
 * This class lacks a mechanism for mapping file extensions to model
 * classes.
 *
 * @author jfitzpat
 * @version $Version$
 */
public class DiskDocumentStorage extends DocumentStorage {
	// the name for untitled documents
	private static String UNTITLED;
	// The file used for storage
	private File dataFile;

	/**
	 * Create a new DiskDocumentStorage.  The storage is
	 * is not ready for reading or writing.
	 */
	public DiskDocumentStorage() {
	}

	/**
	 * Create a new DiskDocumentStorage associated with
	 * a particular file on disk.  If the file exists on disk
	 * the storage is ready for both reading and writing.
	 *
	 * @param dataFile the file on disk
	 */
	public DiskDocumentStorage(File dataFile) {
		if (dataFile.isDirectory()) {
			throw new IllegalArgumentException();
		}
		this.dataFile = dataFile;
	}

	//A function that caches the name for untitled files.
	private static synchronized String getUntitledName() {
		if (UNTITLED == null) {
			UNTITLED = GUIToolsResources.getResource().getUntitledName();
		}
        return UNTITLED;
	}

	/**
	 * Return true if this storage can save the specified object
	 */
	public boolean canStore(String modelClassName) {
		try {
			Class ser = Class.forName("java.io.Serializable");
			Class modelClass = Class.forName(modelClassName);
			return ser.isAssignableFrom(modelClass);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Return true if this storage can save the specified object
	 */
	public boolean canStore(Object object) {
		return object instanceof Serializable;
	}

	/**
	 * Select a new file for output.  Create a file if needed.
	 * The standard GUI save dialog is presented to the user.
	 *
	 * @param frame the parent frame for the dialog
	 * @return true if the user specified a file.  false otherwise.
	 */
	public boolean selectForWriting(Frame frame) {
		FileDialog fd = new FileDialog(frame,
				"Save " + getPresentationName(), FileDialog.SAVE);
		fd.setFile(getPresentationName());
		fd.show();
		String name = fd.getFile();
		if (name != null) {
			dataFile = new File(fd.getDirectory(), name);
			storageChanged.notifyListeners();
		}
		return (name != null);
	}

	/**
	 * Return true if the DiskDocumentStorage can save objects.
	 */
	public boolean isReadyForReading() {
		return (dataFile != null) && (dataFile.exists());
	}

	/**
	 * Return true if the DiskDocumentStorage can restore objects.
	 */
	public boolean isReadyForWriting() {
		return (dataFile != null);
	}

	/**
	 * Return a user presentable name for
	 */
	public String getPresentationName() {
		return (dataFile == null) ? getUntitledName() : dataFile.getName();
	}

	/**
	 * Read the contents of a file. The storage must be ready for
	 * reading before this method is called.
	 *
	 * @return a DocumentDataModel representing the contents of the file.
	 */
	public Object read() throws IOException {
		if (!isReadyForReading()) {
			throw new DocumentStorage.NotReadyForReadingException();
		}
		Serializable result = null;
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(dataFile);
			ObjectInputStream objectIn;
			boolean isObjectStream = true;
			try {
				objectIn = new ObjectInputStream(fileIn);
				try {
					result = (Serializable) objectIn.readObject();
				} finally {
					objectIn.close();
				}
			} catch (IOException e) {
				//TODO hey {jf} - this is a hack.  This should be replaced by
				//a general mechanism for recognizing file extensions.
				fileIn.close();
				fileIn = new FileInputStream(dataFile);
				result = new TextDataModel();
				((java.io.Externalizable) result).readExternal(new OI(fileIn));
			}
		} catch (FileNotFoundException e) {
			throw new DocumentStorage.ReadException(e);
		} catch (ClassNotFoundException e) {
			throw new FormatException();
		} catch (OptionalDataException e) {
			throw new FormatException();
		} catch (IOException e) {
			throw new FormatException();
		}
		return result;
	}

	/**
	 * Rewrite the contents of a file.  The entire contents of the
	 * file are overwritten.  The file must be ready for writing
	 * before this method is called.
	 */
	public void write(Object doc) throws IOException {
		if (!canStore(doc)) throw new IllegalArgumentException();
		if (!isReadyForWriting()) {
			throw new DocumentStorage.NotReadyForWritingException();
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(dataFile);
			if (doc instanceof java.io.Externalizable) {
				//TODO hey {jf} - missing functionality
				//this is a hack to allow documents complete control of the
				//file contents.  However, DiskDocumentStorage does not
				//currently support reading documents created this way.
				//This whole hack is needed because serialization and
				//ObjectOutputStream both include binary garbage in the
				//file.  So if you want to write a plain test file for
				//example, you need another mechanism.
				((java.io.Externalizable) doc).writeExternal(new OO(fileOut));
			} else {
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(doc);
				objOut.close();
			}
			storageContentsChanged.notifyListeners();
		} catch (IOException e) {
			throw new DocumentStorage.WriteException(e);
		}
	}

	/**
	 * Close the storage.
	 */
	public void close() {
	}

	/**
	 * Return the file used by this storage.
	 */
	public File getFile() {
		return dataFile;
	}

	/**
	 * Set the file used by this storage
	 */
	public void setFile(File newFile) {
		dataFile = newFile;
		storageChanged.notifyListeners();
	}

	//This class is only used by the write method.  It is used to allow
	//DocumentDataModels complete control over their data format on disk.
	private static final class OO extends DataOutputStream
			implements ObjectOutput {
		public OO(OutputStream f) {
			super(f);
		}

		public void writeObject(Object obj) throws IOException {
			throw new IOException();
		}
	}

	//This class is only used by the write method.  It is used to allow
	//DocumentDataModels complete control over their data format on disk.
	private static final class OI extends DataInputStream
			implements ObjectInput {
		public OI(InputStream f) {
			super(f);
		}

		public Object readObject() throws ClassNotFoundException, IOException {
			throw new IOException();
		}
	}

	/**
	 * Thrown when the document is in an unknown format and can't be read.
	 */
	public static class FormatException extends IOException {
		public FormatException() {
			super(ExceptionMessages.DOCUMENT_FORMAT_ERROR);
		}
	}
}
