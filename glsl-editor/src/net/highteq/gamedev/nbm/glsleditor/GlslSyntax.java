/*
 * ManifestSyntax.java
 *
 * Created on July 20, 2005, 10:37 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.highteq.gamedev.nbm.glsleditor;

import org.netbeans.editor.Syntax;
import org.netbeans.editor.TokenID;
import org.openide.ErrorManager;

/**
 *
 * @author Administrator
 */
public class GlslSyntax extends Syntax
{
	
	private static final ErrorManager LOGGER = ErrorManager.getDefault().getInstance(GlslSyntax.class.getName());
	private static final boolean LOG = LOGGER.isLoggable(ErrorManager.INFORMATIONAL);
	private String currentToken;
	private boolean inEscape;
	private GlslVocabularyManager vocabularies;
	
	private static final int ISI_NAME = 1;
	private static final int ISI_KEYWORD = 2;
	private static final int ISA_CR = 3;
	private static final int ISI_VALUE = 5;
	private static final int ISI_STRING_VALUE = 7;
	private static final int ISI_PREPROC = 9;
	private static final int ISI_COMMENT = 10;
	private static final int ISI_ML_COMMENT = 11;
	
	/** Creates a new instance of ManifestSyntax */
	public GlslSyntax(String mimetype)
	{
		tokenContextPath = GlslTokenContext.contextPath;
		vocabularies = GlslVocabularyManager.getInstance(mimetype);
	}
	
	protected TokenID parseToken()
	{
		TokenID result = doParseToken();
		if (LOG)
		{
			LOGGER.log(ErrorManager.INFORMATIONAL, "parseToken: " + result);
		}
		return result;
	}
	
	private TokenID doParseToken()
	{
		char lastChar=' ';
		char actChar;
		int startOffset= offset;
		while (offset < stopOffset)
		{
			if(offset>startOffset)lastChar = buffer[offset-1];
			actChar = buffer[offset];
			
			switch (state)
			{
				case INIT:
					currentToken="";
					switch (actChar)
					{
						case '(':
							state = INIT;
							offset++;
							return GlslTokenContext.BRACE;
						case ')':
							state = INIT;
							offset++;
							return GlslTokenContext.BRACE;
						case '{':
							state = INIT;
							offset++;
							return GlslTokenContext.CURLY_BRACE;

						case '}':
							state = INIT;
							offset++;
							return GlslTokenContext.CURLY_BRACE;
						case ',':
							state = INIT;
							offset++;
							return GlslTokenContext.SEPARATOR;
						case '\n':
							state = INIT;
							offset++;
							return GlslTokenContext.END_OF_LINE;
						case ' ':
						case '\t':
							state = INIT;
							offset++;
							return GlslTokenContext.WHITESPACE;

						case '\r':
								state = ISA_CR;
								break;
							
						case '#':
							state = ISI_PREPROC;
							break;
							
						case '"':
							state = ISI_STRING_VALUE;
							break;
						case '/':
							if(lastChar=='/')
							{
								state = ISI_COMMENT;
							}
							break;
						case '*':
							if(lastChar=='/')
							{
								state = ISI_ML_COMMENT;
							}
							break;
						default:
							if(Character.isJavaIdentifierStart(actChar))
							{
								state = ISI_NAME;
							}
							else
							{
								state = INIT;
							}
					}
					break;

				case ISI_NAME:
					switch (actChar)
					{
						case '\r':
							state = ISA_CR;
							return findNameType(currentToken);
						default:
							if(!Character.isJavaIdentifierPart(actChar))
							{
								state = INIT;
								return findNameType(currentToken);
							}
					}
					break;

				case ISI_STRING_VALUE:
					switch (actChar)
					{
						case '\\':
							inEscape=true;
							break;
						case '"':
							if(!inEscape)
							{
								offset++;
								state = INIT;
								return GlslTokenContext.STRING_VALUE;
							}
						default:
							inEscape= false;
							break;
					}
					break;

				case ISI_PREPROC:
					switch (actChar)
					{
						case '\n':
							state = INIT;
							return GlslTokenContext.PREPROCESSOR;
						case '\r':
							state = ISA_CR;
							return GlslTokenContext.PREPROCESSOR;
					}
					break;

				case ISI_COMMENT:
					switch (actChar)
					{
						case '\n':
							state = INIT;
							return GlslTokenContext.COMMENT;
						case '\r':
							state = ISA_CR;
							return GlslTokenContext.COMMENT;
					}
					break;

				case ISI_ML_COMMENT:
					switch (actChar)
					{
						case '/':
							if(lastChar=='*')
							{
								state = INIT;
								return GlslTokenContext.COMMENT;
							}
						default:
							state= ISI_ML_COMMENT;
					}
					break;

				case ISA_CR:
						if (actChar == '\n') {
								offset++;
						}
						state = INIT;
						return GlslTokenContext.END_OF_LINE;
			}
			currentToken+=actChar;
			offset++;
		}

		switch (state)
		{
			case ISI_NAME:
				state = INIT;
				return findNameType(currentToken);
			case ISI_STRING_VALUE:
				state = INIT;
				return GlslTokenContext.STRING_VALUE;
			case ISI_COMMENT:
				state = INIT;
				return GlslTokenContext.COMMENT;
			case ISI_ML_COMMENT:
				state = ISI_ML_COMMENT;
				return GlslTokenContext.COMMENT;
		}

		return null;
	}
	private TokenID findNameType(String token)
	{
		GlslVocabularyManager.Descriptor desc= vocabularies.getDesc(token.trim());
		if(desc!=null && "keyword".equals(desc.getCategory()))
		{
			return GlslTokenContext.KEYWORD;
		}
		if(desc!=null && "build-in-func".equals(desc.getCategory()))
		{
			return GlslTokenContext.BUILD_IN_FUNC;
		}
		if(desc!=null && "build-in-var".equals(desc.getCategory()))
		{
			return GlslTokenContext.BUILD_IN_VAR;
		}
		return GlslTokenContext.NAME;
	}
}
