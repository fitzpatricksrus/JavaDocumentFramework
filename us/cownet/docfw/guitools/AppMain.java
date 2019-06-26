/*
 * $Id: AppMain.java /main/17 1998/07/16 15:21:04 jfitzpat $
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

import java.io.IOException;
import java.util.Enumeration;

//import java.io.File;

/**
 * The AppMain class represents the application itself.	 It maintains
 * application wide state such as a list of documents.	When a document
 * is added to the application, its 'open" method is called.  The
 * document's 'close' method is called when the document is removed
 * from the applications control.
 *
 * @author jfitzpat
 * @version $Revision: /main/17 $
 */
public class AppMain {
	/** Notifier for notification when the last document is closed */
	public final Notifier allClosedNotifier = new Notifier();
	// The list of documents currently controlled by the app
	private IdentityVector docs = new IdentityVector();
	// The list of document types the app can controll
	private DocumentTypeInfo[] docInfo;
	// Flag indicating if new documents should have a gui
	private boolean isGUIFlag = true;
	// Flag that specifies behavior when last documet closes
	private boolean quitAfterLastDocumentClosed = true;

	/**
	 * Create a new AppMain that controls documents of the
	 * specified types.
	 */
	public AppMain() {
		this.docInfo = new DocumentTypeInfo[0];
	}

	/**
	 * Create a new AppMain that controls documents of the
	 * specified types.
	 *
	 * @param docInfo TypeInfo for the document types
	 * the application can create and control.
	 */
	public AppMain(DocumentTypeInfo docInfo) {
		if (docInfo == null) throw new IllegalArgumentException();
		docInfo.verify();
		this.docInfo = new DocumentTypeInfo[1];
		this.docInfo[0] = docInfo;
	}

	/**
	 * Create a new AppMain that controls documents of the
	 * specified types.
	 *
	 * @param docInfo TypeInfo's for the document types
	 * the application can create and control. The
	 * first item in the list is concidered to be the
	 * default document type used by createNewDocument.
	 */
	public AppMain(DocumentTypeInfo[] docInfo) {
		if (docInfo == null) throw new IllegalArgumentException();

		for (int i = 0; i < docInfo.length; i++) {
			if (docInfo[i] == null) throw new IllegalArgumentException();
			docInfo[i].verify();
		}
		this.docInfo = docInfo;
	}

	/**
	 * Return true if application is a GUI application.
	 */
	public boolean isGUIApp() {
		return isGUIFlag;
	}

	/**
	 * Turn the application GUI on or off.	The new setting does not
	 * affect documents that are already managed by the application,
	 * it only affects applications that are passed to addDocument.
	 *
	 * @param isGUI true will cause documents passed to addDocument to
	 * create a gui.
	 */
	public void setGUIApp(boolean isGUI) {
		isGUIFlag = isGUI;
	}

	/**
	 * Return a list of TypeInfos for the types of documents
	 * that this document can create and control
	 */
	public DocumentTypeInfo[] getDocInfo() {
		return docInfo.clone();
	}

	/**
	 * Put a document under the application's control.	The
	 * AppMain adds the document to its internal document
	 * list and calls the document's open method.
	 *
	 * @param d the document
	 * @return true if the document was successfully opened.
	 */
	public void addDocument(final Document d)
			throws Document.WouldNotOpenException {
		if (d == null) throw new IllegalArgumentException();
		try {
			if (!docs.contains(d)) {
				docs.addElement(d);
				d.open(this);
			}
		} catch (Document.WouldNotOpenException e) {
			docs.removeElement(d);
			throw e;
		}
	}

	/**
	 * Put a document under the application's control.	The
	 * AppMain adds the document to its internal document
	 * list and calls the document's open method.
	 *
	 * @param d the document
	 * @return true if the document was successfully opened.
	 */
	public final Document addDocument(Object model)
			throws Document.WouldNotOpenException {
		if (model == null) throw new IllegalArgumentException();
		try {
			Document d = new Document(model);
			addDocument(d);
			return d;
		} catch (StorageFactory.CouldNotCreateStorageException e) {
			throw new Document.WouldNotOpenException(e);
		}
	}

	/**
	 * Put a document under the application's control.	The
	 * AppMain adds the document to its internal document
	 * list and calls the document's open method.
	 *
	 * @param d the document
	 * @return true if the document was successfully opened.
	 */
	public final Document addDocument(DocumentStorage storage)
			throws Document.WouldNotOpenException, IOException {
		if (storage == null) throw new IllegalArgumentException();
		Document d = new Document(storage);
		addDocument(d);
		return d;
	}

	/**
	 * Remove a document from the AppMain's control.  The
	 * documents 'close' method is called and the document
	 * is removed from the AppMain's internal document list.
	 */
	public void removeDocument(Document d) {
		if (d == null) throw new IllegalArgumentException();
		if (docs.contains(d)) {
			docs.removeElement(d);
			d.close();
			if (docs.size() == 0) {
				handleLastDocumentClosed();
			}
		}
	}

	/**
	 * Remove all documents from the AppMain's internal list.
	 * This is equivalent to calling removeDocument on each
	 * document controlled by the AppMain.
	 */
	public final void removeAllDocuments() {
		while (docs.size() != 0) {
			removeDocument((Document) docs.elementAt(0));
		}
	}

	/**
	 * Return an enumeration of the AppMain's internal
	 * document list.
	 */
	public Enumeration documents() {
		return docs.elements();
	}

	/**
	 * Return the number of documents.
	 */
	public int getDocumentCount() {
		return docs.size();
	}

	/**
	 *
	 */
	public boolean getQuitAfterLastDocumentClosed() {
		return quitAfterLastDocumentClosed;
	}

	/**
	 *
	 */
	public void setQuitAfterLastDocumentClosed(boolean quit) {
		quitAfterLastDocumentClosed = quit;
	}

	/**
	 * notify listeners and close application
	 */
	protected void handleLastDocumentClosed() {
		allClosedNotifier.notifyListeners();
		if (getQuitAfterLastDocumentClosed()) {
			System.exit(0);
		}
	}
}

