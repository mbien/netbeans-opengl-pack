package net.highteq.gamedev.nbm.glsleditor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.InputStream;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.UIManager;
import org.netbeans.api.editor.completion.Completion;
import org.openide.ErrorManager;
import org.netbeans.editor.BaseDocument;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Iterator;


/**
 * A testing Completion Provider that provides abbreviations as result.
 *
 * @author Jan Lahoda
 */
public class GlslCompletionProvider implements CompletionProvider
{
	private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslCompletionProvider.class.getName());
	
	public GlslCompletionProvider()
	{
	}
	
	public CompletionTask createTask(int queryType, JTextComponent component)
	{
		String mimetype="text/plain";
		if(component instanceof JEditorPane)
		{
			mimetype= ((JEditorPane)component).getContentType();
		}
		return new AsyncCompletionTask(new GlslCompletionQuery(mimetype),component);
	}
	
	public int getAutoQueryTypes(JTextComponent component, String typedText)
	{
		return 0;
	}
	
	private static class GlslCompletionQuery extends AsyncCompletionQuery
	{
		private GlslVocabularyManager vocabulary;
		public GlslCompletionQuery(String mimetype)
		{
			vocabulary= GlslVocabularyManager.getInstance(mimetype);
		}
			
		protected void query(CompletionResultSet completionResultSet, Document document, int pos)
		{
			fillResultset(completionResultSet,document,pos);
			completionResultSet.finish();
		}
		
		private void fillResultset(CompletionResultSet completionResultSet, Document doc, int pos)
		{
			Element paragraph= ((BaseDocument)doc).getParagraphElement(pos);
			String prefix= "";
			try
			{
				prefix= doc.getText(paragraph.getStartOffset(),pos-paragraph.getStartOffset());
				// dafür sorgen, dass wir in den meisten fällen einen korrekten prefix haben
				// TODO: besser machen, ist ne hau-ruck-methode
				// problem: bei leerzeichen in strings werden auch dort funktoren als autocomplete angeboten...
				prefix= prefix.replaceAll(".*?([\\w-\"]*)$","$1");
			}
			catch (BadLocationException e)
			{
				LOGGER.notify(e);
			}

			Iterator it= vocabulary.getKeys().iterator();
			while (it.hasNext())
			{
				String name= (String)it.next();
				if(name.startsWith(prefix))
				{
					completionResultSet.addItem(new GlslCompletionItem(
						name,
						vocabulary.getDesc(name),
						prefix,
						pos)
					);
				}
			}
		}
	}
	
	private static class GlslCompletionItem implements CompletionItem
	{
		private String key;
		private GlslVocabularyManager.Descriptor content;
		private JLabel itemLabel;
		private int carretPosition= 0;
		private String prefix= "";
		
		public GlslCompletionItem(String key, GlslVocabularyManager.Descriptor content,String prefix, int carretPosition)
		{
			this.key= key;
			this.content= content;
			this.prefix= prefix;
			this.carretPosition= carretPosition;
			this.itemLabel= new JLabel("<html><body>"+content.getSyntax()+"</body></html>");
		}
		
		public String getKey()
		{
			return key;
		}
		
		public GlslVocabularyManager.Descriptor getContent()
		{
			return content;
		}
		
		public int getCarretPosition()
		{
			return carretPosition;
		}
		
		public String getPrefix()
		{
			return prefix;
		}
		
		public void defaultAction(JTextComponent component)
		{
			Completion.get().hideAll();
			try
			{
				component.getDocument().insertString(carretPosition,key.substring(prefix.length()),null);
			}
			catch (BadLocationException e)
			{
				LOGGER.notify(e);
			}
		}
		
		public void processKeyEvent(KeyEvent evt)
		{
		}
		
		public int getPreferredWidth(Graphics g, Font defaultFont)
		{
			return (int) itemLabel.getPreferredSize().getWidth();
		}
		
		public void render(
			Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected
			)
		{
			itemLabel.setFont(defaultFont);
			itemLabel.setForeground(defaultColor);
			itemLabel.setBackground(backgroundColor);
			itemLabel.setBounds(0,0,width,height);
			if(selected)
			{
				itemLabel.setBackground(UIManager.getDefaults().getColor("MenuItem.selectionBackground"));
				itemLabel.setForeground(UIManager.getDefaults().getColor("MenuItem.selectionForeground"));
			}
			itemLabel.paint(g);
		}
		
		public CompletionTask createDocumentationTask()
		{
			// TODO: wieder aktivieren, wenn genug content da ist
			if(true) return null;
			
			if(content.getHelp()==null)
				return null;

			return new AsyncCompletionTask(
				new AsyncCompletionQuery()
				{
					private DocItem item= new DocItem(content);
					protected void query(CompletionResultSet completionResultSet, Document document, int i)
					{
						completionResultSet.setDocumentation(item);
						completionResultSet.finish();
					}
				}
			);
		}
		
		public CompletionTask createToolTipTask()
		{
			return null;
		}
		
		public boolean instantSubstitution(JTextComponent component)
		{
			defaultAction(component);
			return true;
		}
		
		public int getSortPriority()
		{
			return 23; // ?
		}
		
		public CharSequence getSortText()
		{
			return key;
		}
		
		public CharSequence getInsertPrefix()
		{
			return prefix;
		}
	}
	
	public static class DocItem implements CompletionDocumentation
	{
		private GlslVocabularyManager.Descriptor content;
		
		public DocItem(GlslVocabularyManager.Descriptor content)
		{
			this.content = content;
		}
		
		public String getText()
		{
			return content.getHelp();
		}
		
		public URL getURL()
		{
			return null;
		}
		
		public CompletionDocumentation resolveLink(String link)
		{
			return null;
		}
		
		public Action getGotoSourceAction()
		{
			return null;
		}
	}
	
}
