package gw.lang;

import gw.lang.reflect.IType;

import gw.lang.annotation.UsageTarget;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.AnnotationUsages;

/**
 * Gosu annotation used to document what exceptions your method throws.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@AnnotationUsages({
  @AnnotationUsage(target = UsageTarget.MethodTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.PropertyTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.ConstructorTarget, usageModifier = UsageModifier.Many)
})
public class Throws implements IAnnotation
{
  private IType _exceptionType;
  private String _exceptionDescription;

  /**
   *
   * @param aType the type of the exception
   * @param aDescription the description of why the exception can be thrown
   */
  public Throws(IType aType, String aDescription )
  {
    _exceptionType = aType;
    _exceptionDescription = aDescription;
  }

  public IType getExceptionType() {
    return _exceptionType;
  }

  public String getExceptionDescription() {
    return _exceptionDescription;
  }
}
