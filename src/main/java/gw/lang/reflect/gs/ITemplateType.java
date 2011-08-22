package gw.lang.reflect.gs;

import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.IType;
import gw.util.fingerprint.FP64;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITemplateType extends IType, IEnhanceableType
{
  ITemplateGenerator getTemplateGenerator();

  FP64 getFingerprint();
}
