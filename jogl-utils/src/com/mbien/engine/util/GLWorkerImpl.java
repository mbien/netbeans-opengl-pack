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
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

/**
 * Implementation of an OpenGL worker.
 * @author Michael Bien
 */
public class GLWorkerImpl implements GLWorker {
    
 private final Deque<GLRunnable> work;
 private final GLAutoDrawable drawable;
 
 public static GLWorker DEFAULT;
    
    /** Creates a new instance of GLWorker */
    public GLWorkerImpl(GLProfile profile) throws GLException {

        GLDrawableFactory factory = GLDrawableFactory.getFactory(profile);
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();

        drawable = factory.createGLPbuffer(new GLCapabilities(profile), chooser, 1, 1, null);
        work = new ArrayDeque<GLRunnable>();

        init();
    }
    
    public GLWorkerImpl(GLAutoDrawable autoDrawable) {
        
        if(autoDrawable == null) {
            throw new NullPointerException();
        }

        drawable = autoDrawable;
        work = new ArrayDeque<GLRunnable>();

        init();
    }

    private final void init() {
        drawable.addGLEventListener(new Worker());
        if(DEFAULT == null)
            DEFAULT = this;
    }
    
    public static GLWorker getDefault() {
        if(DEFAULT == null)
            DEFAULT = new GLWorkerImpl(GLProfile.getDefault());
        return DEFAULT;
    }
    
    public synchronized void work() {
        if(work.size() > 0)
            drawable.display();
    }
    
    public synchronized void work(GLRunnable runnable) {
        addWork(runnable);
        work();
    }
    
    public synchronized void destroy() {
        drawable.destroy();
    }

    public synchronized void addWork(GLRunnable runnable) {
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

    public void dispose(GLAutoDrawable arg0) {
    }
  }

}
