package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBlockType extends IFunctionType, IGenericMethodInfo
{
  String getRelativeNameSansBlock();

  CaseInsensitiveCharSequence getRelativeParamSignature( boolean bSansBlock );
}
