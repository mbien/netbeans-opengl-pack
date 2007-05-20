package net.highteq.gamedev.nbm.glsleditor.dataobject;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

public class GlslFragmentShaderNode extends DataNode
{
	public GlslFragmentShaderNode(GlslFragmentShaderObject obj)
	{
		super(obj, Children.LEAF);
		setIconBaseWithExtension(GlslFragmentShaderLoaderBeanInfo.IMAGE_ICON_BASE);
	}
//    /** Creates a property sheet. */
//    protected Sheet createSheet() {
//        Sheet s = super.createSheet();
//        Sheet.Set ss = s.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            s.put(ss);
//        }
//        // TODO add some relevant properties: ss.put(...)
//        return s;
//    }
}
