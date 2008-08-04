/*
 * Created on 4. August 2008, 22:51
 */

package net.java.nboglpack.quicksearch;

/**
 *
 * @author Michael Bien
 */
class OpenGLSpecHarvester implements LinkHarvester {

    private final String base;
    
    OpenGLSpecHarvester(String baseURL) {
        this.base = baseURL;
    }

    public String harvest(String href, String name) {
        //ignore inter page links
        if(href.charAt(0) == '#')
            return null;
        return base+href;
    }

}
