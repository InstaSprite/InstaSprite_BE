package org.olaz.instasprite_be.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for measuring method execution time
 * Use this annotation on methods to log their execution duration
 * 
 * Example:
 * <pre>
 * {@code
 * @Timed
 * public void slowMethod() {
 *     // method implementation
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timed {
}

