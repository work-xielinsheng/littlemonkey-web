package com.littlemonkey.web.annotation;


import com.littlemonkey.web.interceptor.MethodInterceptor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptor {
    Class<? extends MethodInterceptor>[] value();
}
