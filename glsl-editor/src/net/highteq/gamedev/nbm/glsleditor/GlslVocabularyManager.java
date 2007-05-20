/*
 * GlslVocabularyManager.java
 *
 * Created on 12. Februar 2006, 03:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.highteq.gamedev.nbm.glsleditor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;

/**
 *
 * @author mhenze
 */
public class GlslVocabularyManager
{
	private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslVocabularyManager.class.getName());
	private XStream xstream = new XStream(new DomDriver());
	private Map vocabulary;
	private String mimetype;
	private static HashMap instances= new HashMap();

	/** Creates a new instance of GlslVocabularyManager */
	private GlslVocabularyManager(String mimetype)
	{
		this.mimetype= mimetype;
		xstream.alias("desc", Descriptor.class);
		loadVocabulary();
	}
	
	public static GlslVocabularyManager getInstance(String mimetype)
	{
		GlslVocabularyManager instance= (GlslVocabularyManager) instances.get(mimetype);
		if(instance==null)
		{
			instance= new GlslVocabularyManager(mimetype);
			instances.put(mimetype,instance);
		}
		return instance;
	}
	
	private void loadVocabulary()
	{
		FileObject vocabularyfile = Repository.getDefault().getDefaultFileSystem().findResource("Editors/"+mimetype+"/vocabulary.xml");
		if (vocabularyfile != null)
		{
			InputStream in= null;
			try
			{
				in= vocabularyfile.getInputStream();
				vocabulary= (Map) xstream.fromXML(in);
			}
			catch (Exception e)
			{
				LOGGER.notify(e);
			}
			finally
			{
				if(in!=null) try
				{in.close();}
				catch (Exception e)
				{LOGGER.notify(e);};
			}
		}
	}
	
	public Set getKeys()
	{
		return vocabulary.keySet();
	}
	
	public Descriptor getDesc(String key)
	{
		Descriptor desc= (Descriptor)(vocabulary==null?null:vocabulary.get(key));	
		if(desc!=null && desc.getName()==null)
		{
			desc.setName(key);
		}
		return desc;
	}
	
	public static class Descriptor
	{
		private String name=null;
		private String syntax=null;
		private String arguments=null;
		private String type=null;
		private String tooltip=null;
		private String help=null;
		private String category=null;
		
		public void setSyntax(String syntax)
		{
			this.syntax= syntax;
		}
		public void setName(String name)
		{
			this.name= name;
		}
		public void setArguments(String arguments)
		{
			this.arguments= arguments;
		}
		public void setType(String type)
		{
			this.type= type;
		}
		public void setTooltip(String tooltip)
		{
			this.tooltip= tooltip;
		}
		public void setHelp(String help)
		{
			this.help= help;
		}
		public void setCategory(String category)
		{
			this.category= category;
		}

		public String getSyntax()
		{
			if(syntax!=null)
				return syntax;
			return "<b>"+getName()+"</b><i>"+getArguments()+"</i>"+(type==null?"":(":"+type));
		}
		public String getName()
		{
			return this.name;
		}
		public String getArguments()
		{
			return this.arguments==null?"":arguments;
		}
		public String getType()
		{
			return this.type==null?"":type;
		}
		public String getTooltip()
		{
			return tooltip==null?name:tooltip;
		}
		public String getHelp()
		{
			return help==null?getTooltip():help;
		}
		public String getCategory()
		{
			return category==null?"keyword":category;
		}

	}
}
