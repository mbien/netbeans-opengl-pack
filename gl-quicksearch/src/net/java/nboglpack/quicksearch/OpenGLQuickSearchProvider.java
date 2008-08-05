/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nboglpack.quicksearch;

/**
 *
 * @author mbien
 */
public class OpenGLQuickSearchProvider extends AbstractQuickSearchProvider {
    
    private final static String base = "http://www.opengl.org/sdk/docs/man/xhtml/";

    public OpenGLQuickSearchProvider() {
        super(base + "index.html");
    }
    
    @Override
    public String filter(String href, String name) {
        //ignore inter page links
        if(href.charAt(0) == '#')
            return null;
        return base+href;
    }

}
