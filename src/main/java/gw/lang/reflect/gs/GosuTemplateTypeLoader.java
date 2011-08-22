package gw.lang.reflect.gs;

import gw.util.fingerprint.FP64;
import gw.util.StreamUtil;
import gw.config.CommonServices;
import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.IIdeLoadableTypeLoader;
import gw.lang.GosuShop;
import gw.lang.Scriptable;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.lang.tidb.ITypeInfoDatabaseIndexableTypeLoader;

import java.io.Reader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.net.URL;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuTemplateTypeLoader extends TypeLoaderBase implements ITypeInfoDatabaseIndexableTypeLoader, IIdeLoadableTypeLoader
{
  public static final String GOSU_TEMPLATE_FILE_EXT = ".gst";

  public static GosuTemplateTypeLoader getDefaultTypeLoader()
  {
    return TypeSystem.getTypeLoader( GosuTemplateTypeLoader.class );
  }

  private Set<String> _allTypeNames;
  
  private IGosuClassRepository _repository;

  public GosuTemplateTypeLoader( IGosuClassRepository classRepository) {
    _repository = classRepository;
  }
  
  public GosuTemplateTypeLoader( IModule module, IGosuClassRepository classRepository) {
    super(module);
    _repository = classRepository;
  }

  @Override
  public FP64 getFingerprint(String fullyQualifiedTypeName) {
    IType intrinsicType = TypeSystem.getByFullName( fullyQualifiedTypeName );
    return !(intrinsicType instanceof ITemplateType) ? null : ((ITemplateType)intrinsicType).getFingerprint();
  }

  public IGosuClassRepository getRepository() {
    return _repository;
  }

  @Override
  public boolean shouldIndex(String fullyQualifiedTypeName) {
    return true;
  }

  @Override
  public Collection<? extends CharSequence> getAllIdeLoadableTypeNames(boolean inInternalMode) {
    return getAllTypeNames();
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
    return new Runnable() {
      @Override
      public void run() {
        try
        {
          IType type = TypeSystem.getByFullNameIfValid( fullyQualifiedTypeName );
          if (type != null && type.getTypeLoader() == GosuTemplateTypeLoader.this) {
            if (_allTypeNames != null) {
              Set<String> allTypeNames = new HashSet<String>(_allTypeNames);
              allTypeNames.add(fullyQualifiedTypeName);
              _allTypeNames = Collections.unmodifiableSet(allTypeNames);
            }
          }
        }
        catch ( Exception e )
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
    if (_allTypeNames != null && _allTypeNames.contains(fullyQualifiedTypeName)) {
      _allTypeNames = null;
    }
  }

  @Override
  public Set<? extends CharSequence> getAllTypeNames() {
    if( _allTypeNames == null )
    {
      _allTypeNames = Collections.unmodifiableSet(_repository.getAllNames( GOSU_TEMPLATE_FILE_EXT ));
    }
    return _allTypeNames;
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.emptyList();
  }

  @Override
  public IType getType(String fullyQualifiedName) {
    Reader reader = _repository.getTemporaryTemplateResourceReader(fullyQualifiedName);
    if (reader == null) {
      try {
        URL resource = _repository.findResource(fullyQualifiedName.replace('.', '/') + GOSU_TEMPLATE_FILE_EXT);
        if (resource != null) {
          reader = StreamUtil.getInputStreamReader(resource.openStream());
        }
      } catch (IOException e) {
        return null;
      }
    }

    ITemplateGenerator template = null;
    if (reader != null) {
      template = GosuShop.createSimpleTemplateHost().getTemplate(reader, fullyQualifiedName);
    }
    return template == null ? null : GosuShop.createTemplate(template.getFullyQualifiedTypeName(), template, this);
  }

  @Override
  public void refresh(boolean clearCachedTypes) {
    super.refresh(clearCachedTypes);
    if (clearCachedTypes) {
      _allTypeNames = null;
      _namespaces = null;
      if (_repository instanceof IFileSystemGosuClassRepository) {
        ((IFileSystemGosuClassRepository)_repository).clearPackageCache();
        ((IFileSystemGosuClassRepository)_repository).clearMissCache();
      }
    }
  }

  @Override
  @Scriptable(ScriptabilityModifier.ALL)
  public URL getResource(String name) {
    return _repository.findResource(name);
  }
  
}
