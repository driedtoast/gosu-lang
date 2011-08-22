package gw.lang.reflect.gs;

import gw.util.fingerprint.FP64;

import gw.lang.Scriptable;
import gw.lang.GosuShop;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.IIdeLoadableTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ISymbolTable;
import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.lang.tidb.IFeatureInfoId;
import gw.lang.tidb.IFeatureInfoRecord;
import gw.lang.tidb.ITypeInfoDatabaseIndexableTypeLoader;
import gw.lang.tidb.TypeInfoDatabaseInit;
import gw.config.CommonServices;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuClassTypeLoader extends TypeLoaderBase implements ITypeInfoDatabaseIndexableTypeLoader, IIdeLoadableTypeLoader
{
  public static final String GOSU_CLASS_FILE_EXT = ".gs";
  public static final String GOSU_ENHANCEMENT_FILE_EXT = ".gsx";
  public static final String[] ALL_NON_TEMPLATE_EXTS = {GOSU_CLASS_FILE_EXT, GOSU_ENHANCEMENT_FILE_EXT};

  // These constants are only here because api can't depend on impl currently; they shouldn't be considered
  // part of the API proper
  public static final String BLOCK_PREFIX = "block_";
  public static final String INNER_BLOCK_PREFIX = "." + BLOCK_PREFIX;
  public static final String BLOCK_POSTFIX = "_";
  public static final String ANNOTATION_POSTFIX = "_AnnotationMap";

  private Set<String> _allTypeNames;
  private IGosuClassRepository _repository;
  private IEnhancementIndex _enhancementIndex;

  public static GosuClassTypeLoader getDefaultClassLoader()
  {
    return TypeSystem.getTypeLoader( GosuClassTypeLoader.class );
  }

  public static GosuClassTypeLoader getDefaultClassLoader(IModule module)
  {
    return TypeSystem.getTypeLoader( GosuClassTypeLoader.class, module );
  }

  public GosuClassTypeLoader( IGosuClassRepository repository )
  {
    _repository = repository;
    _enhancementIndex = GosuShop.createEnhancementIndex( this );
  }

  public GosuClassTypeLoader(IModule module, IGosuClassRepository repository) {
    super(module);
    _repository = repository;
    _enhancementIndex = GosuShop.createEnhancementIndex( this );
  }

  public IGosuClassRepository getRepository()
  {
    return _repository;
  }

  public IEnhancementIndex getEnhancementIndex()
  {
    return _enhancementIndex;
  }

  @Override
  public ICompilableType getType( String strFullyQualifiedName )
  {
    IGosuClass adapterClass = getAdapterClass( strFullyQualifiedName );
    if( adapterClass != null )
    {
      return adapterClass;
    }

    if( strFullyQualifiedName.startsWith( IGosuFragment.FRAGMENT_PACKAGE )
        && !isBlock( strFullyQualifiedName ) ) 
    {
      return GosuShop.findFragment( strFullyQualifiedName );
    }

    if( strFullyQualifiedName.startsWith( IGosuProgram.PACKAGE + '.' )
        && !isBlock( strFullyQualifiedName ) )
    { 
      return (ICompilableType)getModule().getModuleTypeLoader().getTypeRefFactory().get( strFullyQualifiedName );
    }
    _enhancementIndex.maybeLoadEnhancementIndex();

    IType type = TypeSystem.getCompilingType( strFullyQualifiedName );
    ITypeLoader typeLoader = type != null ? type.getTypeLoader() : null;
    if( type != null && typeLoader != this )
    {
//      TypeSystem.getExecutionEnvironment().logI(strFullyQualifiedName, 
//          "  GCTL.getType(" + strFullyQualifiedName + 
//          ") compiling by alternate loader " + typeLoader + " in module " + typeLoader.getOldModule() + ". " + 
//          "This loader is in module " + this.getOldModule() + "." + 
//          "Current module " + TypeSystem.getCurrentModule() + "." +
//          " Returning null.");
      return null;
    }

    if( type == null )
    {
      return  findClass( strFullyQualifiedName );
    }

    return (IGosuClass)type;
  }

  private IGosuClass getAdapterClass( String strFullyQualifiedName )
  {
    if( getClass() == GosuClassTypeLoader.class )
    {
      if( strFullyQualifiedName.length() > IGosuClass.PROXY_PREFIX.length() &&
          strFullyQualifiedName.startsWith( IGosuClass.PROXY_PREFIX ) )
      {
        IType javaType = TypeSystem.getByFullNameIfValid( IGosuClass.ProxyUtil.getNameSansProxy( strFullyQualifiedName ) );
        if( javaType instanceof IJavaType )
        {
          IGosuClass adapterClass = ((IJavaType)javaType).getAdapterClass();
          if( adapterClass == null )
          {
            adapterClass = ((IJavaType)javaType).createAdapterClass();
          }
          return adapterClass;
        }
      }
    }
    return null;
  }

  @Override
  public Set<String> getAllNamespaces()
  {
    if( _namespaces == null )
    {
      Set<String> namespaces = new HashSet<String>(super.getAllNamespaces());
      namespaces.add( "Libraries" );
      _namespaces = Collections.unmodifiableSet(namespaces);
      return _namespaces;
    }
    return super.getAllNamespaces();
  }

  @Override
  @Scriptable(ScriptabilityModifier.ALL)
  public URL getResource(String name) {
    return _repository.findResource(name);
  }

  @Override
  public Set<? extends CharSequence> getAllTypeNames()
  {
    if( _allTypeNames == null )
    {
      _allTypeNames = Collections.unmodifiableSet(_repository.getAllTypeNames());
    }
    return _allTypeNames;
  }

  @Override
  public Collection<? extends CharSequence> getAllIdeLoadableTypeNames( boolean inInternalMode )
  {
    Set<? extends CharSequence> names = getAllTypeNames();
    if( inInternalMode )
    {
      return names;
    }
    else
    {
      // If the type info database is inited (shouldn't it always?), then get the list from there.
      Collection<CharSequence> visibleNames = new HashSet<CharSequence>();
      if( TypeInfoDatabaseInit.isInitialized() )
      {
        Set<IFeatureInfoRecord> recordSet = TypeInfoDatabaseInit.getFeatureInfoRecordFinder().findUsages(
          TypeSystem.getByFullName( "gw.lang.Export" ).getTypeInfo() );
        recordSet.addAll( TypeInfoDatabaseInit.getFeatureInfoRecordFinder().findUsages(
          TypeSystem.getByFullName( "gw.lang.ReadOnly" ).getTypeInfo() ) );
        for( IFeatureInfoRecord infoRecord : recordSet )
        {
          if( infoRecord.getDefUse() == IFeatureInfoRecord.DefUse.READ && IFeatureInfoId.FeatureType.TYPEREF == infoRecord.getEnclosingFeatureInfoID().getFeatureType() )
          {
            visibleNames.add( infoRecord.getQualifyingEnclosingFeatureInfoID().getFeatureName().toString() );
          }
        }
      }
      for( CharSequence name : names )
      {
        ICompilableType gsClass = getType( name.toString() );
        if (gsClass instanceof IGosuEnhancement ) {
          visibleNames.add(name);
        }
        if( gsClass != null )
        {
          String fileName = gsClass.getSourceFileHandle().getResourceName();
          if( fileName != null )
          {
            File sourceFile = new File( fileName );
            if( sourceFile.exists() && CommonServices.getEntityAccess().isVisibleType( sourceFile ) )
            {
              visibleNames.add( name );
              continue;
            }
          }
          if( !TypeInfoDatabaseInit.isInitialized() )
          {
            // Else get the list the hard way.
            if( gsClass.getTypeInfo().hasAnnotation( TypeSystem.getByFullNameIfValid( "gw.lang.Export" ) ) ||
                gsClass.getTypeInfo().hasAnnotation( TypeSystem.getByFullNameIfValid( "gw.lang.ReadOnly" ) ) )
            {
              visibleNames.add( name );
            }
          }
        }
      }
      return visibleNames;
    }
  }

  @Override
  public void refresh(boolean clearCachedTypes)
  {
    super.refresh(clearCachedTypes);
    if (clearCachedTypes) {
      _allTypeNames = null;
      _namespaces = null;
      if (_repository instanceof IFileSystemGosuClassRepository) {
        ((IFileSystemGosuClassRepository)_repository).clearPackageCache();
        ((IFileSystemGosuClassRepository)_repository).clearMissCache();
      }
      GosuShop.clearFragments();
    }
  }

  @Override
  public List<String> getHandledPrefixes()
  {
    return Collections.emptyList();
  }

  public void unloadAll()
  {
    _allTypeNames = null;
  }

  public IGosuClass makeNewClass( ISourceFileHandle sourceFile )
  {
    return makeNewClass( sourceFile, null );
  }

  public IGosuClass makeNewClass( ISourceFileHandle sourceFile, ISymbolTable programSymTable )
  {
    if( sourceFile.getClassType() == ClassType.Enhancement )
    {
      return GosuShop.createEnhancement( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap() );
    }
    else if( sourceFile.getClassType() == ClassType.Program )
    {
      return GosuShop.createProgram( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap(), programSymTable );
    }
    else if( sourceFile.getClassType() == ClassType.Eval )
    {
      return GosuShop.createProgramForEval( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap(), programSymTable );
    }
    else
    {
      return GosuShop.createClass( sourceFile.getTypeNamespace(), sourceFile.getRelativeName(), this, sourceFile, getTypeUsesMap() );
    }
  }

  private IGosuClass getBlockType( String strName )
  {
    IGosuClass classInternal = null;
    if( isBlock( strName ) )
    {
      try
      {
        String strippedName = strName.substring( 0, strName.length() - BLOCK_POSTFIX.length() );
        int iStr = strippedName.lastIndexOf( INNER_BLOCK_PREFIX );
        String indexStr = strippedName.substring( iStr + INNER_BLOCK_PREFIX.length(), strippedName.length() );
        int i = Integer.parseInt( indexStr );
        String enclosingClassStr = strippedName.substring( 0, iStr );

        IType type = getBlockType( enclosingClassStr );
        if( type == null )
        {
          type = TypeSystem.getByFullNameIfValid( enclosingClassStr.replace( '$', '.' ) );
        }

        if( type instanceof ICompilableType )
        {
          classInternal = ((ICompilableType)type).getBlock( i );
        }
      }
      catch( NumberFormatException e )
      {
        //ignore
      }
      catch( IndexOutOfBoundsException e )
      {
        //ignore
      }
    }
    return classInternal;
  }

  private boolean isBlock( String strName )
  {
    return strName.endsWith( BLOCK_POSTFIX ) && strName.contains( INNER_BLOCK_PREFIX );
  }


  private IGosuClass findClass( String strQualifiedClassName )
  {
    IGosuClass blockType = getBlockType( strQualifiedClassName );
    if( blockType != null )
    {
      return blockType;
    }

    ISourceFileHandle sourceFile = _repository.findClass( strQualifiedClassName );

    if( sourceFile == null || !sourceFile.isValid() )
    {
      return null;
    }

    IGosuClass gsClass;
    if( sourceFile.getParentType() != null )
    {
      // It's an inner class

      IGosuClass enclosingType = (IGosuClass)TypeSystem.getByFullNameIfValid( sourceFile.getParentType() );
      gsClass = (IGosuClass)enclosingType.getInnerClass( sourceFile.getRelativeName() );
    }
    else
    {
      // It's a top-level class

      gsClass = makeNewClass( sourceFile );
    }

    return gsClass;
  }

  protected ITypeUsesMap getTypeUsesMap()
  {
    return null;
  }

  @Override
  public Runnable createdType( final String fullyQualifiedTypeName )
  {
    if( TypeSystem.getCurrentModule() != getModule() )
    {
      // Only care if the type was created in my module
      return null;
    }
    
    super.createdType( fullyQualifiedTypeName );
    Set<String> allNamespaces = new HashSet<String>(getAllNamespaces());
    TypeSystem.addNamespaceFromFqnToSet( allNamespaces, fullyQualifiedTypeName );
    _namespaces = Collections.unmodifiableSet(allNamespaces);
    _repository.notifyOfTypeCreation( fullyQualifiedTypeName );
    return
      new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            IType type = TypeSystem.getByFullNameIfValid( fullyQualifiedTypeName );
            if( type instanceof IGosuEnhancement )
            {
              _enhancementIndex.resetIndexes();
            }
            if( type != null && type.getTypeLoader() == GosuClassTypeLoader.this )
            {
              if( _allTypeNames != null )
              {
                Set<String> allTypeNames = new HashSet<String>( _allTypeNames );
                allTypeNames.add( fullyQualifiedTypeName );
                _allTypeNames = Collections.unmodifiableSet( allTypeNames );
              }
            }
          }
          catch( Exception e )
          {
            CommonServices.getEntityAccess().getLogger().warn( "Unable to load type " + fullyQualifiedTypeName + " when detecting enhancement index reset", e );
            //ignore
          }
        }
    };
  }

  @Override
  public void deletedType( String fullyQualifiedTypeName )
  {
    if( TypeSystem.getCurrentModule() != getModule() )
    {
      return;
    }
    
    super.deletedType( fullyQualifiedTypeName );
    _enhancementIndex.resetIndexes();
    if (_allTypeNames != null && _allTypeNames.contains(fullyQualifiedTypeName)) {
      unloadAll();
    }
  }

  @Override
  public void refreshedType(IType type, boolean changed) {
    super.refreshedType(type, changed);
    _repository.notifyOfTypeRefresh(type.getName());
  }

  @Override
  public void refreshed()
  {
    super.refreshed();
    _repository.notifyOfTypeRefresh(null);    
  }
  
  @Override
  public boolean shouldIndex( String fullyQualifiedTypeName )
  {
    return true;
  }

  @Override
  public FP64 getFingerprint( String fullyQualifiedTypeName )
  {
    IType intrinsicType = TypeSystem.getByFullName( fullyQualifiedTypeName );
    return !(intrinsicType instanceof IGosuClass) ? null : ((IGosuClass)intrinsicType).getFingerprint();
  }

  public boolean shouldKeepDebugInfo( IGosuClass gsClass )
  {
    return gsClass.shouldKeepDebugInfo();
  }

  @Override
  public IType getIntrinsicType( Class javaClass )
  {
    if( javaClass.getClassLoader() instanceof IGosuClassLoader )
    {
      String name = javaClass.getName();
      ICompilableType clazz = getType( name );
      while( clazz == null && name.contains( "$" ) )
      {
        name = name.replaceFirst( "\\$", "." );
        clazz = getType( name );
      }
      return clazz;
    }
    else
    {
      return null;
    }
  }
}
