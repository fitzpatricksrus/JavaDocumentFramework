/*
 * $Id: Document.java /main/21 1998/08/07 13:56:18 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/Document.java $
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

import java.io.IOException;
import java.util.Enumeration;

/**
 * A document associates a Serializable with DocumentStorage.
 * It provides notification for major document related events.
 * A document need not be created with both a data model and
 * document storage.  Any missing parts are generated using a
 * TypeInfo object.
 * <p>
 * A document keeps a list of the "views" that depend on its
 * data.  A "view" can be any object that relies on the document
 * but is typically a GUI display or some kind, often a window.
 * When a view is created, it should add itself to the documents
 * list of dependent views using addView() and remove itself using
 * removeView() when it no longer needs the document.
 * <p>
 * A Document should be opened before it is used.  A document is
 * opened by adding it to an AppMain.  If the AppMain is a GUI
 * application, the document will create a GUI when it is opened.
 * <p>
 * When a document is no longer needed, it should be removed from
 * its AppMain.
 * <p>
 * Note: a document is purely a runtime construct.  It is not
 * meant to be persistent.  See the DocumentStorage class for
 * details.
 *
 * @author jfitzpat
 * @version $Version$
 */
public class Document {
	/** Notifier for notification when the document is opened */
	public final Notifier openNotifier = new Notifier();
	/** Notifier for notification when the document is closed */
	public final Notifier closeNotifier = new Notifier();
	/** Notifier for notification when the document is saved */
	public final Notifier saveNotifier = new Notifier();
	/** Notifier for notification when the document's data model is replaced */
	public final Notifier dataNotifier = new Notifier();
	/** Notifier for notification a view is added to the document */
	public final Notifier viewAddedNotifier = new Notifier();
	/** Notifier for notification when a view is removed from this document */
	public final Notifier viewRemovedNotifier = new Notifier();
	/** Notifier for notification when a view is removed from this document */
	public final Notifier storageNotifier = new Notifier();
	//the application currently controlling this document
	private AppMain app;
	// The undo history for this document.
	private GUIUndoManager history = new GUIUndoManager();
	// The document data
	private Object data;
	// The documents storage
	private DocumentStorage store;
	// The list of views open on this document
	private IdentityVector views = IdentityVector.newIdentitySet();

	/**
	 * Create a new document.  The document will synthesize document
	 * storage.
	 *
	 * @param model the document data model
	 */
	public Document(DocumentStorage store) throws IOException {
		if (store == null) throw new IllegalArgumentException();
		setStorage(store);
		this.data = store.read();
	}

	/**
	 * Create a new document.  The document will synthesize document
	 * storage.
	 *
	 * @param model the document data model
	 */
	public Document(Object model) throws
			StorageFactory.CouldNotCreateStorageException {
		if (model == null) throw new IllegalArgumentException();
		this.data = model;
		setStorage(StorageFactory.createDocumentStorage(model));
	}

	/**
	 * Return this document's data model.
	 */
	public Object getDataModel() {
		return data;
	}

	/**
	 * Set this document's data model and generate a notification.
	 */
	public void setDataModel(Object model) throws
			StorageFactory.CouldNotCreateStorageException {
		if (model == null) throw new IllegalArgumentException();
		if (!store.canStore(model)) {
			setStorage(StorageFactory.createDocumentStorage(model));
		}
		this.data = model;
		dataNotifier.notifyListeners(data);
	}

	/**
	 * Get the document storage for this document.  If this
	 * document does not have DocumentStorage, one is created
	 * for it.
	 */
	public DocumentStorage getStorage() {
		return store;
	}

	/**
	 * Set the document storage used by this document
	 */
	public void setStorage(final DocumentStorage store) {
		this.store = store;
		store.storageChanged.addListener(
				new NotifierListener() {
					public void ping() {
						handleStorageChanged();
					}
				}
		);
		storageNotifier.notifyListeners();
	}

	/**
	 * Return the undo history for this document.
	 */
	public GUIUndoManager getHistory() {
		return history;
	}

	/**
	 * Return the application currently controlling this document
	 */
	public AppMain getApp() {
		return app;
	}

	/**
	 * Notify clients that the document has been opened.  This
	 * method is called when this document is added to
	 * an AppMain.  If the application is a GUI app, a view
	 * is opened on the document;
	 *
	 * @return true if the document was opened successfully.
	 */
	protected void open(AppMain newApp) throws WouldNotOpenException {
		if (newApp == null) throw new IllegalArgumentException();
		if (app != null) {
			app.removeDocument(this);
		}
		app = newApp;
		if (app.isGUIApp()) {
			try {
				openNewView();
			} catch (GUIFactory.CouldNotCreateGUIException e) {
				throw new WouldNotOpenException(e);
			}
		}
		openNotifier.notifyListeners();
	}

	/**
	 * Notify clients that the document has been closed.  This
	 * method is called when this document is removed from an AppMain.
	 */
	protected void close() {
		removeAllViews();
		//close storage
		getStorage().close();
		//go home
		app = null;
		closeNotifier.notifyListeners();
	}

	/**
	 * Save the contents of the document.
	 */
	public void save() throws IOException {
		getStorage().write(data);
		saveNotifier.notifyListeners();
	}

	/**
	 * Return a user presentable name for this document.
	 */
	public String getPresentationName() {
		return getStorage().getPresentationName();
	}

	/**
	 * Open a new view of this document.
	 */
	public void openNewView() throws GUIFactory.CouldNotCreateGUIException {
		Object view = StandardGUIFactory.createRootGUIFor(this);
		addView(view);
	}

	/**
	 * Open a new view of this document using the specified gui factory.
	 */
	public void openNewView(String guiFactoryName)
			throws GUIFactory.CouldNotCreateGUIException {
		Object view = StandardGUIFactory.createRootGUIFor(this, guiFactoryName);
		addView(view);
	}

	/**
	 * Inform the document that a new view has been opened on the document
	 */
	public void addView(Object view) {
		views.addElement(view);
		viewAddedNotifier.notifyListeners(view);
	}

	/**
	 * Inform the document that an existing view has been closed
	 */
	public void removeView(Object view) {
		views.removeElement(view);
		viewRemovedNotifier.notifyListeners(view);
	}

	public void removeAllViews() {
		//remove all open views
		while (views.size() != 0) {
			removeView(views.elementAt(0));
		}
	}

	/**
	 * Return an Enumeration of the open views for this document
	 */
	public Enumeration getViews() {
		return views.elements();
	}

	/**
	 * Return an Enumeration of the open views for this document
	 */
	public int getViewCount() {
		return views.size();
	}

	/**
	 * Return true if the specified view is one of the
	 * document's views
	 */
	public boolean containsView(Object view) {
		return views.contains(view);
	}

	//Notify listeners when the storage has changed.
	private void handleStorageChanged() {
		storageNotifier.notifyListeners();
	}

	/**
	 * Thrown when a document fails to open properly
	 */
	public static class WouldNotOpenException extends Exception {
		public final Exception cause;

		public WouldNotOpenException(Exception cause) {
			super(ExceptionMessages.DOCUMENT_WOULDNT_OPEN);
			this.cause = cause;
		}
	}
}
