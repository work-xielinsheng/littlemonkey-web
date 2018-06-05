package com.littlemonkey.web.annotation;

import com.littlemonkey.web.method.build.impl.DefaultMethodBuildProviderImpl;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodBuildBind {
    Class target() default DefaultMethodBuildProviderImpl.class;
}
