package org.github.minidoc.core.annotation;

import java.lang.annotation.*;

/**
 * 文档属性注解
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocField {

    /**
     * 字段描述
     */
    String value() default "";

}
