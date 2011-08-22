package gw.lang.reflect;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.lang.Scriptable;
import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.File;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class TypeLoaderBase extends BaseService implements ITypeLoader, ITypeLoaderListener
{
  private IModule _module;
  protected Set<String> _namespaces;

  protected TypeLoaderBase()
  {
    this( TypeSystem.getExecutionEnvironment().getCurrentModule() );
  }
  
  protected TypeLoaderBase( IModule module )
  {
    _module = module;
    TypeSystem.addTypeLoaderListenerAsWeakRef( this );
  }
  
  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public IType getIntrinsicType( Class javaClass )
  {
    return null;
  }
  
  @Override
  public IType getIntrinsicType( IJavaClassInfo javaClass )
  {
    return null;
  }

  @Override
  public boolean isCaseSensitive()
  {
    return false;
  }

  @Override
  public boolean isNamespaceOfTypeHandled(String fullyQualifiedTypeName) {
    int lastIndex = fullyQualifiedTypeName.lastIndexOf('.');
    if (lastIndex == -1) {
      return false;
    }

    String namespace = fullyQualifiedTypeName.substring(0, lastIndex);
    return getAllNamespaces().contains(namespace);
  }

  @Override
  public List<Throwable> getInitializationErrors() {
    return Collections.emptyList();
  }

  @Override
  public Set<String> getAllNamespaces()
  {
    if( _namespaces == null )
    {
      try
      {
        _namespaces = TypeSystem.getNamespacesFromTypeNames( getAllTypeNames(), new HashSet<String>() );
        if(_module != null) {
          _namespaces.addAll(TypeSystem.getNamespacesFromTypeNames(CommonServices.getJavaClassInfoProvider().getAllTypeNames(_module), _namespaces));
        }
      }
      catch( NullPointerException e )
      {
        //!! hack to get past dependency issue with tests
        return Collections.emptySet();
      }
    }
    return _namespaces;
  }

  @Override
  @Scriptable(ScriptabilityModifier.ALL)
  public URL getResource(String name) {
    return null;
  }

  @Override
  public File getResourceFile(String name) {
    URL resource = getResource(name);
    if (resource != null) {
      return new File(TypeSystem.getResourceFileResolver().resolveURLToFile(name, resource), name);
    } else {
      return null;
    }
  }

  @Override
  public void refresh(boolean clearCachedTypes)
  {
  }

  @Override
  public Runnable createdType( String fullyQualifiedTypeName )
  {
    return null;
  }

  @Override
  public void deletedType( String fullyQualifiedTypeName )
  {
    if( fullyQualifiedTypeName != null && fullyQualifiedTypeName.endsWith( '.' + IClassPath.PLACEHOLDER_FOR_PACKAGE ) )
    {
      _namespaces = null;
    }
  }

  @Override
  public void refreshedType(IType type, boolean changed)
  {
  }

  @Override
  public void refreshed()
  {
  }

  @Override
  protected void doInit()
  {
  }
}
