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
public class GLWorker {
    
 private final Stack<GLRunnable> work;
 private final GLPbuffer pbuffer;
    
    
    /** Creates a new instance of GLWorker */
    public GLWorker() {
        
        this.work = new Stack<GLRunnable>();
        
        GLDrawableFactory factory = GLDrawableFactory.getFactory();
        GLCapabilities c = new GLCapabilities();
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();
        pbuffer = factory.createGLPbuffer(c, chooser, 1, 1, null);
        pbuffer.addGLEventListener(new Renderer());
    }
    
    public void work() {
        pbuffer.display();
    }
    
    public void work(GLRunnable runnable) {
        addWork(runnable);
        work();
    }
    
    public void destroy() {
        pbuffer.destroy();
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
