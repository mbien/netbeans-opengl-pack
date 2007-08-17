/*
 * GLWorkerStatusLineProvider.java
 * 
 * Created on 16.08.2007, 23:22:40
 * 
 */

package net.java.nboglpack.joglutils;

import java.awt.Component;
import org.openide.awt.StatusLineElementProvider;

/**
 *
 * @author Michael Bien
 */
public class GLWorkerStatusLineElementProvider implements StatusLineElementProvider {

  static Component component = null;
    
    @Override
    public Component getStatusLineElement() {
        return component;
    }
    
}
