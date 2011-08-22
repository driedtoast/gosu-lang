package gw.lang.reflect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gw.config.CommonServices;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;

/**
 * Manages features for types that have inheritence.
 *
 * Types that can inherit other types (either through direct inheritence or through interfaces) need to expose all
 * properties and methods for all types in the hierarchy.  This class helps manage this feature by providing a
 * caching store for all properties.
 *
 * This class also provides methods to fetch the public, internal, protected, and private methods of a class.
 *
 * This class will lazily load features when the features are first accessed.  It loads all features at once, methods,
 * properties, and constructors.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"unchecked"})
public class FeatureManager<T extends CharSequence> {

  private enum InitState {
    NotInitialized,
    Initializing,
    ERROR, Initialized
  }
  private IRelativeTypeInfo _typeInfo;
  private volatile Map<IModule, InitState> _isinitialized = new HashMap<IModule, InitState>();
  private volatile InitState _ctorsInitialized = InitState.NotInitialized;
  private Map<IModule, PropertyNameMap<T>[]> _properties = new HashMap<IModule, PropertyNameMap<T>[]>(); 
//    new PropertyNameMap[IRelativeTypeInfo.Accessibility_Size];
  private Map<IModule, List<IMethodInfo>[]> _methods = new HashMap<IModule, List<IMethodInfo>[]>(); 
//    new List[IRelativeTypeInfo.Accessibility_Size];
  private List<IConstructorInfo>[] _constructors = new List[IRelativeTypeInfo.Accessibility_Size];
  private final boolean _caseSensitive;
  private final boolean _addObjectMethods;
  private String _superPropertyPrefix;
  private IType _supertypeToCopyPropertiesFrom;

  public FeatureManager(IRelativeTypeInfo typeInfo, boolean caseSensitive) {
    this(typeInfo, caseSensitive, false);
  }

  public FeatureManager(IRelativeTypeInfo typeInfo, boolean caseSensitive, boolean addObjectMethods) {
    _typeInfo = typeInfo;
    _caseSensitive = caseSensitive;
    _addObjectMethods = addObjectMethods;
  }

  public void clear() {
    _isinitialized = new HashMap<IModule, InitState>();
    _ctorsInitialized = InitState.NotInitialized;
    clearMaps();
  }

  private void clearMaps() {
    for(PropertyNameMap<T>[] properties : _properties.values()) {
      for (int i = 0; i < properties.length; i++) {
        properties[i] = null;
      }
    }
    for (int i = 0; i < _constructors.length; i++) {
      _constructors[i] = null;
    }
    for(List<IMethodInfo>[] methods : _methods.values()) {
      for (int i = 0; i < methods.length; i++) {
        methods[i] = null;
      }
    }
  }
  
  private void clearMaps(IModule module) {
    PropertyNameMap<T>[] properties = _properties.get(module);
    if(properties != null) {
      for (int i = 0; i < properties.length; i++) {
        properties[i] = null;
      }
    }
    List<IMethodInfo>[] methods = _methods.get(module);
    if(methods != null) {
      for (int i = 0; i < methods.length; i++) {
        methods[i] = null;
      }
    }
  }
  
  private void clearCtors() {
    for (int i = 0; i < _constructors.length; i++) {
      _constructors[i] = null;
    }
  }

  public static IRelativeTypeInfo.Accessibility getAccessibilityForClass( IType ownersClass, IType whosAskin )
  {
    if( TypeSystem.isIncludeAll() )
    {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }

    if( ownersClass == null || whosAskin == null )
    {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    }

    if( getTopLevelTypeName( whosAskin ).equalsIgnoreCase( getTopLevelTypeName( ownersClass ) ) )
    {
      // Implies private members, which means everything.
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }
    else if( Modifier.isPrivate( ownersClass.getModifiers() ) )
    {
      return IRelativeTypeInfo.Accessibility.NONE;
    }
    else if( isInSameNamespace( ownersClass, whosAskin ) )
    {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
    else if( Modifier.isInternal( ownersClass.getModifiers() ) )
    {
      return IRelativeTypeInfo.Accessibility.NONE;
    }    
    else if( isInEnclosingClassHierarchy( ownersClass, whosAskin ) )
    {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    }

    return IRelativeTypeInfo.Accessibility.PUBLIC;
  }

  public static boolean isInSameNamespace(IType ownersClass, IType whosAskin) {
    String whosAskinNamespace = getTopLevelEnclosingClassNamespace(whosAskin);
    return (whosAskinNamespace != null && whosAskinNamespace.equalsIgnoreCase(getTopLevelEnclosingClassNamespace(ownersClass)));
  }

  private static String getTopLevelEnclosingClassNamespace(IType type) {
    IType topLevelClass = type;
    while (topLevelClass.getEnclosingType() != null) {
      topLevelClass = topLevelClass.getEnclosingType();
    }
    return topLevelClass.getNamespace();
  }

  public static boolean isInEnclosingClassHierarchy(IType ownersClass, IType whosAskin) {
    return whosAskin != null &&
           (isInHierarchy(ownersClass, whosAskin) ||
            isInEnhancedTypesHierarchy(ownersClass, whosAskin) ||
            isInEnclosingClassHierarchy(ownersClass, whosAskin.getEnclosingType()));  
  }

  protected static boolean isInEnhancedTypesHierarchy(IType ownersClass, IType whosAskin) {
    return (whosAskin instanceof IGosuEnhancement &&
           ((IGosuEnhancement)whosAskin).getEnhancedType() != null &&
           ownersClass.isAssignableFrom(((IGosuEnhancement) whosAskin).getEnhancedType()));
  }

  protected static boolean isInHierarchy(IType ownersClass, IType whosAskin) {
    return ownersClass.isAssignableFrom(whosAskin) ||
           (ownersClass instanceof IGosuClass && ((IGosuClass) ownersClass).isSubClass( whosAskin ));
  }

  private static String getTopLevelTypeName( IType type )
  {
    while( type.getEnclosingType() != null )
    {
      type = TypeSystem.getPureGenericType( type.getEnclosingType() );
    }
    return TypeSystem.getPureGenericType( type ).getName();
  }

  @SuppressWarnings({"unchecked"})
  public List<IPropertyInfo> getProperties( IRelativeTypeInfo.Accessibility accessibility, IModule module ) {
    maybeInit(module);
    PropertyNameMap<T>[] arr = _properties.get( module );
    if( arr == null )
    {
      return Collections.emptyList();
    }
    PropertyNameMap<T> props = arr[accessibility.ordinal()];
    return (List<IPropertyInfo>) (props == null ? Collections.emptyList() : props.values());
  }

  public IPropertyInfo getProperty( IRelativeTypeInfo.Accessibility accessibility, IModule module, CharSequence propName ) {
    maybeInit(module);
    PropertyNameMap<T>[] arr = _properties.get( module );
    if( arr == null )
    {
      return null;
    }
    PropertyNameMap<T> accessMap = arr[accessibility.ordinal()];
    return accessMap == null ? null : accessMap.get(convertCharSequenceToCorrectSensitivity(propName));
  }

  private T convertCharSequenceToCorrectSensitivity(CharSequence propName) {
    return (T) (_caseSensitive ? propName.toString() : CaseInsensitiveCharSequence.get(propName));
  }

  @SuppressWarnings({"unchecked"})
  public Collection<T> getPropertyNames(IRelativeTypeInfo.Accessibility accessibility, IModule module) {
    maybeInit(module);
    PropertyNameMap<T>[] lists = _properties.get( module );
    if( lists == null )
    {
      return Collections.emptyList();
    }
    return lists[accessibility.ordinal()].keySet();
  }

  @SuppressWarnings({"unchecked"})
  public List<? extends IMethodInfo> getMethods( IRelativeTypeInfo.Accessibility accessibility, IModule module ) {
    maybeInit(module);
    List<IMethodInfo>[] arr = _methods.get( module );
    if( arr == null )
    {
      return Collections.emptyList();
    }
    List<IMethodInfo> iMethodInfos = arr[accessibility.ordinal()];
    return (List<IMethodInfo>) (iMethodInfos == null ? Collections.emptyList() : iMethodInfos);
  }

  public IMethodInfo getMethod( IRelativeTypeInfo.Accessibility accessibility, IModule module, CharSequence methodName, IType... params ) {
    maybeInit(module);
    return ITypeInfo.FIND.method( getMethods( accessibility, module ), methodName, params );
  }

  @SuppressWarnings({"unchecked"})
  public List<? extends IConstructorInfo> getConstructors( IRelativeTypeInfo.Accessibility accessibility ) {
    maybeInit();
    List<IConstructorInfo> list = _constructors[accessibility.ordinal()];
    return (List<IConstructorInfo>) (list == null ? Collections.emptyList() : list);
  }

  public IConstructorInfo getConstructor( IRelativeTypeInfo.Accessibility accessibility, IType[] params ) {
    maybeInit();
    return ITypeInfo.FIND.constructor( getConstructors(accessibility), params );
  }

  @SuppressWarnings({"unchecked"})
  protected void maybeInit() {
    maybeInit(_typeInfo.getOwnersType().getTypeLoader().getModule());
  }
  
  @SuppressWarnings({"ConstantConditions"})
  private void maybeInit(IModule module) {
    if (_isinitialized.get(module) != InitState.Initialized && _isinitialized.get(module) != InitState.ERROR) {
      TypeSystem.lock();
      try {
        if (_isinitialized.get(module) != InitState.Initialized) {
          if (_isinitialized.get(module) == InitState.Initializing) {
            throw new IllegalStateException("Properties for " + _typeInfo.getOwnersType() + " are cyclic.");
          }
          _isinitialized.put(module, InitState.Initializing);
          clearMaps(module);
          try {
           PropertyNameMap<T>[] properties = new PropertyNameMap[IRelativeTypeInfo.Accessibility_Size];
            {
              PropertyNameMap privateProps = new PropertyNameMap();
              for (IType type : _typeInfo.getOwnersType().getInterfaces()) {
                mergeProperties(privateProps, convertType(type), false);
              }
              IType supertype = _typeInfo.getOwnersType().getSupertype();
              if ( supertype != null ) {
                mergeProperties( privateProps, convertType( supertype ), true );
              }

              List<IPropertyInfo> declaredProperties = (List<IPropertyInfo>) _typeInfo.getDeclaredProperties();
              for (IPropertyInfo property : declaredProperties) {
                mergeProperty(privateProps, property, true);
              }
              addEnhancementProperties(privateProps, _caseSensitive);
              privateProps.freeze();
              // The size checking madness is to save memory.  If the lists/maps are the same then reuse.
              properties[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = privateProps;
              properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.PROTECTED));
              if (properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateProps.size()) {
                properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateProps;
              }
              properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.INTERNAL));
              if (properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
              }
              properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.PUBLIC));
              if (properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
              }
              properties[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = new PropertyNameMap();
            }
            _properties.put(module, properties);

            List<IMethodInfo>[] methods = new List[IRelativeTypeInfo.Accessibility_Size];
            {
              List<IMethodInfo> privateMethods = new ArrayList<IMethodInfo>();
              if( _addObjectMethods ) {
                mergeMethods( privateMethods, convertType( IJavaType.OBJECT ), false );
              }
              for (IType type : _typeInfo.getOwnersType().getInterfaces()) {
                mergeMethods(privateMethods, convertType(type), false);
              }
              if ( _typeInfo.getOwnersType().getSupertype() != null) {
                mergeMethods(privateMethods, convertType( _typeInfo.getOwnersType().getSupertype()), true);
              }
              List<? extends IMethodInfo> declaredMethods = _typeInfo.getDeclaredMethods();
              for (IMethodInfo methodInfo : declaredMethods) {
                mergeMethod(privateMethods, methodInfo, true);
              }
              addEnhancementMethods(privateMethods);
              ((ArrayList) privateMethods).trimToSize();
              privateMethods = Collections.unmodifiableList(privateMethods);
              // The size checking madness is to save memory.  If the lists/maps are the same then reuse.
              methods[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = Collections.unmodifiableList(privateMethods);
              methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = Collections.unmodifiableList((List<IMethodInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.PROTECTED));
              if (methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateMethods.size()) {
                methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateMethods;
              }
              methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = Collections.unmodifiableList((List<IMethodInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.INTERNAL));
              if (methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
              }
              methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = Collections.unmodifiableList((List<IMethodInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.PUBLIC));
              if (methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
              }
              methods[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = Collections.emptyList();
            }
            _methods.put(module, methods);

            if(_ctorsInitialized != InitState.Initialized && _ctorsInitialized != InitState.ERROR) {
              clearCtors();
              try {
                
                List<IConstructorInfo>[] constructors = new List[IRelativeTypeInfo.Accessibility_Size];
                {
                  List<IConstructorInfo> privateMethods = new ArrayList<IConstructorInfo>( _typeInfo.getDeclaredConstructors());
                  ((ArrayList) privateMethods).trimToSize();
                  privateMethods = Collections.unmodifiableList(privateMethods);
                  constructors[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = Collections.unmodifiableList(privateMethods);
                  constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.PROTECTED));
                  if (constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateMethods.size()) {
                    constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateMethods;
                  }
                  constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.INTERNAL));
                  if (constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                    constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
                  }
                  constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateMethods, IRelativeTypeInfo.Accessibility.PUBLIC));
                  if (constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                    constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
                  }
                  constructors[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = Collections.emptyList();
                }
                _constructors = constructors;
                _ctorsInitialized = InitState.Initialized;
              } finally {
                if(_ctorsInitialized != InitState.Initialized) {
                  _ctorsInitialized = InitState.ERROR;
                }
              }
            }
            _isinitialized.put(module, InitState.Initialized);
          } finally {
            if (_isinitialized.get(module) != InitState.Initialized) {
              _isinitialized.put(module, InitState.ERROR);
            }
          }
        }
      } catch ( Exception ex ) {
        ex.printStackTrace(); // exception is swallowed by source diff handler? print it again here
        throw GosuExceptionUtil.forceThrow( ex, _typeInfo.getOwnersType().getName() );
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  protected IType convertType(IType type) {
    return type;
  }

  protected void addEnhancementMethods(List<IMethodInfo> privateMethods) {
    CommonServices.getEntityAccess().addEnhancementMethods( _typeInfo.getOwnersType(), privateMethods );
  }

  protected void addEnhancementProperties(PropertyNameMap<T> privateProps, boolean caseSensitive) {
    CommonServices.getEntityAccess().addEnhancementProperties( _typeInfo.getOwnersType(), privateProps, caseSensitive );
  }

  public void setSuperPropertyPrefix( String superPropertyPrefix ) {
    _superPropertyPrefix = superPropertyPrefix;
  }

  public void setSupertypeToCopyPropertiesFrom( IType supertypeToCopyPropertiesFrom ) {
    _supertypeToCopyPropertiesFrom = supertypeToCopyPropertiesFrom;
  }

  private PropertyNameMap<T> convertToMap(List<IPropertyInfo> features) {
    PropertyNameMap<T> ret = new PropertyNameMap();
    for (IPropertyInfo feature : features) {
      ret.put(convertCharSequenceToCorrectSensitivity(feature.getName()), feature);
    }
    ret.freeze();
    return ret;
  }

  private List filterFeatures(List props, IRelativeTypeInfo.Accessibility accessibility) {
    ArrayList<IFeatureInfo> ret = new ArrayList<IFeatureInfo>();
    for (Object o : props) {
      IAttributedFeatureInfo property = (IAttributedFeatureInfo) o;
      if (isFeatureAccessible(property, accessibility)) {
        ret.add(property);
      }
    }
    ret.trimToSize();
    return ret;
  }

  public static boolean isFeatureAccessible(IAttributedFeatureInfo property, IRelativeTypeInfo.Accessibility accessibility) {
    boolean isAccessible = false;
    switch (accessibility) {
      case NONE:
        break;        
      case PUBLIC:
        if (property.isPublic()) {
          isAccessible = true;
        }
        break;
      case PROTECTED:
        if (property.isPublic() || property.isProtected()) {
          isAccessible = true;
        }
        break;
      case INTERNAL:
        if (property.isPublic() || property.isInternal() || property.isProtected()) {
          isAccessible = true;
        }
        break;
      case PRIVATE:
        if (property.isPublic() || property.isInternal() || property.isProtected() || property.isPrivate()) {
          isAccessible = true;
        }
        break;
    }
    return isAccessible;
  }

  protected void mergeProperties(PropertyNameMap<T> props, IType type, boolean replace) {
    if( type != null )
    {
      List<? extends IPropertyInfo> propertyInfos;
      if (type.getTypeInfo() instanceof IRelativeTypeInfo) {
        propertyInfos = ((IRelativeTypeInfo) type.getTypeInfo()).getProperties( _typeInfo.getOwnersType());
      } else {
        propertyInfos = type.getTypeInfo().getProperties();
      }
      for (IPropertyInfo propertyInfo : propertyInfos) {
        IType ownersType = propertyInfo.getOwnersType();
        if ( _supertypeToCopyPropertiesFrom == null || ownersType.isAssignableFrom( _supertypeToCopyPropertiesFrom ) || ownersType instanceof IGosuEnhancement ) {
          mergeProperty( props, propertyInfo, replace );
        }
      }
    }
  }

  protected void mergeProperty(PropertyNameMap<T> props, IPropertyInfo propertyInfo, boolean replace) {
    boolean prependPrefix = _superPropertyPrefix != null && ! propertyInfo.getOwnersType().equals( _typeInfo.getOwnersType() );
    T cs = convertCharSequenceToCorrectSensitivity( prependPrefix ? ( _superPropertyPrefix + propertyInfo.getName() ) : propertyInfo.getName() );
    if (replace || !props.containsKey(cs)) {
      if ( prependPrefix ) {
        props.put( cs, new PropertyInfoBuilder().like( propertyInfo ).withName( cs.toString() ).build( propertyInfo.getContainer() ) );
      }
      else {
        props.put(cs, propertyInfo);
      }
    }
  }

  protected void mergeMethods(List<IMethodInfo> methods, IType type, boolean replace) {
    List<? extends IMethodInfo> methodInfos;
    if (type != null) {
      if (type.getTypeInfo() instanceof IRelativeTypeInfo) {
        methodInfos = ((IRelativeTypeInfo) type.getTypeInfo()).getMethods( _typeInfo.getOwnersType());
      } else {
        methodInfos = type.getTypeInfo().getMethods();
      }

      for (IMethodInfo methodInfo : methodInfos) {
        mergeMethod(methods, methodInfo, replace);
      }
    }
  }

  protected void mergeMethod(List<IMethodInfo> methods, IMethodInfo thisMethodInfo, boolean replace) {
    IParameterInfo[] thisMethodParameters;
    if (thisMethodInfo instanceof IJavaMethodInfo) {
      thisMethodParameters = ((IJavaMethodInfo)thisMethodInfo).getGenericParameters();
    } else {
      thisMethodParameters = thisMethodInfo.getParameters();
    }
    boolean add = true;
    int replacementIndex = -1;
    for (int i = 0; i < methods.size(); i++) {
      IMethodInfo superMethodInfo = methods.get(i);
      replacementIndex++;
      if (superMethodInfo.getDisplayName().equalsIgnoreCase(thisMethodInfo.getDisplayName())) {
        IParameterInfo[] superMethodParameters;
        if (superMethodInfo instanceof IJavaMethodInfo ) {
          superMethodParameters = ((IJavaMethodInfo) superMethodInfo).getGenericParameters();
        } else {
          superMethodParameters = superMethodInfo.getParameters();
        }
        if (argsEqual(superMethodParameters, thisMethodParameters)) {
          if (replace) {
            methods.set(replacementIndex, thisMethodInfo);
          }
          add = false;
          break;
        }
      }
    }

    if (add) {
      methods.add(thisMethodInfo);
    }
  }

  protected boolean areMethodParamsEqual(IParameterInfo thisMethodParam, IParameterInfo superMethodParam) {
    IType thisMethodParamType = thisMethodParam.getFeatureType();
    IType superMethodParamType = superMethodParam.getFeatureType();
    if(thisMethodParamType instanceof ITypeVariableType && superMethodParamType instanceof ITypeVariableType) {
      return ((ITypeVariableType)thisMethodParamType).getBoundingType().equals(((ITypeVariableType)superMethodParamType).getBoundingType());
    }
    return thisMethodParamType.equals(superMethodParamType);
  }
  
  private boolean argsEqual(IParameterInfo[] parameters, IParameterInfo[] parameters1) {
    if (parameters.length == parameters1.length) {
      for (int i = 0; i < parameters.length; i++) {
        IParameterInfo parameter = parameters[i];
        if ( !areMethodParamsEqual(parameter, parameters1[i]) ) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
