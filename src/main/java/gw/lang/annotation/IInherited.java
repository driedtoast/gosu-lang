package gw.lang.annotation;

/**
 * Indicates that an annotation type is automatically inherited.  If
 * an Inherited meta-annotation is present on an annotation type
 * declaration, and the user queries the annotation type on a type
 * declaration, and the type declaration has no annotation for this type,
 * then the class's superclass will automatically be queried for the
 * annotation type.  This process will be repeated until an annotation for this
 * type is found, or the top of the class hierarchy (Object)
 * is reached.  If no superclass has an annotation for this type, then
 * the query will indicate that the class in question has no such annotation.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IInherited {
}
