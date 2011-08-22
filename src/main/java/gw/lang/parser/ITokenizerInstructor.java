package gw.lang.parser;

/**
 * A tokizer instructor provides instructions for each character a tokenizer
 * reads. Instructions are high-level in the form of "Ignore the character",
 * "Analyze the character", or "Analyze the character in a sub-context".
 * <p/>
 * A tokenizer instructor is useful when you need to mediate access to the
 * tokenizer the parser uses. For example, if the parser's input has parsable
 * content embedded inside unparsable content e.g., a template, then you can
 * implement an instructor to signal the tokenizer which parts of the content
 * are parsable.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITokenizerInstructor
{
  public static final int INSTR_IGNORE = 0;
  public static final int INSTR_PENDING = 1;
  public static final int INSTR_ANALYZE = 2;
  public static final int INSTR_ANALYZE_SEPARATELY = 3;
  public static final int INSTR_ANALYZE_AS_DIRECTIVE = 4;

  public int getInstructionFor( int c );

  public void reset();

  public ITokenizerInstructor createNewInstance(ISourceCodeTokenizer tokenizer);

}
