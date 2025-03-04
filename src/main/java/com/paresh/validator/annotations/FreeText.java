package com.paresh.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  // This annotation can be applied to fields
@Retention(RetentionPolicy.RUNTIME)  // This annotation will be available at runtime
public @interface FreeText {
}
