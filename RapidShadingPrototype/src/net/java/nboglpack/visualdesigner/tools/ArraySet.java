/*
 * ArraySet.java
 *
 * Created on 28. April 2006, 13:12
 *
 */

package net.java.nboglpack.visualdesigner.tools;

/**
 * Diese Klasse bietet die Möglichkeit eine Menge von Objekten zu bilden.
 * @author Samuel Sperling
 */
public class ArraySet {
    
    /**
     * Liste aller Objekte dieses Sets.
     */
    public Object[] items;
    
    /** Creates a new instance of ArraySet */
    public ArraySet() {
    }
    
    /**
     * Fügt dem Set ein Objekt an.
     * Es wird nur aufgenommen, wenn es nicht bereits in dem Set vorhanden ist.
     * @param item Objekt, welches in das Set eingefügt werden soll.
     * @return True, wenn das Objekt eingefügt werden konnte.
     * False, wenn das Objekt bereits in dem Set vorhanden war und es daher nicht mehr
     * eingefügt werden musste.
     */
    public boolean  add(Object item) {
        if (this.items == null) {
            this.items = new Object[] {item};
        } else {
            if (!contains(item)) {
                // ein um 1 element größeres Array erzeugen
                Object[] tmpArray = new Object[this.items.length + 1];
                
                // Daten kopieren
                for (int i = 0; i < this.items.length; i++) {
                    tmpArray[i] = this.items[i];
                }
                tmpArray[this.items.length] = item;
                this.items = tmpArray;
                tmpArray = null;
            } else {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Testen, ob sie das angegebene Element schon in dem Set befindet.
     * Die Gleichheitsüberprüfung ist wahr, wenn beide Objekte auf das selbe
     * Objekt zeigen, nicht wenn es den selben Inhalt hat.
     * @return True, wenn das Objekt bereits in dem Set vorhanden ist.
     *          False, wenn das Objekt noch nicht in dem Set vorhanden ist.
     * @param item Objekt welches nach welchem in dem Set gesucht wird.
     */
    public boolean contains(Object item) {
        // bewusst nicht contains()! Da contains mit equals prüft.
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i] == item)
                return true;
        }
        return false;
    }
}