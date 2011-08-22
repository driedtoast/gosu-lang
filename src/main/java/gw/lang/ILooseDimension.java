package gw.lang;

/**
 * Implement this interface instead of directly implementing IDimension if your
 * dimension assumes compatiblity with Numbers in add/subtract and relational
 * expressions. Note doing so compromises type safety.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ILooseDimension<S extends ILooseDimension<S,T>, T extends Number> extends IDimension<S,T>
{
}