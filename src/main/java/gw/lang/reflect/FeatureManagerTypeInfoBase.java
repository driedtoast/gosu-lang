package gw.lang.reflect;

import gw.lang.reflect.module.IModule;

import java.util.Collections;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class FeatureManagerTypeInfoBase<T extends CharSequence> extends TypeInfoBase implements IRelativeTypeInfo
{
  private final IType _ownersType;
  private final FeatureManager<T> _fm;

  protected FeatureManagerTypeInfoBase( IType ownersType ) {
    _ownersType = TypeSystem.getOrCreateTypeReference( ownersType );
    _fm = new FeatureManager<T>( this, _ownersType.getTypeLoader().isCaseSensitive() );
  }

  public final IType getOwnersType() {
    return _ownersType;
  }

  public final Accessibility getAccessibilityForType( IType whosaskin )
  {
    return FeatureManager.getAccessibilityForClass( getOwnersType(), whosaskin == null ? getCompilingClass() : whosaskin );
  }

  public final List<? extends IPropertyInfo> getProperties()
  {
    return getProperties( null );
  }

  public final List<IPropertyInfo> getProperties( IType whosAskin )
  {
    return _fm.getProperties( getAccessibilityForType( whosAskin ), getModuleForType(whosAskin) );
  }

  public final IPropertyInfo getProperty( CharSequence propName )
  {
    return getProperty( null, propName );
  }

  public final CharSequence getRealPropertyName(CharSequence propName) {
    return FIND.findCorrectString( propName, _fm.getPropertyNames( Accessibility.PRIVATE, getOwnersType().getTypeLoader().getModule() ) );
  }

  public final IPropertyInfo getProperty( IType whosAskin, CharSequence propName )
  {
    return _fm.getProperty( getAccessibilityForType( whosAskin ), getModuleForType(whosAskin), propName );
  }

  public final List<? extends IMethodInfo> getMethods()
  {
    return getMethods( getCompilingClass() );
  }

  public final List<? extends IMethodInfo> getMethods( IType whosAskin )
  {
    return _fm.getMethods( getAccessibilityForType( whosAskin ), getModuleForType(whosAskin) );
  }

  @Override
  public List<? extends IMethodInfo> getMethods(IType whosaskin, IModule module) {
    return _fm.getMethods(getAccessibilityForType(whosaskin), module);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties(IType whosaskin, IModule module) {
    return _fm.getProperties(getAccessibilityForType(whosaskin), module);
  }

  public final List<? extends IConstructorInfo> getConstructors()
  {
    return getConstructors( null );
  }

  public final List<? extends IConstructorInfo> getConstructors( IType whosAskin )
  {
    return _fm.getConstructors( getAccessibilityForType( whosAskin ) );
  }

  public final IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  public final IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params )
  {
    List<? extends IMethodInfo> methods = getMethods( whosaskin );
    return FIND.method( methods, methodName, params );
  }

  public final IConstructorInfo getConstructor( IType... params )
  {
    return FIND.constructor( getConstructors(), params );
  }

  public final IConstructorInfo getConstructor( IType whosAskin, IType[] params )
  {
    List<? extends IConstructorInfo> ctors = getConstructors( whosAskin );
    return FIND.constructor( ctors, params );
  }

  public final List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  public final boolean hasAnnotation( IType type ) {
    return false;
  }

  public abstract List<? extends IPropertyInfo> getDeclaredProperties();

  public abstract List<? extends IMethodInfo> getDeclaredMethods();

  public abstract List<? extends IConstructorInfo> getDeclaredConstructors();

  public final IType getCompilingClass() {
    return getOwnersType();
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  private IModule getModuleForType(IType whosAskin) {
    IType moduleType = whosAskin == null ? getOwnersType() : whosAskin;
    if(moduleType instanceof ITypeVariableType) {
      moduleType = ((ITypeVariableType)moduleType).getBoundingType();
    }
    return moduleType.getTypeLoader().getModule();
  }

}
