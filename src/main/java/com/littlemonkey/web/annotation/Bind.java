package com.littlemonkey.web.annotation;

import java.lang.annotation.*;

/**
 * @author xielinsheng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bind {
    Class target();
}
