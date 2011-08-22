package gw.lang.parser.coercers;

import gw.lang.parser.IResolvingCoercer;
import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BasePrimitiveCoercer extends StandardCoercer implements IResolvingCoercer
{
  //The non-primitive coercer
  private final ICoercer _nonPrimitiveCoercer;
  private final IType _primitiveType;
  private final IType _nonPrimitveType;

  public BasePrimitiveCoercer( ICoercer nonPrimitiveCoercer, IType primitiveType, IType nonPrimitiveType )
  {
    _nonPrimitiveCoercer = nonPrimitiveCoercer;
    _primitiveType = primitiveType;
    _nonPrimitveType = nonPrimitiveType;
  }

  public final Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value == null )
    {
      return CommonServices.getCoercionManager().convertNullAsPrimitive( _primitiveType, false );
    }
    else
    {
      return _nonPrimitiveCoercer.coerceValue( typeToCoerceTo, value );
    }
  }

  @Override
  public boolean handlesNull()
  {
    return true;
  }

  public IType resolveType( IType target, IType source )
  {
    return target.isPrimitive() ? _primitiveType : _nonPrimitveType;
  }

  @Override
  public int getPriority( IType to, IType from )
  {
    int iPriority = 2;
    if( (isFloatFamily( to ) && isFloatFamily( from )) ||
        (isIntFamily( to ) && isIntFamily( from )) )
    {
      iPriority+=2;
    }
    else if( IJavaType.OBJECT.equals( to ) )
    {
      iPriority++;
    }
    return iPriority;
  }

  private boolean isFloatFamily( IType type )
  {
    return type == IJavaType.FLOAT ||
           type == IJavaType.pFLOAT ||
           type == IJavaType.DOUBLE ||
           type == IJavaType.pDOUBLE;
  }

  private boolean isIntFamily( IType type )
  {
    return type == IJavaType.INTEGER ||
           type == IJavaType.pINT ||
           type == IJavaType.LONG ||
           type == IJavaType.pLONG ||
           type == IJavaType.SHORT ||
           type == IJavaType.pSHORT ||
           type == IJavaType.BYTE ||
           type == IJavaType.pBYTE;
  }
}
