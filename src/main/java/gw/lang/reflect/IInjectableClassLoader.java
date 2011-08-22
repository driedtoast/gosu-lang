package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IInjectableClassLoader {

  Class defineClass(String className, byte[] bytes);
}
