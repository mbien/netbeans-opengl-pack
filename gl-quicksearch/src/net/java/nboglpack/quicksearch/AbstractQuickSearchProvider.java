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
 * Abstract Quicksearch provider.
 * @author Michael Bien
 */
public abstract class AbstractQuickSearchProvider implements SearchProvider {
    
    /**
     * Pattern to find hyperlinks and group them into attributes and name part.
     */
    private final static Pattern linkPattern = Pattern.compile("<a([^>]+)>([^<]+)</a>", Pattern.CASE_INSENSITIVE);

    /**
     * Pattern to find the value of the 'href' attribute.
     */
    private final static Pattern hrefPattern = Pattern.compile("href\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

    private final ArrayList<SearchItem> items;
    
    
    public AbstractQuickSearchProvider(String url) {
        
        items = new ArrayList<SearchItem>(128);
        
        try {
            harvest(new URL(url));
        } catch (MalformedURLException ex) {
            Logger.getLogger(AbstractQuickSearchProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    private final void harvest(URL url) {
        
        try {
            // download content
            BufferedReader content = new BufferedReader(new InputStreamReader(url.openStream()));
            char[] buffer = new char[512];
            StringBuilder sb = new StringBuilder();
            while(content.read(buffer) != -1){
                sb.append(buffer);
            }
            
            // harvest everything usefull
            Matcher linkMatcher = linkPattern.matcher(sb);
            
            // find <a ></a>
            while(linkMatcher.find()) {
                
                String attributes = linkMatcher.group(1);
                Matcher hrefMatcher = hrefPattern.matcher(attributes);
                String name = linkMatcher.group(2);
                
                // find href and link name
                if(hrefMatcher.find()) {
                    
                    String href = hrefMatcher.group(1);
                    
                    try{
                        href = filter(href, name);
//                        System.out.println(href);
                        if(href != null) {
                            items.add(new SearchItem(name, href));
                        }
                    }catch(MalformedURLException ex) {
                        Logger.getLogger(AbstractQuickSearchProvider.class.getName()).log(Level.WARNING, "unable to assamble valid URL", ex);
                    }
                    
                }else{
                    Logger.getLogger(AbstractQuickSearchProvider.class.getName()).log(Level.WARNING, 
                            "no href found in attributes:\n"+attributes+"\nof hyplerlink: "+name);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AbstractQuickSearchProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        items.trimToSize();
    }
    
    /**
     * Returns a valid link or null computed from the href attribute and the link name.
     */
    abstract String filter(String href, String name);
    
    
    /**
     * Method is called by infrastructure when search operation was requested.
     * Implementors should evaluate given request and fill response object with
     * apropriate results
     *
     * @param request Search request object that contains information what to search for
     * @param response Search response object that stores search results. 
     * Note that it's important to react to return value of SearchResponse.addResult(...) method
     * and stop computation if false value is returned.
     */
    public void evaluate(SearchRequest request, SearchResponse response) {

        // handle multi token search properly (everything case insensitive)
        String[] token = request.getText().toLowerCase().split("\\s+");
        
        for (SearchItem searchItem : items) {
            
            for (int i = 0; i < token.length; i++) {
                if(searchItem.nameLC.contains(token[i])) {
                    boolean doContinue = response.addResult(searchItem, searchItem.name);
                    if(!doContinue)
                        break;
                }
            }
        }
        
    }
    
    /**
     * Item which may fit to the search string. Openes URL in browser when selected.
     */
    private class SearchItem implements Runnable {
        
        private final URL url;
        private final String name;
        private final String nameLC;
        
        public SearchItem(String name, String url) throws MalformedURLException {
            this.name = name;
            this.nameLC = name.toLowerCase();
            this.url = new URL(url);
        }

        public void run() {
            URLDisplayer.getDefault().showURL(url);
        }
        
    }
    
    
}
