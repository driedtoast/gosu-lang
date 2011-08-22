package gw.lang.reflect;

import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LazyVar;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class BaseHandBuiltTypeInfo extends BaseJavaTypeInfo implements IUnloadable, IRelativeTypeInfo
{
  private final FeatureManager _fm;
  private final LazyVar<List<? extends IEventInfo>> _eventsCache;

  public BaseHandBuiltTypeInfo(Class javaClass) {
    super(javaClass);
    _fm = new FeatureManager(this, false);
    _eventsCache = new LazyVar<List<? extends IEventInfo>>() {
      protected List<? extends IEventInfo> init() {
        return loadEvents();
      }
    };
  }

  protected abstract List<? extends IEventInfo> loadEvents();

  public List<? extends IPropertyInfo> getProperties()
  {
    return getProperties(null);
  }

  public IPropertyInfo getProperty( CharSequence property )
  {
    return getProperty(null, property);
  }

  public CharSequence getRealPropertyName(CharSequence propName) {
    //noinspection unchecked
    return FIND.findCorrectString(propName, _fm.getPropertyNames(Accessibility.PRIVATE, getOwnersType().getTypeLoader().getModule()));
  }

  public List<? extends IMethodInfo> getMethods()
  {
    return getMethods(null);
  }

  public List<? extends IConstructorInfo> getConstructors()
  {
    return getConstructors(null);
  }

  public void unload() {
    _fm.clear();
    _eventsCache.clear();
  }

  public Accessibility getAccessibilityForType(IType whosaskin) {
    return FeatureManager.getAccessibilityForClass( getOwnersType(), whosaskin);
  }

  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return _fm.getConstructor(getAccessibilityForType(whosAskin), params);
  }

  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    //noinspection unchecked
    return _fm.getConstructors(getAccessibilityForType(whosaskin));
  }

  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    return _fm.getMethod(getAccessibilityForType(whosaskin), getModuleForType(whosaskin), methodName, params);
  }

  public List<? extends IMethodInfo> getMethods(IType whosaskin) {
    //noinspection unchecked
    return _fm.getMethods(getAccessibilityForType(whosaskin), getModuleForType(whosaskin));
  }

  public List<? extends IPropertyInfo> getProperties(IType whosaskin) {
    //noinspection unchecked
    return _fm.getProperties(getAccessibilityForType(whosaskin), getModuleForType(whosaskin));
  }

  @Override
  public List<? extends IMethodInfo> getMethods(IType whosaskin, IModule module) {
    //noinspection unchecked
    return _fm.getMethods(getAccessibilityForType(whosaskin), module);
  }

  @Override
  public List<? extends IPropertyInfo> getProperties(IType whosaskin, IModule module) {
    //noinspection unchecked
    return _fm.getProperties(getAccessibilityForType(whosaskin), module);
  }

  public IPropertyInfo getProperty(IType whosaskin, CharSequence propName) {
    return _fm.getProperty(getAccessibilityForType(whosaskin), getModuleForType(whosaskin), propName);
  }

  public IEventInfo getEvent(CharSequence event) {
    for (IEventInfo eventInfo : getEvents()) {
      if (eventInfo.getName().equals(event)) {
        return eventInfo;
      }
    }
    return null;
  }

  public List<? extends IEventInfo> getEvents() {
    return _eventsCache.get();
  }
  
  private IModule getModuleForType(IType whosAskin) {
    IType moduleType = whosAskin == null ? getOwnersType() : whosAskin;
    if(moduleType instanceof ITypeVariableType) {
      moduleType = ((ITypeVariableType)moduleType).getBoundingType();
    }
    return moduleType.getTypeLoader().getModule();
  }

}
