package gw.lang;

import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.UsageTarget;
import gw.lang.annotation.AnnotationUsages;

/**
 * Gosu annotation used to document the parameters of a function or constructor
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@AnnotationUsages({
  @AnnotationUsage(target = UsageTarget.MethodTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.ConstructorTarget, usageModifier = UsageModifier.Many)
})
public class Param implements IAnnotation
{
  private String _fieldName;
  private String _fieldDescription;

  public Param(String aFieldName, String aFieldDescription )
  {
    _fieldName = aFieldName;
    _fieldDescription = aFieldDescription;
  }

  public String getFieldName() {
    return _fieldName;
  }

  public String getFieldDescription() {
    return _fieldDescription;
  }

}