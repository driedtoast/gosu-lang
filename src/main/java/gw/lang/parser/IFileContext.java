package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFileContext
{
  /**
   * @return Some additional contextual information about the place within a file that this program is located (e.g. an attribute in an XML file)
   */
  String getContextString();

  /**
   * @return a class name that uniquely represents this file context when combined with the context string above.
   */
  String getClassNameForFile();

  /**
   * @return a relative file name for this element
   */
  String getFileContext();
}
