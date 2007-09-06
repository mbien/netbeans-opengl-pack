/*
 * UniqueCodeElementCollection.java
 *
 * Created on May 25, 2007, 4:40 PM
 */

package net.java.nboglpack.visualdesigner.shader.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Samuel Sperling
 */
public class UniqueCodeElementCollection {
    
    private IShaderCodeExporter exporter;
    private HashMap<UniqueCodeElement, String> elements = new HashMap<UniqueCodeElement, String>();
    private HashSet<String> reservedWords = new HashSet<String>();

//    private HashMap<UniqueCodeElement, String> vertexElements = new HashMap<UniqueCodeElement, String>();
//    private HashMap<UniqueCodeElement, String> fragmentElements = new HashMap<UniqueCodeElement, String>();
    
    /** Creates a new instance of UniqueCodeElementCollection */
    public UniqueCodeElementCollection(IShaderCodeExporter exporter, String[] reservedWords) {
        this.exporter = exporter;
        for (String word : reservedWords)
            this.reservedWords.add(word);
    }
//    
//    public String addVertexElement(UniqueCodeElement element) {
//        String vertexName = element.getElementName();
//        int i = 2;
//        while (this.vertexElements.containsValue(vertexName))
//            vertexName = element.getElementName() + i++;
//        element.setVertexName(vertexName);
//        this.vertexElements.put(element, vertexName);
//        return vertexName;
//    }
//    
//    public String addFragmentElement(UniqueCodeElement element) {
//        String fragmentName = element.getElementName();
//        int i = 2;
//        while (this.fragmentElements.containsValue(fragmentName))
//            fragmentName = element.getElementName() + i++;
//        element.setFragmentName(fragmentName);
//        this.fragmentElements.put(element, fragmentName);
//        return fragmentName;
//    }
//    
//    public String getElementsVertexName(UniqueCodeElement element) {
//        return vertexElements.get(element);
//    }
//    
//    public String getElementsFragmentName(UniqueCodeElement element) {
//        return fragmentElements.get(element);
//    }
//    public String getElementsName(UniqueCodeElement element, boolean isCurrentActivityFragment) {
//        if (isCurrentActivityFragment)
//            return getElementsFragmentName(element);
//        else
//            return getElementsVertexName(element);
//    }
    
    public UniqueCodeElement addElement(UniqueCodeElement element) throws ExportingExeption {
        UniqueCodeElement existingElement = findElement(element);
        if (existingElement != null) return existingElement;
        
        String elementName = element.getElementName();
        int i = 2;
        while (elements.containsValue(elementName) || reservedWords.contains(elementName))
            elementName = element.getElementName() + i++;
        element.setUniqueName(elementName);
        this.elements.put(element, elementName);
        return element;
    }
    
    public Set<UniqueCodeElement> getValues() {
        return elements.keySet();
    }
    
    public String getElementsName(UniqueCodeElement element) {
        return elements.get(element);
    }

    protected IShaderCodeExporter getExporter() {
        return exporter;
    }

    public UniqueCodeElement findElement(UniqueCodeElement element) {
        for (UniqueCodeElement uniqueCodeElement : elements.keySet()) {
            if (element.hasSameElement(uniqueCodeElement)) return uniqueCodeElement;
        }
        return null;
    }
    
}
