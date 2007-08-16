/*
 * GLWorkerImpl.java
 * 
 * Created on 16.08.2007, 14:35:07
 * 
 */

package net.java.nbjoglpack.joglutils;

import javax.media.opengl.GLDrawableFactory;

/**
 *
 * @author Michel Bien
 */
public class NBGLWorkerImpl {

 private final com.mbien.engine.util.GLWorkerImpl worker;
    
 public NBGLWorkerImpl() {
     
     if(GLDrawableFactory.getFactory().canCreateGLPbuffer()){
         worker = new com.mbien.engine.util.GLWorkerImpl();
     }else{
         //fallback mode
         worker = null;
     }
         
 }
    
}
