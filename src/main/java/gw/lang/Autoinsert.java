package gw.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;

/**
 * Properties that are of type List, and are annotated with @Autoinsert will have new elements inserted automatically
 * during an assignment of an index that is one greater than the highest existing index in the List.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface Autoinsert {

}
