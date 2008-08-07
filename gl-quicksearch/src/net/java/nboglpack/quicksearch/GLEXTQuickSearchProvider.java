/*
 * Created on 7. August 2008, 18:14
 */

package net.java.nboglpack.quicksearch;

/**
 * OpenGL extentions quicksearch provider for http://www.opengl.org/registry/.
 * @author Michael Bien
 */
public class GLEXTQuickSearchProvider extends WebPageQuickSearchProvider {

    private final static String base = "http://www.opengl.org/registry/";

    public GLEXTQuickSearchProvider() {
        super(base);
    }
    
    @Override
    String filter(String href, String name) {
        System.out.println(href);
        if(href.startsWith("specs/"))
            return base+href;
        else
            return null;
    }

}
