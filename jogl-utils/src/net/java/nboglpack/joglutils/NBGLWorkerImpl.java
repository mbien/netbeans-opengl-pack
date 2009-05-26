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
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;

/**
 * GLWorker implementation with fallback mode.
 * @author Michel Bien
 */
public class NBGLWorkerImpl implements GLWorker {

    private GLWorker glworker;

    public NBGLWorkerImpl() {

        if (GLDrawableFactory.getFactory().canCreateGLPbuffer()) {
            try {
                glworker = new GLWorkerImpl();
            } catch (GLException ex) {
                Logger.getLogger(NBGLWorkerImpl.class.getName()).log(
                        Level.INFO, "unable to create GLWorkerImpl, switching to fallback mode", ex);
                glworker = createFallbackWorkerImpl();
            }
        } else {
            glworker = createFallbackWorkerImpl();
        }
        GLWorkerImpl.DEFAULT = this;
    }

    private final GLWorkerImpl createFallbackWorkerImpl() {
        
        Logger.getLogger(NBGLWorkerImpl.class.getName()).log(Level.INFO, "using GLWorker fallback mode");
        
        // fallback mode
        // use a heavy weight drawable if pixel buffers are not supported
        Beans.setDesignTime(false); // TODO designtime = false workaround
        GLCapabilities caps = new GLCapabilities();
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();

        GLCanvas canvas = new GLCanvas(caps, chooser, null, device);
        canvas.setPreferredSize(new Dimension(8, 8));
        
        // the bottom right corner of the status bar seems to be the safest position for a heavyweight component
        GLWorkerStatusLineElementProvider.component.add(canvas);

        return new GLWorkerImpl(canvas);
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