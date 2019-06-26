/*
 * $Source: /project/wamali/code/com/mot/wamali/guitools/tests/ModelTest_GUI_UT.java $
 * $Id: ModelTest_GUI_UT.java /main/11 1998/08/07 09:35:41 jfitzpat $
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
package us.cownet.docfw.guitools.tests;

import us.cownet.docfw.guitools.AppMain;
import us.cownet.docfw.guitools.Document;
import us.cownet.docfw.guitools.DocumentTypeInfo;
import us.cownet.docfw.guitools.Notifier;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * The ModelTest_GUI_UT class is simply a test class for the guitools package.
 * The model class simply holds an int value.  The model has an associated
 * GUIFactory class called ModelTest_GUI_UT_GUI that creates a gui for
 * the model.  Note the the model knows nothing of the gui class.  The
 * association between the two classes is made by the GUIFactory class.
 *
 * @author jfitzpat
 * <p>
 * Screen Layout
 * <p>
 * Are components laid out as described in the design?
 * Are menu items in the menu bar and popup menu as described in the design?
 * Is there any spelling mistakes in some components such as labels?
 * Is the choice of components used appropriate?, ie do they reflect what
 * was expected from the design?
 * Will resizing the GUI compromise the screen layout?
 * <p>
 * Initialisation
 * <p>
 * Apart from the laying out of the components, some initialisation of the
 * components may be required:
 * Is component XX appropriately populated
 * Are there any components which should be disabled
 * <p>
 * Clean Up
 * <p>
 * Before closing a screen, some clean up actions may need to be performed:
 * Was there a prompt to indicate updates in the GUI were not saved?
 * <p>
 * Events
 * <p>
 * Usually events are triggered by selecting a menu item or buttons. All
 * responses to events listed in the design needs to be tested to see that they
 * are executed when their corresponding event occurs.
 * Does selecting [File -> Close] cause the GUI to disappear
 * When the cursor moves into the area occupied by component XX, does this
 * cause the component to be highlighted?
 * <p>
 * States
 * <p>
 * New - A document is new if it has never been saved or modified.
 * Unsaved - A document is Unsaved if it has never been saved and its
 * state is different from its state when it was New.
 * Dirty - A document is dirty if the state of the document no
 * longer matches its last saved state.
 * Clean - A document is clean if the document matches it last save
 * state.
 * @version $Version$
 */
public class ModelTest_GUI_UT implements Serializable {
	/* This static variable tells the application
	 * what type of documents to "new" and "open".
	 * A Document.TypeInfo is passed to the AppMain
	 * when it is created.  If the app should handle
	 * several types of documents (with different models),
	 * an array of Document.TypeInfos can be passed
	 * to the AppMain.
	 */
	private static final DocumentTypeInfo[] info = {
			new DocumentTypeInfo(
					"Button",    //user visable document type name
					//ex. "New Dummy Document" in File menu

					"us.cownet.docfw.guitools.tests.ModelTest_GUI_UT"),
			//the model class
			new DocumentTypeInfo(
					"Text",
					"us.cownet.docfw.guitools.textdatamodel.TextDataModel")
	};
	/** The model data is a single int value */
	private int label = 0;
	/**
	 * The changeNotifier is used to notify listeners of
	 * changes in the model.  Note that this field is
	 * set to null when the object is deserialized because
	 * it is transient.  DummyModel overrides the
	 * readObject method to initialize this field when
	 * the model is deserialized
	 */
	private transient Notifier changeNotifier;

	public ModelTest_GUI_UT() {
		//create the notifier used for change notification
		changeNotifier = new Notifier();
	}

	/**
	 * The main function of an application creates an AppMain instance,
	 * passing it a list of the document types it should handle.
	 */
	public static void main(String[] args) {
		try {

			// Force SwingApplet to come up in the System L&F
			String laf = UIManager.getSystemLookAndFeelClassName();
			try {
				//UIManager.setLookAndFeel(laf);
				// If you want the Cross Platform L&F instead, comment out the above line and
				// uncomment the following:
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException exc) {
				System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
			} catch (Exception exc) {
				System.err.println("Error loading " + laf + ": " + exc);
			}

			//create an application instance passing it
			//a list of DocumentTypeInfo objects
			AppMain app = new AppMain(info);
			//create a new model and tell the application
			//to open a new document for it.
			app.addDocument(new ModelTest_GUI_UT());
			//and let it go.
		} catch (IllegalArgumentException e) {
			//this exception is thrown if the class name
			//specified in the DocumentTypeInfo passed to the
			//the application specifies a class that couldn't be
			//used.  Either it doesn't exist (the class name is
			//incorrect) or it isn't public.
		} catch (Document.WouldNotOpenException e) {
		}
	}

	//I override readObject so that I can initialize
	//the changeNotifier when the model is deserialized.
	//readObject and writeObject must be overriden as
	//a pair, so I override writeObject as well.
	private void writeObject(java.io.ObjectOutputStream s)
			throws IOException {
		//write the model's data to the stream.
		s.defaultWriteObject();
		s.writeInt(label);
	}

	//This method is overriden so changeNotifier can
	//be initialized when the model is deserialized from
	//a file.
	private void readObject(java.io.ObjectInputStream s)
			throws IOException, ClassNotFoundException {
		//initialize the notifier
		s.defaultReadObject();
		changeNotifier = new Notifier();
		//read in the model's data
		label = s.readInt();
	}

	/**
	 * Return the notifier for change notification.  The
	 * view connects to this notifier for change notification
	 */
	public Notifier getNotifier() {
		return changeNotifier;
	}

	/** return the model data */
	public int getLabel() {
		return label;
	}

	/**
	 * Set the model data to a new value.  This is an application
	 * specific function.
	 */
	public void setLabel(int l) {
		label = l;
		//don't forget to notify listeners that the
		//model has chaned
		changeNotifier.notifyListeners();
	}

}
