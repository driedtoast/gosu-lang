package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.GosuShop;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FunctionFromInterfaceCoercer extends BaseCoercer
{
  private static FunctionFromInterfaceCoercer _instance = new FunctionFromInterfaceCoercer();

  public static FunctionFromInterfaceCoercer instance()
  {
    return _instance;
  }

  private FunctionFromInterfaceCoercer() {}

  public boolean handlesNull()
  {
    return false;
  }

  public Object coerceValue( IType typeToCoerceTo, final Object value )
  {
    return GosuShop.createFunctionFromInterface( typeToCoerceTo, value );
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public static boolean areTypesCompatible( IFunctionType functionType, IType interfaceType )
  {
    if( interfaceType.isInterface() && interfaceType instanceof IJavaType)
    {
      IJavaType javaIntrinsicType = (IJavaType)interfaceType;
      List<? extends IMethodInfo> list = javaIntrinsicType.getTypeInfo().getMethods();
      int nonObjectMethods = 0;
      for( IMethodInfo iMethodInfo : list )
      {
        if( !iMethodInfo.getOwnersType().equals( IJavaType.OBJECT ) && iMethodInfo instanceof IJavaMethodInfo )
        {
          nonObjectMethods++;
        }
      }
      if( nonObjectMethods == 1 )
      {
        IFunctionType tempFunctionType = GosuShop.createFunctionType( list.get( 0 ) );
        return functionType.isAssignableFrom( tempFunctionType );
      }
    }
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }
}