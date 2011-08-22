package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A builder for creating IPropertyInfos from scratch.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class PropertyInfoBuilder {

  private boolean _isStatic;
  private String _name;
  private IPropertyAccessor _accessor;
  private boolean _readable = true;
  private boolean _writable = true;
  private IType _type;
  private String _description;
  private String _deprecated;
  private String _javaGetterMethodName;
  private List<IAnnotationInfo> _annotations = Collections.emptyList();

  public PropertyInfoBuilder withName(String name) {
    _name = name;
    return this;
  }

  public PropertyInfoBuilder withType(IType type) {
    _type = type;
    return this;
  }

  public PropertyInfoBuilder withType(Class returnType) {
    return withType(TypeSystem.get(returnType));
  }

  public PropertyInfoBuilder withStatic() {
    return withStatic(true);
  }

  public PropertyInfoBuilder withStatic(boolean isStatic) {
    _isStatic = isStatic;
    return this;
  }

  public PropertyInfoBuilder withAccessor(IPropertyAccessor accessor) {
    _accessor = accessor;
    return this;
  }

  public PropertyInfoBuilder withGetter(String javaGetterMethodName) {
    _javaGetterMethodName = javaGetterMethodName;
    return this;
  }

  public PropertyInfoBuilder withReadable(boolean readable) {
    _readable = readable;
    return this;
  }

  public PropertyInfoBuilder withWritable(boolean writable) {
    _writable = writable;
    return this;
  }

  public PropertyInfoBuilder withDescription(String description) {
    _description = description;
    return this;
  }

  public PropertyInfoBuilder withDeprecated(String deprecated) {
    _deprecated = deprecated;
    return this;
  }

  public IPropertyInfo build(IFeatureInfo container) {
    return new BuiltPropertyInfo(this, container);
  }

  public PropertyInfoBuilder withAnnotations( IAnnotationInfo... annotations ) {
    _annotations = Arrays.asList(annotations);
    return this;
  }

  public PropertyInfoBuilder like( IPropertyInfo prop ) {
    _isStatic = prop.isStatic();
    _name = prop.getName();
    _accessor = prop.getAccessor();
    _readable = prop.isReadable();
    _writable = prop.isWritable();
    _type = prop.getFeatureType();
    _description = prop.getDescription();
    if ( prop.isDeprecated() ) {
      _deprecated = prop.getDeprecatedReason() == null ? "" : prop.getDeprecatedReason();
    }
    else {
      _deprecated = null;
    }
    _annotations = prop.getAnnotations(); // todo dgreen - any need to step through annotations and recreate 1-by-1?
    return this;
  }

  public static class BuiltPropertyInfo extends BaseFeatureInfo implements IPropertyInfo {

    private final boolean _isStatic;
    private final String _name;
    private final String _javaGetterMethodName;
    private IPropertyAccessor _accessor;
    private boolean _readable = true; // default to true
    private final boolean _writable;
    private IType _type;
    private final String _description;
    private final String _deprecated;
    private List<IAnnotationInfo> _annotations = Collections.emptyList();

    public BuiltPropertyInfo(PropertyInfoBuilder builder, IFeatureInfo container) {
      super(container);
      assert container != null;
      _isStatic = builder._isStatic;
      _name = builder._name;
      _accessor = builder._accessor;
      _javaGetterMethodName = builder._javaGetterMethodName;
      _readable = builder._readable;
      _writable = builder._writable;
      _type = builder._type;
      _description = builder._description;
      _deprecated = builder._deprecated;
      _annotations = builder._annotations;

      inferAccessorAndTypeFromName();

      assert _accessor != null;
      assert _type != null;
    }

    public String getJavaMethodName()
    {
      return _javaGetterMethodName;
    }

    private void inferAccessorAndTypeFromName()
    {
      if( _accessor == null && (_type == null || _type instanceof IJavaType) )
      {
        IType ownerType = getOwnersType();
        if( ownerType instanceof IJavaType )
        {
          IJavaType propertyType = (IJavaType)_type;
          Method runtimeGetter;
          IJavaClassMethod compiletimeGetter;
          if( _javaGetterMethodName != null )
          {
            try
            {
              runtimeGetter = ((IJavaType)ownerType).getBackingClass().getMethod( _javaGetterMethodName );
              compiletimeGetter = ((IJavaType)ownerType).getClassInfo().getMethod( _javaGetterMethodName );
            }
            catch( NoSuchMethodException e )
            {
              throw new RuntimeException( e );
            }
          }
          else
          {
            try
            {
              runtimeGetter = ((IJavaType)ownerType).getBackingClass().getMethod( "get" + _name );
              compiletimeGetter = ((IJavaType)ownerType).getClassInfo().getMethod( "get" + _name );
            }
            catch( NoSuchMethodException e )
            {
              try
              {
                runtimeGetter = ((IJavaType)ownerType).getBackingClass().getMethod( "is" + _name );
                compiletimeGetter = ((IJavaType)ownerType).getClassInfo().getMethod( "is" + _name );
              }
              catch( NoSuchMethodException e1 )
              {
                throw new RuntimeException( e1 );
              }
            }
          }

          if( propertyType == null )
          {
            _type = propertyType = (IJavaType) compiletimeGetter.getReturnType();
          }

          Method setter = null;
          if( _writable )
          {
            try
            {
              setter = ((IJavaType)ownerType).getBackingClass().getMethod( "set" + _name, propertyType.getIntrinsicClass() );
            }
            catch( NoSuchMethodException e )
            {
              throw new RuntimeException( e );
            }
          }
          final Method getter1 = runtimeGetter;
          final Method setter1 = setter;
          _accessor =
            new IPropertyAccessor()
            {
              public Object getValue( Object ctx )
              {
                try
                {
                  return getter1.invoke( ctx );
                }
                catch( IllegalAccessException e )
                {
                  throw new RuntimeException( e );
                }
                catch( InvocationTargetException e )
                {
                  throw new RuntimeException( e );
                }
              }

              public void setValue( Object ctx, Object value )
              {
                try
                {
                  setter1.invoke( ctx, value );
                }
                catch( IllegalAccessException e )
                {
                  throw new RuntimeException( e );
                }
                catch( InvocationTargetException e )
                {
                  throw new RuntimeException( e );
                }
              }
            };
        }
      }
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return _annotations;
    }

    public boolean isStatic() {
      return _isStatic;
    }

    public String getName() {
      return _name;
    }

    public boolean isReadable() {
      return _readable;
    }

    public boolean isWritable(IType whosAskin) {
      return _writable;
    }

    public boolean isWritable() {
      return isWritable(null);
    }

    public IPropertyAccessor getAccessor() {
      return _accessor;
    }

    public IPresentationInfo getPresentationInfo() {
      return IPresentationInfo.Default.GET;
    }

    public IType getFeatureType() {
      return _type;
    }

    public String getDescription() {
      return _description;
    }

    public boolean isDeprecated() {
      return _deprecated != null;
    }

    public String getDeprecatedReason() {
      return _deprecated;
    }

    public String toString() {
      return _name;
    }

  }

}
