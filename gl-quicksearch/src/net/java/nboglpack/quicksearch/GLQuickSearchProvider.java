/*
 * Created on 4. August 2008, 21:51
 */
package net.java.nboglpack.quicksearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.spi.quicksearch.SearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;
import org.openide.awt.HtmlBrowser.URLDisplayer;


/**
 * OpenGL SDK Quicksearch provider.
 * @author Michael Bien
 */
public class GLQuickSearchProvider implements SearchProvider {
    
    /**
     * Pattern to find hyperlinks and group them into attributes and name part.
     */
    private final static Pattern linkPattern = Pattern.compile("<a([^>]+)>([^<]+)</a>", Pattern.CASE_INSENSITIVE);

    /**
     * Pattern to find the value of the 'href' attribute.
     */
    private final static Pattern hrefPattern = Pattern.compile("href\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

    private final ArrayList<SearchItem> openGLitems;
    
    
    public GLQuickSearchProvider() {
        
        openGLitems = new ArrayList<SearchItem>(128);
        
        try {
            String base = "http://www.opengl.org/sdk/docs/man/xhtml/";
            OpenGLSpecHarvester glHarvester = new OpenGLSpecHarvester(base);
            
            URL url = new URL(base+"index.html");
            harvest(url, glHarvester, openGLitems);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GLQuickSearchProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        String page = "http://developer.nvidia.com/object/nvidia_opengl_specs.html";
        
    }
    

    private final void harvest(URL url, LinkHarvester harvester, ArrayList<SearchItem> items) {
        
        try {
            BufferedReader content = new BufferedReader(new InputStreamReader(url.openStream()));
            char[] buffer = new char[512];
            StringBuilder sb = new StringBuilder();
            while(content.read(buffer) != -1){
                sb.append(buffer);
            }
            
            Matcher linkMatcher = linkPattern.matcher(sb);
            
            while(linkMatcher.find()) {
                
                String attributes = linkMatcher.group(1);
                Matcher hrefMatcher = hrefPattern.matcher(attributes);
                String name = linkMatcher.group(2);
                
                if(hrefMatcher.find()) {
                    
                    String href = hrefMatcher.group(1);
                    
                    try{
                        href = harvester.harvest(href, name);
//                        System.out.println(href);
                        if(href != null) {
                            items.add(new SearchItem(name, href));
                        }
                    }catch(MalformedURLException ex) {
                        Logger.getLogger(GLQuickSearchProvider.class.getName()).log(Level.WARNING, "unable to assamble valid URL", ex);
                    }
                    
                }else{
                    Logger.getLogger(GLQuickSearchProvider.class.getName()).log(Level.WARNING, 
                            "no href found in attributes:\n"+attributes+"\nof hyplerlink: "+name);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GLQuickSearchProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Method is called by infrastructure when search operation was requested.
     * Implementors should evaluate given request and fill response object with
     * apropriate results
     *
     * @param request Search request object that contains information what to search for
     * @param response Search response object that stores search results. Note that it's important to react to return value of SearchResponse.addResult(...) method and stop computation if false value is returned.
     */
    public void evaluate(SearchRequest request, SearchResponse response) {
        
        for (SearchItem searchItem : openGLitems) {
            if(searchItem.name.toLowerCase().contains(request.getText().toLowerCase())) {
                boolean doContinue = response.addResult(searchItem, searchItem.name + " [OpenGL.org]");
                if(!doContinue)
                    break;
            }
        }
        
    }
    
    private class SearchItem implements Runnable {
        
        private final URL url;
        private final String name;
        
        public SearchItem(String name, String url) throws MalformedURLException {
            this.name = name;
            this.url = new URL(url);
        }

        public void run() {
            URLDisplayer.getDefault().showURL(url);
        }
        
    }
    
    
}
