package net.java.nboglpack.glsleditor.dataobject;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;
import org.openide.loaders.UniFileLoader;


/**
 * @author Michael Bien
 */
public class GlslGeometryShaderDataLoaderBeanInfo extends SimpleBeanInfo {
    
    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[] {Introspector.getBeanInfo(UniFileLoader.class)};
        } catch (IntrospectionException e) {
            throw new AssertionError(e);
        }
    }
    
    @Override
    public Image getIcon(int type) {
        return super.getIcon(type); // TODO @cylab we need a fancy geo shader icon
        
    }
    
}
