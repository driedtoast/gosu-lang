package gw.lang;

/**
 * Any property annotated with this annotation that's subsequently used in a member access expression will be
 * short-circuited to the default value:  false for boolean properties, the appropriate 0 value for any other
 * primitive value.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public @interface ShortCircuitingProperty {
}
