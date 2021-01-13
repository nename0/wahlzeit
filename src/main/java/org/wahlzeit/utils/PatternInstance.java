package org.wahlzeit.utils;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE) // not needed at runtime
@Target(ElementType.TYPE) // only allowed on classes/interfaces
@Repeatable(PatternInstances.class) // allow multiple annotations on same class, will be wrapped in PatternInstances
public @interface PatternInstance {
    String patternName();

    String[] participants();
}
