package gw.config;

import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.util.ILogger;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated this class will be removed in a future release
 */
public class TypeLoaderSpec
{

  String _typeloaderClassName = null;
  HashMap _additionalArgs = new HashMap();

  public TypeLoaderSpec( Node xmlNode )
  {
    NamedNodeMap map = xmlNode.getAttributes();
    int length = map.getLength() - 1;
    while( length >= 0 )
    {
      Node node = map.item( length );
      if( node.getNodeName().equals( Registry.TAG_CLASS ) )
      {
        _typeloaderClassName = node.getNodeValue();
      }
      else
      {
        _additionalArgs.put( node.getNodeName(), node.getNodeValue() );
      }
      length--;
    }
  }

  public String getTypeloaderClassName()
  {
    return _typeloaderClassName;
  }

  public HashMap getAdditionalArgs()
  {
    return _additionalArgs;
  }

  public ITypeLoader createTypeLoader( IExecutionEnvironment execEnv )
  {
    return createTypeLoader( execEnv, _typeloaderClassName, _additionalArgs );
  }

  public static ITypeLoader createTypeLoader( IExecutionEnvironment execEnv, String typeloaderClassName, HashMap additionalArgs )
  {
    ILogger logger = CommonServices.getEntityAccess().getLogger();
    ITypeLoader additionalTypeLoader = null;
    boolean gosu = false;
    try
    {
      Class clazz = TypeSystem.getCurrentModule().getClassLoader().loadClass( typeloaderClassName );
      Constructor constructor = getConstructor( clazz, IExecutionEnvironment.class, Map.class );
      if( constructor != null )
      {
        additionalTypeLoader = (ITypeLoader)constructor.newInstance( execEnv, additionalArgs );
      }
      else
      {
        constructor = getConstructor( clazz, IExecutionEnvironment.class );
        if( constructor != null )
        {
          additionalTypeLoader = (ITypeLoader)constructor.newInstance( execEnv );
        }
        else
        {
          constructor = getConstructor( clazz );
          if( constructor != null )
          {
            additionalTypeLoader = (ITypeLoader)constructor.newInstance();
          }
          else
          {
            logger.error( "The type loader " + typeloaderClassName + " does not have a callable constructor.  It must provide\n" +
                          "a constructor that takes either (IExecutionEnvironment, Map), (IExecutionEnvironment) or no paramters" );
          }
        }
      }
    }
    catch( Exception e )
    {
      try {
        IType type = TypeSystem.getByFullName(typeloaderClassName);
        gosu = true;
        IConstructorInfo constructor = getConstructor( type, TypeSystem.get(IExecutionEnvironment.class), TypeSystem.get(Map.class) );
        if( constructor != null )
        {
          additionalTypeLoader = (ITypeLoader)constructor.getConstructor().newInstance( execEnv, additionalArgs );
        }
        else
        {
          constructor = getConstructor( type, TypeSystem.get(IExecutionEnvironment.class) );
          if( constructor != null )
          {
            additionalTypeLoader = (ITypeLoader)constructor.getConstructor().newInstance( execEnv );
          }
          else
          {
            constructor = getConstructor( type );
            if( constructor != null )
            {
              additionalTypeLoader = (ITypeLoader)constructor.getConstructor().newInstance();
            }
            else
            {
              logger.error( "The type loader " + typeloaderClassName + " does not have a callable constructor.  It must provide\n" +
                            "a constructor that takes either (IExecutionEnvironment, Map), (IExecutionEnvironment) or no paramters" );
            }
          }
        }
      } catch (Exception e2) {
        logger.error( "Unable to load type loader " + typeloaderClassName, gosu ? e2 : e );
        return null;
      }
    }
    return additionalTypeLoader;
  }

  private static Constructor getConstructor( Class clazz, Class... argTypes )
  {
    try
    {
      return clazz.getConstructor( argTypes );
    }
    catch( NoSuchMethodException e )
    {
      return null;
    }
  }

  private static IConstructorInfo getConstructor(IType type, IType... argTypes) {
    return type.getTypeInfo().getConstructor(argTypes);
  }

}
