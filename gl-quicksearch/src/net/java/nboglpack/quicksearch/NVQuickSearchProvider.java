/*
 * Created on 5. August 2008, 01:24
 */

package net.java.nboglpack.quicksearch;

/**
 * NVIDIA extentions quicksearch provider.
 * @author Michael Bien
 */
public class NVQuickSearchProvider extends AbstractWebPageQuickSearchProvider {
    
    /**
     * allow only links starting with those urls.
     */
    private static final String[] urls = new String[] {
        "http://www.nvidia.com/dev_content/nvopenglspecs/",
        "http://developer.download.nvidia.com/opengl/specs/"
    };

    public NVQuickSearchProvider() {
        super("http://developer.nvidia.com/object/nvidia_opengl_specs.html");
    }
    
    
    
    @Override
    String filter(String href, String name) {
        
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            if(href.startsWith(url)) { // case sensitive, but urls are not...
                return href;
            }
        }
        
        return null;
    }

}
