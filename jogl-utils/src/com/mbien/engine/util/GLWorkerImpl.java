/*
 * Created on 28. March 2007, 10:46
 * 
 */
package com.mbien.engine.util;

import java.util.ArrayDeque;
import java.util.Deque;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLPbuffer;

/**
 * Implementation of an OpenGL worker.
 * @author Michael Bien
 */
public class GLWorkerImpl implements GLWorker {
    
 private final Deque<GLRunnable> work;
 private final GLAutoDrawable drawable;
 
 public static GLWorker DEFAULT;
    
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
        
        drawable.addGLEventListener(new Worker());
        work = new ArrayDeque<GLRunnable>();
        
        if(DEFAULT == null)
            DEFAULT = this;
    }
    
    public static GLWorker getDefault() {
        if(DEFAULT == null)
            DEFAULT = new GLWorkerImpl();
        return DEFAULT;
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

    
 private final class Worker implements GLEventListener {
     
    public void init(GLAutoDrawable drawable) {
        drawable.getGL().glClearColor(0, 0, 0, 0);
    }

    public void display(GLAutoDrawable drawable) {
        GLContext context = drawable.getContext();
        try{
            while(work.size() > 0) {
                work.pollFirst().run(context);
                Thread.yield();
            }
        }finally{
            drawable.getGL().glClear(GL.GL_COLOR_BUFFER_BIT);
//            drawable.getGL().glFinish();
        }
    }

    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
    }
  }

}
