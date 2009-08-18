/*
 * GLWorkerImpl.java
 *
 * Created on 16.08.2007, 14:35:07
 *
 */

package net.java.nboglpack.joglutils;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import com.mbien.engine.util.GLWorkerImpl;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.beans.Beans;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;


import static java.util.logging.Level.*;

/**
 * GLWorker implementation with fallback mode.
 * @author Michel Bien
 */
public class NBGLWorkerImpl implements GLWorker {

    private GLWorker glworker;

    public NBGLWorkerImpl() {

        // try to create a GL3 worker first, fallback to GL2 and finally to the
        // workaround if something wents wrong
        if (GLDrawableFactory.getFactory(GLProfile.getDefault()).canCreateGLPbuffer()) {
            if((glworker = initGLWorker(GLProfile.GL3)) == null)
                if((glworker = initGLWorker(GLProfile.GL2)) == null)
                    glworker = initFallbackWorkerImpl();

        } else {
            glworker = initFallbackWorkerImpl();
        }
        
        GLWorkerImpl.DEFAULT = this;
    }

    private final GLWorker initGLWorker(String p) {
        GLProfile profile = GLProfile.get(p);
        try {
            GLWorker worker = new GLWorkerImpl(profile);
            log().info("created context with profile: "+profile);
            return worker;
        } catch (GLException ex) {
            log().log(INFO, "unable to create GLContext with profile: "+profile, ex);
            return null;
        }

    }

    private final GLWorkerImpl initFallbackWorkerImpl() {

        GLProfile profile = GLProfile.getDefault();
        
        log().info("using GLWorker fallback mode with profile: "+profile);
        
        // fallback mode
        // use a heavy weight drawable if pixel buffers are not supported
        Beans.setDesignTime(false); // TODO designtime = false; workaround
        GLCapabilities caps = new GLCapabilities(profile);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();

        GLCanvas canvas = new GLCanvas(caps, chooser, null, device);
        canvas.setPreferredSize(new Dimension(8, 8));
        
        // the bottom right corner of the status bar seems to be the safest position for a heavyweight component
        GLWorkerStatusLineElementProvider.component.add(canvas);

        return new GLWorkerImpl(canvas);
    }

    private final Logger log() {
        return Logger.getLogger(NBGLWorkerImpl.class.getName());
    }

    @Override
    public synchronized void addWork(GLRunnable runnable) {
        glworker.addWork(runnable);
    }

    @Override
    public synchronized void destroy() {
        glworker.destroy();
    }

    @Override
    public synchronized void work() {
        glworker.work();
    }

    @Override
    public synchronized void work(GLRunnable runnable) {
        glworker.addWork(runnable);
        work();
    }
}