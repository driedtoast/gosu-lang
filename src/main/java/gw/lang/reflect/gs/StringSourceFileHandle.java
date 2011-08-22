package gw.lang.reflect.gs;

import gw.lang.parser.ParserSource;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.expressions.ITypeVariableDefinition;

import java.util.Map;

/**
 * Holds the source for a gosu class in a local string
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StringSourceFileHandle implements ISourceFileHandle
{
  private CharSequence _source;
  private boolean _bTestResource;
  private ClassType _classType;
  private String _typeName;
  private String _strPath;
  private String _strEnclosingType;
  private ITypeUsesMap _typeUsesMap;
  private Map<String, ITypeVariableDefinition> _capturedTypeVars;
  private int _iOffset;
  private int _iEnd;
  private String _fileRef;

  public StringSourceFileHandle( String typeName, CharSequence source, boolean isTestResource, ClassType classType )
  {
    this( typeName, source, null, isTestResource, classType );
  }
  public StringSourceFileHandle( String typeName, CharSequence source, String strPath, boolean isTestResource, ClassType classType )
  {
    _typeName = typeName;
    _source = source;
    _bTestResource = isTestResource;
    _classType = classType;
    _strPath = strPath;
  }

  protected CharSequence getRawSource()
  {
    return _source;
  }
  protected void setRawSource( CharSequence source )
  {
    _source = source;
  }

  public ParserSource getSource()
  {
    return new ParserSource(_source.toString());
  }

  public String getParentType()
  {
    return _strEnclosingType;
  }
  public void setParentType( String strEnclosingType )
  {
    _strEnclosingType = strEnclosingType;
  }

  public String getNamespace()
  {
    return null;
  }

  public String getResourceName() 
  {
    return _strPath;
  }

  public boolean isTestClass()
  {
    return _bTestResource;
  }

  public boolean isValid()
  {
    return true;
  }

  public void cleanAfterCompile()
  {
  }

  public void cleanAfterVerification() {
  }

  public ClassType getClassType()
  {
    return _classType;
  }

  public String getTypeName()
  {
    return _typeName;
  }

  public String getRelativeName() {
    if (_typeName.lastIndexOf('.') == -1) {
      return _typeName;
    }
    return _typeName.substring(_typeName.lastIndexOf('.') +1);
  }

  public Object getUserData()
  {
    return null;
  }

  public void setUserData( Object data )
  {
    throw new UnsupportedOperationException();
  }

  public String getTypeNamespace() {
    if (_typeName.lastIndexOf('.') == -1) {
      return "";
    }
    return _typeName.substring(0, _typeName.lastIndexOf('.'));
  }

  @Override
  public void setOffset( int iOffset )
  {
    _iOffset = iOffset;
  }
  @Override
  public int getOffset()
  {
    return _iOffset;
  }

  @Override
  public void setEnd( int iEnd )
  {
    _iEnd = iEnd;
  }
  @Override
  public int getEnd()
  {
    return _iEnd;
  }

  public void setFileRef( String fileRef )
  {
    _fileRef = fileRef;
  }

  @Override
  public String getFileRef()
  {
    return _fileRef;
  }

  @Override
  public long getFileTimestamp() {
    return 0;
  }

  public void setTypeUsesMap( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap;
  }
  public ITypeUsesMap getTypeUsesMap()
  {
    return _typeUsesMap;
  }

  public void setCapturedTypeVars( Map<String, ITypeVariableDefinition> capturedTypeVars )
  {
    _capturedTypeVars = capturedTypeVars;
  }
  public Map<String, ITypeVariableDefinition> getCapturedTypeVars()
  {
    return _capturedTypeVars;
  }
}
