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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.beans.Beans;
import java.util.logging.Logger;
import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLDrawableFactory;

/**
 * GLWorker implementation with fallback mode.
 * @author Michel Bien
 */
public class NBGLWorkerImpl implements GLWorker {

 private final static Logger logger = Logger.getLogger(NBGLWorkerImpl.class.getName());
    
 private final GLWorker glworker;
    
 public NBGLWorkerImpl() {
     
     if(GLDrawableFactory.getFactory().canCreateGLPbuffer()){
         glworker = new GLWorkerImpl();
     }else{
         logger.info("going to fallback mode");
         // fallback mode
         // use a heavy weight drawable if pixel buffers are not supported
         Beans.setDesignTime(false); // TODO designtime = false workaround

         GLCapabilities caps = new GLCapabilities();
         GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
         GLCapabilitiesChooser chooser = new DefaultGLCapabilitiesChooser();

         GLCanvas canvas = new GLCanvas(caps, chooser, null, device);
         // canvas.setMaximumSize(new Dimension(16, 16));
         // canvas.setMinimumSize(new Dimension(16, 16));
         glworker = new GLWorkerImpl(canvas);
         
         // the bottom right corner of the status bar seems to be the safest position for a heavyweight component
         GLWorkerStatusLineElementProvider.component = canvas;
     }
         
 }
 
    @Override
    public void addWork(GLRunnable runnable) {
        glworker.addWork(runnable);
    }

    @Override
    public void destroy() {
        glworker.destroy();
    }

    @Override
    public void work() {
        glworker.work();
    }

    @Override
    public void work(GLRunnable runnable) {
        glworker.work(runnable);
    }
    
}
