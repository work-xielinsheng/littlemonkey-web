package com.littlemonkey.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Index {
    int value() default Integer.MIN_VALUE;
}
