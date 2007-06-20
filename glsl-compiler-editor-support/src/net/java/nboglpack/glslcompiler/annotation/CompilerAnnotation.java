package net.java.nboglpack.glslcompiler.annotation;

import org.openide.text.Annotation;

/**
 * Created on 29. March 2007, 01:06
 * @author Michael Bien
 */
public class CompilerAnnotation extends Annotation {
    
 private final AnnotationType type;
 private final String description;
    
    /** Creates a new instance of CompilerAnnotation */
    CompilerAnnotation(AnnotationType type, String description) {
        this.type = type;
        this.description = description;
    }
    
    public String getAnnotationType() {
        return type.type;
    }

    public String getShortDescription() {
        return description;
    }
    
 public enum AnnotationType {
     
     WARNING("net-java-nboglpack-glslcompiler-GlslCompilerWarningAnnotation"),
     ERROR("net-java-nboglpack-glslcompiler-GlslCompilerErrorAnnotation");
     
     private final String type;
             
     private AnnotationType(String type){
         this.type = type;
     }
 }
}
