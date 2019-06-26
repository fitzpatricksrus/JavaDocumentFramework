/*
 * $Id: DocumentStorage.java /main/18 1998/08/11 09:38:14 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/DocumentStorage.java $
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

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * The DocumentStorage interface allows documents to be stored
 * using an arbitrary storage mechanism (ex. disk, database).
 * A DocumentStorage is able to save and restore serializable objects
 * and select and create new external stores (ex. files) for data.
 * <p>
 * When a DocumentStorage object itself is serialized, it does not
 * copy the contents of the external storage, it simple serializes
 * a reference to that storage (ex. a file name).
 *
 * @author jfitzpat
 * @version $Version$
 */
public class DocumentStorage implements Serializable {
	//TODO hey {jf} - these should be final
	/** called when the DocumentStorage object changes */
	public final Notifier storageChanged = new Notifier();
	/** called when the backing store changes */
	public final Notifier storageContentsChanged = new Notifier();

	/**
	 * Return a name for this DocumentStorage suitable for
	 * display in a GUI.
	 */
	public String getPresentationName() {
		return "";
	}

	/**
	 * Return true if this storage can save the specified object
	 */
	public boolean canStore(String modelClassName) {
		return false;
	}

	/**
	 * Return true if this storage can save the specified object
	 */
	public boolean canStore(Object object) {
		return false;
	}

	/**
	 * Select new physical storage for this DocumentStorage.
	 * A GUI mechanism may be used if needed.
	 */
	public boolean selectForReading(Frame f) {
		return false;
	}

	/**
	 * Select new physical storage for this DocumentStorage.
	 * A GUI mechanism may be used if needed.
	 */
	public boolean selectForWriting(Frame f) {
		return false;
	}

	/**
	 * Return true if this DocumentStorage can read().
	 */
	public boolean isReadyForReading() {
		return false;
	}

	/**
	 * Return true if this DocumentStorage can write().
	 */
	public boolean isReadyForWriting() {
		return false;
	}

	/**
	 * Read the contents of this DocumentStorage and return
	 * a DocumentDataModel that represents them.
	 */
	public Object read() throws IOException {
		throw new NotReadyForReadingException();
	}

	/**
	 * Rewrite the contents of this DocumentStorage with
	 * the specified DocumentDataModel.
	 *
	 * @param DocumentDataModel the data model to write.
	 */
	public void write(Object doc) throws IOException {
		throw new NotReadyForWritingException();
	}

	/**
	 * Close the DocumentStorage and do necessary clean up so
	 * that this object can be safely destroyed.
	 */
	public void close() {
	}

	//This method is overriden so the change notifier can
	//be initialized when the model is deserialized from
	//a file.
	private void readObject(java.io.ObjectInputStream s)
			throws IOException, ClassNotFoundException {
		//initialize the notifier
		s.defaultReadObject();
		storageChanged = new Notifier();
		storageContentsChanged = new Notifier();
	}

	public static class DocumentIOException extends IOException {
		private IOException cause;

		public DocumentIOException(IOException cause, String message) {
			super(message);
			this.cause = cause;
		}

		public IOException getCause() {
			return cause;
		}
	}

	public static class ReadException extends DocumentIOException {
		public ReadException(IOException cause) {
			super(cause, ExceptionMessages.COULD_NOT_READ);
		}

		public ReadException(IOException cause, String message) {
			super(cause, message);
		}
	}

	public static class WriteException extends DocumentIOException {
		public WriteException(IOException cause) {
			super(cause, ExceptionMessages.COULD_NOT_WRITE);
		}

		public WriteException(IOException cause, String message) {
			super(cause, message);
		}
	}

	public static class NotReadyForReadingException
			extends IOException {
		public NotReadyForReadingException() {
			super(ExceptionMessages.COULD_NOT_READ);
		}
	}

	public static class NotReadyForWritingException
			extends IOException {
		public NotReadyForWritingException() {
			super(ExceptionMessages.COULD_NOT_WRITE);
		}
	}
}
