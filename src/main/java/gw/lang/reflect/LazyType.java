package gw.lang.reflect;

import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.TypeVarToTypeMap;
import gw.util.concurrent.LazyVar;

import java.util.HashMap;

/**
 * @deprecated This class exists only to bridge older PCF code which used the now obsolete IntrinsicTypeReference.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LazyType extends LazyVar<IType>
{
  private final CharSequence _typeName;
  private final ITypeUsesMap _typeUsesMap;

  public LazyType( String typeName )
  {
    super(TypeSystem.getGlobalLock());
    _typeName = typeName.intern();
    _typeUsesMap = null;
  }

  public LazyType( CharSequence typeName, ITypeUsesMap typeUsesMap )
  {
    super(TypeSystem.getGlobalLock());
    _typeName = typeName instanceof String ? ((String)typeName).intern() : typeName;
    _typeUsesMap = typeUsesMap;
  }

  public LazyType( IType entryType )
  {
    super(TypeSystem.getGlobalLock());
    _typeName = entryType.getName();
    _typeUsesMap = null;
    initDirectly( entryType );
  }

  public String getName()
  {
    return _typeName.toString();
  }

  public String getNameFromType()
  {
    String retValue;
    try
    {
      IType type = get();
      if( type instanceof IMetaType )
      {
        retValue = "Type";
      }
      else
      {
        retValue = type.getName();
      }
    }
    catch( Exception e )
    {
      retValue = getName();
    }
    return retValue;
  }

  protected IType init()
  {
    try
    {
      String strType = _typeName.toString();
      IType type;
      if( strType.contains( "<" ) || strType.startsWith( Keyword.KW_block.toString() )  )
      {
        type = TypeSystem.parseType( strType, new TypeVarToTypeMap() );
      }
      // TODO - AHK - I'm going to hell for this . . .
      else if ( strType.startsWith( "entity." ) )
      {
        type = TypeSystem.getByFullName( _typeName.toString() );
      }
      else
      {
        type = _typeUsesMap == null
                ? TypeSystem.getByRelativeName( strType )
                : TypeSystem.getByRelativeName( strType, _typeUsesMap );
      }
      return type;
    }
    catch( ClassNotFoundException e )
    {
      return TypeSystem.getErrorType();
    }
  }
}
