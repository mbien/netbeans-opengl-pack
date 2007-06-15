/*
 * ManifestSettingsInitializer.java
 *
 * Created on October 20, 2005, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.nboglpack.glsleditor;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Coloring;
import org.netbeans.editor.Settings;
import org.netbeans.editor.SettingsDefaults;
import org.netbeans.editor.SettingsNames;
import org.netbeans.editor.SettingsUtil;
import org.netbeans.editor.TokenCategory;
import org.netbeans.editor.TokenContext;
import org.netbeans.editor.TokenContextPath;

/**
 *
 * @author Administrator
 */
public class GlslSettingsInitializer extends Settings.AbstractInitializer
{
	
	public static final String NAME = "glsl-settings-initializer"; // NOI18N
	
	/**
	 * Constructor
	 */
	public GlslSettingsInitializer()
	{
		super(NAME);
	}
	
	/**
	 * Update map filled with the settings.
	 * @param kitClass kit class for which the settings are being updated.
	 *   It is always non-null value.
	 * @param settingsMap map holding [setting-name, setting-value] pairs.
	 *   The map can be empty if this is the first initializer
	 *   that updates it or if no previous initializers updated it.
	 */
	public void updateSettingsMap(Class kitClass, Map settingsMap)
	{
		if (kitClass == BaseKit.class)
		{
			new GlslTokenColoringInitializer().updateSettingsMap(kitClass, settingsMap);
		}
		
		if (kitClass == GlslVertexShaderEditorKit.class)
		{
			SettingsUtil.updateListSetting(
				settingsMap,
				SettingsNames.TOKEN_CONTEXT_LIST,
				new TokenContext[] { GlslTokenContext.context }
			);
		}

		if (kitClass == GlslFragmentShaderEditorKit.class)
		{
			SettingsUtil.updateListSetting(
				settingsMap,
				SettingsNames.TOKEN_CONTEXT_LIST,
				new TokenContext[] { GlslTokenContext.context }
			);
		}
	}
	
	/**
	 * Class for adding syntax coloring to the editor
	 */
	/** Properties token coloring initializer. */
	private static class GlslTokenColoringInitializer extends SettingsUtil.TokenColoringInitializer
	{
		private static final Coloring emptyColoring = new Coloring(null, Color.BLACK, null);
		
		
		/** Constructs <code>PropertiesTokenColoringInitializer</code>. */
		public GlslTokenColoringInitializer()
		{
			super(GlslTokenContext.context);
		}
		
		
		/** Gets token coloring. */
		public Object getTokenColoring(TokenContextPath tokenContextPath,
			TokenCategory tokenIDOrCategory, boolean printingSet)
		{
			
			if(!printingSet)
			{
				return emptyColoring;
			}
			else
			{ // printing set
				return SettingsUtil.defaultPrintColoringEvaluator;
			}
		}
		
	} // End of class ManifestTokenColoringInitializer.
	
}