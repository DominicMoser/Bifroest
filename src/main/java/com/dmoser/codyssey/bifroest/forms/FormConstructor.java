package com.dmoser.codyssey.bifroest.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a static factory method that creates a {@link Form} for a specific type.
 * The annotated method must be static, take no arguments, and return an instance of {@link Form}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FormConstructor {}
