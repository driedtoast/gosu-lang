package gw.config;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;


class DomWriter
{
  protected Writer _writer;
  protected Document _doc;
  protected boolean _bOutline;

  /**
   * Number of attributes above which attributes will be printed on separate lines. If
   * a node has more than this number of attributes, then each attribute will be printed
   * on a separate line. Only applicable in "outline" mode. To disable this, use
   * {@link DomWriter#UNLIMITED_ATTRIBUTE_THRESHOLD}.
   */
  protected int _nAttributeThreshold;

  /**
   * The default attribute threshold.
   */
  public static final int DEFAULT_ATTRIBUTE_THRESHOLD = 3;

  /**
   * Indicates an unlimited attribute threshold, meaning that attributes will never
   * be printed on separate lines.
   */
  public static final int UNLIMITED_ATTRIBUTE_THRESHOLD = -1;

  /**
   * Indciates that text for an element will be output on a new line.
   */
  private boolean textOnNewLine;


  public static Document create()
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();
      return doc;
    }
    catch( FactoryConfigurationError error )
    {
      throw new RuntimeException( error );
    }
    catch( ParserConfigurationException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static Document load( InputStream inputStream )
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document d = db.parse( new InputSource( inputStream ) );
      return d;
    }
    catch( FactoryConfigurationError error )
    {
      throw new RuntimeException( error );
    }
    catch( ParserConfigurationException e )
    {
      throw new RuntimeException( e );
    }
    catch( SAXException e )
    {
      throw new RuntimeException( e );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  /**
   * Convert given document to an XML string, with no newlines or unnecessary
   * whitespace
   */
  public static String documentToString( Document doc )
  {
    StringWriter writer = new StringWriter();
    write( doc, writer, false, UNLIMITED_ATTRIBUTE_THRESHOLD );
    return writer.toString();
  }

  /**
   * Convert given document to an XML string, with appropriate indentations and
   * newlines
   */
  public static String documentToPrettyString( Document doc )
  {
    StringWriter writer = new StringWriter();
    write( doc, writer, true, DEFAULT_ATTRIBUTE_THRESHOLD );
    return writer.toString();
  }

  public static void write( Document doc, Writer writer )
  {
    write( doc, writer, false );
  }

  public static void write( Document doc, Writer writer, boolean bOutline )
  {
    write( doc, writer, bOutline, DEFAULT_ATTRIBUTE_THRESHOLD );
  }

  public static void write( Document doc, Writer writer, boolean bOutline, int nAttributeThreshold )
  {
    write( doc, writer, bOutline, nAttributeThreshold, false );
  }

  public static void write( Document doc, Writer writer, boolean bOutline, int nAttributeThreshold, boolean textOnNewline )
  {
    try
    {
      DomWriter domWriter = new DomWriter( writer, doc, bOutline, nAttributeThreshold, textOnNewline );
      domWriter.write( doc );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  public DomWriter( Writer writer, Document doc, boolean bOutline, int nAttributeThreshold, boolean textOnNewline )
  {
    _writer = writer;
    _doc = doc;
    _bOutline = bOutline;
    _nAttributeThreshold = nAttributeThreshold;
    this.textOnNewLine = textOnNewline;
  }

  public void write( Node node )
  {
    if( node == null )
    {
      return;
    }

    try
    {
      int type = node.getNodeType();

      switch( type )
      {
        case Node.DOCUMENT_NODE:
        {
          _writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );

          NodeList children = node.getChildNodes();

          for( int iChild = 0; iChild < children.getLength(); iChild++ )
          {
            write( children.item( iChild ) );
          }
          _writer.flush();
          break;
        }

        case Node.ELEMENT_NODE:
        {
          if( _bOutline )
          {
            writeIndent( node );
          }

          _writer.write( '<' );
          _writer.write( node.getNodeName() );

          NamedNodeMap attrs = node.getAttributes();
          int iLength = attrs.getLength();
          for( int i = 0; i < iLength; i++ )
          {
            Attr attr = (Attr)attrs.item( i );

            if( _bOutline && (_nAttributeThreshold != UNLIMITED_ATTRIBUTE_THRESHOLD) && (iLength > _nAttributeThreshold) )
            {
              writeIndent( node );
              _writer.write( "  " );
            }
            _writer.write( ' ' );
            _writer.write( attr.getNodeName() );
            _writer.write( "=\"" );
            writeEscaped( attr.getNodeValue() );
            _writer.write( '"' );
          }
          NodeList children = node.getChildNodes();
          iLength = children.getLength();
          if( iLength > 0 )
          {
            _writer.write( '>' );

            boolean bHasText = false;
            for( int i = 0; i < iLength; i++ )
            {
              Node child = children.item( i );
              write( child );

              if( _bOutline && (child.getNodeType() == Node.TEXT_NODE) )
              {
                bHasText = true;
              }
            }

            if( _bOutline && !bHasText )
            {
              writeIndent( node );
            }

            _writer.write( "</" );
            _writer.write( node.getNodeName() );
            _writer.write( '>' );
          }
          else
          {
            _writer.write( "/>" );
            return;
          }

          break;
        }

        case Node.ENTITY_REFERENCE_NODE:
        {
          _writer.write( '&' );
          _writer.write( node.getNodeName() );
          _writer.write( ';' );
          break;
        }

        case Node.CDATA_SECTION_NODE:
        {
          if( _bOutline )
          {
            writeIndent( node );
          }

          _writer.write( "<![CDATA[" );
          _writer.write( node.getNodeValue() );
          _writer.write( "]]>" );

          break;
        }

        case Node.TEXT_NODE:
        {
          if( _bOutline && textOnNewLine )
          {
            writeIndent( node );
          }
          writeEscaped( node.getNodeValue() );
          if( _bOutline && textOnNewLine )
          {
            writeIndent( node.getParentNode() );
          }

          break;
        }

        case Node.PROCESSING_INSTRUCTION_NODE:
        {
          if( _bOutline )
          {
            writeIndent( node );
          }

          _writer.write( "<?" );
          _writer.write( node.getNodeName() );
          String data = node.getNodeValue();

          if( data != null && data.length() > 0 )
          {
            _writer.write( ' ' );
            _writer.write( data );
          }
          _writer.write( "?>\n" );

          break;
        }
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  protected void writeEscaped( String strToken ) throws IOException
  {
    writeEscaped( _writer, strToken );
  }

  public static void writeEscaped( Writer out, String strToken ) throws IOException
  {
    int iLength = (strToken != null) ? strToken.length() : 0;

    for( int i = 0; i < iLength; i++ )
    {
      char ch = strToken.charAt( i );

      switch( ch )
      {
        case '<':
        {
          out.write( "&lt;" );
          break;
        }
        case '>':
        {
          out.write( "&gt;" );
          break;
        }
        case '&':
        {
          out.write( "&amp;" );
          break;
        }
        case '"':
        {
          out.write( "&quot;" );
          break;
        }
        case '\'':
        {
          out.write( "&apos;" );
          break;
        }

        default:
        {
          if( ((ch >= ' ') && (ch <= 0xFFFF) && (ch != 0xF7)) ||
              (ch == '\n') ||
              (ch == '\r') ||
              (ch == '\t') )
          {
            out.write( ch );
          }
          else
          {
            // Character is not printable, print as character reference. Non printables are below ASCII
            // space but not tab, line terminator, ASCII delete, or above a certain Unicode threshold.

            out.write( "&#" );
            out.write( Integer.toString( ch ) );
            out.write( ';' );
          }
        }
      }
    }
  }

  protected void writeIndent( Node node ) throws IOException
  {
    Node parent = node.getParentNode();
    if( parent == null )
    {
      return;
    }

    _writer.write( '\n' );
    while( parent != _doc )
    {
      _writer.write( "  " );
      parent = parent.getParentNode();
    }
  }
}
