package gw.lang.reflect.java;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeInfoBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomTypeInfoBase extends TypeInfoBase
{
  private List<IMethodInfo> _methods;
  private List<IConstructorInfo> _constructors;
  private List<IAnnotationInfo> _annotations;
  private IType _owner;
  private HashMap<CharSequence, IPropertyInfo> _props;

  public CustomTypeInfoBase( IType owner )
  {
    _owner = owner;
    _methods = new ArrayList<IMethodInfo>();
    _constructors = new ArrayList<IConstructorInfo>();
    _annotations = new ArrayList<IAnnotationInfo>();
    _props = new HashMap<CharSequence, IPropertyInfo>();
  }

  @Override
  public List<? extends IPropertyInfo> getProperties()
  {
    return new ArrayList<IPropertyInfo>( _props.values() );
  }

  @Override
  public IPropertyInfo getProperty( CharSequence propName )
  {
    return _props.get( propName );
  }

  @Override
  public CharSequence getRealPropertyName( CharSequence propName )
  {
    for( IPropertyInfo propertyInfo : getProperties() )
    {
      if( propName.equals( propertyInfo.getName() ) )
      {
        return propertyInfo.getName();
      }
    }
    return null;
  }

  @Override
  public List<? extends IMethodInfo> getMethods()
  {
    return _methods;
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors()
  {
    return _constructors;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return _annotations;
  }

  @Override
  public IType getOwnersType()
  {
    return _owner;
  }

  protected void addMethod( IMethodInfo mi )
  {
    _methods.add( mi );
  }
  
  protected void addMethods( List<? extends IMethodInfo> mis )
  {
    for( IMethodInfo mi : mis )
    {
      addMethod( mi );
    }
  }
  
  protected void addConstructor( IConstructorInfo ci )
  {
    _constructors.add( ci );
  }
  
  protected void addConstructors( List<? extends IConstructorInfo> cis )
  {
    for( IConstructorInfo ci : cis )
    {
      addConstructor( ci );
    }
  }
  
  protected void addProperty( IPropertyInfo pi )
  {
    _props.put( pi.getName(), pi );
  }
  
  protected void addProperties( List<? extends IPropertyInfo> pis )
  {
    for( IPropertyInfo pi : pis )
    {
      addProperty( pi );
    }
  }
  
  protected void addAnnotation( IAnnotationInfo ai )
  {
    _annotations.add( ai );
  }
  
  protected void addAnnotations( List<? extends IAnnotationInfo> ais )
  {
    for( IAnnotationInfo ai : ais )
    {
      addAnnotation( ai );
    }
  }

  protected void addAll( ITypeInfo ti )
  {
    if( ti.getAnnotations() != null )
    {
      addAnnotations( ti.getAnnotations() );
    }
    if( ti.getConstructors() != null )
    {
      addConstructors( ti.getConstructors() );
    }
    if( ti.getMethods() != null )
    {
      addMethods( ti.getMethods() );
    }
    if( ti.getProperties() != null )
    {
      addProperties( ti.getProperties() );
    }
  }
  
}
