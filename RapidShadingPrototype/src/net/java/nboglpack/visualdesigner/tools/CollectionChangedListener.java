/*
 * CollectionChangedListener.java
 *
 * Created on May 9, 2007, 2:16 PM
 */

package net.java.nboglpack.visualdesigner.tools;

import java.util.EventListener;

/**
 * Interface for Eventlisteners that want to be notified about changes in a
 * collection.
 *
 * @author Samuel Sperling
 */
public interface CollectionChangedListener<E> extends EventListener {
    public void itemAdded(E element);
    public void itemRemoved(E element);
    public void itemChanged(E element);
}
