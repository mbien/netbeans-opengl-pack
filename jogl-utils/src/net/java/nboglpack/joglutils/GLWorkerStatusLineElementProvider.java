/*
 * GLWorkerStatusLineProvider.java
 * 
 * Created on 16.08.2007, 23:22:40
 * 
 */

package net.java.nboglpack.joglutils;

import java.awt.Component;
import javax.swing.JPanel;
import org.openide.awt.StatusLineElementProvider;

/**
 *
 * @author Michael Bien
 */
public class GLWorkerStatusLineElementProvider implements StatusLineElementProvider {

  static JPanel component = new JPanel();
    
    @Override
    public Component getStatusLineElement() {
        return component;
    }
    
}
