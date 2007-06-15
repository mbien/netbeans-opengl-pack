/*
 * ManifestTokenContext.java
 *
 * Created on July 20, 2005, 11:41 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.java.nboglpack.glsleditor;

import org.netbeans.editor.BaseTokenID;
import org.netbeans.editor.TokenContext;
import org.netbeans.editor.TokenContextPath;
import org.netbeans.editor.Utilities;

/**
 *
 * @author Administrator
 */
public class GlslTokenContext extends TokenContext
{
	
	// Numeric-ids for token categories
	public static final int NAME_ID = 1;
	public static final int FUNCTION_ID = 2;
	public static final int KEYWORD_ID = 3;
	public static final int BUILD_IN_FUNC_ID = 4;
	public static final int BUILD_IN_VAR_ID = 5;
	public static final int BRACE_ID = 6;
	public static final int VALUE_ID = 7;
	public static final int CURLY_BRACE_ID = 8;
	public static final int STRING_VALUE_ID = 9;
	public static final int COMMENT_ID = 10;
	public static final int SEPARATOR_ID = 11;
	public static final int WHITESPACE_ID = 12;
	public static final int END_OF_LINE_ID = 13;
	public static final int PREPROCESSOR_ID = 14;
	
	// Token-ids
	public static final BaseTokenID NAME = new BaseTokenID("name", NAME_ID);
	public static final BaseTokenID FUNCTION = new BaseTokenID("function", FUNCTION_ID);
	public static final BaseTokenID KEYWORD = new BaseTokenID("keyword", KEYWORD_ID);
	public static final BaseTokenID BUILD_IN_FUNC = new BaseTokenID("build-in-func", BUILD_IN_FUNC_ID);
	public static final BaseTokenID BUILD_IN_VAR = new BaseTokenID("build-in-var", BUILD_IN_VAR_ID);
	public static final BaseTokenID BRACE = new BaseTokenID("brace", BRACE_ID);
	public static final BaseTokenID VALUE = new BaseTokenID("value", VALUE_ID);
	public static final BaseTokenID CURLY_BRACE = new BaseTokenID("curly-brace", CURLY_BRACE_ID);
	public static final BaseTokenID STRING_VALUE = new BaseTokenID("string-value", STRING_VALUE_ID);
	public static final BaseTokenID COMMENT = new BaseTokenID("comment", COMMENT_ID);
	public static final BaseTokenID SEPARATOR = new BaseTokenID("separator", SEPARATOR_ID);
	public static final BaseTokenID WHITESPACE = new BaseTokenID("whitespace", WHITESPACE_ID);
	public static final BaseTokenID END_OF_LINE = new BaseTokenID("end-of-line", END_OF_LINE_ID);
	public static final BaseTokenID PREPROCESSOR = new BaseTokenID("preprocessor", PREPROCESSOR_ID);
	
	// Context instance declaration
	public static final GlslTokenContext context = new GlslTokenContext();
	public static final TokenContextPath contextPath = context.getContextPath();
	
	/**
	 * Construct a new ManifestTokenContext
	 */
	public GlslTokenContext()
	{
		super("glsl-");
		
		try
		{
			addDeclaredTokenIDs();
		}
		catch (Exception e)
		{
			Utilities.annotateLoggable(e);
		}
	}
}
