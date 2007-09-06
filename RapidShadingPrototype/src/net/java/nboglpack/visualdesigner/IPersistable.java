/*
 * IPersistable.java
 *
 * Created on June 2, 2007, 2:14 PM
 */

package net.java.nboglpack.visualdesigner;

import net.java.nboglpack.visualdesigner.PersistanceException;

/**
 *
 * @author Samuel Sperling
 */
public interface IPersistable {
    
    public void saveState(ProjectPersistor saveVisitor) throws PersistanceException;
    public void loadState(ProjectPersistor loadVisitor) throws PersistanceException;
}
