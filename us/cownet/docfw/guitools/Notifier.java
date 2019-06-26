/*
 * $Id: Notifier.java /main/5 1998/08/07 09:35:32 jfitzpat $
 * $Source: /project/wamali/code/com/mot/wamali/guitools/Notifier.java $
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

import java.util.Enumeration;

/**
 * The Notifier class supports simple change notification.
 *
 * @author jfitzpat
 * @version $Version$
 */
public final class Notifier {
	//A list of all listeners
	private IdentityVector listeners = new IdentityVector();
	//A cache of all listeners used to support reentrancy during
	//a notification
	private transient NotifierListener[] cache;

	/**
	 * Add a listener to the notifier list.  If listener
	 * is already known by this notifer, nothing is done.
	 */
	public synchronized void addListener(NotifierListener l) {
		listeners.addElement(l);
		//clear the cache.  It's now invalid
		cache = null;
	}

	/**
	 * Remove the listener from the notifier list.
	 */
	public synchronized void removeListener(NotifierListener l) {
		listeners.removeElement(l);
		//clear the cache.  It's now invalid
		cache = null;
	}

	/**
	 * Remove ALL listeners from the notifier list.
	 */
	public synchronized void removeAllListeners() {
		listeners.removeAllElements();
		//clear the cache.  It's now invalid
		cache = null;
	}

	/**
	 * Return an enumeration of the listeners.
	 */
	public Enumeration getListeners() {
		return listeners.elements();
	}

	/**
	 * Return a count of the listeners.
	 */
	public int getListenerCount() {
		return listeners.size();
	}

	/**
	 * Ping all listeners.
	 */
	public void notifyListeners() {
		notifyListeners(null);
	}

	/**
	 * Ping all listeners.
	 */
	public void notifyListeners(Object message) {
		//we notify using a local cache so that the main listener list
		//can be modified by the listeners themselves during notification.
		//It is not uncommon for a listener to remove itself when
		//is recieves a notification.
		NotifierListener[] ears = getCache();
		for (int i = 0; i < ears.length; i++) {
			ears[i].ping(message);
		}
	}

	//build a cache of the listeners and return it.
	private synchronized NotifierListener[] getCache() {
		if (cache == null) {
			cache = new NotifierListener[listeners.size()];
			listeners.toArray(cache);
		}
		return cache;
	}
}
