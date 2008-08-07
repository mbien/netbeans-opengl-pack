/*
 * Created on 5. August 2008, 02:24
 */

package net.java.nboglpack.quicksearch;

/**
 * ATI extentions quicksearch provider.
 * @author Michael Bien
 */
public class ATIQuickSearchProvider extends AbstractWebPageQuickSearchProvider {
    
    /**
     * allow only links starting with those urls.
     */
    private static final String[] urls = new String[] {
        "/developer/sdk/RadeonSDK/Html/Info/Extensions/",
        "http://oss.sgi.com/projects/ogl-sample/registry/ARB/"
    };

    public ATIQuickSearchProvider() {
        super("http://ati.amd.com/developer/sdk/radeonSDK/html/info/Prog3D.html");
        
    }

    
    @Override
    String filter(String href, String name) {
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            if(href.startsWith(url)) {
                // check if we got a releative link
                if(href.charAt(0) == '/') 
                    href = "http://ati.amd.com"+href;
                return href;
            }
        }
        
        return null;
    }

}
