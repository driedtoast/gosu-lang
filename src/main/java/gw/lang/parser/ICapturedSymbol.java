package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICapturedSymbol extends IFunctionSymbol
{
  BoxedValue getCurrentBoxedValue();

  void setBoxedValue( BoxedValue valueReference );

  void clearBoxedValue();

  ISymbol getReferredSymbol();
}
