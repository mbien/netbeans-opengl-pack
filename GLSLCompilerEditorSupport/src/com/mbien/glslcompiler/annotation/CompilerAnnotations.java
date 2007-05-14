package com.mbien.glslcompiler.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import org.openide.cookies.LineCookie;
import org.openide.loaders.DataObject;
import org.openide.text.Line;

/**
 * Created on 15. March 2007, 16:10
 * @author Michael Bien
 */
public class CompilerAnnotations {
 
 private final static HashMap<DataObject, ArrayList<CompilerAnnotation>> annotationMap 
                = new HashMap<DataObject, ArrayList<CompilerAnnotation>>();
 

    
    public static void addAnnotation(DataObject dao, CompilerAnnotation.AnnotationType type, String msg, int lineNumber) {
        
        Line.Set lines = dao.getCookie(LineCookie.class).getLineSet();

        if(lineNumber < 1 || lineNumber >= lines.getLines().size())
            return;
                
        ArrayList<CompilerAnnotation> annotations;
        if(!annotationMap.containsKey(dao)) {
            annotations = new ArrayList<CompilerAnnotation>();
            annotationMap.put(dao, annotations);
        }else{
            annotations = annotationMap.get(dao);
        }
        
        CompilerAnnotation annotation = new CompilerAnnotation(type, msg);
        Line line = lines.getCurrent(lineNumber-1);
        char[] text = line.getText().toCharArray();
        int start;
        int end;
        for(start = 0; start < text.length; start++)
            if(text[start] != ' ')
                break;
        
        
        for(end = text.length-1; end > start; end--)
            if(text[end] != ' ')
                break;
        
        annotation.attach(line.createPart(start, end-start));
        annotations.add(annotation);
        
    }
    
    public static void clearAll() {
        for (ArrayList<CompilerAnnotation> list : annotationMap.values()) 
            for (CompilerAnnotation annotation : list) 
                annotation.detach();
        annotationMap.clear();
    }
    
    public static void removeAnnotations(DataObject dao) {
        ArrayList<CompilerAnnotation> annotations = annotationMap.remove(dao);
        if(annotations != null)
            for (CompilerAnnotation compilerAnnotation : annotations)
                compilerAnnotation.detach();
    }        

}
