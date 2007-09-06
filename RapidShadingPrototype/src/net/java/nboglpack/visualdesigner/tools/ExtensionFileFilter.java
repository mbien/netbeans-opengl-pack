/*
 * ExtensionFileFilter.java
 *
 * Created on June 2, 2007, 1:15 PM
 */

package net.java.nboglpack.visualdesigner.tools;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * FileFilter that filters by extension
 *
 * @author Samuel Sperling
 */
public class ExtensionFileFilter extends FileFilter {
    
    private String[] validExtensions;
    private String description;
            
    /** Creates a new instance of ExtensionFileFilter */
    public ExtensionFileFilter(String[] validExtensions, String description) {
        this.validExtensions = validExtensions;
        this.description = description;
    }
    
    /** Creates a new instance of ExtensionFileFilter */
    public ExtensionFileFilter(String validExtensions, String description) {
        this.validExtensions = new String[] {validExtensions};
        this.description = description;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1);
        for (String validExtension : validExtensions) {
            if (extension.equals(validExtension)) return true;
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
    
}
