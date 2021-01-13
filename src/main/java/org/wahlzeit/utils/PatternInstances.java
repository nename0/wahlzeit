package org.wahlzeit.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // not needed at runtime
@Target(ElementType.TYPE) // only allowed on classes/interfaces
public @interface PatternInstances {
    PatternInstance[] value();
}
