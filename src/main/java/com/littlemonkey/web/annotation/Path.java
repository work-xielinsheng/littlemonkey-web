package com.littlemonkey.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
    String url() default "/**";
    String exclude() default "";
}
