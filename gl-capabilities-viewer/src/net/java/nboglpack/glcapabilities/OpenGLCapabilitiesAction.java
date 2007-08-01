/*
 * OpenGLCapabilitiesAction.java
 *
 * Created on 20. Juni 2007, 18:51
 */

package net.java.nboglpack.glcapabilities;

import com.mbien.engine.util.GLRunnable;
import com.mbien.engine.util.GLWorker;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.swing.JDialog;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;


/**
 *
 * @author Michael Bien
 */
public final class OpenGLCapabilitiesAction extends CallableSystemAction {
    
 private GLCapabilitiesPanel capsPanel;
    
    public void performAction() {
        
        JDialog dialog = new JDialog(   WindowManager.getDefault().getMainWindow(),
                                        "OpenGL Capabilities", false);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
//        dialog.addWindowListener(createWindowObserver());
        capsPanel = new GLCapabilitiesPanel();
        dialog.setContentPane(capsPanel);
        dialog.pack();
        
        dialog.setLocationRelativeTo(null);
        
        
        dialog.setVisible(true);
        
        EventQueue.invokeLater(createGLCapabilitiesQuery());
    }
    
    private Runnable createGLCapabilitiesQuery() {
        
        final GLWorker worker = new GLWorker();
        
        final GLRunnable query = new GLRunnable() {

            public void run(GLContext context) {
                
                GL gl = context.getGL();
                
                GLCapabilitiesModel model = capsPanel.getModel();
                
                // overview
                model.setGLVersion(gl.glGetString(GL.GL_VERSION));
                model.setGLVendor(gl.glGetString(GL.GL_VENDOR));
                model.setRenderer(gl.glGetString(GL.GL_RENDERER));
                model.setGLSLVersion(gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION));
                model.setGLImplVersion(Package.getPackage("javax.media.opengl").getImplementationVersion());// NOI18N
                
                int[] buffer = new int[2];
                gl.glGetIntegerv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buffer, 0);
                model.setMaxAnisotropy(buffer[0]+"x");
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_VIEWPORT_DIMS, buffer, 0);
                model.setMaxViewportSize(buffer[0]+"x"+buffer[1]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS, buffer, 0);
                model.setMaxTextureUnits(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, buffer, 0);
                model.setMaxVertexTextureImageUnits(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_TEXTURE_IMAGE_UNITS, buffer, 0);
                model.setMaxTextureImageUnits(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT, buffer, 0);
                model.setMaxGeometryTextureImageUnits(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, buffer, 0);
                model.setMaxTextureSize(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_LIGHTS, buffer, 0);
                model.setMaxLights(""+buffer[0]);
                
                buffer[0] = 0; buffer[1] = 0;
                gl.glGetIntegerv(GL.GL_MAX_DRAW_BUFFERS, buffer, 0);
                model.setMaxDrawBuffers(""+buffer[0]);
                
                
                // extentions
                ArrayList<String> extentions = model.getExtensions();
                extentions.clear();
                StringTokenizer tokenizer = new StringTokenizer(gl.glGetString(GL.GL_EXTENSIONS));
                while(tokenizer.hasMoreTokens())
                    extentions.add(tokenizer.nextToken());
                
                                
                // capabilities
                Field[] fields = GL.class.getDeclaredFields();
                HashMap<String, String> capabilities = new HashMap<String, String>();
                
                for (Field field : fields) {
                    
                    try {
                        
                        StringBuilder sb = new StringBuilder();
                        
                        if(field.getName().startsWith("GL_MAX_")) {// NOI18N
                            
                            buffer[0] = 0; buffer[1] = 0;
                            
                            gl.glGetIntegerv((Integer)field.getInt(gl), buffer, 0);
                            
                            for(int i = 0; i < buffer.length; i++) {
                                sb.append(buffer[i]);
                                if(i != buffer.length-1)
                                    sb.append(", ");// NOI18N
                            }
                            while(sb.toString().endsWith(", 0"))// NOI18N
                                sb.delete(sb.length()-3, sb.length());

                            capabilities.put(field.getName(), sb.toString());
                            sb.delete(0, sb.length());
                            
                        }
                        
                    } catch (IllegalArgumentException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IllegalAccessException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    
                }
                
                List<GLCapabilitiesModel.Capability> elements = model.getCapabilities();
                Iterator<String> keys = capabilities.keySet().iterator();
                Iterator<String> values = capabilities.values().iterator();

                elements.clear();
                while(keys.hasNext()) 
                    elements.add(new GLCapabilitiesModel.Capability(keys.next(), values.next()));
                
                capsPanel.updateFromModel();
            }
            
        };
        
        return new Runnable(){

            public void run() {
                worker.work(query);
                worker.destroy();
            }
            
        };
    }
    
    private WindowListener createWindowObserver() {
        
        WindowListener windowListener = new WindowAdapter() {
            
            @Override
            public void windowDeactivated(WindowEvent windowEvent) {
                close(windowEvent.getWindow());
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                close(windowEvent.getWindow());
            }

            private void close(Window window) {
                window.setVisible(false);
                window.removeWindowListener(this);
                window.dispose();
            }
        };
        
        return windowListener;
    }
    
    
    public String getName() {
        return NbBundle.getMessage(OpenGLCapabilitiesAction.class, "CTL_OpenGLCapabilitiesAction");
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
