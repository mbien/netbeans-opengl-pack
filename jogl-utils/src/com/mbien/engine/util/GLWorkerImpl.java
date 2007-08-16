/*
 * Created on 28. March 2007, 10:46
 * 
 */
package com.mbien.engine.util;

import java.util.Stack;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLPbuffer;

/**
 * @author Michael Bien
 */
public class GLWorkerImpl {
    
 private final Stack<GLRunnable> work;
 private final GLAutoDrawable drawable;
    
    
    /** Creates a new instance of GLWorker */
    public GLWorkerImpl() {
        this(null);
    }
    
    public GLWorkerImpl(GLAutoDrawable autoDrawable) {
        
        if(autoDrawable == null) {
            GLDrawableFactory factory = GLDrawableFactory.getFactory();
            GLCapabilities c = new GLCapabilities();
            GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();

            drawable = factory.createGLPbuffer(c, chooser, 1, 1, null);
        }else{
            drawable = autoDrawable;
        }
        
        drawable.addGLEventListener(new Renderer());
        work = new Stack<GLRunnable>();
    }
    
    public void work() {
        drawable.display();
    }
    
    public void work(GLRunnable runnable) {
        addWork(runnable);
        work();
    }
    
    public void destroy() {
        if(drawable instanceof GLPbuffer)
            ((GLPbuffer)drawable).destroy();
    }

    public void addWork(GLRunnable runnable) {
        work.add(runnable);
    }
    
 private class Renderer implements GLEventListener {
     
    public void init(GLAutoDrawable arg0) {
    }

    public void display(GLAutoDrawable arg0) {
        GLContext context = GLContext.getCurrent();
        while(!work.empty()) {
            work.pop().run(context);
            Thread.yield();
        }
    }

    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
    }
  }

}
